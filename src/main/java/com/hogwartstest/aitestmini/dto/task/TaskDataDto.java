package com.hogwartstest.aitestmini.dto.task;

import com.hogwartstest.aitestmini.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value="执行测试任务类",description="请求参数类" )
@Data
public class TaskDataDto extends BaseDto {

    /**
     * 任务数量
     */
    @ApiModelProperty(value="任务数量",required=true)
    @NotNull
    private Integer taskCount;

    /**
     * 分类的key
     */
    @ApiModelProperty(value="分类的key", required = true)
    private Integer taskKey;

    /**
     * 描述
     */
    @ApiModelProperty(value="描述", required = true)
    private String desc;

}