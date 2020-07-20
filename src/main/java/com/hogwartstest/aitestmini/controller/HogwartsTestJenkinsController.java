package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartstest.aitestmini.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartstest.aitestmini.dto.jenkins.UpdateHogwartsTestJenkinsDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.service.HogwartsTestJenkinsService;
import com.hogwartstest.aitestmini.util.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "霍格沃兹测试学院-Jenkins管理")
@RestController
@RequestMapping("/jenkins")
public class HogwartsTestJenkinsController {

    @Autowired
    private HogwartsTestJenkinsService hogwartsTestJenkinsService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param addHogwartsTestJenkinsDto
     * @return
     */
    @ApiOperation(value = "添加Jenkins")
    @PostMapping
    public ResultDto<HogwartsTestJenkins> save(HttpServletRequest request, @RequestBody AddHogwartsTestJenkinsDto addHogwartsTestJenkinsDto){

        log.info("添加Jenkins-入参= "+ JSONObject.toJSONString(addHogwartsTestJenkinsDto));

        if(Objects.isNull(addHogwartsTestJenkinsDto)){
            return ResultDto.success("Jenkins信息不能为空");
        }

        String name = addHogwartsTestJenkinsDto.getName();

        if(StringUtils.isEmpty(name)){
            return ResultDto.success("Jenkins名称不能为空");
        }

        HogwartsTestJenkins hogwartsTestJenkins = new HogwartsTestJenkins();
        CopyUtil.copyPropertiesCglib(addHogwartsTestJenkinsDto,hogwartsTestJenkins);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

        String commandRunCasSuffix = addHogwartsTestJenkinsDto.getCommandRunCasSuffix();
        //过滤待.的后缀，如.yml改为yml
        if(!StringUtils.isEmpty(commandRunCasSuffix)){
            hogwartsTestJenkins.setCommandRunCasSuffix(commandRunCasSuffix.replace(".",""));
        }


        ResultDto<HogwartsTestJenkins> resultDto = hogwartsTestJenkinsService.save(tokenDto, hogwartsTestJenkins);
        return resultDto;
    }

    /**
     *
     * @param updateHogwartsTestJenkinsDto
     * @return
     */
    @ApiOperation(value = "修改Jenkins")
    @PutMapping
    public ResultDto<HogwartsTestJenkins> update(HttpServletRequest request, @RequestBody UpdateHogwartsTestJenkinsDto updateHogwartsTestJenkinsDto){

        log.info("修改Jenkins-入参= "+ JSONObject.toJSONString(updateHogwartsTestJenkinsDto));

        if(Objects.isNull(updateHogwartsTestJenkinsDto)){
            return ResultDto.success("Jenkins信息不能为空");
        }

        Integer jenkinsId = updateHogwartsTestJenkinsDto.getId();
        String name = updateHogwartsTestJenkinsDto.getName();

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }


        if(StringUtils.isEmpty(name)){
            return ResultDto.success("Jenkins名称不能为空");
        }

        HogwartsTestJenkins hogwartsTestJenkins = new HogwartsTestJenkins();
        CopyUtil.copyPropertiesCglib(updateHogwartsTestJenkinsDto,hogwartsTestJenkins);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

        String commandRunCasSuffix = updateHogwartsTestJenkinsDto.getCommandRunCasSuffix();
        //过滤待.的后缀，如.yml改为yml
        if(!StringUtils.isEmpty(commandRunCasSuffix)){
            hogwartsTestJenkins.setCommandRunCasSuffix(commandRunCasSuffix.replace(".",""));
        }

        ResultDto<HogwartsTestJenkins> resultDto = hogwartsTestJenkinsService.update(tokenDto, hogwartsTestJenkins);
        return resultDto;
    }

    /**
     *
     * @param jenkinsId
     * @return
     */
    @ApiOperation(value = "根据jenkinsId查询")
    @GetMapping("/{jenkinsId}")
    public ResultDto<HogwartsTestJenkins> getById(HttpServletRequest request, @PathVariable Integer jenkinsId){

        log.info("根据jenkinsId查询-入参= "+ jenkinsId);

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<HogwartsTestJenkins> resultDto = hogwartsTestJenkinsService.getById(jenkinsId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param jenkinsId
     * @return
     */
    @ApiOperation(value = "根据jenkinsId删除")
    @DeleteMapping("/{jenkinsId}")
    public ResultDto<HogwartsTestJenkins> delete(HttpServletRequest request, @PathVariable Integer jenkinsId){

        log.info("根据jenkinsId删除-入参= "+ jenkinsId);

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<HogwartsTestJenkins> resultDto = hogwartsTestJenkinsService.delete(jenkinsId, tokenDto);
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<HogwartsTestJenkins>> list(HttpServletRequest request, PageTableRequest<QueryHogwartsTestJenkinsListDto> pageTableRequest){

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        log.info("列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest) + "tokenDto=  " +JSONObject.toJSONString(tokenDto));

        QueryHogwartsTestJenkinsListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryHogwartsTestJenkinsListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<HogwartsTestJenkins>> responseResultDto = hogwartsTestJenkinsService.list(pageTableRequest);
        return responseResultDto;
    }

}
