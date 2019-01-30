package net.qmgf.frame.biz.generator.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: guohao
 * @date: 2019/1/30/17:44
 * @description: 代码生成
 */
@SuppressWarnings("unchecked")
@Slf4j
public class BaseGeneratorUtil extends VelocityTemplateEngineUtil {

    /**
     * @param outputDir       java文件的输出目录
     * @param fileOverrideFlg 是否覆盖原来已经生成的同名文件
     * @param tableArray      要生成的表
     * @param packageDir      类文件的包名
     * @param xmlDir          xml生成目录
     */
    public void autoGenerator(String outputDir, boolean fileOverrideFlg, String[] tableArray, String packageDir, String xmlDir, String moduleName, Map customConfig) {

        AutoGeneratorUtil mpg = new AutoGeneratorUtil();

        mpg.setGlobalConfig(initGlobalConfig(outputDir, fileOverrideFlg));

        mpg.setDataSource(initDataSourceConfig());

        mpg.setStrategy(initStrategyConfig(tableArray));
        mpg.setPackageInfo(initPackageConfig(packageDir, moduleName));

        mpg.setCfg(initInjectionConfig(xmlDir, outputDir, packageDir, customConfig, moduleName));
        mpg.setTemplate(initTemplateConfig());

        // 执行生成
        mpg.execute();
    }

    /**
     * 全局配置
     * @param outputDir
     * @param fileOverrideFlg
     * @return
     */
    public GlobalConfig initGlobalConfig(String outputDir, Boolean fileOverrideFlg) {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(fileOverrideFlg);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(false);
        // XML columList
        gc.setBaseColumnList(false);
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setOpen(false);
        //创建人
        gc.setAuthor("qmgf");

        /// 自定义文件命名，注意 %s 会自动填充表实体属性！
        /// gc.setMapperName("%sDao");
        /// gc.setXmlName("%sDao");
        /// gc.setServiceName("MP%sService");
        /// gc.setServiceImplName("%sServiceDiy");
        /// gc.setControllerName("%sAction");

        return gc;

    }

    /**
     * 数据源配置
     * @return
     */
    public DataSourceConfig initDataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.ORACLE);

        dsc.setTypeConvert(new OracleTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                System.out.println("转换类型：" + fieldType);
                if (t.contains("char")) {
                    return DbColumnType.STRING;
                } else {
                    if (!t.contains("date") && !t.contains("timestamp")) {

                        if (t.contains("number")) {

                            if (t.matches("number\\(+\\d\\)") || "number".equals(t)) {
                                return DbColumnType.INTEGER;
                            }

                            if (t.matches("number\\(+\\d{2}+\\)")) {
                                return DbColumnType.LONG;
                            }

                            return DbColumnType.BIG_DECIMAL;
                        }

                        if (t.contains("float")) {
                            return DbColumnType.BIG_DECIMAL;
                        }

                        if (t.contains("clob")) {
                            return DbColumnType.STRING;
                        }

                        if (t.contains("blob")) {
                            return DbColumnType.STRING;
                        }

                        if (t.contains("binary")) {
                            return DbColumnType.BYTE_ARRAY;
                        }

                        if (t.contains("raw")) {
                            return DbColumnType.BYTE_ARRAY;
                        }
                    } else {
                        switch (globalConfig.getDateType()) {
                            case ONLY_DATE:
                                return DbColumnType.DATE;
                            case SQL_PACK:
                                return DbColumnType.TIMESTAMP;
                            case TIME_PACK:
                                return DbColumnType.LOCAL_DATE_TIME;
                        }
                    }

                    return DbColumnType.STRING;
                }
            }
        });
        dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
        dsc.setUsername("frame_dev");
        dsc.setPassword("frame_dev");
        dsc.setUrl("jdbc:oracle:thin:@192.168.101.249:1521/ctrm");
