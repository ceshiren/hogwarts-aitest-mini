package com.hogwartstest.aitestmini.common.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 获取Jenkins服务
 * @Author tlibn
 * @Date 2019/8/28 8:48
 **/

@Slf4j
@Component
public class JenkinsFactory {


    @Value("${jenkins.url}")
    private String jenkinsUrl;
    @Value("${jenkins.username}")
    private String jenkinsUserName;
    @Value("${jenkins.password}")
    private String jenkinsPassword;
    @Value("${jenkins.casetype}")
    private Integer jenkinsCaseType;
    @Value("${jenkins.casesuffix}")
    private String jenkinsCaseSuffix;
    @Value("${jenkins.testcommand}")
    private String jenkinsTestCommand;

    /**
     * 获取Jenkins的java客户端的JenkinsServer
     * @return
     */

    public JenkinsServer getJenkinsServer(){
        JenkinsServer jenkinsServer = new JenkinsServer(getJenkinsHttpClient());
        return jenkinsServer;
    }

    /**
     * 获取Jenkins的java客户端的JenkinsServer
     * @return
     */

/*    public JenkinsServer getJenkinsServer(JenkinsHttpClient jenkinsHttpClient){
        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
        return jenkinsServer;
    }*/

    /**
     * 获取Jenkins的java客户端的JenkinsHttpClient
     * @return
     */


    public JenkinsHttpClient getJenkinsHttpClient(){
        JenkinsHttpClient jenkinsHttpClient = null;
        try {

            //Jenkins配置

            log.info("getJenkinsHttpClient--HogwartsTestJenkins=  "+ JSONObject.toJSONString(""));

            jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsUrl), jenkinsUserName, jenkinsPassword);
        } catch (URISyntaxException e) {
            String tips = "获取Jenkins服务异常"+e.getMessage();
            log.error(tips,e);
            throw new ServiceException(tips);
        }
        return jenkinsHttpClient;
    }
}
