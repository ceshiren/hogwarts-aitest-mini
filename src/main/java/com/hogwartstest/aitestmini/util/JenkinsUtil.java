package com.hogwartstest.aitestmini.util;

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
     * 获取生成测试用例的Job名称
     * @return
     */

    public static String getCreateCaseJobName(Integer createUserId){
        String jobName = "mini_auto_create_"+createUserId;
        return jobName;
    }

    /**
     * 获取执行测试的Job名称
     * @return
     */

    public static String getStartTestJobName(Integer createUserId){
        String jobName = "mini_task_"+createUserId;
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


}
