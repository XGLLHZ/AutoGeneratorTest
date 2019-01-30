package org.huangzi.huangziframethree.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.huangzi.huangziframethree.dto.HostInfoDto;
import org.huangzi.huangziframethree.entity.HostInfoEntity;

import java.util.List;

/**
 * @author: guohao
 * @date: "2018/12/18"
 * @description:主播表mapper接口
 */
@Mapper
public interface IHostInfoMapper extends BaseMapper<HostInfoEntity> {

    /**
     * 获取主播信息列表
     * @param page 分页参数
     * @param condition 查询条件
     * @return 主播信息列表
     */
    List<HostInfoDto> list(Page<HostInfoDto> page, @Param("condition") HostInfoDto condition);

}
