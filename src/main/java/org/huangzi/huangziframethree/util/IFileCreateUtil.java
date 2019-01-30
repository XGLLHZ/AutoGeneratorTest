package net.qmgf.frame.biz.generator.utils;

import com.baomidou.mybatisplus.extension.toolkit.PackageHelper;
import com.baomidou.mybatisplus.generator.config.rules.FileType;

import java.io.File;

/**
 * @author: guohao
 * @date: 2019/1/30/16:42
 * @description: 文件覆盖接口
 */
public interface IFileCreateUtil {

    /**
     * <p>
     * 自定义判断是否创建文件
     * </p>
     *
     * @param configBuilder 配置构建器
     * @param fileType      文件类型
     * @param filePath      文件路径
     * @return
     */
    boolean isCreate(ConfigBuilderUtil configBuilder, FileType fileType, String filePath);

    /**
     * <p>
     * 检查文件目录，不存在自动递归创建
     * </p>
     *
     * @param filePath 文件路径
     */
    default void checkDir(String filePath) {
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            PackageHelper.mkDir(file.getParentFile());
        }
    }

}
