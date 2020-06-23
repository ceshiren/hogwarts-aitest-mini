package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.common.Token;
import com.hogwartstest.aitestmini.dto.RequestInfoDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;

import java.io.IOException;

public interface HogwartsTestUserService {


	ResultDto<HogwartsTestUser> getById(Integer id);


	ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser);



	ResultDto<Token> login(String username, String password);

	/**
	 *  作业
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	ResultDto<HogwartsTestUser> changePassword(String username, String oldPassword, String newPassword);

}
