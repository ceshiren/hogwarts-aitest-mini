package com.hogwartstest.aitestmini.dto.testcase;

import com.hogwartstest.aitestmini.entity.HogwartsTestHis;
import lombok.Data;

/**
 *@Author tlibn
 *@Date 2021/8/24 17:27
 **/
@Data
public class RunCaseDto {

    private String hogwartsTestCommand;

    private HogwartsTestHis hogwartsTestHis;

}
