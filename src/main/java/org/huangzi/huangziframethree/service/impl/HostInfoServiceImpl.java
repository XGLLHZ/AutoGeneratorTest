package org.huangzi.huangziframethree.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.huangzi.huangziframethree.mapper.IHostInfoMapper;
import org.huangzi.huangziframethree.dto.HostInfoDto;
import org.huangzi.huangziframethree.entity.HostInfoEntity;
import org.huangzi.huangziframethree.service.IHostInfoService;
import org.huangzi.huangziframethree.util.BeanConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: guohao
 * @date: "2018/12/18"
 * @description:
 */
@Service
public class HostInfoServiceImpl implements IHostInfoService {

    @Autowired
    private IHostInfoMapper hostInfoMapper;

    /**
     * 获取主播列表
     * @param hostInfoDto 查询参数
     * @return 主播列表
     */
    @Override
    public Page<HostInfoDto> list(HostInfoDto hostInfoDto) {
        if(hostInfoDto.getCurrentPage() == null){
            hostInfoDto.setCurrentPage(1);
        }
        if(hostInfoDto.getPageSize() == null){
            hostInfoDto.setPageSize(10);
        }
        Page<HostInfoDto> page = new Page<>(hostInfoDto.getCurrentPage(),hostInfoDto.getPageSize());
        List<HostInfoDto> list = hostInfoMapper.list(page,hostInfoDto);
        page.setRecords(list);
        return page;
    }

    /**
     * 增加主播信息
     * @param hostInfoDto 主播信息
     * @return 是否增加成功
     */
    @Override
    public Integer insert(HostInfoDto hostInfoDto) {
        HostInfoEntity hostInfoEntitys = new HostInfoEntity();
        HostInfoEntity hostInfoEntity = BeanConverterUtil.beanConvert(hostInfoEntitys,hostInfoDto);
        return hostInfoMapper.insert(hostInfoEntity);
    }

    /**
     * 删除主播信息
     * @param id 主播id
     * @return 是否删除成功
     */
    @Override
    public Integer delete(int id) {
        return hostInfoMapper.deleteById(id);
    }

}
