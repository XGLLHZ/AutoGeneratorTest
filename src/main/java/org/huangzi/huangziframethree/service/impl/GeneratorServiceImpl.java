package net.qmgf.frame.biz.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.qmgf.frame.biz.generator.dto.GeneratorTableDto;
import net.qmgf.frame.biz.generator.mapper.GeneratorMapper;
import net.qmgf.frame.biz.generator.service.IGeneratorService;
import net.qmgf.frame.biz.generator.utils.CommonLogGenerator;
import net.qmgf.frame.common.base.model.QmParamTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: guohao
 * @date: "2019/1/18"
 * @description:
 */
@Service
@Transactional(rollbackFor = {Exception.class, Error.class})
public class GeneratorServiceImpl implements IGeneratorService {

    @Autowired
    GeneratorMapper generatorMapper;

    /**
     * 获取当前数据库下表的列表
     * @param qmParamTable table name
     * @return table list page
     */
    @Override
    public IPage<GeneratorTableDto> getTableList(QmParamTable<GeneratorTableDto> qmParamTable) {
        GeneratorTableDto generatorTableDto = qmParamTable.getData();
        Page page = qmParamTable.toPageInfo();
        List<GeneratorTableDto> mapList = this.generatorMapper.getTableList(page,generatorTableDto);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 代码生成方法
     * @param tableArray table list
     */
    @Override
    public void generatorUtil(String[] tableArray){
        CommonLogGenerator generator = new CommonLogGenerator();
        final String rootDir = "E:\\baseframework\\svn\\04_编码\\dev\\vrm-parent\\vrm-biz\\vrm-biz-base\\vrm-biz-base-generator\\src\\main";
        final String outputDir = rootDir + "\\java";
        final  String xmlDir =  rootDir + "\\resources\\mapper\\modules\\frame\\generator\\";
        final  String packageDir = "net.qmgf.frame.biz";
        final String moduleName = "generator";
        final String logModel = "FrameModelConstant.GENERATOR";
        Map<String,String> customConfig = new HashMap<>(6);
        customConfig.put("logModel",logModel);
        generator.autoGenerator(outputDir, true, tableArray, packageDir, xmlDir, moduleName, customConfig);
    }

}

