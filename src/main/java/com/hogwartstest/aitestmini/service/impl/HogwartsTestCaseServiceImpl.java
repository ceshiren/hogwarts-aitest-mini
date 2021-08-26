package com.hogwartstest.aitestmini.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.jmeter.ApiRunMode;
import com.hogwartstest.aitestmini.common.jmeter.JMeterVars;
import com.hogwartstest.aitestmini.common.jmeter.JmeterProperties;
import com.hogwartstest.aitestmini.common.jmeter.LocalRunner;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestCaseMapper;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartstest.aitestmini.dto.testcase.RunCaseDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.entity.HogwartsTestHis;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import com.hogwartstest.aitestmini.service.HogwartsTestHisService;
import com.hogwartstest.aitestmini.util.CommandUtil;
import com.hogwartstest.aitestmini.util.CopyUtil;
import com.hogwartstest.aitestmini.util.JMeterUtil;
import com.hogwartstest.aitestmini.util.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HogwartsTestCaseServiceImpl implements HogwartsTestCaseService {

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    @Autowired
    private HogwartsTestHisService hogwartsTestHisService;

    @Autowired
    private CommandUtil commandUtil;

    @Resource
    private JmeterProperties jmeterProperties;

    @PostConstruct
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

    /*//初始化jmeter属性配置
    private void initJMeterProperties() {
        if (!StringUtils.isEmpty(JMeterUtils.getJMeterProperties())){
            return;
        }
        try {
            ClassPathResource classPathResource = new ClassPathResource("/jmeter/jmeter.properties");
            InputStream inputStream = classPathResource.getInputStream();
            File tempFile = FileUtil.createTempFile(null);
            FileUtil.writeFromStream(inputStream,tempFile);
            //这里面loadJMeterProperties方法必须写成临时文件这样的形式，否则会获取不到jmeter.properties
            JMeterUtils.loadJMeterProperties(tempFile.getAbsolutePath());
            JMeterUtils.setJMeterHome(classPathResource.getPath());
            JMeterUtils.setLocale(LocaleContextHolder.getLocale());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


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
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestCase>> list(PageTableRequest<QueryHogwartsTestCaseListDto> pageTableRequest) {

        QueryHogwartsTestCaseListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal =  hogwartsTestCaseMapper.count(params);

        //分页查询数据
        List<HogwartsTestCase> hogwartsTestJenkinsList = hogwartsTestCaseMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<HogwartsTestCase> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
        hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);

        return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
    }

    /**
     * 执行测试用例
     *
     * @param createUserId
     * @param caseId
     * @return
     */
    @Override
    public ResultDto runCase(Integer createUserId, Integer caseId) throws Exception {
        init();
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

        InputStream is = StreamUtil.getStrToStream(caseData);
        Object scriptWrapper = SaveService.loadElement(is);
        HashTree testPlan = JMeterUtil.getHashTree(scriptWrapper);
        JMeterVars.addJSR223PostProcessor(testPlan);

        //是否debug模式运行
        String debugReportId = "";
        String runMode = StringUtils.isEmpty(debugReportId) ? ApiRunMode.RUN.name() : ApiRunMode.DEBUG.name();

        String testId = resultHogwartsTestCase.getId().toString();

        JMeterUtil.addBackendListener(testId, debugReportId, runMode, testPlan);

        LocalRunner runner = new LocalRunner(testPlan);
        runner.run(testId);
        HogwartsTestHis hogwartsTestHis = new HogwartsTestHis();

        hogwartsTestHis.setCreateUserId(createUserId);
        hogwartsTestHis.setCaseId(caseId);
        hogwartsTestHis.setStatus(2);

        ResultDto<HogwartsTestHis>  hogwartsTestHisResultDto =
                hogwartsTestHisService.save(hogwartsTestHis);

        if(hogwartsTestHisResultDto.getResultCode()==0){
            return hogwartsTestHisResultDto;
        }

        HogwartsTestHis resultHogwartsTestHis = hogwartsTestHisResultDto.getData();

        /*RunCaseDto runCaseDto = new RunCaseDto();
        runCaseDto.setHogwartsTestHis(resultHogwartsTestHis);
        runCaseDto.setHogwartsTestCommand(caseData);

        commandUtil.run(runCaseDto);*/

        return ResultDto.success("成功");
    }

}
