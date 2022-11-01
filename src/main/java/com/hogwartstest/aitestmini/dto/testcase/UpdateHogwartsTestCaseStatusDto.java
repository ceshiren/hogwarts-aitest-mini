package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="修改测试用例对象")
@Data
public class UpdateHogwartsTestCaseStatusDto extends BaseEntityNew {
    /**
     * 主键
     */
    @ApiModelProperty(value="测试用例主键",required=true)
    private Integer caseId;

    /**
     * 备注
     */
    @ApiModelProperty(value="状态",required=true)
    private Integer status;

}
