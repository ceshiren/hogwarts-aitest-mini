package com.hogwartstest.aitestmini.dto.testcase;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class SaveTestCaseListDto {

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

    @ApiModelProperty(value="测试用例列表",required=true)
    private List<AddHogwartsTestCaseDto> testCaseList;

}