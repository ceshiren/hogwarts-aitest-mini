package com.hogwartstest.aitestmini.common.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestUserMapper;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;
import com.hogwartstest.aitestmini.util.FileUtil;
import com.hogwartstest.aitestmini.util.JenkinsUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class JenkinsClient {


	@Autowired
	private HogwartsTestUserMapper hogwartsTestUserMapper;

	@Autowired
	private JenkinsFactory jenkinsServerFactory;

	private final String PREFIX_TIPS = "调用Jenkins异常-";

	/**
	 *
	 * @param operateJenkinsJobDto
	 * @return
	 */
	public ResultDto operateJenkinsJob(OperateJenkinsJobDto operateJenkinsJobDto) throws IOException {

		log.info("operateJenkinsJobDto==  "+ JSONObject.toJSONString(operateJenkinsJobDto));

		HogwartsTestJenkins hogwartsTestJenkins = operateJenkinsJobDto.getHogwartsTestJenkins();
		TokenDto tokenDto = operateJenkinsJobDto.getTokenDto();
		Map<String, String> params = operateJenkinsJobDto.getParams();

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		queryHogwartsTestUser.setId(tokenDto.getUserId());
		HogwartsTestUser resultHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);

		//拼接Job名称
		String jobName = JenkinsUtil.getStartTestJobName(tokenDto.getUserId());
		String jobSign = JenkinsUtil.getJobSignByName(jobName);

		log.info("=====拼接Job名称====："+ jobName);
		log.info("=====拼接Job标识====："+ jobSign);

		if(StringUtils.isEmpty(jobSign)){
			return ResultDto.fail("Jenkins的Job标识不符合规范");
		}
		ClassPathResource classPathResource = new ClassPathResource("jenkinsDir/"+jobSign+".xml");
		InputStream inputStream =classPathResource.getInputStream();

		String jobXml = FileUtil.getText(inputStream);

		log.info("jobXml" + jobXml);

		if(StringUtils.isEmpty(jobXml)){
			return ResultDto.fail("Job配置信息不能为空");
		}

		//获取根据job类型获取数据库中对应的job名称
		String dbJobName = resultHogwartsTestUser.getStartTestJobName();

		if(StringUtils.isEmpty(dbJobName)){
			log.info("=====新建Jenkins执行测试的Job====：");

			createOrUpdateJob(jobName, jobXml, tokenDto.getUserId(), tokenDto.getDefaultJenkinsId(), 1);

            resultHogwartsTestUser.setStartTestJobName(jobName);
            hogwartsTestUserMapper.updateByPrimaryKeySelective(resultHogwartsTestUser);

		}else {
			createOrUpdateJob(jobName, jobXml, tokenDto.getUserId(), tokenDto.getDefaultJenkinsId(), 0);
		}

		try{

			JenkinsHttpClient jenkinsHttpClient = jenkinsServerFactory.getJenkinsHttpClient(tokenDto.getUserId(), tokenDto.getDefaultJenkinsId());

			Job job = getJob(jobName, jenkinsHttpClient, hogwartsTestJenkins.getUrl());
			build(job, params);
			return ResultDto.success("成功");
		}catch (Exception e){
			String tips = PREFIX_TIPS + "操作Jenkins的Job异常"+e.getMessage();
			log.error(tips,e);
			throw new ServiceException(tips);
		}

	}

	/**
	 * 创建或更新Jenkins的Job
	 *
	 * @param jobName
	 * @param jobXml
	 * @param createUserId
	 * @param jenkinsId
	 * @param createJobFlag 1 是 0 否
	 * @return
	 */
	public ResultDto createOrUpdateJob(String jobName, String jobXml, Integer createUserId, Integer jenkinsId, Integer createJobFlag) {

		JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer(createUserId, jenkinsId);

		try{

			if(Objects.nonNull(createJobFlag)&&createJobFlag==1){
				jenkinsServer.createJob(null, jobName, jobXml, true);
			}else {
				jenkinsServer.updateJob(null,jobName,jobXml,true);
			}
			return ResultDto.success("成功");
		}catch (Exception e){
			String tips = PREFIX_TIPS + "创建或更新Jenkins的Job时异常"+e.getMessage();
			log.error(tips,e);
			throw new ServiceException(tips);
		}
	}

	/**
	 *  获取Job
	 * @param jobName
	 * @param jenkinsHttpClient
	 * @return
	 */
	private Job getJob(String jobName, JenkinsHttpClient jenkinsHttpClient, String jenkinsBaseUrl) {
		///job/test_aitestbuild/api/json  404  jobName后面需加/，后面改源码
		Job job = new Job(jobName,jenkinsBaseUrl+"job/"+jobName+"/");
		job.setClient(jenkinsHttpClient);
		return job;
	}


	/**
	 *  构建job - 无参方式
	 * @param job
	 * @return
	 */
	private QueueReference build(Job job) throws IOException {
		return build(job,null);
	}

	/**
	 *  构建job - 有参方式
	 * @param job
	 * @return
	 */
	private QueueReference build(Job job, Map<String, String> params) throws IOException {
		QueueReference queueReference = null;
		if(Objects.isNull(params)){
			queueReference = job.build(true);
		}else {
			queueReference = job.build(params, true);
		}
		log.info("queueReference==  "+JSONObject.toJSONString(queueReference));
		return queueReference;
	}

}
