package com.hogwartstest.aitestmini.common.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.util.JenkinsUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Queue;
import com.offbytwo.jenkins.model.*;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

@Component
@Slf4j
public class JenkinsClient {


	private final String PREFIX_TIPS = "调用Jenkins异常-";

	/**
	 *
	 * @param operateJenkinsJobDto
	 * @return
	 */
	public ResultDto operateJenkinsJob(OperateJenkinsJobDto operateJenkinsJobDto){

		return ResultDto.fail("暂未开放");

/*		log.info("operateJenkinsJobDto==  "+ JSONObject.toJSONString(operateJenkinsJobDto));

		HogwartsTestJenkins hogwartsTestJenkins = operateJenkinsJobDto.getHogwartsTestJenkins();
		Integer jobType = operateJenkinsJobDto.getJobType();
		TokenDto tokenDto = operateJenkinsJobDto.getTokenDto();
		Map<String, String> params = operateJenkinsJobDto.getParams();

		if(Objects.isNull(jobType)){
			return ResultDto.fail("Job类型不能为空");
		}


		if (StringUtils.isEmpty(hogwartsTestJenkins.getGitUrl())){
			return ResultDto.fail("Jenkins未配置git地址");
		}

		if (StringUtils.isEmpty(hogwartsTestJenkins.getGitBranch())){
			return ResultDto.fail("Jenkins未配置分支信息");
		}


		//拼接Job名称
		String jobName = "";
		String jobSign = "";

		if(Constants.JOB_TYPE_ONE.equals(jobType)){
			//拼接Job名称
			jobName = JenkinsUtil.getCreateCaseJobName(tokenDto.getUserId());
			jobSign = JenkinsUtil.getJobSignByName(jobName);
		}
		if(Constants.JOB_TYPE_TWO.equals(jobType)){
			//拼接Job名称
			jobName = JenkinsUtil.getCreateCaseJobName(tokenDto.getUserId());
			jobSign = JenkinsUtil.getJobSignByName(jobName);

		}

		log.info("=====拼接Job名称====："+ jobName);
		log.info("=====拼接Job标识====："+ jobSign);

		TestUserJenkinsJob testUserJenkinsJob = new TestUserJenkinsJob();
		testUserJenkinsJob.setJobName(jobName);
		testUserJenkinsJob.setProjectId(projectId);
		testUserJenkinsJob.setStatus(Constants.STATUS_ONE);
		TestUserJenkinsJob queryTestUserJenkinsJob = testUserJenkinsJobMapper.selectOne(testUserJenkinsJob);

		//获取Job的配置文件，用于创建或修改Job
		TestJenkinsJobConfig queryTestJenkinsJobConfig = new TestJenkinsJobConfig();
		queryTestJenkinsJobConfig.setJobSign(jobSign);
		queryTestJenkinsJobConfig.setStatus(Constants.STATUS_ONE);
		TestJenkinsJobConfig resultTestJenkinsJobConfig = testJenkinsJobConfigMapper.selectOne(queryTestJenkinsJobConfig);

		if(Objects.isNull(resultTestJenkinsJobConfig)){
			return ResultDto.fail("根据唯一标识未查到Job的配置数据,唯一标识为："+jobSign);
		}

		String jobXml = resultTestJenkinsJobConfig.getJobConfigXml();

		if(StringUtils.isEmpty(jobXml)){
			return ResultDto.fail("新建Job时的配置信息不能为空");
		}

		String utilSign = operateJenkinsJobDto.getUtilSign();
		Integer projectType = resultTestProject.getProjectType();
		if(StringUtils.isEmpty(utilSign)){
			ResultDto.fail("项目未指定测试命令类型");
		}

		if(Constants.JOB_TYPE_ONE.equals(jobType)){
			if(Constants.UTIL_SIGN_JUNIT_TEST.equals(utilSign)){
				ResultDto<String> resultDto2 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "parse_case_mvn_test_junit_command_1", systemRuntimeDataList);

				if(resultDto2.getResultCode()==0){
					return resultDto2;
				}
				jobXml = resultDto2.getData();
				ResultDto<String> resultDto3 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "parse_case_mvn_test_junit_tag_1", systemRuntimeDataList);

				if(resultDto3.getResultCode()==0){
					return resultDto3;
				}
				jobXml = resultDto3.getData();
			}
			if(Constants.UTIL_SIGN_PYTEST.equals(utilSign)){
				ResultDto<String> resultDto4 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "parse_case_pytest_command_1", systemRuntimeDataList);
				if(resultDto4.getResultCode()==0){
					return resultDto4;
				}
				jobXml = resultDto4.getData();

				ResultDto<String> resultDto5 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "parse_case_pytest_tag_1", systemRuntimeDataList);

				if(resultDto5.getResultCode()==0){
					return resultDto5;
				}
				jobXml = resultDto5.getData();
			}
		}
		if(Constants.JOB_TYPE_TWO.equals(jobType)){

			//遍历任务配置表，看有没有测试工具使用了allure报告

			String taskConfStr = operateJenkinsJobDto.getTaskConf();
			Integer useAllureFlagResult = getAllueFlag(taskConfStr);
			log.info("useAllureFlagResult== "+useAllureFlagResult);

			//使用了allure报告
			if(useAllureFlagResult==1){
				ResultDto<String> resultDto4 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "start_test_AllureReportPublisher_tag_1", systemRuntimeDataList);
				if(resultDto4.getResultCode()==0){
					return resultDto4;
				}
				jobXml = resultDto4.getData();
			//未使用了allure报告
			}else {
				ResultDto<String> resultDto4 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "parse_case_mvn_test_junit_tag_1", systemRuntimeDataList);
				if(resultDto4.getResultCode()==0){
					return resultDto4;
				}
				jobXml = resultDto4.getData();
			}

			//替换测试命令
			jobXml = xmlUtil.replaceSign(jobXml,"start_test_all_case",operateJenkinsJobDto.getTestCommandAll());

			//替换 mvn clean
			if(Constants.UTIL_SIGN_JUNIT_TEST.equals(utilSign)){
				ResultDto<String> resultDto4 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "start_test_mvn_test_junit_command_1", systemRuntimeDataList);
				if(resultDto4.getResultCode()==0){
					return resultDto4;
				}
				jobXml = resultDto4.getData();
			}

		}
		if(Constants.JOB_TYPE_THREE.equals(jobType)){
		}
		if(Constants.JOB_TYPE_FOUR.equals(jobType)){
		}
		if(Constants.JOB_TYPE_FIVE.equals(jobType)){
		}

		//暂时未用到
		if(Constants.JOB_TYPE_AMR.equals(jobType)){
			String amrInstallFlag = params.get("amrInstallFlag");

			if((!StringUtils.isEmpty(amrInstallFlag)) && amrInstallFlag.equals("1")){
				ResultDto<String> resultDto4 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "get_code_bygit_amr_install_list", systemRuntimeDataList);
				if(resultDto4.getResultCode()==0){
					return resultDto4;
				}
				jobXml = resultDto4.getData();
			}

		}
		if(Constants.JOB_TYPE_AMR_START_TEST.equals(jobType)){
		}

		if(Objects.isNull(projectType)){
			ResultDto.fail("项目未指定测试命令类型");
		}

		if(Objects.isNull(projectType)){
			return ResultDto.fail("项目类型不能为空");
		}

		//如果是app项目或者通用项目且为执行测试用例或安装app或自动遍历时
		if((ProjectConstants.PROJECT_TYPE_APP == projectType || ProjectConstants.PROJECT_TYPE_COMMON == projectType)
				&& (Constants.JOB_TYPE_TWO.equals(jobType)
				|| Constants.JOB_TYPE_FOUR.equals(jobType)
				||  Constants.JOB_TYPE_FIVE.equals(jobType))){

			ResultDto<String> resultDto4 = xmlUtil.getConfigBySignAndReplaceStr(jobXml, "start_test_stf_tag_1", systemRuntimeDataList);
			if(resultDto4.getResultCode()==0){
				return resultDto4;
			}
			jobXml = resultDto4.getData();

			jobXml = serviceUtil.setJobXmlDeviceList(resultTestProject, jobXml);

		}

		if(Objects.isNull(queryTestUserJenkinsJob)){
			log.info("=====新建Jenkins执行测试的Job====：");

			//创建Job
			log.info("=====开始创建执行测试的Jenkins的Job====：");
			createJob(null, jobName, jobXml, true, resultTestProject.getId(), resultTestProject.getDefaultJenkinsId());

			//Job数据入库
			TestUserJenkinsJob testUserJenkinsJob2 = new TestUserJenkinsJob();
			testUserJenkinsJob2.setProjectId(projectId);
			testUserJenkinsJob2.setTeamId(resultTestProject.getTeamId());
			testUserJenkinsJob2.setUserId(user.getId());
			testUserJenkinsJob2.setJobName(jobName);
			testUserJenkinsJob2.setJobType(jobType);
			testUserJenkinsJob2.setJenkinsJobConfigId(resultTestJenkinsJobConfig.getId());
			testUserJenkinsJob2.setStatus(Constants.STATUS_ONE);
			testUserJenkinsJob2.setCreateTime(new Date());
			testUserJenkinsJob2.setUpdateTime(new Date());
			log.info("=====开始创建Jenkins执行测试的Job-落库入参====："+ JSONObject.toJSONString(testUserJenkinsJob2));
			testUserJenkinsJobMapper.insertUseGeneratedKeys(testUserJenkinsJob2);

			if(Constants.JOB_TYPE_ONE.equals(jobType)){
				//更新项目信息表中的JenkinsJobId
				resultTestProject.setJobId(testUserJenkinsJob.getId());
				resultTestProject.setUpdateBy(user.getId());
				resultTestProject.setUpdateTime(new Date());
				testProjectMapper.updateByPrimaryKeySelective(resultTestProject);
			}

			queryTestUserJenkinsJob = testUserJenkinsJob2;
		}else {
			createOrUpdateJob(null, jobName, jobXml, true, resultTestProject.getId(), resultTestProject.getDefaultJenkinsId());
		}

		TestJenkinsDict testJenkinsDict = jenkinsFactory.getTestJenkinsDict(projectId, resultTestProject.getDefaultJenkinsId());
		log.info("执行测试-testJenkinsDict=  "+ JSONObject.toJSONString(testJenkinsDict));

		try{

			JenkinsHttpClient jenkinsHttpClient = jenkinsServerFactory.getJenkinsHttpClient(projectId, jenkinsServerId);

			Job job = getJob(jobName, jenkinsHttpClient, testJenkinsDict);

			//构建job
			if(Constants.OPER_TYPE_ONE.equals(operateType) || Constants.OPER_TYPE_TWO.equals(operateType)
					|| Constants.OPER_TYPE_THREE.equals(operateType)
					|| Constants.OPER_TYPE_FOUR.equals(operateType)
					|| Constants.OPER_TYPE_FIVE.equals(operateType)){
				QueueReference queueReference = build(job, params);

				log.info("=======queueReference== "+ JSONObject.toJSONString(queueReference));

				*//*JobWithDetails jobWithDetails = getJobWithDetails(job);
				Build currentBuild = currentBuild(jobWithDetails);*//*

				//构建数据入库
				TestUserJenkinsBuild testUserJenkinsBuild = new TestUserJenkinsBuild();
				testUserJenkinsBuild.setProjectId(resultTestProject.getId());
				testUserJenkinsBuild.setTeamId(resultTestProject.getTeamId());
				testUserJenkinsBuild.setUserId(user.getId());
				testUserJenkinsBuild.setAppId(operateJenkinsJobDto.getAppId());
				testUserJenkinsBuild.setCreateTime(new Date());
				testUserJenkinsBuild.setUpdateTime(new Date());
				testUserJenkinsBuild.setJobId(queryTestUserJenkinsJob.getId());
				testUserJenkinsBuild.setStatus(Constants.STATUS_ONE);

				//默认设置为0
				testUserJenkinsBuild.setBuildNumber(0L);
				testUserJenkinsBuild.setBuildParams(JSONObject.toJSONString(params));
				//暂时未用到
				testUserJenkinsBuild.setBuildQueueid("0");
				testUserJenkinsBuild.setReviseFlag(0);
				testUserJenkinsBuild.setQueueItemPartUrl(queueReference.getQueueItemUrlPart());
				testUserJenkinsBuild.setBuildType(jobType);
				testUserJenkinsBuild.setBuildResult(Constants.STATUS_ONE);
				testUserJenkinsBuild.setBuildStatus(Constants.STATUS_ONE);
				//暂时存放job的url
				testUserJenkinsBuild.setBuildUrl(job.getUrl());

				log.info("=====当前Jenkins执行测试的Job的构建信息-落库入参====："+ JSONObject.toJSONString(testUserJenkinsBuild));
				testUserJenkinsBuildMapper.insertUseGeneratedKeys(testUserJenkinsBuild);

				//不为空才操作
				if(Objects.nonNull(operateJenkinsJobDto.getTaskId())){
					//每次执行任务时都需要插入测试任务与Job关联数据
					TestTaskJob testTaskJob = new TestTaskJob();
					testTaskJob.setProjectId(projectId);
					testTaskJob.setTeamId(resultTestProject.getTeamId());
					testTaskJob.setTaskId(operateJenkinsJobDto.getTaskId());
					testTaskJob.setJobId(queryTestUserJenkinsJob.getId());
					testTaskJob.setBuildId(testUserJenkinsBuild.getId());
					testTaskJob.setCreateBy(user.getId());
					testTaskJob.setUpdateBy(user.getId());
					testTaskJob.setCreatetime(new Date());
					testTaskJob.setUpdatetime(new Date());
					testTaskJob.setStatus(Constants.STATUS_ONE);
					testTaskJob.setDelFlag(Constants.DEL_FLAG_ONE);
					testTaskJobMapper.insertUseGeneratedKeys(testTaskJob);
				}

				//不为空才操作
				Long buildRelationId = operateJenkinsJobDto.getBuildRelationId();
				if(Objects.nonNull(buildRelationId)){
					TestBuildRelation queryTestBuildRelation = new TestBuildRelation();
					queryTestBuildRelation.setProjectId(projectId);
					queryTestBuildRelation.setId(buildRelationId);

					TestBuildRelation testBuildRelation = testBuildRelationMapper.selectOne(queryTestBuildRelation);
					if(Objects.nonNull(testBuildRelation)){
						testBuildRelation.setPrevBuildId(testUserJenkinsBuild.getId());
						testBuildRelationMapper.updateByPrimaryKeySelective(testBuildRelation);
					}
				}

			}

			return ResultDto.success("成功");
		}catch (Exception e){
			String tips = PREFIX_TIPS + "操作Jenkins的Job异常"+e.getMessage();
			log.error(tips,e);
			throw new ServiceException("J0000",tips);
		}*/
	}

}
