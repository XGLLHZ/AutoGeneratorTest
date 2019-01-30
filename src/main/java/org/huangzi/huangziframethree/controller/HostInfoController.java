package org.huangzi.huangziframethree.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.huangzi.huangziframethree.dto.HostInfoDto;
import org.huangzi.huangziframethree.service.IHostInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: guohao
 * @date: "2018/12/18"
 * @description: 主播表前端控制器
 */
@RestController
@RequestMapping("api/hostinfo")
public class HostInfoController {

    @Autowired
    IHostInfoService hostInfoService;

    /**
     * 获取主播信息列表
     * @param hostInfoDto 查询参数
     * @return 主播信息列表
     */
    @RequestMapping("/list")
    public List<HostInfoDto> list(@RequestBody @Param("hostInfoDto") HostInfoDto hostInfoDto){
        Page<HostInfoDto> page = hostInfoService.list(hostInfoDto);
        List<HostInfoDto> list = page.getRecords();
        return list;
    }

    /**
     * 新增主播信息
     * @param hostInfoDto 主播信息
     * @return 是否新增成功
     */
    @RequestMapping("/insert")
    public Integer insert(@RequestBody @Param("hostInfoDto") HostInfoDto hostInfoDto){
        return hostInfoService.insert(hostInfoDto);
    }

    /**
     * 删除主播信息
     * @param hostInfoDto 主播id
     * @return 是否删除成功
     */
    @RequestMapping("/delete")
    public Integer delete(@RequestBody @Param("hostInfoDto") HostInfoDto hostInfoDto){
        return hostInfoService.delete(hostInfoDto.getId());
    }

    /**
     * 天空它像什么
     * 爱情就像什么
     * 几朵云在天边忘了该往哪儿走
     * 思念和寂寞
     * 被吹进了左耳
     * 也许我记不住
     * 可是也忘不掉
     * 那时候
     * 那种秘密的快乐
     * 听阴天说什么
     * 在昏暗中的我
     * 想对着天讲 说无论如何 阴天快乐
     * 叫阴天别闹了
     * 想念你都那么久那么久了
     * 我一回头
     * 就看见了
     * 那个笑容
     */

}

