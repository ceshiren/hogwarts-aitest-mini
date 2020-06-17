package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.dto.task.QueryHogwartsTestTaskListDto;
import com.hogwartstest.aitestmini.dto.task.TestTaskDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;

import java.io.IOException;

public interface HogwartsTestTaskService {

	/**
	 *  新增测试任务信息
	 * @param testTaskDto
	 * @return
	 */
	ResultDto<HogwartsTestTask> save(TestTaskDto testTaskDto, Integer taskType);

	/**
	 *  删除测试任务信息
	 * @param taskId
	 * @param createUserId
	 * @return
	 */
	ResultDto<HogwartsTestTask> delete(Integer taskId,Integer createUserId);

	/**
	 *  修改测试任务信息
	 * @param hogwartsTestTask
	 * @return
	 */
	ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask);

	/**
	 *  根据id查询
	 * @param taskId
	 * @return
	 */
	ResultDto<HogwartsTestTask> getById(Integer taskId,Integer createUserId);

	/**
	 *  查询测试任务信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest);

	/**
	 *  开始执行测试任务信息
	 * @param hogwartsTestTask
	 * @return
	 */
	ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask) throws IOException;

	/**
	 *  修改测试任务状态信息
	 * @param hogwartsTestTask
	 * @return
	 */
	ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask);


}
