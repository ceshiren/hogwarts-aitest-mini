package com.hogwartstest.aitestmini.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.Token;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.common.jenkins.JenkinsClient;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dao.HogwartsTestJenkinsMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestTaskMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestUserMapper;
import com.hogwartstest.aitestmini.dto.RequestInfoDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;
import com.hogwartstest.aitestmini.service.HogwartsTestUserService;
import com.hogwartstest.aitestmini.util.DateUtil;
import com.hogwartstest.aitestmini.util.JenkinsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class HogwartsTestUserServiceImpl implements HogwartsTestUserService {


	@Autowired
	private HogwartsTestUserMapper hogwartsTestUserMapper;

	@Autowired
	private HogwartsTestTaskMapper hogwartsTestTaskMapper;

	@Autowired
	private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;

	@Autowired
	private JenkinsClient jenkinsClient;

	@Autowired
	private TokenDb tokenDb;


	@Override
	public ResultDto<HogwartsTestUser> getById(Integer id) {

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		queryHogwartsTestUser.setId(id);

		HogwartsTestUser resultHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);

		if(Objects.isNull(resultHogwartsTestUser)){
			return ResultDto.fail("用户不存在");
		}

		return ResultDto.success("成功",resultHogwartsTestUser);
	}

	@Override
	public ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser) {

		String userName = hogwartsTestUser.getUserName();
		String password = hogwartsTestUser.getPassword();

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		queryHogwartsTestUser.setUserName(userName);

		HogwartsTestUser resultHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);

		if(Objects.nonNull(resultHogwartsTestUser)){
			return ResultDto.fail("用户名已存在");
		}

		String newPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+password).getBytes());
		hogwartsTestUser.setPassword(newPwd);
		hogwartsTestUser.setCreateTime(new Date());
		hogwartsTestUser.setUpdateTime(new Date());

		hogwartsTestUserMapper.insert(hogwartsTestUser);

		return ResultDto.success("成功", hogwartsTestUser);
	}


	@Override
	public ResultDto<Token> login(String userName, String password) {

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		String newPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+password).getBytes());
		queryHogwartsTestUser.setUserName(userName);
		queryHogwartsTestUser.setPassword(newPwd);

		HogwartsTestUser resultHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);

		if(Objects.isNull(resultHogwartsTestUser)){
			return ResultDto.fail("用户不存在或密码错误");
		}

		Token token = new Token();

		String tokenStr = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + userName+password).getBytes());

		token.setToken(tokenStr);

		TokenDto tokenDto = new TokenDto();
		tokenDto.setUserId(resultHogwartsTestUser.getId());
		tokenDto.setUserName(userName);
		tokenDto.setToken(tokenStr);
		tokenDto.setDefaultJenkinsId(resultHogwartsTestUser.getDefaultJenkinsId());

		tokenDb.addTokenDto(tokenStr, tokenDto);

		return ResultDto.success("成功", token);
	}

	/**
	 * 作业
	 *
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@Override
	public ResultDto<HogwartsTestUser> changePassword(String username, String oldPassword, String newPassword) {
		return null;
	}

	/**
	 * 生成测试用例
	 *
	 * @param tokenDto
	 * @param requestInfoDto
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<HogwartsTestUser> parse(TokenDto tokenDto, RequestInfoDto requestInfoDto) throws IOException {

		log.info("=====生成测试用例-Service入参====："+ JSONObject.toJSONString(tokenDto) +"+++++"+ JSONObject.toJSONString(requestInfoDto));

		//拼接Job名称
		String jobName = JenkinsUtil.getCreateCaseJobName(tokenDto.getUserId());

		Integer defaultJenkinsId = tokenDto.getDefaultJenkinsId();

		if(Objects.isNull(defaultJenkinsId)){
			return ResultDto.fail("未配置默认Jenkins");
		}

		HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();
		queryHogwartsTestJenkins.setId(defaultJenkinsId);
		queryHogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

		HogwartsTestJenkins resultHogwartsTestJenkins = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

		if(Objects.isNull(resultHogwartsTestJenkins)){
			return ResultDto.fail("默认Jenkins不存在或已失效");
		}

		HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
		hogwartsTestTask.setTaskType(Constants.TASK_TYPE_90);
		hogwartsTestTask.setStatus(Constants.STATUS_TWO);
		hogwartsTestTask.setCreateUserId(tokenDto.getUserId());
		hogwartsTestTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());
		hogwartsTestTask.setRemark("生成用例时自动创建任务");
		hogwartsTestTask.setTestCommand("生成用例无测试命令");
		hogwartsTestTask.setName("生成用例-"+ DateUtil.getNowTime_EN());
		hogwartsTestTask.setCaseConut(0);
		hogwartsTestTask.setCreateTime(new Date());
		hogwartsTestTask.setUpdateTime(new Date());

		hogwartsTestTaskMapper.insertUseGeneratedKeys(hogwartsTestTask);

		StringBuilder updateStatusUrl = JenkinsUtil.getUpdateTaskStatusUrl(requestInfoDto, hogwartsTestTask);
		//构建参数组装
		Map<String, String> params = new HashMap<>();

		params.put("gitUrl",resultHogwartsTestJenkins.getGitUrl());
		params.put("gitBranch",resultHogwartsTestJenkins.getGitBranch());
		params.put("jobName",jobName);
		params.put("saveListUrl",requestInfoDto.getRequestUrl());
		params.put("saveListToken",requestInfoDto.getToken());
		params.put("userId",tokenDto.getUserId() + "");
		params.put("taskId",hogwartsTestTask.getId() + "");
		params.put("updateStatusData",updateStatusUrl.toString());

		log.info("=====生成用例Job的构建参数组装====：" +JSONObject.toJSONString(params));
		log.info("=====生成用例Job的修改任务状态的数据组装====：" +updateStatusUrl);

		OperateJenkinsJobDto operateJenkinsJobDto = new OperateJenkinsJobDto();

		operateJenkinsJobDto.setHogwartsTestJenkins(resultHogwartsTestJenkins);
		operateJenkinsJobDto.setTokenDto(tokenDto);
		operateJenkinsJobDto.setParams(params);
		operateJenkinsJobDto.setJobType(Constants.JOB_TYPE_ONE);

		return jenkinsClient.operateJenkinsJob(operateJenkinsJobDto);

	}

}
