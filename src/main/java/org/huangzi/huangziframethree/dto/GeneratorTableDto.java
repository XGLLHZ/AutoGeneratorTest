package net.qmgf.frame.biz.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.qmgf.frame.common.base.model.BaseEntityDto;

import java.util.Date;

/**
 * @author: guohao
 * @date: "2019/1/22"
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "职能表")
public class GeneratorTableDto extends BaseEntityDto {

    /**
     * 数据库表名
     */
    @ApiModelProperty(value = "数据库表名")
    private String tableName;

    /**
     * 数据库描述
     */
    @ApiModelProperty(value = "数据库描述")
    private String tableComments;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
