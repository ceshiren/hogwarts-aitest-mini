package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;

public interface HogwartsTestJenkinsService {


	/**
	 *  新增Jenkins信息
	 * @param hogwartsTestJenkins
	 * @return
	 */
	ResultDto<HogwartsTestJenkins> save(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins);

	/**
	 *  删除Jenkins信息
	 * @param id
	 * @return
	 */
	ResultDto<HogwartsTestJenkins> delete(Integer jenkinsId,TokenDto tokenDto);

	/**
	 *  修改Jenkins信息
	 * @param hogwartsTestJenkins
	 * @return
	 */
	ResultDto<HogwartsTestJenkins> update(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins);

	/**
	 *  根据id查询Jenkins信息
	 * @param createUserId
	 * @return
	 */
	ResultDto<HogwartsTestJenkins> getById(Integer jenkinsId,Integer createUserId);

	/**
	 *  查询Jenkins信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<HogwartsTestJenkins>> list(PageTableRequest<QueryHogwartsTestJenkinsListDto> pageTableRequest);

}
