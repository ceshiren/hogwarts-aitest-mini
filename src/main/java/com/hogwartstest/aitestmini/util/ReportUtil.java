package com.hogwartstest.aitestmini.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Jenkins工具类
 * @Author tlibn
 * @Date 2019/8/28 8:48
 **/

@Slf4j
public class ReportUtil {

    public static void main(String[] args) {

        String buildUrl = "http:///job/hogwarts_test_mini_start_test_1/label=jenkins_slave/2/allure/";

         String allureReportBaseUrl = buildUrl.substring(buildUrl.indexOf("/job"));

        System.out.println("allureReportBaseUrl== "+allureReportBaseUrl);

    }

}
