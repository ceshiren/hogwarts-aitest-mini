package com.hogwartstest.aitestmini.util;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.jmeter.CustomBackendListenerClient;
import com.hogwartstest.aitestmini.common.jmeter.CustomInfluxdbBackendListenerClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jmeter.visualizers.backend.influxdb.InfluxdbBackendListenerClient;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
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
            , String runMode, HashTree testPlan, ReportGenerator reportGenerator) {
        BackendListener backendListener = new BackendListener();
        backendListener.setName(testId);
        Arguments arguments = new Arguments();
        arguments.addArgument(CustomBackendListenerClient.TEST_ID, testId);
        arguments.addArgument("influxdbMetricsSender", "org.apache.jmeter.visualizers.backend.influxdb.HttpMetricsSender");
        arguments.addArgument("influxdbUrl", "http://stuq.ceshiren.com:18086/write?db=jmeter");
        arguments.addArgument("application", "aitest1-"+testId);
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
        backendListener.setClassname(CustomBackendListenerClient.class.getCanonicalName());
        testPlan.add(testPlan.getArray()[0], backendListener);
    }

}


