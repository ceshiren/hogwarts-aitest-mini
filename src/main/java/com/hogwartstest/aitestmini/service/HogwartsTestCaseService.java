package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.testcase.RunCaseDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;

import java.util.List;

public interface HogwartsTestCaseService {

	/**
	 *  新增测试用例
	 * @param hogwartsTestCase
	 * @return
	 */
	ResultDto save(HogwartsTestCase hogwartsTestCase);

	/**
	 *  删除测试用例信息
	 * @param caseId
	 * @param createUserId
	 * @return
	 */
	ResultDto<HogwartsTestCase> delete(Integer caseId,Integer createUserId);

	/**
	 *  修改测试用例信息
	 * @param hogwartsTestCase
	 * @return
	 */
	ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase);

	/**
	 *  根据id查询测试用例
	 * @param jenkinsId
	 * @param createUserId
	 * @return
	 */
	ResultDto<HogwartsTestCase> getById(Integer caseId,Integer createUserId);

	/**
	 *  查询Jenkins信息列表
	 * @param createUserId
	 * @return
	 */
	ResultDto<List<HogwartsTestCase>> list(Integer createUserId);

	/**
	 *  运行jmeter测试用例
	 * @param runCaseDto
	 * @return
	 */
	ResultDto runCase3(RunCaseDto runCaseDto) throws Exception;

}
