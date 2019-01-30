package net.qmgf.frame.biz.generator.utils;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author: guohao
 * @date: 2019/1/30/16:44
 * @description: 抽象的对外接口
 */
@Data
@Accessors(chain = true)
public abstract class InjectionConfigUtil {

    /**
     * 全局配置
     */
    private ConfigBuilderUtil config;

    /**
     * 自定义返回配置 Map 对象
     */
    private Map<String, Object> map;

    /**
     * 自定义输出文件
     */
    private List<FileOutConfig> fileOutConfigList;

    /**
     * 自定义判断是否创建文件
     */
    private IFileCreateUtil fileCreate;

    /**
     * 注入自定义 Map 对象
     */
    public abstract void initMap();

}
