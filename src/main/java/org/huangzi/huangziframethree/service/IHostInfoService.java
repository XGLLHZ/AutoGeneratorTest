package org.huangzi.huangziframethree.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.huangzi.huangziframethree.dto.HostInfoDto;


/**
 * @author: guohao
 * @date: "2018/12/18"
 * @description: 主播表事务层
 */
public interface IHostInfoService{

    /**
     * 获取主播列表
     * @param hostInfoDto 查询参数
     * @return 主播列表
     */
    Page<HostInfoDto> list(HostInfoDto hostInfoDto);

    /**
     * 新增主播信息
     * @param hostInfoDto 主播信息
     * @return  是否新增成功
     */
    Integer insert(HostInfoDto hostInfoDto);

    /**
     * 删除主播信息
     * @param id 主播ids
     * @return 是否删除成功
     */
    Integer delete(int id);

}
