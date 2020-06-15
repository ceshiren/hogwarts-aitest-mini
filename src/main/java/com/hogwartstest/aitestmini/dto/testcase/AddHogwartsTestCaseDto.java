package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="添加测试用例对象")
@Data
public class AddHogwartsTestCaseDto extends BaseEntityNew {

    /**
     * 包名
     */
    @ApiModelProperty(value="测试用例包名",required=true)
    private String packageName;

    /**
     * 类名
     */
    @ApiModelProperty(value="测试用例类名",required=true)
    private String className;

    /**
     * 方法名
     */
    @ApiModelProperty(value="测试用例方法名",required=true)
    private String methodName;

    /**
     * 用例标识
     */
    @ApiModelProperty(value="测试用例标识",required=true)
    private String caseSign;

    /**
     * 备注
     */
    @ApiModelProperty(value="测试用例备注",required=true)
    private String remark;

}