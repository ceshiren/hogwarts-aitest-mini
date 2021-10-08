package com.hogwartstest.aitestmini.common.jmeter;

import org.apache.jmeter.engine.JMeterEngine;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//不推荐这样做，不然服务会有状态，无法横向扩展
public class MessageCache {
    public static Map<String, ReportCounter> cache = new HashMap();

    public static ConcurrentHashMap<String, Session> reportCache = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, JMeterEngine> runningEngine = new ConcurrentHashMap<>();

}
