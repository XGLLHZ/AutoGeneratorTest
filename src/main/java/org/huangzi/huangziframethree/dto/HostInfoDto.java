package org.huangzi.huangziframethree.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: guohao
 * @date: "2018/12/18"
 * @description: 主播表实体
 */
@Data
@Accessors(chain = true)
public class HostInfoDto extends BaseDto {

    /**
     * 主播信息id
     */
    private int id;

    /**
     * 主播房间号
     */
    private String hostRoom;

    /**
     * 主播姓名
     */
    private String hostName;

    /**
     * 主播性别
     */
    private String hostGender;

    /**
     * 主播年龄
     */
    private String hostAge;

    /**
     * 主播身份证号
     */
    private String hostIdcard;

    /**
     * 主播电话
     */
    private String hostPhone;

    /**
     * 主播地址
     */
    private String hostLoc;

    /**
     * 逻辑删除状态
     */
    private int deleteFlag;

}
