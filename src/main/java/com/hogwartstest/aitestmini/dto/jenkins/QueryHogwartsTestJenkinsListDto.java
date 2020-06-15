package com.hogwartstest.aitestmini.dto.jenkins;

import com.hogwartstest.aitestmini.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author tlibn
 * @Date 2020/6/15 11:42
 **/

@ApiModel(value="查询Jenkins信息列表对象")
@Data
public class QueryHogwartsTestJenkinsListDto extends BaseDto {

    @ApiModelProperty(value="Jenkins名称")
    private String name;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}
