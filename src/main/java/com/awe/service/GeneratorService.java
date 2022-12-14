package com.awe.service;

import com.awe.model.vo.TableDefinitionVO;

public interface GeneratorService {

    String createTableSql(TableDefinitionVO definition);
}
