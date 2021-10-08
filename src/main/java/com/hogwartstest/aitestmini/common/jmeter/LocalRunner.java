package com.hogwartstest.aitestmini.common.jmeter;

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
        } catch (JMeterEngineException e) {
            engine.stopTest(true);
        }
    }
}
