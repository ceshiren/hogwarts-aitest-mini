package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value="执行测试任务类",description="请求参数类" )
@Data
public class StartTestDto extends BaseDto {

    /**
     * ID
     */
    @ApiModelProperty(value="测试用例id",required=true, example = "112")
    @NotNull
    private Integer caseId;

}
