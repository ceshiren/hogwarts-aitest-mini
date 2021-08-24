package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.entity.HogwartsTestHis;

public interface HogwartsTestHisService {


	/**
	 *  新增测试记录信息
	 * @param hogwartsTestHis
	 * @return
	 */
	ResultDto<HogwartsTestHis> save(TokenDto tokenDto, HogwartsTestHis hogwartsTestHis);

	/**
	 *  修改测试记录信息
	 * @param hogwartsTestHis
	 * @return
	 */
	ResultDto<HogwartsTestHis> update(TokenDto tokenDto, HogwartsTestHis hogwartsTestHis);

	/**
	 *  查询测试记录信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<HogwartsTestHis>> list(PageTableRequest1 pageTableRequest);

}
