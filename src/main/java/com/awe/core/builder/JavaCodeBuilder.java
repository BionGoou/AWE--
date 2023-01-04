package com.awe.core.builder;

import cn.hutool.core.util.StrUtil;
import com.awe.core.builder.enums.FieldTypeEnum;
import com.awe.core.builder.enums.MockTypeEnum;
import com.awe.model.dto.JavaEntityGenerateDto;
import com.awe.model.dto.JavaEntityGenerateDto.FieldDTO;
import com.awe.model.dto.JavaObjectGenerateDto;
import com.awe.model.vo.TableDefinitionVO;
import com.awe.model.vo.TableDefinitionVO.Field;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Java 代码生成器
 *
 * @author BionGo
 */
@Component
@Slf4j
public class JavaCodeBuilder {

    private static Configuration configuration;

    @Resource
    public void setConfiguration(Configuration configuration) {
        JavaCodeBuilder.configuration = configuration;
    }

    /**
     * 构造 Java 实体代码
     *
     * @param tableDefinition 表概要
     * @return 生成的 java 代码
     */
    @SneakyThrows
    public static String buildJavaEntityCode(TableDefinitionVO tableDefinition) {
        // 传递参数
        JavaEntityGenerateDto javaEntityGenerateDTO = new JavaEntityGenerateDto();
        String tableName = tableDefinition.getTableName();
        String tableComment = tableDefinition.getTableComment();
        String upperCamelTableName = StringUtils.capitalize(StrUtil.toCamelCase(tableName));
        // 类名为大写的表名
        javaEntityGenerateDTO.setClassName(upperCamelTableName);
        // 类注释为表注释 > 表名
        javaEntityGenerateDTO.setClassComment(Optional.ofNullable(tableComment).orElse(upperCamelTableName));
        // 依次填充每一列
        List<FieldDTO> fieldDTOList = new ArrayList<>();
        for (TableDefinitionVO.Field field : tableDefinition.getFieldList()) {
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setComment(field.getComment());
            FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType())).orElse(FieldTypeEnum.TEXT);
            fieldDTO.setJavaType(fieldTypeEnum.getJavaType());

            fieldDTO.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
            fieldDTOList.add(fieldDTO);
        }
        javaEntityGenerateDTO.setFieldList(fieldDTOList);
        StringWriter stringWriter = new StringWriter();
        Template temp = configuration.getTemplate("java_entity.ftl");
        temp.process(javaEntityGenerateDTO, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 构造 Java 对象代码
     *
     * @param tableDefinition 表概要
     * @param dataList    数据列表
     * @return 生成的 java 代码
     */
    @SneakyThrows
    public static String buildJavaObjectCode(TableDefinitionVO tableDefinition, List<Map<String, Object>> dataList) {
//        if (CollectionUtils.isEmpty(dataList)) {
//            throw new RuntimeException("缺少示例数据");
//        }
        // 传递参数
        JavaObjectGenerateDto javaObjectGenerateDto = new JavaObjectGenerateDto();
        String tableName = tableDefinition.getTableName();
        String camelTableName = StrUtil.toCamelCase(tableName);
        // 类名为大写的表名
        javaObjectGenerateDto.setClassName(StringUtils.capitalize(camelTableName));
        // 变量名为表名
        javaObjectGenerateDto.setObjectName(camelTableName);
        // 依次填充每一列
        Map<String, Object> fillData = dataList.get(0);
        List<JavaObjectGenerateDto.FieldDTO> fieldDTOList = new ArrayList<>();
        List<Field> fieldList = tableDefinition.getFieldList();
        // 过滤掉不模拟的字段
        fieldList = fieldList.stream()
                .filter(field -> {
                    MockTypeEnum mockTypeEnum = Optional.ofNullable(MockTypeEnum.getEnumByValue(field.getMockDataType())).orElse(MockTypeEnum.NONE);
                    return !MockTypeEnum.NONE.equals(mockTypeEnum);
                })
                .collect(Collectors.toList());
        for (Field field : fieldList) {
            JavaObjectGenerateDto.FieldDTO fieldDTO = new JavaObjectGenerateDto.FieldDTO();
            // 驼峰字段名
            String fieldName = field.getFieldName();
            fieldDTO.setSetMethod(StrUtil.toCamelCase("set_" + fieldName));
            fieldDTO.setValue(getValueStr(field, fillData.get(fieldName)));
            fieldDTOList.add(fieldDTO);
        }
        javaObjectGenerateDto.setFieldList(fieldDTOList);
        StringWriter stringWriter = new StringWriter();
        Template temp = configuration.getTemplate("java_object.ftl");
        temp.process(javaObjectGenerateDto, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 根据列的属性获取值字符串
     *
     * @param field 单列定义信息
     * @param value 值
     * @return String
     */
    public static String getValueStr(Field field, Object value) {
        if (field == null || value == null) {
            return "''";
        }
        FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType()))
                .orElse(FieldTypeEnum.TEXT);
        switch (fieldTypeEnum) {
            case DATE:
            case TIME:
            case DATETIME:
            case CHAR:
            case VARCHAR:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BINARY:
            case VARBINARY:
                return String.format("\"%s\"", value);
            default:
                return String.valueOf(value);
        }
    }
}
