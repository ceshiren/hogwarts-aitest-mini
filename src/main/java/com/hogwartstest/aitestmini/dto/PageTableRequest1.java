package com.hogwartstest.aitestmini.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页查询参数
 *
 */

@ApiModel(value="列表查询的分页参数",description="请求参数类" )
@Data
public class PageTableRequest1 implements Serializable {

	private static final long serialVersionUID = 7328071045193618467L;

	@ApiModelProperty(value="页码",required=true, example = "1")
	private Integer pageNum = 1;
	@ApiModelProperty(value="每页数据量",required=true, example = "10")
	private Integer pageSize = 10;
	@ApiModelProperty(value="特定查询参数",required=true, example = "status=1")
	private Map<String, Object> params;
}
