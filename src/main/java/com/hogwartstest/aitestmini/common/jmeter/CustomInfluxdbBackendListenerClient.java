package com.hogwartstest.aitestmini.common.jmeter;

import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.apache.jmeter.visualizers.backend.influxdb.InfluxdbBackendListenerClient;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *@Author tlibn
 *@Date 2021/8/25 16:47
 **/
public class CustomInfluxdbBackendListenerClient extends InfluxdbBackendListenerClient {

    private final List<SampleResult> queue = new ArrayList<>();

    public final static String TEST_ID = "ms.test.id";

    //获得控制台内容。
    private PrintStream oldPrintStream = System.out;
    private ByteArrayOutputStream bos = new ByteArrayOutputStream();

    private void setConsole() {
        System.setOut(new PrintStream(bos));
    }

    private String getConsole() {
        System.setOut(oldPrintStream);
        return bos.toString();
    }
    // engine执行之前会进行前置处理器
    @Override
    public void setupTest(BackendListenerContext context) throws Exception {
        setConsole();
        super.setupTest(context);
    }
    //engine执行中的处理
    @Override
    public void handleSampleResults(List<SampleResult> sampleResults, BackendListenerContext context) {
        //结果集添加至集合中
        queue.addAll(sampleResults);
    }
    //engine结束后的后置处理器
    @Override
    public void teardownTest(BackendListenerContext context) throws Exception {

       /* String reportGenerator = context.getParameter("reportGenerator");
        ReportGenerator generator = JSON.parseObject(reportGenerator, ReportGenerator.class);
        generator.generate();*/

        //处理结果集中的数据并封装至JMeterRequestResult对象中
        List<JMeterRequestResult> jMeterRequestResults = Lists.newArrayList();
        String testId = context.getParameter("testId");
        queue.stream().forEach(result -> {
            setRequestResult(result, jMeterRequestResults);
        });
        queue.clear();
        //runJMeterRequestService.addDebugResult(testId, jMeterRequestResults);
        super.teardownTest(context);
    }

    private void setRequestResult(SampleResult result, List<JMeterRequestResult> jMeterRequestResults) {

        JMeterRequestResult metricResult = new JMeterRequestResult();
        Long responseTime = result.getEndTime() - result.getStartTime();
        metricResult.setUrl(result.getUrlAsString());
        metricResult.setResponseSize(((Integer) result.getBodySize()).toString());
        metricResult.setResponseTime(responseTime.toString());
        metricResult.setResponseResult(result.getResponseDataAsString());
        metricResult.setConsoleResult(getConsole());
        if (result instanceof HTTPSampleResult) {
            HTTPSampleResult res = (HTTPSampleResult) result;
            metricResult.setCookie(res.getCookies());
            metricResult.setRequestMethod(res.getHTTPMethod());
        }
        metricResult.setRequestData(result.getSamplerData());
        metricResult.setResponseHeader(result.getResponseHeaders());
        metricResult.setRequestHeader(result.getRequestHeaders());
        metricResult.setStatusCode(result.getResponseCode());
        jMeterRequestResults.add(metricResult);
    }
}
