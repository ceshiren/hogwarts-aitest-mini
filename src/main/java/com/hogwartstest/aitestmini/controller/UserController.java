package com.hogwartstest.aitestmini.controller;

import com.hogwartstest.aitestmini.common.Token;
import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.constants.UserConstants;
import com.hogwartstest.aitestmini.dto.AddUserDto;
import com.hogwartstest.aitestmini.dto.LoginUserDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.User;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param addUserDto
     * @return
     */
    @ApiOperation(value = "用户注册", notes="仅用于测试用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDto createUser(@RequestBody AddUserDto addUserDto){

        log.info("用户注册-入参= "+ addUserDto);

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

        String newPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+pwd).getBytes());

        User user = new User();
        user.setId(tokenDb.getUserMapSize());
        user.setUserName(addUserDto.getUserName());
        user.setPassword(newPwd);

        tokenDb.addUser(user);

        return ResultDto.success("注册成功");
    }

    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public ResultDto<Token> restfulLogin(@RequestBody LoginUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        log.info("username= "+username);
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return ResultDto.fail("用户名或密码不能为空");
        }

        Token token = new Token();

        String tokenStr = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + username+password).getBytes());

        token.setToken(tokenStr);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setUserId(12);
        tokenDto.setUserName(username);


        tokenDb.addTokenDto(tokenStr, tokenDto);

        return ResultDto.success("登录成功",token);
    }

    @ApiOperation(value = "Restful方式登陆,前后端分离时登录接口")
    @GetMapping("/login/{token}")
    public ResultDto isLogin(@PathVariable String token) {

        boolean loginFlag = tokenDb.isLogin(token);

        tokenDb.getTokenDto(token);

        return ResultDto.success("成功",tokenDb.getTokenDto(token));
    }



    public static void main(String[] args) {


        String pwd2 = DigestUtils.md5DigestAsHex("2343".getBytes());

        System.out.println(pwd2);
    }


}
