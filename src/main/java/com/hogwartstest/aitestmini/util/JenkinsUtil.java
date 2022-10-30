package com.hogwartstest.aitestmini.util;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Jenkins工具类
 * @Author tlibn
 * @Date 2019/8/28 8:48
 **/

@Slf4j
public class JenkinsUtil {

    public static void main(String[] args) {

    }

    /**
     * 获取执行测试的Job名称
     * @return
     */

    public static String getStartTestJobName(Integer caseId){
        String jobName = "hogwarts_test_mini_"+caseId;
        return jobName;
    }

    /**
     * 获取执行测试的Job名称
     * @return
     */

    public static String getJobSignByName(String jobName){
        if(StringUtils.isEmpty(jobName) || !jobName.contains("_")){
            return "";
        }
        String jobSign = jobName.substring(0, jobName.lastIndexOf("_"));
        return jobSign;
    }

    public static StringBuilder getUpdateTaskStatusUrl(String baseUrl, HogwartsTestCase hogwartsTestCase) {

        StringBuilder updateStatusUrl = new StringBuilder();

        updateStatusUrl.append("curl -X PUT ");
        updateStatusUrl.append("\""+baseUrl + "/testCase/status \" ");
        updateStatusUrl.append("-H \"Content-Type: application/json \" ");
        updateStatusUrl.append("-d ");
        JSONObject json = new JSONObject();

        json.put("caseId",hogwartsTestCase.getId());
        json.put("status", Constants.STATUS_THREE);

        updateStatusUrl.append("'"+json.toJSONString()+"'");

        return updateStatusUrl;
    }



}
