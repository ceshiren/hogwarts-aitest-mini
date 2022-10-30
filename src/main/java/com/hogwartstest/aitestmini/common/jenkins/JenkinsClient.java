package com.hogwartstest.aitestmini.common.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartstest.aitestmini.util.FileUtil;
import com.hogwartstest.aitestmini.util.JenkinsUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class JenkinsClient {

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

		Map<String, String> params = operateJenkinsJobDto.getParams();

		///todo 拼接Job名称
		String jobName = JenkinsUtil.getStartTestJobName(operateJenkinsJobDto.getCaseId());
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

		createOrUpdateJob(jobName, jobXml);

		try{

			JenkinsHttpClient jenkinsHttpClient = jenkinsServerFactory.getJenkinsHttpClient();

			Job job = getJob(jobName, jenkinsHttpClient, jenkinsUrl);
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
	 * @return
	 */
	public ResultDto createOrUpdateJob(String jobName, String jobXml) {

		JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer();

		try{

			jenkinsServer.updateJob(null,jobName,jobXml,true);

			return ResultDto.success("成功");
		}catch (Exception e){
			try {
				jenkinsServer.createJob(null, jobName, jobXml, true);
			} catch (IOException e1) {
				return ResultDto.success("创建job失败");
			}
			return ResultDto.success("成功");
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
