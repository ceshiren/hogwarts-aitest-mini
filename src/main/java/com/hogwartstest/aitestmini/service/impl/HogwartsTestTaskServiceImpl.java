package com.hogwartstest.aitestmini.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.hogwartstest.aitestmini.common.jenkins.JenkinsClient;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestCaseMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestJenkinsMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestTaskCaseRelMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestTaskMapper;
import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartstest.aitestmini.dto.task.AddHogwartsTestTaskDto;
import com.hogwartstest.aitestmini.dto.task.QueryHogwartsTestTaskListDto;
import com.hogwartstest.aitestmini.dto.task.TestTaskDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;
import com.hogwartstest.aitestmini.entity.HogwartsTestTaskCaseRel;
import com.hogwartstest.aitestmini.service.HogwartsTestTaskService;
import com.hogwartstest.aitestmini.util.JenkinsUtil;
import com.hogwartstest.aitestmini.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    private JenkinsClient jenkinsClient;

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

        makeTestCommand(testCommand, result, hogwartsTestCaseList);

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

        result.setUpdateTime(new Date());
        result.setName(hogwartsTestTask.getName());
        result.setRemark(hogwartsTestTask.getRemark());

        hogwartsTestTaskMapper.updateByPrimaryKeySelective(result);

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
    @Transactional(rollbackFor = Exception.class)
    public ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask) throws IOException {
        log.info("=====开始测试-Service请求入参====："+ JSONObject.toJSONString(requestInfoDto)+"+++++"+JSONObject.toJSONString(hogwartsTestTask));
        if(Objects.isNull(hogwartsTestTask)){
            return ResultDto.fail("测试任务参数不能为空");
        }

        Integer defaultJenkinsId = tokenDto.getDefaultJenkinsId();

        if(Objects.isNull(defaultJenkinsId)){
            return ResultDto.fail("未配置默认Jenkins");
        }

        HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();
        queryHogwartsTestJenkins.setId(defaultJenkinsId);
        queryHogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

        HogwartsTestJenkins resultHogwartsTestJenkins = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

        if(Objects.isNull(resultHogwartsTestJenkins)){
            return ResultDto.fail("默认Jenkins不存在或已失效");
        }

        HogwartsTestTask queryHogwartsTestTask = new HogwartsTestTask();

        queryHogwartsTestTask.setId(hogwartsTestTask.getId());
        queryHogwartsTestTask.setCreateUserId(hogwartsTestTask.getCreateUserId());

        HogwartsTestTask resultHogwartsTestTask = hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);


        if(Objects.isNull(resultHogwartsTestTask)){
            String tips = "未查到测试任务";
            log.info(tips+queryHogwartsTestTask.getId());
            return ResultDto.fail(tips);
        }

        String testCommandStr =  hogwartsTestTask.getTestCommand();
        if(StringUtils.isEmpty(testCommandStr)){
            testCommandStr = resultHogwartsTestTask.getTestCommand();
        }

        if(StringUtils.isEmpty(testCommandStr)){
            return ResultDto.fail("任务的测试命令不能为空");
        }

        //更新任务状态
        resultHogwartsTestTask.setStatus(Constants.STATUS_TWO);
        hogwartsTestTaskMapper.updateByPrimaryKeySelective(resultHogwartsTestTask);

        StringBuilder testCommand = new StringBuilder();

        //添加保存测试任务接口拼装的mvn test 命令
        testCommand.append(testCommandStr);
        testCommand.append(" \n");


        StringBuilder updateStatusUrl = JenkinsUtil.getUpdateTaskStatusUrl(requestInfoDto, resultHogwartsTestTask);

        //构建参数组装
        Map<String, String> params = new HashMap<>();

        params.put("aitestBaseUrl",requestInfoDto.getBaseUrl());
        params.put("token",requestInfoDto.getToken());
        params.put("testCommand",testCommand.toString());
        params.put("updateStatusData",updateStatusUrl.toString());

        log.info("=====执行测试Job的构建参数组装====：" +JSONObject.toJSONString(params));
        log.info("=====执行测试Job的修改任务状态的数据组装====：" +updateStatusUrl);


        OperateJenkinsJobDto operateJenkinsJobDto = new OperateJenkinsJobDto();

        operateJenkinsJobDto.setParams(params);
        operateJenkinsJobDto.setTokenDto(tokenDto);
        operateJenkinsJobDto.setHogwartsTestJenkins(resultHogwartsTestJenkins);

        operateJenkinsJobDto.setParams(params);

        ResultDto resultDto = jenkinsClient.operateJenkinsJob(operateJenkinsJobDto);
        //此处抛出异常，阻止事务提交
        if(0 == resultDto.getResultCode()){
            throw new ServiceException("执行测试时异常-"+resultDto.getMessage());
        }
        return resultDto;
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
        result.setUpdateTime(new Date());

        //仅状态为已完成时修改
        if(Constants.STATUS_THREE.equals(hogwartsTestTask.getStatus())){
            result.setBuildUrl(hogwartsTestTask.getBuildUrl());
            result.setStatus(Constants.STATUS_THREE);
            hogwartsTestTaskMapper.updateByPrimaryKey(result);
        }

        return ResultDto.success("成功");
    }

    /**
     *
     * @param testCommand
     * @param testCaseList
     */
    private void makeTestCommand(StringBuilder testCommand, HogwartsTestJenkins hogwartsTestJenkins, List<HogwartsTestCase> testCaseList) {

        //打印测试目录
        testCommand.append("pwd");
        testCommand.append("\n");

        if(Objects.isNull(hogwartsTestJenkins)){
            throw new ServiceException("组装测试命令时，Jenkins信息为空");
        }
        if(Objects.isNull(testCaseList) || testCaseList.size()==0){
            throw new ServiceException("组装测试命令时，测试用例列表信息为空");
        }

        String runCommand = hogwartsTestJenkins.getTestCommand();

        Integer commandRunCaseType = hogwartsTestJenkins.getCommandRunCaseType();
        String systemTestCommand = hogwartsTestJenkins.getTestCommand();

        if(StringUtils.isEmpty(systemTestCommand)){
            throw new ServiceException("组装测试命令时，运行的测试命令信息为空");
        }

        //默认文本类型
        if(Objects.isNull(commandRunCaseType)){
            commandRunCaseType = 1;
        }

        //文本类型
        if(commandRunCaseType==1){
            for (HogwartsTestCase hogwartsTestCase :testCaseList) {
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //拼装测试数据
                testCommand.append(hogwartsTestCase.getCaseData())
                .append("\n");
            }
        }
        //文件类型
        if(commandRunCaseType==2){

            String commandRunCasSuffix = hogwartsTestJenkins.getCommandRunCasSuffix();

            if(StringUtils.isEmpty(commandRunCasSuffix)){
                throw new ServiceException("组装测试命令且case为文件时，测试用例后缀名不能为空");
            }

            for (HogwartsTestCase hogwartsTestCase :testCaseList) {

                //拼装下载文件的curl命令
                makeCurlCommand(testCommand, hogwartsTestCase, commandRunCasSuffix);
                testCommand.append("\n");
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //平台测试用例名称
                testCommand.append(hogwartsTestCase.getCaseName())
                        //拼装.分隔符
                        .append(".")
                        //拼装case文件后缀
                        .append(commandRunCasSuffix)
                        .append(" || true")
                        .append("\n");
            }
        }



        log.info("testCommand.toString()== "+testCommand.toString() + "  runCommand== " + runCommand);


        testCommand.append("\n");
    }

    /**
     *  拼装下载文件的curl命令
     * @param testCommand
     * @param hogwartsTestCase
     * @param commandRunCasSuffix
     */
    private void makeCurlCommand(StringBuilder testCommand, HogwartsTestCase hogwartsTestCase, String commandRunCasSuffix) {

        //通过curl命令获取测试数据并保存为文件
        testCommand.append("curl ")
                .append("-o ");

        String caseName = hogwartsTestCase.getCaseName();

        if(StringUtils.isEmpty(caseName)){
            caseName = "测试用例无测试名称";
        }

        testCommand.append(caseName)
                .append(".")
                .append(commandRunCasSuffix)
                .append(" ${aitestBaseUrl}/testCase/data/")
                .append(hogwartsTestCase.getId())
                .append(" -H \"token: ${token}\" ");

        //本行命令执行失败，继续运行下面的命令行
        testCommand.append(" || true");

        testCommand.append("\n");
    }

}
