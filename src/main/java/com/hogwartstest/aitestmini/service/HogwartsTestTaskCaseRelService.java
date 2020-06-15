package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.testcase.HogwartsTestTaskCaseRelDetailDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestTaskCaseRelListDto;

public interface HogwartsTestTaskCaseRelService {

	/**
	 *  查询任务关联的详细信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<HogwartsTestTaskCaseRelDetailDto>> listDetail(PageTableRequest<QueryHogwartsTestTaskCaseRelListDto> pageTableRequest);


}
