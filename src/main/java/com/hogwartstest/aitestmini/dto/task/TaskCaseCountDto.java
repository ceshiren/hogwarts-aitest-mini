package com.hogwartstest.aitestmini.dto.task;

import com.hogwartstest.aitestmini.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value="执行测试任务类",description="请求参数类" )
@Data
public class TaskCaseCountDto extends BaseDto {

    /**
     * 任务总和
     */
    @ApiModelProperty(value="任务总和",required=true)
    @NotNull
    private Integer taskSum;

    /**
     * 任务数据对象
     */
    @ApiModelProperty(value="任务数据对象", required = true)
    private List<TaskDataDto> taskDataDtoList;

}