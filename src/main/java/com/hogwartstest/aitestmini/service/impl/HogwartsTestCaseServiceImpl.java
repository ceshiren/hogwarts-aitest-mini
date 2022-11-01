package com.hogwartstest.aitestmini.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.ServiceException;
import com.hogwartstest.aitestmini.common.jenkins.JenkinsClient;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestCaseMapper;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import com.hogwartstest.aitestmini.util.JenkinsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class HogwartsTestCaseServiceImpl implements HogwartsTestCaseService {

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    @Autowired
    private JenkinsClient jenkinsClient;

    @Value("${jenkins.url}")
    private String jenkinsUrl;
    @Value("${jenkins.username}")
    private String jenkinsUserName;
    @Value("${jenkins.password}")
    private String jenkinsPassword;
    @Value("${jenkins.casesuffix}")
    private String jenkinsCaseSuffix;
    @Value("${jenkins.testcommand}")
    private String jenkinsTestCommand;
    @Value("${jenkins.callbackurl}")
    private String jenkinsCallbackurl;

    /**
     *
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> save(HogwartsTestCase hogwartsTestCase) {

        hogwartsTestCase.setCreateTime(new Date());
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCase.setStatus(Constants.STATUS_ONE);
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);
        hogwartsTestCaseMapper.insertUseGeneratedKeys(hogwartsTestCase);

        StringBuilder testCommand = new StringBuilder();
        makeTestCommand(testCommand, hogwartsTestCase);
        hogwartsTestCase.setTestCommand(testCommand.toString());

        hogwartsTestCaseMapper.updateByPrimaryKey(hogwartsTestCase);

        return ResultDto.success("成功", hogwartsTestCase);
    }

    /**
     * 修改测试用例信息
     *
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(hogwartsTestCase.getId());
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        hogwartsTestCase.setCreateTime(result.getCreateTime());
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        hogwartsTestCaseMapper.updateByPrimaryKey(hogwartsTestCase);

        return ResultDto.success("成功");
    }

    /**
     * 根据id查询测试用例信息
     *
     * @param jenkinsId
     */
    @Override
    public ResultDto<HogwartsTestCase> getById(Integer jenkinsId) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(jenkinsId);
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示，也可以直接返回成功
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        return ResultDto.success("成功",result);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public ResultDto<List<HogwartsTestCase>> list() {

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        List<HogwartsTestCase> hogwartsTestCaseList = hogwartsTestCaseMapper.select(hogwartsTestCase);

        return ResultDto.success("成功", hogwartsTestCaseList);
    }

    /**
     * 根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
     *
     * @param caseId
     * @return
     */
    @Override
    public String getCaseDataById(Integer caseId) {
        if(Objects.isNull(caseId)){
            return "用例id为空";
        }

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();
        queryHogwartsTestCase.setId(caseId);
        log.info("=====根据测试用例id查询case原始数据-查库入参====："+ JSONObject.toJSONString(queryHogwartsTestCase));
        HogwartsTestCase resultHogwartsTestCase = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        if(Objects.isNull(resultHogwartsTestCase)){
            return "用例数据未查到";
        }
        if(StringUtils.isEmpty(resultHogwartsTestCase.getCaseData())){
            return "用例原始数据未查到";
        }

        return resultHogwartsTestCase.getCaseData();
    }

    /**
     * 执行测试
     *
     * @param hogwartsTestCase
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto startTask(HogwartsTestCase hogwartsTestCase) throws IOException {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();
        queryHogwartsTestCase.setId(hogwartsTestCase.getId());
        log.info("=====根据测试用例id查询case原始数据-查库入参====："+ JSONObject.toJSONString(queryHogwartsTestCase));
        HogwartsTestCase resultHogwartsTestCase = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        if(Objects.isNull(resultHogwartsTestCase)){
            return ResultDto.fail("测试用例为空");
        }
        if(StringUtils.isEmpty(resultHogwartsTestCase.getCaseData())){
            return ResultDto.fail("测试用例数据为空");
        }


        if(StringUtils.isEmpty(resultHogwartsTestCase.getTestCommand())){
            return ResultDto.fail("测试命令不能为空");
        }

        //更新状态
        resultHogwartsTestCase.setStatus(Constants.STATUS_TWO);
        hogwartsTestCaseMapper.updateByPrimaryKeySelective(resultHogwartsTestCase);

        StringBuilder testCommand = new StringBuilder();

        //添加保存测试命令
        testCommand.append(resultHogwartsTestCase.getTestCommand());
        testCommand.append(" \n");


        StringBuilder updateStatusUrl = JenkinsUtil.getUpdateTaskStatusUrl(jenkinsCallbackurl, resultHogwartsTestCase);

        log.info("updateStatusUrl== " + updateStatusUrl);
        //构建参数组装
        Map<String, String> params = new HashMap<>();

        params.put("callbackurl",jenkinsCallbackurl);
        params.put("testCommand",testCommand.toString());
        params.put("updateStatusData",updateStatusUrl.toString());

        log.info("=====执行测试Job的构建参数组装====：" +JSONObject.toJSONString(params));
        log.info("=====执行测试Job的修改用例状态的数据组装====：" +updateStatusUrl);

        OperateJenkinsJobDto operateJenkinsJobDto = new OperateJenkinsJobDto();
        operateJenkinsJobDto.setCaseId(resultHogwartsTestCase.getId());
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
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> updateStatus(HogwartsTestCase hogwartsTestCase) {
        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();
        queryHogwartsTestCase.setId(hogwartsTestCase.getId());
        log.info("=====根据测试用例id查询case原始数据-查库入参====："+ JSONObject.toJSONString(queryHogwartsTestCase));
        HogwartsTestCase resultHogwartsTestCase = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        if(Objects.isNull(resultHogwartsTestCase)){
            return ResultDto.fail("测试用例为空");
        }

        //如果已经完成，则不重复修改
        if(Constants.STATUS_THREE.equals(resultHogwartsTestCase.getStatus())){
            return ResultDto.fail("测试用例已完成，无需修改");
        }
        resultHogwartsTestCase.setUpdateTime(new Date());

        //仅状态为已完成时修改
        if(!Constants.STATUS_THREE.equals(resultHogwartsTestCase.getStatus())){
            resultHogwartsTestCase.setStatus(Constants.STATUS_THREE);
            hogwartsTestCaseMapper.updateByPrimaryKey(resultHogwartsTestCase);
        }

        return ResultDto.success("成功");
    }

    /**
     * 删除测试用例信息
     *
     * @param caseId
     */
    @Override
    public ResultDto<HogwartsTestCase> delete(Integer caseId) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(caseId);
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }
        result.setDelFlag(Constants.DEL_FLAG_ZERO);
        hogwartsTestCaseMapper.updateByPrimaryKey(result);

        return ResultDto.success("成功");
    }

    /**
     *
     * @param testCommand
     */
    private void makeTestCommand(StringBuilder testCommand, HogwartsTestCase hogwartsTestCase) {

        if(Objects.isNull(hogwartsTestCase)){
            throw new ServiceException("组装测试命令时，测试用例信息为空");
        }

        //打印测试目录
        testCommand.append("pwd");
        testCommand.append("\n");

        //拼装下载文件的curl命令
        String culStr = makeCurlCommand(hogwartsTestCase);
        log.info("culStr== " + culStr);
        testCommand.append(culStr);
        //拼装jmeter执行命令
        String runCommand = jenkinsTestCommand.replace("{jmxName}",hogwartsTestCase.getCaseName()+jenkinsCaseSuffix);
        log.info("runCommand== " + runCommand);
        testCommand.append(runCommand);
        testCommand.append("\n");

    }

    /**
     *  拼装下载文件的curl命令
     * @param hogwartsTestCase
     */
    private String makeCurlCommand(HogwartsTestCase hogwartsTestCase) {

        String culStr = "curl -o {jmxName} ${callbackurl}/case/data/{caseId} \n";

        culStr = culStr.replace("{jmxName}",hogwartsTestCase.getCaseName()+jenkinsCaseSuffix)
                .replace("{caseId}",hogwartsTestCase.getId()+"");

        return culStr;
    }

}
