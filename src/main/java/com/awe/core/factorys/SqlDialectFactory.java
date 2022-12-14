package com.awe.core.factorys;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL 方言工厂
 * 工厂 + 单例模式，降低开销
 *
 * @author BionGo
 */
public class SqlDialectFactory {
    /**
     * className => 方言实例映射
     */
    private static final Map<String, SqlDialect> DIALECT_POOL = new ConcurrentHashMap<>();

    private SqlDialectFactory() {}

    /**
     * 获取方言实例
     *
     * @param className 类名
     * @return SQLDialect
     */
    public static SqlDialect getDialect(String className) {
        SqlDialect dialect = DIALECT_POOL.get(className);
        if (null == dialect) {
            synchronized (className.intern()) {
                dialect = DIALECT_POOL.computeIfAbsent(className,
                        key -> {
                            try {
                                return (SqlDialect) Class.forName(className)
                                        .getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException("213");
                            }
                        });
            }
        }
        return dialect;
    }

    /**
     * SQL 方言
     *
     * @author BionGo
     */
    public interface SqlDialect {
        /**
         * 封装字段名
         * @param fieldName 字段名
         * @return String
         */
        String wrapFieldName(String fieldName);

        /**
         * 解析字段名
         * @param fieldName 字段名
         * @return String
         */
        String parseFieldName(String fieldName);

        /**
         * 封装表名
         * @param tableName 表名
         * @return String
         */
        String wrapTableName(String tableName);

        /**
         * 解析表名
         * @param tableName 表名
         * @return String
         */
        String parseTableName(String tableName);
    }
}
