package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;

import java.io.IOException;
import java.util.List;

public interface HogwartsTestCaseService {

	/**
	 *  新增测试用例
	 * @param hogwartsTestCase
	 * @return
	 */
	ResultDto save(HogwartsTestCase hogwartsTestCase);

	/**
	 *  修改测试用例信息
	 * @param hogwartsTestCase
	 * @return
	 */
	ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase);

	/**
	 *  根据id查询测试用例
	 * @param caseId
	 * @return
	 */
	ResultDto<HogwartsTestCase> getById(Integer caseId);

	/**
	 *  查询列表
	 * @return
	 */
	ResultDto<List<HogwartsTestCase>> list();

	/**
	 *  根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
	 * @param caseId
	 * @return
	 */
	String getCaseDataById(Integer caseId);


	/**
	 *  修改状态信息
	 * @param hogwartsTestCase
	 * @return
	 */
	ResultDto<HogwartsTestCase> updateStatus(HogwartsTestCase hogwartsTestCase);

	/**
	 *  开始执行测试
	 * @param hogwartsTestCase
	 * @return
	 */
	ResultDto startTask(HogwartsTestCase hogwartsTestCase) throws IOException;

	/**
	 *  删除测试用例信息
	 * @param caseId
	 * @return
	 */
	ResultDto<HogwartsTestCase> delete(Integer caseId);

}
