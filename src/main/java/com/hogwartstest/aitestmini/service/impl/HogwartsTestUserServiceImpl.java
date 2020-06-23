package com.hogwartstest.aitestmini.service.impl;

import com.hogwartstest.aitestmini.common.Token;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dao.HogwartsTestUserMapper;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;
import com.hogwartstest.aitestmini.service.HogwartsTestUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

@Service
@Slf4j
public class HogwartsTestUserServiceImpl implements HogwartsTestUserService {


	@Autowired
	private HogwartsTestUserMapper hogwartsTestUserMapper;

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

}
