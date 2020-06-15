package com.hogwartstest.aitestmini.dto.task;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value="新增测试任务类",description="请求参数类" )
@Data
public class TestTaskDto {

    @ApiModelProperty(value="测试任务信息",required=true)
    private AddHogwartsTestTaskDto testTask;

    @ApiModelProperty(value="测试用例的id列表", example="12",required=true)
    private List<Integer> caseIdList;

}