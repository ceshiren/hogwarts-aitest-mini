package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.testcase.HogwartsTestTaskCaseRelDetailDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestTaskCaseRelListDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.service.HogwartsTestTaskCaseRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "霍格沃兹测试学院-任务与用例关联管理")
@RestController
@RequestMapping("/taskCaseRel")
public class HogwartsTestTaskCaseRelController {

    @Autowired
    private HogwartsTestTaskCaseRelService hogwartsTestTaskCaseRelService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/listDetail")
    public ResultDto<PageTableResponse<HogwartsTestTaskCaseRelDetailDto>> list(HttpServletRequest request, PageTableRequest<QueryHogwartsTestTaskCaseRelListDto> pageTableRequest){

        log.info("任务与用例关联管理 列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryHogwartsTestTaskCaseRelListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryHogwartsTestTaskCaseRelListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<HogwartsTestTaskCaseRelDetailDto>> responseResultDto = hogwartsTestTaskCaseRelService.listDetail(pageTableRequest);
        return responseResultDto;
    }

}
