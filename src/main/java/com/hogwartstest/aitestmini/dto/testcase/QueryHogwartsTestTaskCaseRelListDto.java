package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author tlibn
 * @Date 2020/6/15 11:42
 **/

@ApiModel(value="查询任务关联的测试用例详情列表对象")
@Data
public class QueryHogwartsTestTaskCaseRelListDto extends BaseDto {

    @ApiModelProperty(value="用例标识")
    private String caseSign;

    @ApiModelProperty(value="任务id")
    private Integer taskId;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}
