package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartstest.aitestmini.dto.testcase.UpdateHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import com.hogwartstest.aitestmini.util.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "霍格沃兹测试学院-测试用例管理")
@RestController
@RequestMapping("/testCase")
public class HogwartsTestCaseController {

    @Autowired
    private HogwartsTestCaseService hogwartsTestCaseService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param addHogwartsTestCaseDto
     * @return
     */
    @ApiOperation(value = "批量新增测试用例", notes="仅用于测试用户")
    @PostMapping("text")
    public ResultDto saveText(HttpServletRequest request, @RequestBody AddHogwartsTestCaseDto addHogwartsTestCaseDto){

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

        CopyUtil.copyPropertiesCglib(addHogwartsTestCaseDto,hogwartsTestCase);
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestCase.setCreateUserId(tokenDto.getUserId());

        ResultDto resultDto = hogwartsTestCaseService.save(hogwartsTestCase);
        return resultDto;
    }

    /**
     *
     * @param addHogwartsTestCaseDto
     * @return
     */
    @ApiOperation(value = "批量新增测试用例", notes="仅用于测试用户")
    @PostMapping("file")
    public ResultDto saveFile(HttpServletRequest request, @RequestParam("caseFile") MultipartFile caseFile, AddHogwartsTestCaseDto addHogwartsTestCaseDto) throws IOException {

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

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
        hogwartsTestCase.setCreateUserId(tokenDto.getUserId());
        CopyUtil.copyPropertiesCglib(addHogwartsTestCaseDto,hogwartsTestCase);
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
    @ApiOperation(value = "修改测试用例")
    @PutMapping
    public ResultDto<HogwartsTestCase> update(HttpServletRequest request, @RequestBody UpdateHogwartsTestCaseDto updateHogwartsTestCaseDto){

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
        CopyUtil.copyPropertiesCglib(updateHogwartsTestCaseDto,hogwartsTestCase);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestCase.setCreateUserId(tokenDto.getUserId());

        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.update(hogwartsTestCase);
        return resultDto;
    }

    /**
     *
     * @param caseId
     * @return
     */
    @ApiOperation(value = "根据caseId查询")
    @GetMapping("/{caseId}")
    public ResultDto<HogwartsTestCase> getById(HttpServletRequest request, @PathVariable Integer caseId){

        log.info("根据caseId查询-入参= "+ caseId);

        if(Objects.isNull(caseId)){
            return ResultDto.success("caseId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.getById(caseId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param caseId
     * @return
     */
    @ApiOperation(value = "根据caseId删除")
    @DeleteMapping("/{caseId}")
    public ResultDto<HogwartsTestCase> delete(HttpServletRequest request, @PathVariable Integer caseId){

        log.info("根据caseId删除-入参= "+ caseId);

        if(Objects.isNull(caseId)){
            return ResultDto.success("caseId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<HogwartsTestCase> resultDto = hogwartsTestCaseService.delete(caseId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<HogwartsTestCase>> list(HttpServletRequest request, PageTableRequest<QueryHogwartsTestCaseListDto> pageTableRequest){

        log.info("测试用例列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryHogwartsTestCaseListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryHogwartsTestCaseListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<HogwartsTestCase>> responseResultDto = hogwartsTestCaseService.list(pageTableRequest);
        return responseResultDto;
    }

    /**
     * 根据caseId查询case原始数据
     *  地址不要随便改 ${caseDataUrl}/testcase/data/  有引用
     * @param caseId 测试用例id
     * @return
     */
    @ApiOperation(value = "根据测试用例id查询")
    @GetMapping("data/{caseId}")
    public String getCaseDataById(HttpServletRequest request, @PathVariable Integer caseId) {
        log.info("=====根据用户id和caseId查询case原始数据-请求入参====："+ caseId);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        String caseData = hogwartsTestCaseService.getCaseDataById(tokenDto.getUserId(), caseId);
        log.info("=====根据用户id和caseId查询case原始数据-请求出参====："+ caseData);
        return caseData;
    }


}