///        dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
///        dsc.setUsername("citsonline");
///        dsc.setPassword("disney601cz888");
///        dsc.setUrl("jdbc:oracle:thin:@172.18.2.34:1521/racdev");

        return dsc;
    }

    /**
     * 策略配置
     * @param tables
     * @return
     */
    public StrategyConfig initStrategyConfig(String[] tables) {

        StrategyConfig strategy = new StrategyConfig();
        // 全局大写命名 ORACLE 注意
        ///strategy.setCapitalMode(true);
        // 此处可以修改为您的表前缀
        strategy.setTablePrefix(new String[]{"ST_TBL_", "B2C_", "DEMO_TBL_"});
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityColumnConstant(true);
        // 需要生成的表
        strategy.setInclude(tables);
        // 排除生成的表
        /// strategy.setExclude(new String[]{"test"});
        // 自定义实体父类
        strategy.setSuperEntityClass("net.qmgf.frame.common.base.model.BaseEntity");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(new String[]{"ID", "CREATE_USER", "CREATE_DATE", "UPDATE_DATE", "UPDATE_USER", "VERSION_NUM", "DELETE_FLAG"});
        // 自定义 mapper 父类
        /// strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass("net.qmgf.frame.common.base.service.IBaseService");
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass("net.qmgf.frame.common.base.service.impl.BaseServiceImpl");
        // 自定义 controller 父类
        strategy.setSuperControllerClass("net.qmgf.frame.common.web.base.BaseController");
        strategy.setSuperMapperClass("net.qmgf.frame.common.base.mapper.BizBaseMapper");
        /// 【实体】是否生成字段常量（默认 false）
        /// public  final String ID = "test_id";
        /// strategy.setEntityColumnConstant(true);
        /// 【实体】是否为构建者模型（默认 false）
        /// public User setName(String name) {this.name = name; return this;}
        /// strategy.setEntityBuilderModel(true);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        return strategy;
    }

    /**
     * 包配置
     * @param packagePath
     * @param moduleName
     * @return
     */
    public PackageConfigUtil initPackageConfig(String packagePath, String moduleName) {
        PackageConfigUtil pc = new PackageConfigUtil();
        pc.setParent(packagePath);
        pc.setEntity("entity");
        pc.setMapper("mapper");
        ///pc.setXml("mapper.xml");
        pc.setServiceImpl("service.impl");
        pc.setService("service");
        pc.setController("web");
        pc.setIndex("index");
        pc.setEdit("edit");
        pc.setModuleName(moduleName);
        return pc;
    }

    /**
     * 自定义配置
     * @param xmlPath
     * @param outputDir
     * @param packageDir
     * @param customConfig
     * @param moduleName
     * @return
     */
    public InjectionConfigUtil initInjectionConfig(final String xmlPath, String outputDir, final String packageDir, Map customConfig, final String moduleName) {
        InjectionConfigUtil cfg = new InjectionConfigUtil() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(6);

                if (customConfig != null) {
                    map.putAll(customConfig);
                }
                if (StringUtils.isNotEmpty(moduleName)) {
                    map.put("dto", packageDir + "." + moduleName + ".dto");

                } else {
                    map.put("dto", packageDir + ".dto");

                }
                this.setMap(map);
            }
        };
        // 自定义 xxList.jsp 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/frame/dto.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                String entityName = tableInfo.getEntityName();
                if (StringUtils.isNotEmpty(moduleName)) {

                    // 自定义输入文件名称
                    return joinPath(outputDir, packageDir + "." + moduleName) + "\\dto\\" + tableInfo.getEntityName() + "Dto.java";
                } else {

                    // 自定义输入文件名称
                    return joinPath(outputDir, packageDir) + "\\dto\\" + tableInfo.getEntityName() + "Dto.java";
                }

            }
        });
        cfg.setFileOutConfigList(focList);

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/frame/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return xmlPath + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);

        return cfg;
    }

    /**
     * <p>
     * 连接路径字符串
     * </p>
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstValUtil.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * 模板配置
     * @return
     */
    public TemplateConfigUtil initTemplateConfig() {
        TemplateConfigUtil tc = new TemplateConfigUtil();
        tc.setXml(null);
        // 自定义模板配置，可以 copy 源码 mybatis-net.qmgf.frame.common.biz.plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        /// TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/frame/controller.java.vm");
        tc.setEntity("/templates/frame/entity.java.vm");
        tc.setMapper("/templates/frame/mapper.java.vm");
        tc.setXml("/templates/frame/mapper.xml.vm");
        tc.setService("/templates/frame/service.java.vm");
        tc.setServiceImpl("/templates/frame/serviceImpl.java.vm");
        tc.setIndex("/templates/frame/index.vue.vm");
        tc.setEdit("/templates/frame/edit.vue.vm");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        /// mpg.setTemplate(tc);
        return tc;
    }

}
