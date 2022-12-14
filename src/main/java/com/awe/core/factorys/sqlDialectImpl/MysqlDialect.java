package com.awe.core.factorys.sqlDialectImpl;


import com.awe.core.factorys.SqlDialectFactory;

/**
 * MySQL 方言
 *
 * @author BionGo
 */
public class MysqlDialect implements SqlDialectFactory.SqlDialect {

    /**
     * 封装字段名
     *
     * @param name 字段名
     * @return String
     */
    @Override
    public String wrapFieldName(String name) {
        return String.format("`%s`", name);
    }

    /**
     * &#x89E3;&#x6790;&#x5B57;&#x6BB5;&#x540D;
     *
     * @param fieldName &#x5B57;&#x6BB5;&#x540D;
     * @return String
     */
    @Override
    public String parseFieldName(String fieldName) {
        if (fieldName.startsWith("`") && fieldName.endsWith("`")) {
            return fieldName.substring(1, fieldName.length() - 1);
        }
        return fieldName;
    }

    /**
     * 包装表名
     *
     * @param name 表名
     * @return String
     */
    @Override
    public String wrapTableName(String name) {
        return String.format("`%s`", name);
    }

    /**
     * 解析表名
     *
     * @param tableName 表名
     * @return String
     */
    @Override
    public String parseTableName(String tableName) {
        if (tableName.startsWith("`") && tableName.endsWith("`")) {
            return tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }
}
