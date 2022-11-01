package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.dto.testcase.StartTestDto;
import com.hogwartstest.aitestmini.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.UpdateHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.UpdateHogwartsTestCaseStatusDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "霍格沃兹测试学院-测试用例管理")
@RestController
@RequestMapping("/case")
public class HogwartsTestCaseController {

    @Autowired
    private HogwartsTestCaseService hogwartsTestCaseService;

    /**
     *
     * @param addHogwartsTestCaseDto
     * @return
     */
    @ApiOperation("新增文本型测试用例")
    @PostMapping("text")
    public ResultDto saveText(@RequestBody AddHogwartsTestCaseDto addHogwartsTestCaseDto){

        log.info("=====新增文本测试用例-请求入参====："+ JSONObject.toJSONString(addHogwartsTestCaseDto));

        if(Objects.isNull(addHogwartsTestCaseDto)){
            return ResultDto.fail("请求参数不能为空");
        }

        if(StringUtils.isEmpty(addHogwartsTestCaseDto.getCaseData())){
            return ResultDto.fail("测试用例数据不能为空");
        }
        if(StringUtils.isEmpty(addHogwartsTestCaseDto.getCaseName())){
            return ResultDto.fail("测试用例名称不能为空");
        }

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();

        BeanUtils.copyProperties(addHogwartsTestCaseDto, hogwartsTestCase);

        ResultDto resultDto = hogwartsTestCaseService.save(hogwartsTestCase);
        return resultDto;
    }

    /**
     *
     * @param addHogwartsTestCaseDto
     * @return
     */
    @ApiOperation("新增文件型测试用例")
    @PostMapping("file")
    public ResultDto saveFile(@RequestParam("caseFile") MultipartFile caseFile, AddHogwartsTestCaseDto addHogwartsTestCaseDto) throws IOException {

        log.info("=====新增文件测试用例-请求入参====："+ JSONObject.toJSONString(addHogwartsTestCaseDto));

        if(Objects.isNull(addHogwartsTestCaseDto)){
            return ResultDto.fail("请求参数不能为空");
        }

        if(Objects.isNull(caseFile)){
            return ResultDto.fail("测试用例文件不能为空");
        }

        if(StringUtils.isEmpty(addHogwartsTestCaseDto.getCaseName())){
            return ResultDto.fail("测试用例名称不能为空");
        }

        InputStream inputStream =  caseFile.getInputStream();
        String caseData = IOUtils.toString(inputStream,"UTF-8");
        inputStream.close();

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
        BeanUtils.copyProperties(addHogwartsTestCaseDto, hogwartsTestCase);
        //文件类型时需要将文件中的数据进行赋值
        hogwartsTestCase.setCaseData(caseData);

        ResultDto resultDto = hogwartsTestCaseService.save(hogwartsTestCase);
        return resultDto;
    }

    /**
     *
     * @param updateHogwartsTestCaseDto
     * @return
     */
    @ApiOperation("修改测试用例")
    @PutMapping
    public ResultDto<HogwartsTestCase> update(@RequestBody UpdateHogwartsTestCaseDto updateHogwartsTestCaseDto){

        log.info("修改测试用例-入参= "+ JSONObject.toJSONString(updateHogwartsTestCaseDto));

        if(Objects.isNull(updateHogwartsTestCaseDto)){
            return ResultDto.success("测试用例信息不能为空");
        }

        Integer caseId = updateHogwartsTestCaseDto.getId();
        String caseName = updateHogwartsTestCaseDto.getCaseName();

        if(Objects.isNull(caseId)){
            return ResultDto.success("测试用例id不能为空");
        }

        if(StringUtils.isEmpty(caseName)){
            return ResultDto.success("测试用例名称不能为空");
        }

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
        BeanUtils.copyProperties(updateHogwartsTestCaseDto, hogwartsTestCase);

        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.update(hogwartsTestCase);
        return resultDto;
    }

    /**
     *
     * @param caseId
     * @return
     */
    @ApiOperation("根据caseId查询")
    @GetMapping("/{caseId}")
    public ResultDto<HogwartsTestCase> getById(@PathVariable Integer caseId){

        log.info("根据caseId查询-入参= "+ caseId);

        if(Objects.isNull(caseId)){
            return ResultDto.success("caseId不能为空");
        }

        ///todo
        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.getById(caseId);
        return resultDto;
    }


    /**
     * 根据caseId查询case原始数据
     *  地址不要随便改 ${callbackurl}/case/data/  有引用
     * @param caseId 测试用例id
     * @return
     */
    @ApiOperation("根据测试用例id查询")
    @GetMapping("data/{caseId}")
    public String getCaseDataById(@PathVariable Integer caseId) {
        log.info("=====根据用户id和caseId查询case原始数据-请求入参====："+ caseId);

        ///todo
        String caseData = hogwartsTestCaseService.getCaseDataById(caseId);
        log.info("=====根据用户id和caseId查询case原始数据-请求出参====："+ caseData);
        return caseData;
    }

    /**
     *
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    public ResultDto<List<HogwartsTestCase>> list(){

        ResultDto<List<HogwartsTestCase>> responseResultDto = hogwartsTestCaseService.list();
        return responseResultDto;
    }

    /**
     * 执行测试
     * @param startTestDto
     * @return
     * @throws Exception
     */
    @PostMapping("start")
    @ApiOperation("开始测试")
    public ResultDto testStart(@RequestBody StartTestDto startTestDto) throws Exception {
        log.info("=====开始测试-请求入参====："+ JSONObject.toJSONString(startTestDto));

        if(Objects.isNull(startTestDto)){
            return ResultDto.fail("开始测试请求不能为空");
        }
        if(Objects.isNull(startTestDto.getCaseId())){
            return ResultDto.fail("用例id不能为空");
        }

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
        hogwartsTestCase.setId(startTestDto.getCaseId());

        return hogwartsTestCaseService.startTask(hogwartsTestCase);
    }

    /**
     *
     * @param updateHogwartsTestCaseStatusDto
     * @return
     */
    @ApiOperation("修改测试用例状态")
    @PutMapping("status")
    public ResultDto<HogwartsTestCase> updateStatus(@RequestBody UpdateHogwartsTestCaseStatusDto updateHogwartsTestCaseStatusDto){

        log.info("修改测试用例状态-入参= "+ JSONObject.toJSONString(updateHogwartsTestCaseStatusDto));

        if(Objects.isNull(updateHogwartsTestCaseStatusDto)){
            return ResultDto.success("修改测试用例状态信息不能为空");
        }

        Integer caseId = updateHogwartsTestCaseStatusDto.getCaseId();
        Integer status = updateHogwartsTestCaseStatusDto.getStatus();

        if(Objects.isNull(caseId)){
            return ResultDto.success("用例id不能为空");
        }

        if(StringUtils.isEmpty(status)){
            return ResultDto.success("用例状态码不能为空");
        }

        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();

        hogwartsTestCase.setId(caseId);
        hogwartsTestCase.setStatus(status);

        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.updateStatus(hogwartsTestCase);
        return resultDto;
    }

    /**
     *
     * @param caseId
     * @return
     */
    @ApiOperation("根据caseId删除")
    @DeleteMapping("/{caseId}")
    public ResultDto<HogwartsTestCase> delete(@PathVariable Integer caseId){

        log.info("根据caseId删除-入参= "+ caseId);

        if(Objects.isNull(caseId)){
            return ResultDto.success("caseId不能为空");
        }

        ///todo
        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.delete(caseId);
        return resultDto;
    }


}
