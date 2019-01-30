package org.huangzi.huangziframethree.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: guohao
 * @date: 2019/1/30/11:30
 * @description: 基础实体模型
 */
@Data
@Accessors(chain = true)
public class BaseDto {

    /**
     * 分页参数-当前页数
     */
    public Integer currentPage;

    /**
     * 分页参数-页面大小
     */
    public Integer pageSize;

}
