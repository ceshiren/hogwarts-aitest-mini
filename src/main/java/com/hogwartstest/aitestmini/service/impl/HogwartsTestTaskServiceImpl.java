package com.hogwartstest.aitestmini.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestCaseMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestJenkinsMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestTaskCaseRelMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestTaskMapper;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.task.AddHogwartsTestTaskDto;
import com.hogwartstest.aitestmini.dto.task.QueryHogwartsTestTaskListDto;
import com.hogwartstest.aitestmini.dto.task.TestTaskDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;
import com.hogwartstest.aitestmini.entity.HogwartsTestTaskCaseRel;
import com.hogwartstest.aitestmini.service.HogwartsTestTaskService;
import com.hogwartstest.aitestmini.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HogwartsTestTaskServiceImpl implements HogwartsTestTaskService {

    @Autowired
    private HogwartsTestTaskMapper hogwartsTestTaskMapper;

    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    @Autowired
    private HogwartsTestTaskCaseRelMapper hogwartsTestTaskCaseRelMapper;

    /**
     * 新增测试任务信息
     *
     * @param testTaskDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<HogwartsTestTask> save(TestTaskDto testTaskDto, Integer taskType) {

        StringBuilder testCommand = new StringBuilder();

        AddHogwartsTestTaskDto testTask = testTaskDto.getTestTask();
        List<Integer> caseIdList = testTaskDto.getCaseIdList();

        HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();
        queryHogwartsTestJenkins.setId(testTask.getTestJenkinsId());
        queryHogwartsTestJenkins.setCreateUserId(testTask.getCreateUserId());

        HogwartsTestJenkins result = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

        if(Objects.isNull(result)){
            return ResultDto.fail("Jenkins信息为空");
        }

        List<HogwartsTestCase> hogwartsTestCaseList = hogwartsTestCaseMapper.selectByIds(StrUtil.list2IdsStr(caseIdList));

        String runCommand = result.getTestCommand();

        makeMvnTest(testCommand, runCommand, hogwartsTestCaseList);

        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();

        hogwartsTestTask.setName(testTask.getName());
        hogwartsTestTask.setTestJenkinsId(testTask.getTestJenkinsId());
        hogwartsTestTask.setCreateUserId(testTask.getCreateUserId());
        hogwartsTestTask.setRemark(testTask.getRemark());

        hogwartsTestTask.setTaskType(taskType);
        hogwartsTestTask.setTestCommand(testCommand.toString());
        hogwartsTestTask.setCaseConut(caseIdList.size());
        hogwartsTestTask.setStatus(Constants.STATUS_ONE);
        hogwartsTestTask.setCreateTime(new Date());
        hogwartsTestTask.setUpdateTime(new Date());

        hogwartsTestTaskMapper.insert(hogwartsTestTask);

        if(Objects.nonNull(caseIdList) && caseIdList.size()>0){

            List<HogwartsTestTaskCaseRel> testTaskCaseList = new ArrayList<>();

            for (Integer testCaseId:caseIdList) {

                HogwartsTestTaskCaseRel hogwartsTestTaskCaseRel = new HogwartsTestTaskCaseRel();
                hogwartsTestTaskCaseRel.setTaskId(hogwartsTestTask.getId());
                hogwartsTestTaskCaseRel.setCaseId(testCaseId);
                hogwartsTestTaskCaseRel.setCreateUserId(hogwartsTestTask.getCreateUserId());
                hogwartsTestTaskCaseRel.setCreateTime(new Date());
                hogwartsTestTaskCaseRel.setUpdateTime(new Date());
                testTaskCaseList.add(hogwartsTestTaskCaseRel);
            }

            log.info("=====测试任务详情保存-落库入参====："+ JSONObject.toJSONString(testTaskCaseList));
            hogwartsTestTaskCaseRelMapper.insertList(testTaskCaseList);
        }

        return ResultDto.success("成功", hogwartsTestTask);
    }

    /**
     * 删除测试任务信息
     *
     * @param taskId
     * @param createUserId
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> delete(Integer taskId, Integer createUserId) {
        HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

        queryHogwartsTestTask.setId(taskId);
        queryHogwartsTestTask.setCreateUserId(createUserId);

        HogwartsTestTask result = hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }
        hogwartsTestTaskMapper.deleteByPrimaryKey(taskId);

        return ResultDto.success("成功");
    }

    /**
     * 修改测试任务信息
     *
     * @param hogwartsTestTask
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask) {
        HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

        queryHogwartsTestTask.setId(hogwartsTestTask.getId());
        queryHogwartsTestTask.setCreateUserId(hogwartsTestTask.getCreateUserId());

        HogwartsTestTask result = hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        hogwartsTestTask.setCreateTime(result.getCreateTime());
        hogwartsTestTask.setUpdateTime(new Date());

        hogwartsTestTaskMapper.updateByPrimaryKey(hogwartsTestTask);

        return ResultDto.success("成功");
    }

    /**
     * 根据id查询
     *
     * @param taskId
     * @param createUserId
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> getById(Integer taskId, Integer createUserId) {
        HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

        queryHogwartsTestTask.setId(taskId);
        queryHogwartsTestTask.setCreateUserId(createUserId);

        HogwartsTestTask result = hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            ResultDto.fail("未查到测试任务信息");
        }

        return ResultDto.success("成功", result);
    }

    /**
     * 查询测试任务信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest) {
        QueryHogwartsTestTaskListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal = hogwartsTestTaskMapper.count(params);

        //分页查询数据
        List<HogwartsTestTask> hogwartsTestJenkinsList = hogwartsTestTaskMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<HogwartsTestTask> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
        hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);

        return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
    }

    /**
     * 开始执行测试任务信息
     *
     * @param hogwartsTestTask
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> startTask(HogwartsTestTask hogwartsTestTask) {
        return null;
    }

    /**
     * 修改测试任务状态信息
     *
     * @param hogwartsTestTask
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask) {
        HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

        queryHogwartsTestTask.setId(hogwartsTestTask.getId());
        queryHogwartsTestTask.setCreateUserId(hogwartsTestTask.getCreateUserId());

        HogwartsTestTask result = hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);

        //如果为空，则提示
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        //如果任务已经完成，则不重复修改
        if(Constants.STATUS_THREE.equals(result.getStatus())){
            return ResultDto.fail("测试任务已完成，无需修改");
        }

        hogwartsTestTask.setCreateTime(result.getCreateTime());
        hogwartsTestTask.setUpdateTime(new Date());

        //仅状态为已完成时修改
        if(Constants.STATUS_THREE.equals(hogwartsTestTask.getStatus())){
            hogwartsTestTaskMapper.updateByPrimaryKey(hogwartsTestTask);
        }

        return ResultDto.success("成功");
    }

    /**
     *
     * @param testCommand
     * @param testCaseList
     */
    private void makeMvnTest(StringBuilder testCommand, String runCommand, List<HogwartsTestCase> testCaseList) {

        log.info("testCommand.toString()== "+testCommand.toString() + "  runCommand== " + runCommand);

        testCommand.append(runCommand + "-Dtest=");
        //后面具体按类型细分进行不同的拼装
        int testCaseListSize = testCaseList.size();
        //后面具体按类型细分进行不同的拼装
        for (int i = 0; i < testCaseListSize; i++) {
            HogwartsTestCase hogwartsTestCase = testCaseList.get(i);

            if(Objects.isNull(hogwartsTestCase)){
                continue;
            }

            String packageName = hogwartsTestCase.getPackageName();
            String className = hogwartsTestCase.getClassName();

            //测试用例类型为包类型时，才添加包名，含有类名时不需要添加包名
            if(StringUtils.isEmpty(className)&&!StringUtils.isEmpty(packageName)){
                testCommand.append(packageName);
            }

            if(!StringUtils.isEmpty(className)){
                testCommand.append(className);
            }
            String methodName = hogwartsTestCase.getMethodName();
            //先拼装类名
            //如果方法名不为空，则拼装方法名
            if(!StringUtils.isEmpty(methodName)){
                methodName = methodName
                        .replace("(","")
                        .replace(")","");

                testCommand.append("#").append(methodName);
            }
            //只有不是最后一个测试用例，就追加逗号
            if(i!=testCaseListSize-1){
                testCommand.append(",");
            }

        }

        //本行命令执行失败，继续运行下面的命令行
        testCommand.append(" || true");

        testCommand.append("\n");
    }

}
