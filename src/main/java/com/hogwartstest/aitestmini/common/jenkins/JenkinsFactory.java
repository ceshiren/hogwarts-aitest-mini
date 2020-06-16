package com.hogwartstest.aitestmini.common.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.hogwartstest.aitestmini.dao.HogwartsTestJenkinsMapper;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 获取Jenkins服务
 * @Author tlibn
 * @Date 2019/8/28 8:48
 **/

@Slf4j
@Component
public class JenkinsFactory {

    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;

    /**
     * 获取Jenkins的java客户端的JenkinsServer
     * @return
     */

    public JenkinsServer getJenkinsServer(Integer createUserId, Integer jenkinsId){
        JenkinsServer jenkinsServer = new JenkinsServer(getJenkinsHttpClient(createUserId, jenkinsId));
        return jenkinsServer;
    }

    /**
     * 获取Jenkins的java客户端的JenkinsServer
     * @return
     */

    public JenkinsServer getJenkinsServer(JenkinsHttpClient jenkinsHttpClient){
        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
        return jenkinsServer;
    }

    /**
     * 获取Jenkins的java客户端的JenkinsHttpClient
     * @return
     */


    public JenkinsHttpClient getJenkinsHttpClient(Integer createUserId, Integer jenkinsId){
        JenkinsHttpClient jenkinsHttpClient = null;
        try {

            HogwartsTestJenkins result = getHogwartsTestJenkins(createUserId, jenkinsId);

            log.info("getJenkinsHttpClient--HogwartsTestJenkins=  "+ JSONObject.toJSONString(result));

            jenkinsHttpClient = new JenkinsHttpClient(new URI(result.getUrl()), result.getUserName(), result.getPassword());
        } catch (URISyntaxException e) {
            String tips = "获取Jenkins服务异常"+e.getMessage();
            log.error(tips,e);
            throw new ServiceException(tips);
        }
        return jenkinsHttpClient;
    }

    /**
     * 根据环境信息获取数据库中配置的Jenkins服务信息
     * @return
     */

    public HogwartsTestJenkins getHogwartsTestJenkins(Integer createUserId, Integer jenkinsId){


        HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();

        queryHogwartsTestJenkins.setId(jenkinsId);
        queryHogwartsTestJenkins.setCreateUserId(createUserId);


        HogwartsTestJenkins result = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

        if(Objects.isNull(result)){
            throw new ServiceException("未查到Jenkins信息");
        }

        return result;
    }
}
