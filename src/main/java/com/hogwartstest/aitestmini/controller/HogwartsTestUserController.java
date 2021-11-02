package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartstest.aitestmini.common.Token;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.user.AddUserDto;
import com.hogwartstest.aitestmini.dto.user.LoginUserDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;
import com.hogwartstest.aitestmini.service.HogwartsTestUserService;
import com.hogwartstest.aitestmini.util.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@Api(tags = "霍格沃兹测试学院-用户管理")
@RestController
@RequestMapping("/user")
public class HogwartsTestUserController {

    @Autowired
    private HogwartsTestUserService hogwartsTestUserService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param addUserDto
     * @return
     */
    @ApiImplicitParam(name = "token", value = "无需token值", required = false, dataType = "String",paramType="header")
    @ApiOperation(value = "用户注册", notes="仅用于测试用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDto<HogwartsTestUser> save(@RequestBody AddUserDto addUserDto){

        log.info("用户注册-入参= "+ addUserDto);

        if(true){
            return ResultDto.fail("演示环境不可操作");
        }

        if(Objects.isNull(addUserDto)){
            return ResultDto.success("用户信息不能为空");
        }

        String userName = addUserDto.getUserName();

        if(StringUtils.isEmpty(userName)){
            return ResultDto.success("用户名不能为空");
        }

        String pwd = addUserDto.getPassword();

        if(StringUtils.isEmpty(pwd)){
            return ResultDto.success("密码不能为空");
        }

        HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
        CopyUtil.copyPropertiesCglib(addUserDto,hogwartsTestUser);
        ResultDto<HogwartsTestUser> resultDto = hogwartsTestUserService.save(hogwartsTestUser);
        return resultDto;
    }

    @ApiImplicitParam(name = "token", value = "无需token值", required = false,  dataType = "String",paramType="header")
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public ResultDto<Token> login(@RequestBody LoginUserDto loginUserDto) {

        log.info("用户登录-入参= "+ JSONObject.toJSONString(loginUserDto));
        if(true){
            return ResultDto.fail("演示环境不可操作");
        }

        String userName = loginUserDto.getUserName();
        String password = loginUserDto.getPassword();
        log.info("userName= "+userName);
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            return ResultDto.fail("用户名或密码不能为空");
        }

        ResultDto<Token> resultDto = hogwartsTestUserService.login(userName, password);

        return resultDto;
    }

    @ApiOperation(value = "登出接口")
    @DeleteMapping("/logout")
    public ResultDto logout(HttpServletRequest request) {

        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);

        if(!loginFlag){
            return ResultDto.fail("用户未登录，无需退出");
        }

        TokenDto tokenDto = tokenDb.removeTokenDto(token);

        return ResultDto.success("成功",tokenDto);
    }

    @ApiOperation(value = "是否已经登录接口")
    @GetMapping("/isLogin")
    public ResultDto<TokenDto> isLogin(HttpServletRequest request) {

        String token = request.getHeader(UserConstants.LOGIN_TOKEN);

        boolean loginFlag = tokenDb.isLogin(token);

        TokenDto tokenDto = tokenDb.getTokenDto(token);

        return ResultDto.success("成功",tokenDto);
    }

}
