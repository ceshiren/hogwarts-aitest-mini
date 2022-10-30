package com.hogwartstest.aitestmini.dto.jenkins;

import lombok.Data;

import java.util.Map;

/**
 * @Author tlibn
 * @Date 2020/2/6 13:09
 **/
@Data
public class OperateJenkinsJobDto {

    private Integer caseId;
    //构建参数
    private Map<String, String> params;

}
