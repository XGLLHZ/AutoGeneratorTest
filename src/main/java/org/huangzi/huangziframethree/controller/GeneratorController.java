package net.qmgf.frame.biz.generator.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.qmgf.frame.biz.generator.dto.GeneratorTableDto;
import net.qmgf.frame.biz.generator.service.IGeneratorService;
import net.qmgf.frame.common.base.model.QmParam;
import net.qmgf.frame.common.base.model.QmParamTable;
import net.qmgf.frame.common.base.model.QmResultTable;
import net.qmgf.frame.common.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: guohao
 * @date: "2019/1/18"
 * @description: 代码生成 前端控制器
 */
@RestController
@Slf4j
@Api(
        tags = "代码生成",
        description = "代码生成"
)
@RequestMapping("/api/base/generator")
public class GeneratorController extends BaseController {

    @Autowired
    public IGeneratorService generatorService;

    /**
     * 获取当前数据库下表的列表
     * @param qmParamTable table name
     * @return table list page
     */
    @ApiOperation(value = "获取当前数据库下表的列表",notes = "获取当前数据库下表的列表")
    @PostMapping("/list")
    public QmResultTable<GeneratorTableDto> list(@RequestBody @Validated QmParamTable<GeneratorTableDto> qmParamTable){
        return new QmResultTable<>(this.generatorService.getTableList(qmParamTable));
    }

    /**
     * 代码生成
     * @param qmParam table list
     */
    @ApiOperation(value = "代码生成",notes = "代码生成")
    @PostMapping("/generator")
    public void generator(@RequestBody @Validated QmParam<GeneratorTableDto> qmParam){
        String[] tableName = qmParam.getData().getTableName().split("");
        generatorService.generatorUtil(tableName);
    }

}
