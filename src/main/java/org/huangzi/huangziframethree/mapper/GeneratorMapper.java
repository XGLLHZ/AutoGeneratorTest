package net.qmgf.frame.biz.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.qmgf.frame.biz.generator.dto.GeneratorTableDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: guohao
 * @date: "2019/1/18"
 * @description: 代码生成器 mapper接口
 */
public interface GeneratorMapper {

    /**
     * 查询数据库表列表   table list
     * @param page 分页参数
     * @param condition 查询条件   table name
     * @return table list
     */
    List<GeneratorTableDto> getTableList(IPage<GeneratorTableDto> page, @Param("condition") GeneratorTableDto condition);

}
