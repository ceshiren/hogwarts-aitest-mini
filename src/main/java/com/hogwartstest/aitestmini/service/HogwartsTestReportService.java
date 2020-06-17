package com.hogwartstest.aitestmini.service;

import com.hogwartstest.aitestmini.dto.AllureReportDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.dto.task.TaskReportDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;

import java.util.List;

public interface HogwartsTestReportService {

	/**
	 *  获取allure报告
	 * @param tokenDto
	 * @param taskId
	 * @return
	 */
	ResultDto<AllureReportDto> getAllureReport(TokenDto tokenDto, Integer taskId);

	/**
	 *  根据任务类型获取任务统计信息
	 * @param tokenDto
	 * @param taskId
	 * @return
	 */
	ResultDto<TaskReportDto> getTaskByType(TokenDto tokenDto);

	/**
	 *  根据任务状态获取任务统计信息
	 * @param tokenDto
	 * @param taskId
	 * @return
	 */
	ResultDto<TaskReportDto> getTaskByStatus(TokenDto tokenDto);

	/**
	 * 任务中用例的数量统计信息
	 * @param tokenDto
	 * @param start 按时间倒叙开始序号
	 * @param end 按时间倒叙结束序号
	 * @return
	 */
	ResultDto<List<HogwartsTestTask>> getTaskByCaseCount(TokenDto tokenDto, Integer start, Integer end);

}
