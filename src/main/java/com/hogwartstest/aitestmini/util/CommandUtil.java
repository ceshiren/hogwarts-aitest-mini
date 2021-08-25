package com.hogwartstest.aitestmini.util;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.hogwartstest.aitestmini.dto.testcase.RunCaseDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestHis;
import com.hogwartstest.aitestmini.service.HogwartsTestHisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *@Author tlibn
 *@Date 2021/8/24 17:20
 **/
@Slf4j
@Component
public class CommandUtil {

    @Autowired
    private HogwartsTestHisService hogwartsTestHisService;

    @Value("${jmeter.result.path}")
    private String jmeterResultPath;

    public static void main(String[] args) {
        CommandUtil s = new CommandUtil();
        RunCaseDto runCaseDto = new RunCaseDto();
        runCaseDto.setHogwartsTestCommand("ping 127.0.0.1");
        s.run(runCaseDto);
    }
    public void run(RunCaseDto runCaseDto){
        Runtime run =Runtime.getRuntime();
        try {


            if(runCaseDto==null || runCaseDto.getHogwartsTestHis()==null){
                throw new ServiceException("执行完测试后，测试记录为空");
            }
            HogwartsTestHis hogwartsTestHis = runCaseDto.getHogwartsTestHis();

            Integer caseId = hogwartsTestHis.getCaseId();
            Integer hogwartsTestHisId = hogwartsTestHis.getId();

            /**
             * 拼接jmeter相关路径
             *
             */

            String root = jmeterResultPath + caseId + "_" + hogwartsTestHisId + "/";

            String jmxPath = root + hogwartsTestHisId + ".jmx";
            String logPath = root + "log/" + hogwartsTestHisId;
            String reportPath = root + "report/" + hogwartsTestHisId;

            FileUtil.saveTextFile(runCaseDto.getHogwartsTestCommand(), jmxPath);
            String logReadmeFile = logPath+"/readme.txt";
            FileUtil.saveTextFile("初始化", logReadmeFile);
            FileUtil.deleteFile(logReadmeFile);

            String reportReadmeFile = reportPath+"/readme.txt";
            FileUtil.saveTextFile("初始化", reportReadmeFile);
            FileUtil.deleteFile(reportReadmeFile);

            StringBuilder command = getCommand(jmxPath, logPath, reportPath);
            log.info("command.toString()=  " + command.toString());

            Process p = run.exec(command.toString());
            InputStream ins;
            ins = p.getInputStream();
            //InputStream ers= p.getErrorStream();
            new Thread(new inputStreamThread(ins, runCaseDto)).start();
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *  拼接测试命令
     *
     *  jmeter -n -t G:/ceba/jmeter_install_dir/demo/templatebaidu.jmx -l G:/ceba/jmeter_install_dir/demo/log/csvlog.log -e -o G:/ceba/jmeter_install_dir/demo/report
     * @param jmxPath
     * @param logPath
     * @param reportPath
     * @return
     */
    private StringBuilder getCommand(String jmxPath, String logPath, String reportPath) {
        StringBuilder command = new StringBuilder();

        command.append("jmeter -n -t ")
                .append(jmxPath).append(" ")
                .append(" -l ")
                .append(logPath)
                .append(" -e -o ")
                .append(reportPath);
        return command;
    }

    class inputStreamThread implements Runnable{
        private InputStream ins = null;
        private BufferedReader bfr = null;
        private RunCaseDto runCaseDto = null;
        public inputStreamThread(InputStream ins, RunCaseDto runCaseDto){
            this.ins = ins;
            this.bfr = new BufferedReader(new InputStreamReader(ins));
            this.runCaseDto = runCaseDto;
        }
        @Override
        public void run() {

            log.info(JSONObject.toJSONString(runCaseDto));

            String line = null;
            byte[] b = new byte[100];
            int num = 0;
            try {
                while((num=ins.read(b))!=-1){
                    System.out.println(new String(b,"gb2312"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            HogwartsTestHis hogwartsTestHis = runCaseDto.getHogwartsTestHis();
            hogwartsTestHis.setStatus(3);
            hogwartsTestHisService.update(hogwartsTestHis);
        }
    }
}

