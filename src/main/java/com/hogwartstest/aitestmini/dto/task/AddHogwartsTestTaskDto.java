package com.hogwartstest.aitestmini.dto.task;

import com.hogwartstest.aitestmini.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="添加任务对象")
@Data
public class AddHogwartsTestTaskDto extends BaseEntityNew {
    /**
     * 名称
     */
    @ApiModelProperty(value="任务名称",required=true)
    private String name;

    /**
     * 运行测试的Jenkins服务器id
     */
    @ApiModelProperty(value="运行测试的Jenkins服务器id",required=true)
    private Integer testJenkinsId;

    /**
     * 备注
     */
    @ApiModelProperty(value="任务备注",required=true)
    private String remark;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}