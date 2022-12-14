package com.awe.core.builder;

import com.awe.core.factorys.sqlDialectImpl.MysqlDialect;
import com.awe.core.factorys.SqlDialectFactory;
import com.awe.core.factorys.SqlDialectFactory.SqlDialect;
import com.awe.model.vo.TableDefinitionVO;
import com.awe.model.vo.TableDefinitionVO.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.awe.core.builder.enums.FieldTypeEnum;

import java.util.List;
import java.util.Optional;

/**
 * SQL 生成器
 * 支持方言，策略模式
 *
 * @author BionGo
 */
@Slf4j
public class SqlBuilder {

    /**
     * 方言
     */
    private final SqlDialect sqlDialect;

    /**
     * 默认Mysql
     */
    public SqlBuilder() {
        this.sqlDialect = SqlDialectFactory.getDialect(MysqlDialect.class.getName());
    }

    public SqlBuilder(String className) {
        this.sqlDialect = SqlDialectFactory.getDialect(className);
    }

    /**
     * 构造建表 SQL
     *
     * @param tableSchemaVO 表概要
     * @return 生成的 SQL
     */
    public String buildCreateTableSql(TableDefinitionVO tableSchemaVO) {
        // 构造模板
        String template = "%s\n"
                + "create table if not exists %s\n"
                + "(\n"
                + "%s\n"
                + ") %s;";
        // 构造表名
        String tableName = sqlDialect.wrapTableName(tableSchemaVO.getTableName());
        String dbName = tableSchemaVO.getDbName();
        if (StringUtils.isNotBlank(dbName)) {
            tableName = String.format("%s.%s", dbName, tableName);
        }
        // 构造表前缀注释
        String tableComment = tableSchemaVO.getTableComment();
        if (StringUtils.isBlank(tableComment)) {
            tableComment = tableName;
        }
        String tablePrefixComment = String.format("-- %s", tableComment);
        // 构造表后缀注释
        String tableSuffixComment = String.format("comment '%s'", tableComment);
        // 构造表字段
        List<Field> fieldList = tableSchemaVO.getFieldList();
        StringBuilder fieldStrBuilder = new StringBuilder();
        int fieldSize = fieldList.size();
        for (int i = 0; i < fieldSize; i++) {
            Field field = fieldList.get(i);
            fieldStrBuilder.append(buildCreateFieldSql(field));
            // 最后一个字段后没有逗号和换行
            if (i != fieldSize - 1) {
                fieldStrBuilder.append(",");
                fieldStrBuilder.append("\n");
            }
        }
        String fieldStr = fieldStrBuilder.toString();
        // 填充模板
        String result = String.format(template, tablePrefixComment, tableName, fieldStr, tableSuffixComment);
        log.info("sql result = " + result);
        return result;
    }

    /**
     * 生成创建字段的 SQL
     *
     * @param field 单列定义信息
     * @return String
     */
    public String buildCreateFieldSql(Field field) {
        if (field == null) {
            throw new RuntimeException("");
        }
        String fieldName = sqlDialect.wrapFieldName(field.getFieldName());
        String fieldType = field.getFieldType();
        String defaultValue = field.getDefaultValue();
        boolean notNull = field.isNotNull();
        String comment = field.getComment();
        String onUpdate = field.getOnUpdate();
        boolean primaryKey = field.isPrimaryKey();
        boolean autoIncrement = field.isAutoIncrement();
        // e.g. column_name int default 0 not null auto_increment comment '注释' primary key,
        StringBuilder fieldStrBuilder = new StringBuilder();
        // 字段名
        fieldStrBuilder.append(fieldName);
        // 字段类型
        fieldStrBuilder.append(" ").append(fieldType);
        // 默认值
        if (StringUtils.isNotBlank(defaultValue)) {
            fieldStrBuilder.append(" ").append("default ").append(getValueStr(field, defaultValue));
        }
        // 是否非空
        fieldStrBuilder.append(" ").append(notNull ? "not null" : "null");
        // 是否自增
        if (autoIncrement) {
            fieldStrBuilder.append(" ").append("auto_increment");
        }
        // 附加条件
        if (StringUtils.isNotBlank(onUpdate)) {
            fieldStrBuilder.append(" ").append("on update ").append(onUpdate);
        }
        // 注释
        if (StringUtils.isNotBlank(comment)) {
            fieldStrBuilder.append(" ").append(String.format("comment '%s'", comment));
        }
        // 是否为主键
        if (primaryKey) {
            fieldStrBuilder.append(" ").append("primary key");
        }
        return fieldStrBuilder.toString();
    }

    /**
     * 根据列的属性获取值字符串
     *
     * @param field
     * @param value
     * @return
     */
    public static String getValueStr(Field field, Object value) {
        if (field == null || value == null) {
            return "''";
        }
        FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType()))
                .orElse(FieldTypeEnum.TEXT);
        String result = String.valueOf(value);
        switch (fieldTypeEnum) {
            case DATETIME:
            case TIMESTAMP:
                return result.equalsIgnoreCase("CURRENT_TIMESTAMP") ? result : String.format("'%s'", value);
            case DATE:
            case TIME:
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
                return String.format("'%s'", value);
            default:
                return result;
        }
    }
}
