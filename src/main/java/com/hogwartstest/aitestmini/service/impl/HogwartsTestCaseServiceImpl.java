package com.hogwartstest.aitestmini.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.jmeter.ApiRunMode;
import com.hogwartstest.aitestmini.common.jmeter.JMeterVars;
import com.hogwartstest.aitestmini.common.jmeter.JmeterProperties;
import com.hogwartstest.aitestmini.common.jmeter.LocalRunner;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestCaseMapper;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.testcase.RunCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.RunCaseParamsDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import com.hogwartstest.aitestmini.util.JMeterUtil;
import com.hogwartstest.aitestmini.util.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class HogwartsTestCaseServiceImpl implements HogwartsTestCaseService {

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    @Resource
    private JmeterProperties jmeterProperties;

    public void init() {
        String JMETER_HOME = getJmeterHome();

        String JMETER_PROPERTIES = JMETER_HOME + "/bin/jmeter.properties";
        JMeterUtils.loadJMeterProperties(JMETER_PROPERTIES);
        JMeterUtils.setJMeterHome(JMETER_HOME);
        JMeterUtils.setLocale(LocaleContextHolder.getLocale());

    }

    /**
     *  获取jmeter配置信息
     * @return
     */
    public String getJmeterHome() {

        String home = getClass().getResource("/").getPath() + "jmeter";
        try {
            File file = new File(home);
            if (file.exists()) {
                return home;
            } else {
                return jmeterProperties.getHome();
            }
        } catch (Exception e) {
            return jmeterProperties.getHome();
        }
    }


    /**
     *
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> save(HogwartsTestCase hogwartsTestCase) {


        hogwartsTestCase.setCreateTime(new Date());
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        hogwartsTestCaseMapper.insertUseGeneratedKeys(hogwartsTestCase);
        return ResultDto.success("成功", hogwartsTestCase);
    }

    /**
     * 删除测试用例信息
     *
     * @param caseId
     * @return createUserId
     */
    @Override
    public ResultDto<HogwartsTestCase> delete(Integer caseId,Integer createUserId) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(caseId);
        queryHogwartsTestCase.setCreateUserId(createUserId);
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
     * 修改测试用例信息
     *
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(hogwartsTestCase.getId());
        queryHogwartsTestCase.setCreateUserId(hogwartsTestCase.getCreateUserId());
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
     * @return createUserId
     */
    @Override
    public ResultDto<HogwartsTestCase> getById(Integer jenkinsId,Integer createUserId) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(jenkinsId);
        queryHogwartsTestCase.setCreateUserId(createUserId);
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示，也可以直接返回成功
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        return ResultDto.success("成功",result);
    }

    /**
     * 查询Jenkins信息列表
     *
     * @param createUserId
     * @return
     */
    @Override
    public ResultDto<List<HogwartsTestCase>> list(Integer createUserId) {

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
        hogwartsTestCase.setCreateUserId(createUserId);
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);
        List<HogwartsTestCase> hogwartsTestJenkinsList = hogwartsTestCaseMapper.select(hogwartsTestCase);

        return ResultDto.success("成功", hogwartsTestJenkinsList);
    }

    /**
     * 执行测试用例
     *
     *  允许用户录入application名称和需要替换的值(key-value数组形式)，其中key以${}形式
     *
     * @param runCaseDto
     * @return
     */
    @Override
    public ResultDto runCase3(RunCaseDto runCaseDto) throws Exception {
        init();

        Integer createUserId = runCaseDto.getCreateUserId();
        Integer caseId = runCaseDto.getCaseId();

        if(Objects.isNull(caseId)){
            return ResultDto.fail("用例id为空");
        }

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();
        queryHogwartsTestCase.setCreateUserId(createUserId);
        queryHogwartsTestCase.setId(caseId);
        log.info("=====执行测试用例-查库入参====："+ JSONObject.toJSONString(queryHogwartsTestCase));
        HogwartsTestCase resultHogwartsTestCase = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        if(Objects.isNull(resultHogwartsTestCase)){
            return ResultDto.fail("用例数据未查到");
        }

        String caseData = resultHogwartsTestCase.getCaseData();

        if(StringUtils.isEmpty(caseData)){
            return ResultDto.fail("用例测试命令未查到");
        }

        List<RunCaseParamsDto> params = runCaseDto.getParams();

        caseData = parseJmeterParams(caseData, params);

        InputStream is = StreamUtil.getStrToStream(caseData);
        Object scriptWrapper = SaveService.loadElement(is);
        HashTree testPlan = JMeterUtil.getHashTree(scriptWrapper);
        JMeterVars.addJSR223PostProcessor(testPlan);

        //是否debug模式运行
        String debugReportId = "";
        String runMode = StringUtils.isEmpty(debugReportId) ? ApiRunMode.RUN.name() : ApiRunMode.DEBUG.name();

        String testId = resultHogwartsTestCase.getId().toString();

        JMeterUtil.addBackendListener(testId, debugReportId, runMode, testPlan, runCaseDto);

        LocalRunner runner = new LocalRunner(testPlan);
        runner.run(testId);

        return ResultDto.success("成功");
    }

    /**
     *  动态解析jmeter参数
     * @param caseData
     * @param params
     * @return
     */
    private String parseJmeterParams(String caseData, List<RunCaseParamsDto> params) {
        if(Objects.nonNull(params)){

            for (RunCaseParamsDto runCaseParamsDto:params) {

                String key = runCaseParamsDto.getKey();

                if(Objects.isNull(key)){
                    continue;
                }
                StringBuilder keyStr = new StringBuilder();
                if(!key.startsWith("${")){
                    keyStr.append("${");
                }
                keyStr.append(key);
                if(!key.endsWith("}")){
                    keyStr.append("}");
                }

                String value = runCaseParamsDto.getValue();
                caseData = caseData.replace(keyStr.toString(),value);

            }

        }
        return caseData;
    }

}
