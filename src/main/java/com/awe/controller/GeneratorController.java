package com.awe.controller;

import com.awe.model.other.AjaxResult;
import com.awe.model.vo.TableDefinitionVO;
import com.awe.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * sql
     *
     * @param definition 信息
     * @return 结果
     */
    @PostMapping("/sql")
    public AjaxResult buildCreateTableSql(@RequestBody TableDefinitionVO definition) {
        AjaxResult ajax = AjaxResult.success();
        String tableSql = generatorService.createTableSql(definition);
        ajax.put("tableSql", tableSql);
        return ajax;
    }
    /**
     * db实体对象
     *
     * @param definition 信息
     * @return 结果
     */
    @PostMapping("/getEntityCode")
    public AjaxResult buildEntityCode(@RequestBody TableDefinitionVO definition) {
        AjaxResult ajax = AjaxResult.success();
        String aaa = generatorService.buildEntityCode(definition);
        ajax.put("aaa", aaa);
        return ajax;
    }
}
