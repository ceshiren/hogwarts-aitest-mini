package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.Token;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartstest.aitestmini.dto.jenkins.UpdateHogwartsTestJenkinsDto;
import com.hogwartstest.aitestmini.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartstest.aitestmini.dto.testcase.SaveTestCaseListDto;
import com.hogwartstest.aitestmini.dto.testcase.UpdateHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.user.AddUserDto;
import com.hogwartstest.aitestmini.dto.user.LoginUserDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import com.hogwartstest.aitestmini.service.HogwartsTestUserService;
import com.hogwartstest.aitestmini.util.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
     * @param saveTestCaseListDto
     * @return
     */
    @ApiOperation(value = "批量新增测试用例", notes="仅用于测试用户")
    @PostMapping
    public ResultDto<HogwartsTestUser> saveList(HttpServletRequest request, @RequestBody SaveTestCaseListDto saveTestCaseListDto){

        log.info("=====批量新增测试用例-请求入参====："+ JSONObject.toJSONString(saveTestCaseListDto));

        if(Objects.isNull(saveTestCaseListDto)){
            return ResultDto.success("请求参数不能为空");
        }

        List<AddHogwartsTestCaseDto> testCaseList = saveTestCaseListDto.getTestCaseList();

        if(Objects.isNull(testCaseList) || testCaseList.size()==0){
            return ResultDto.success("测试用例列表不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        saveTestCaseListDto.setCreateUserId(tokenDto.getUserId());


        ResultDto resultDto = hogwartsTestCaseService.saveList(saveTestCaseListDto);
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
        String caseSign = updateHogwartsTestCaseDto.getCaseSign();

        if(Objects.isNull(caseId)){
            return ResultDto.success("测试用例id不能为空");
        }

        if(StringUtils.isEmpty(caseSign)){
            return ResultDto.success("测试用例标识不能为空");
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
    @ApiOperation(value = "根据jenkinsId删除")
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


}
