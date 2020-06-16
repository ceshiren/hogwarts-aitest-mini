package com.hogwartstest.aitestmini.dto.jenkins;

import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author tlibn
 * @Date 2020/2/6 13:09
 **/
@Data
public class OperateJenkinsJobDto {


    private TokenDto tokenDto;


    private HogwartsTestJenkins hogwartsTestJenkins;

    //Job类型
    private Integer jobType;

    //构建参数
    private Map<String, String> params;

}
