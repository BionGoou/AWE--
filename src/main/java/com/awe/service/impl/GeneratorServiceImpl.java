package com.awe.service.impl;

import com.awe.core.builder.JavaCodeBuilder;
import com.awe.core.builder.SqlBuilder;
import com.awe.core.factorys.sqlDialectImpl.MysqlDialect;
import com.awe.model.vo.TableDefinitionVO;
import com.awe.service.GeneratorService;
import org.springframework.stereotype.Service;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Override
    public String createTableSql(TableDefinitionVO definition) {
        SqlBuilder sqlBuilder = new SqlBuilder(MysqlDialect.class.getName());
        return sqlBuilder.buildCreateTableSql(definition);
    }

    @Override
    public String buildEntityCode(TableDefinitionVO definition) {
        JavaCodeBuilder javaCodeBuilder = new JavaCodeBuilder();
        return JavaCodeBuilder.buildJavaEntityCode(definition);
    }
}
