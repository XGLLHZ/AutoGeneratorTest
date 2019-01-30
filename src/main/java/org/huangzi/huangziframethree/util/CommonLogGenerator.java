package net.qmgf.frame.biz.generator.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: guohao
 * @date: 2019/1/30/18:34
 * @description:
 */
public class CommonLogGenerator extends BaseGeneratorUtil {

    /**
     * 测试 run 执行 注意：不生成service接口 注意：不生成service接口 注意：不生成service接口
     * <p>
     * 配置方法查看
     * </p>
     */
    public static void main(String[] args) {
        CommonLogGenerator generator = new CommonLogGenerator();
        String outputDir = "D:\\workspace\\svn\\qmgf\\frame-parent\\frame-common\\frame-common-log\\src\\main\\java";
        String[] tableArray = new String[]{
                "ST_TBL_SYS_LOG", "ST_TBL_SYS_LOG_DETAIL"
        };
        String packageDir = "net.qmgf.frame.common.log";
        String xmlDir = "D:\\workspace\\svn\\qmgf\\frame-parent\\frame-common\\frame-common-log\\src\\main\\resources\\mapper\\modules\\frame\\log\\";

        Map customConfig = new HashMap(6);
        customConfig.put("logModel", "FrameModelConstant.SYS");
        generator.autoGenerator(outputDir, false, tableArray, packageDir, xmlDir, "sys", customConfig);

    }

}
