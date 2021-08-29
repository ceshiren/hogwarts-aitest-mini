package com.hogwartstest.aitestmini.dto.testcase;

import lombok.Data;

import java.util.List;

/**
 *@Author tlibn
 *@Date 2021/8/27 17:00
 **/
@Data
public class RunCaseDto {

    private Integer caseId;
    private Integer createUserId;
    private String application;

    private List<RunCaseParamsDto> params;

}
