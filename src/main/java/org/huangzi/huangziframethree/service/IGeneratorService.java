package net.qmgf.frame.biz.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.qmgf.frame.biz.generator.dto.GeneratorTableDto;
import net.qmgf.frame.common.base.model.QmParamTable;
import net.qmgf.frame.common.base.service.IBaseService;
import org.springframework.stereotype.Service;

/**
 * @author: guohao
 * @date: "2019/1/18"
 * @description: 代码生成 事务层
 */
public interface IGeneratorService {

    /**
     * 获取当前数据库下表列表
     * @param qmParamTable table name
     * @return table list page
     */
    IPage<GeneratorTableDto> getTableList(QmParamTable<GeneratorTableDto> qmParamTable);

    /**
     * 代码生成方法
     * @param tableArray table list
     */
    void generatorUtil(String[] tableArray);

}
