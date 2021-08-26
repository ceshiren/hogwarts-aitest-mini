package com.hogwartstest.aitestmini.util;

import com.hogwartstest.aitestmini.common.jmeter.MessageCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jorphan.collections.HashTree;

@Slf4j
public class LocalRunner {
    private HashTree jmxTree;

    public LocalRunner(HashTree jmxTree) {
        this.jmxTree = jmxTree;
    }

    public LocalRunner() {
    }

    public void run(String report) {
        JMeterEngine engine = new StandardJMeterEngine();
        engine.configure(jmxTree);
        try {
            engine.runTest();
            MessageCache.runningEngine.put(report, engine);
        } catch (JMeterEngineException e) {
            engine.stopTest(true);
        }
    }

    public void stop(String report) {
        try {
            JMeterEngine engine = MessageCache.runningEngine.get(report);
            if (engine != null) {
                engine.stopTest();
                MessageCache.runningEngine.remove(report);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
