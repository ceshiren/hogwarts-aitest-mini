package com.hogwartstest.aitestmini.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RequestInfoDto {

    //请求的接口地址
    @ApiModelProperty(value="请求的接口地址，用于拼装命令",hidden=true)
    private String  requestUrl;
    //请求的服务器地址
    @ApiModelProperty(value="请求的服务器地址，用于拼装命令",hidden=true)
    private String  baseUrl;

    //文件服务器地址
    @ApiModelProperty(value="文件服务器地址，用于拼装命令",hidden=true)
    private String  fileServer;

    //token
    @ApiModelProperty(value="token信息，用于拼装命令",hidden=true)
    private String  token;

    /**
     * ID
     */
    @ApiModelProperty(value="操作类型",example = "2")
    private Integer operType;

}