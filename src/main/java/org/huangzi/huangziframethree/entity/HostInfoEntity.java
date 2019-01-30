package org.huangzi.huangziframethree.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: guohao
 * @date: "2018/12/18"
 * @description: 主播表
 */
@Data
@Accessors(chain = true)
@TableName("HOST_INFO")
public class HostInfoEntity {

    /**
     * 主播信息id
     */
    @TableId(value = "ID")
    @TableField("ID")
    private int id;

    /**
     * 主播房间号
     */
    @TableField("HOST_ROOM")
    private String hostRoom;

    /**
     * 主播姓名
     */
    @TableField("HOST_NAME")
    private String hostName;

    /**
     * 主播性别
     */
    @TableField("HOST_GENDER")
    private String hostGender;

    /**
     * 主播年龄
     */
    @TableField("HOST_AGE")
    private String hostAge;

    /**
     * 主播身份证号
     */
    @TableField("HOST_IDCARD")
    private String hostIdcard;

    /**
     * 主播电话
     */
    @TableField("HOST_PHONE")
    private String hostPhone;

    /**
     * 主播地址
     */
    @TableField("HOST_LOC")
    private String hostLoc;

    /**
     * 逻辑删除状态
     */
    @TableField("DELETE_FLAG")
    private int deleteFlag;

}
