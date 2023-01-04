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
     * @param tableDefinition 表概要
     * @return 生成的 SQL
     */
    public String buildCreateTableSql(TableDefinitionVO tableDefinition) {
        // 创建模板
        String template = "%s\n" + "create table if not exists %s\n" + "(\n" + "%s\n" + ") %s;";
        //  给表名包上反单引号（防止关键字）
        String tableName = this.getWrappedTableName(tableDefinition);
        // 构造表注释内容
        String commentContent = StringUtils.isBlank(tableDefinition.getTableComment())
                ? tableDefinition.getTableName() : tableDefinition.getTableComment();
        // 构造表前缀注释
        String prefixComment = String.format("-- %s", commentContent);
        // 构造表后缀注释
        String suffixComment = String.format("comment '%s'", commentContent);
        // 构造表字段
        List<Field> fieldList = tableDefinition.getFieldList();
        StringBuilder fieldStrBuilder = new StringBuilder();
        int length = fieldList.size();
        for (int i = 0; i < length; i++) {
            Field field = fieldList.get(i);
            fieldStrBuilder.append(buildCreateFieldSql(field));
            // 最后一个字段后没有逗号和换行
            if (i == length - 1) break;
            fieldStrBuilder.append(",");
            fieldStrBuilder.append("\n");
        }
        String fieldStr = fieldStrBuilder.toString();
        String result = String.format(template, prefixComment, tableName, fieldStr, suffixComment);
        log.info("建表sql最终输出结果:" + result);
        return result;
    }

    private String getWrappedTableName(TableDefinitionVO tableDefinition) {
        String wrappedTableName = this.sqlDialect.wrapTableName(tableDefinition.getTableName());
        String dbName = tableDefinition.getDbName();
        if (StringUtils.isNotBlank(dbName)) {
            return String.format("%s.%s", dbName, wrappedTableName);
        }
        return wrappedTableName;
    }

    /**
     * 生成创建字段的 SQL
     *
     * @param field 单列定义信息
     * @return String
     */
    public String buildCreateFieldSql(Field field) {
        // 开始拼串串
        StringBuilder result = new StringBuilder();
        // 字段名
        result.append(sqlDialect.wrapFieldName(field.getFieldName()));
        // 字段类型
        result.append(" ").append(field.getFieldType());
        // 默认值
        String defaultValue = field.getDefaultValue();
        if (StringUtils.isNotBlank(defaultValue)) result.append(" ")
                .append("default ").append(getValueStr(field, defaultValue));
        // 是否非空
        result.append(" ").append(field.isNotNull() ? "not null" : "null");
        // 是否自增
        if (field.isAutoIncrement()) {
            result.append(" ").append("auto_increment");
        }
        // 附加条件
        String onUpdate = field.getOnUpdate();
        if (StringUtils.isNotBlank(onUpdate)) result.append(" ").append("on update ").append(onUpdate);
        // 注释
        String comment = field.getComment();
        if (StringUtils.isNotBlank(comment)) result.append(" ").append(String.format("comment '%s'", comment));
        // 是否为主键
        if (field.isPrimaryKey()) result.append(" ").append("primary key");
        return result.toString();
    }

    /**
     * 根据列的属性获取值字符串
     *
     * @param field 单列定义信息
     * @param value 默认值
     * @return String
     */
    public static String getValueStr(Field field, String value) {
        if (value == null) {
            return "''";
        }
        FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType()))
                .orElse(FieldTypeEnum.TEXT);
        String result = value;
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
