package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.dto.task.*;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;
import com.hogwartstest.aitestmini.service.HogwartsTestTaskService;
import com.hogwartstest.aitestmini.util.CopyUtil;
import com.hogwartstest.aitestmini.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "霍格沃兹测试学院-测试任务管理")
@RestController
@RequestMapping("/task")
public class HogwartsTestTaskController {

    @Autowired
    private HogwartsTestTaskService hogwartsTestTaskService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param testTaskDto
     * @return
     */
    @ApiOperation(value = "添加测试任务")
    @PostMapping
    public ResultDto<HogwartsTestTask> save(HttpServletRequest request, @RequestBody TestTaskDto testTaskDto){

        log.info("添加测试任务-入参= "+ JSONObject.toJSONString(testTaskDto));

        if(Objects.isNull(testTaskDto)){
            return ResultDto.fail("测试任务入参不能为空");
        }

        AddHogwartsTestTaskDto addHogwartsTestTaskDto = testTaskDto.getTestTask();

        if(Objects.isNull(addHogwartsTestTaskDto)){
            return ResultDto.fail("测试任务不能为空");
        }

        if(Objects.isNull(addHogwartsTestTaskDto.getName())){
            return ResultDto.fail("测试任务名称不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        addHogwartsTestTaskDto.setCreateUserId(tokenDto.getUserId());
        addHogwartsTestTaskDto.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        ResultDto<HogwartsTestTask> resultDto = hogwartsTestTaskService.save(testTaskDto, Constants.TASK_TYPE_ONE);
        return resultDto;
    }

    /**
     *
     * @param updateHogwartsTestTaskDto
     * @return
     */
    @ApiOperation(value = "修改测试任务")
    @PutMapping
    public ResultDto<HogwartsTestTask> update(HttpServletRequest request, @RequestBody UpdateHogwartsTestTaskDto updateHogwartsTestTaskDto){

        log.info("修改测试任务-入参= "+ JSONObject.toJSONString(updateHogwartsTestTaskDto));

        if(Objects.isNull(updateHogwartsTestTaskDto)){
            return ResultDto.fail("测试任务信息不能为空");
        }

        Integer taskId = updateHogwartsTestTaskDto.getId();
        String name = updateHogwartsTestTaskDto.getName();

        if(Objects.isNull(taskId)){
            return ResultDto.fail("任务id不能为空");
        }


        if(StringUtils.isEmpty(name)){
            return ResultDto.fail("任务名称不能为空");
        }

        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        CopyUtil.copyPropertiesCglib(updateHogwartsTestTaskDto,hogwartsTestTask);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserId());

        ResultDto<HogwartsTestTask> resultDto = hogwartsTestTaskService.update(hogwartsTestTask);
        return resultDto;
    }

    /**
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "根据任务Id查询")
    @GetMapping("/{taskId}")
    public ResultDto<HogwartsTestTask> getById(HttpServletRequest request, @PathVariable Integer taskId){

        log.info("根据任务Id查询-入参= "+ taskId);

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<HogwartsTestTask> resultDto = hogwartsTestTaskService.getById(taskId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "根据任务Id删除")
    @DeleteMapping("/{taskId}")
    public ResultDto<HogwartsTestTask> delete(HttpServletRequest request, @PathVariable Integer taskId){

        log.info("根据任务Id删除-入参= "+ taskId);

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<HogwartsTestTask> resultDto = hogwartsTestTaskService.delete(taskId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(HttpServletRequest request, PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest){

        log.info("任务列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryHogwartsTestTaskListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryHogwartsTestTaskListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<HogwartsTestTask>> responseResultDto = hogwartsTestTaskService.list(pageTableRequest);
        return responseResultDto;
    }

    /**
     *
     * @param updateHogwartsTestTaskStatusDto
     * @return
     */
    @ApiOperation(value = "修改测试任务")
    @PutMapping("status")
    public ResultDto<HogwartsTestTask> updateStatus(HttpServletRequest request, @RequestBody UpdateHogwartsTestTaskStatusDto updateHogwartsTestTaskStatusDto){

        log.info("修改测试任务状态-入参= "+ JSONObject.toJSONString(updateHogwartsTestTaskStatusDto));

        if(Objects.isNull(updateHogwartsTestTaskStatusDto)){
            return ResultDto.success("修改测试任务状态信息不能为空");
        }

        Integer taskId = updateHogwartsTestTaskStatusDto.getTaskId();
        String buildUrl = updateHogwartsTestTaskStatusDto.getBuildUrl();
        Integer status = updateHogwartsTestTaskStatusDto.getStatus();

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务id不能为空");
        }

        if(StringUtils.isEmpty(buildUrl)){
            return ResultDto.success("任务构建地址不能为空");
        }

        if(StringUtils.isEmpty(status)){
            return ResultDto.success("任务状态码不能为空");
        }

        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();

        hogwartsTestTask.setId(taskId);
        hogwartsTestTask.setBuildUrl(buildUrl);
        hogwartsTestTask.setStatus(status);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserId());

        ResultDto<HogwartsTestTask> resultDto = hogwartsTestTaskService.updateStatus(hogwartsTestTask);
        return resultDto;
    }

    /**
     * 开始测试接口
     * @param startTestDto
     * @return
     * @throws Exception
     */
    @PostMapping("start")
    @ApiOperation(value = "开始测试", notes = "开始测试-说明", httpMethod = "POST", response = ResultDto.class)
    public ResultDto testStart(HttpServletRequest request
            ,@ApiParam(name="修改测试任务状态对象", required=true)@RequestBody StartTestDto startTestDto) throws Exception {
        log.info("=====开始测试-请求入参====："+ JSONObject.toJSONString(startTestDto));

        if(Objects.isNull(startTestDto)){
            return ResultDto.fail("开始测试请求不能为空");
        }
        if(Objects.isNull(startTestDto.getTaskId())){
            return ResultDto.fail("任务id不能为空");
        }

        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        log.info("token== "+token);

        HogwartsTestTask hogwartsTestTask = new HogwartsTestTask();
        hogwartsTestTask.setId(startTestDto.getTaskId());
        hogwartsTestTask.setTestCommand(startTestDto.getTestCommand());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserId());
        hogwartsTestTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());


        String url = request.getRequestURL().toString();
        log.info("请求地址== "+url);
        url = StrUtil.getHostAndPort(request.getRequestURL().toString());

        RequestInfoDto requestInfoDto = new RequestInfoDto();
        requestInfoDto.setBaseUrl(url);
        requestInfoDto.setRequestUrl(url);
        requestInfoDto.setToken(token);
        log.info("requestInfoDto== "+ JSONObject.toJSONString(requestInfoDto));
        return hogwartsTestTaskService.startTask(tokenDto, requestInfoDto, hogwartsTestTask);
    }

}
