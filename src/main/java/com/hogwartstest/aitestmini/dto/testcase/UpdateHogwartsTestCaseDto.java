package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value="修改测试用例对象")
@Data
public class UpdateHogwartsTestCaseDto extends BaseEntityNew {
    /**
     * 主键
     */
    @ApiModelProperty(value="测试用例主键",required=true)
    private Integer id;

    /**
     * 测试用例模板
     */
    @ApiModelProperty(value="测试用例模板", notes = "文件类型case时不传值", required=true)
    private String caseTemplate;

    /**
     * 用例名称
     */
    @ApiModelProperty(value="测试用例名称",required=true)
    private String caseName;

    /**
     * 备注
     */
    @ApiModelProperty(value="测试用例备注",required=true)
    private String remark;

    /**
     * 测试模板参数
     */
    @ApiModelProperty(value="测试模板参数")
    private List<RunCaseParamsDto> params;


}
