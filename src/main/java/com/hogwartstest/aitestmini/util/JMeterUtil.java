package com.hogwartstest.aitestmini.util;

import com.hogwartstest.aitestmini.dto.testcase.RunCaseDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jmeter.visualizers.backend.influxdb.InfluxdbBackendListenerClient;
import org.apache.jorphan.collections.HashTree;

import java.lang.reflect.Field;

/**
 *@Author tlibn
 *@Date 2021/8/25 16:20
 **/
public class JMeterUtil {

    public static HashTree getHashTree(Object scriptWrapper) throws Exception {
        Field field = scriptWrapper.getClass().getDeclaredField("testPlan");
        field.setAccessible(true);
        return (HashTree) field.get(scriptWrapper);
    }

    public static void addBackendListener(String testId, String debugReportId
            , String runMode, HashTree testPlan, RunCaseDto runCaseDto) {
        BackendListener backendListener = new BackendListener();
        backendListener.setName(testId);
        Arguments arguments = new Arguments();
        arguments.addArgument("influxdbMetricsSender", "org.apache.jmeter.visualizers.backend.influxdb.HttpMetricsSender");
        //InfluxDB服务器
        arguments.addArgument("influxdbUrl", "http://39.107.221.71:8086/write?db=jmeter");
        //InfluxDB 服务器
        arguments.addArgument("application", runCaseDto.getApplication());
        //InfluxDB 服务器
        arguments.addArgument("measurement", "jmeter");

        arguments.addArgument("summaryOnly", "false");
        arguments.addArgument("samplersRegex", ".*");
        arguments.addArgument("percentiles", "99;95;90");
        arguments.addArgument("testTitle", "Test name");
        arguments.addArgument("eventTags", "");
        //arguments.addArgument(InfluxdbBackendListenerClient.TEST_ID, testId);
        if (StringUtils.isNotBlank(runMode)) {
            arguments.addArgument("runMode", runMode);
        }
        if (StringUtils.isNotBlank(debugReportId)) {
            arguments.addArgument("debugReportId", debugReportId);
        }
        backendListener.setArguments(arguments);
        backendListener.setClassname(InfluxdbBackendListenerClient.class.getCanonicalName());
        testPlan.add(testPlan.getArray()[0], backendListener);
    }

}


