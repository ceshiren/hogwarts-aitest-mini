package com.hogwartstest.aitestmini.service.impl;

import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestJenkinsMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestTaskMapper;
import com.hogwartstest.aitestmini.dto.AllureReportDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.dto.task.TaskDataDto;
import com.hogwartstest.aitestmini.dto.task.TaskReportDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;
import com.hogwartstest.aitestmini.service.HogwartsTestReportService;
import com.hogwartstest.aitestmini.util.ReportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class HogwartsTestReportServiceImpl implements HogwartsTestReportService {

	@Autowired
	private HogwartsTestTaskMapper hogwartsTestTaskMapper;

	@Autowired
	private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;

	/**
	 * 获取allure报告
	 *
	 * @param tokenDto
	 * @param taskId
	 * @return
	 */
	@Override
	public ResultDto<AllureReportDto> getAllureReport(TokenDto tokenDto, Integer taskId) {

		HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

		queryHogwartsTestTask.setId(taskId);
		queryHogwartsTestTask.setCreateUserId(tokenDto.getUserId());

		HogwartsTestTask resultHogwartsTestTask = hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);

		if(Objects.isNull(resultHogwartsTestTask)){
			return ResultDto.fail("测试任务不存在 "+taskId);
		}

		String buildUrl = resultHogwartsTestTask.getBuildUrl();

		if(StringUtils.isEmpty(buildUrl)){
			return ResultDto.fail("测试任务的构建地址不存在 "+taskId);
		}

		Integer testJenkinsId = resultHogwartsTestTask.getTestJenkinsId();

		if(Objects.isNull(testJenkinsId)){
			return ResultDto.fail("测试任务的jenkinsId不存在 "+taskId);
		}

		HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();
		queryHogwartsTestJenkins.setId(testJenkinsId);
		queryHogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

		HogwartsTestJenkins resultHogwartsTestJenkins = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

		String allureReportUrl = ReportUtil.getAllureReportUrl(buildUrl, resultHogwartsTestJenkins, true);

		AllureReportDto allureReportDto = new AllureReportDto();
		allureReportDto.setTaskId(taskId);
		allureReportDto.setAllureReportUrl(allureReportUrl);

		return ResultDto.success("成功", allureReportDto);
	}

	/**
	 * 根据任务类型获取任务统计信息
	 *
	 * @param tokenDto
	 * @return
	 */
	@Override
	public ResultDto<TaskReportDto> getTaskByType(TokenDto tokenDto) {

		TaskReportDto taskReportDto = new TaskReportDto();

		Integer taskSum = 0;

		List<TaskDataDto>  taskDataDtoList = hogwartsTestTaskMapper.getTaskByType(tokenDto.getUserId());

		if(Objects.isNull(taskDataDtoList) || taskDataDtoList.size()==0){
			ResultDto.fail("无数据");
		}

		List<TaskDataDto>  newtTaskDataDtoList = new ArrayList<>();

		for (TaskDataDto taskDataDto:taskDataDtoList) {

			Integer taskKey = taskDataDto.getTaskKey();
			if(Objects.isNull(taskKey)){
				taskKey = 0;
			}

			if(0==taskKey){
				taskDataDto.setDesc("无匹配任务");
			}
			if(Constants.TASK_TYPE_ONE.equals(taskKey)){
				taskDataDto.setDesc("普通测试任务");
			}
			if(Constants.TASK_TYPE_TWO.equals(taskKey)){
				taskDataDto.setDesc("一键执行测试的任务");
			}
			if(Constants.TASK_TYPE_90.equals(taskKey)){
				taskDataDto.setDesc("生成用例任务");
			}

			taskSum = taskSum + taskDataDto.getTaskCount();

			newtTaskDataDtoList.add(taskDataDto);

		}

		taskReportDto.setTaskSum(taskSum);
		taskReportDto.setTaskDataDtoList(newtTaskDataDtoList);

		return ResultDto.success("成功",taskReportDto);
	}

	/**
	 * 根据任务状态获取任务统计信息
	 *
	 * @param tokenDto
	 * @return
	 */
	@Override
	public ResultDto<TaskReportDto> getTaskByStatus(TokenDto tokenDto) {
		TaskReportDto taskReportDto = new TaskReportDto();

		Integer taskSum = 0;

		List<TaskDataDto>  taskDataDtoList = hogwartsTestTaskMapper.getTaskByStatus(tokenDto.getUserId());

		if(Objects.isNull(taskDataDtoList) || taskDataDtoList.size()==0){
			ResultDto.fail("无数据");
		}

		List<TaskDataDto>  newtTaskDataDtoList = new ArrayList<>();

		for (TaskDataDto taskDataDto:taskDataDtoList) {

			Integer taskKey = taskDataDto.getTaskKey();
			if(Objects.isNull(taskKey)){
				taskKey = 0;
			}

			if(0==taskKey){
				taskDataDto.setDesc("无匹配任务");
			}
			if(Constants.STATUS_ONE.equals(taskKey)){
				taskDataDto.setDesc("新建");
			}
			if(Constants.STATUS_TWO.equals(taskKey)){
				taskDataDto.setDesc("执行中");
			}
			if(Constants.STATUS_THREE.equals(taskKey)){
				taskDataDto.setDesc("已完成");
			}

			taskSum = taskSum + taskDataDto.getTaskCount();

			newtTaskDataDtoList.add(taskDataDto);

		}

		taskReportDto.setTaskSum(taskSum);
		taskReportDto.setTaskDataDtoList(newtTaskDataDtoList);

		return ResultDto.success("成功",taskReportDto);
	}

	/**
	 * 任务中用例的数量统计信息
	 * @param tokenDto
	 * @param start 按时间倒叙开始序号
	 * @param end 按时间倒叙结束序号
	 * @return
	 */
	@Override
	public ResultDto<List<HogwartsTestTask>> getTaskByCaseCount(TokenDto tokenDto, Integer start, Integer end) {

		List<HogwartsTestTask> hogwartsTestTaskList = hogwartsTestTaskMapper.getCaseCountByTask(tokenDto.getUserId(), start, end);

		if(Objects.isNull(hogwartsTestTaskList) || hogwartsTestTaskList.size()==0){
			return ResultDto.fail("无数据");
		}

		return ResultDto.success("成功",hogwartsTestTaskList);
	}
}
