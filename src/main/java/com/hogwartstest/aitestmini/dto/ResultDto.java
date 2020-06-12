package com.hogwartstest.aitestmini.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 响应返回类
 * @author tlibn
 * @description
 * @create 2019-08-02
 */
@ApiModel(value="基础返回类",description="基础返回类")
public class ResultDto<T> implements Serializable {

    private static final long serialVersionUID = -7472879865481412372L;

    /**
     * 结果状态码 1 成功 0 失败
     */

    @ApiModelProperty(value="返回结果码", example="1",allowableValues = "1,0")
    private Integer resultCode;

    /**
     * 可为空,由业务接口设置
     */
    @ApiModelProperty(value="提示信息", example="成功",allowableValues = "成功,失败")
    private String message = "";

    /**
     * 响应结果数据,对象/array类型,对应后端的 Bean/List
     */

    @ApiModelProperty(value="具体响应数据")
    private T data = null;



    private Integer pageSize;       //每页多少条
    private Integer pageIndex;    //当前页面
    private Integer totalSize;      //总条数

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public static ResultDto newInstance(){
        return new ResultDto();
    }

    /**
     * 设置为成功状态
     */
    public void setAsSuccess() {
        this.resultCode = 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultDto success(String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        return resultDto;
    }
    public static <T> ResultDto<T> success(String message,T data){
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }

    /**
     * 设置为失败状态
     */
    public void setAsFailure() {
        this.resultCode = 0;
    }

    public static <T> ResultDto<T> fail(String message){
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        return resultDto;
    }

    public static <T> ResultDto<T> fail(String message,T data){
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

}
