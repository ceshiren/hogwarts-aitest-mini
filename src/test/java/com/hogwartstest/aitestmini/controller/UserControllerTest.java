package com.hogwartstest.aitestmini.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

/**
 * @Author tlibn
 * @Date 2019/8/4 10:43
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    //需要使用shiro进行权限校验的接口使用的MockMvc
    private MockMvc shiroMvc;
    //不需要使用shiro进行权限校验的接口使用的MockMvc
    private MockMvc mvc;

    private String token;

    @Autowired
    private WebApplicationContext context;

    /*@Before
    public void init() {
        System.out.println("测试开始-----------------");

        //设置shiro的过滤器，用于单元测试获取shiro的session
        shiroMvc = MockMvcBuilders.webAppContextSetup(context).addFilters((Filter) context.getBean("shiroFilter")).build();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
*/

    /**
     * 用户注册
     * @throws Exception
     */
    //@Test
    public void userRegister() throws Exception {

        /*User user = new User();
        user.setUsername("田利彬1");
        user.setPassword("ttt123");
        user.setEmail("132@qq.com");
        user.setPhone("13222223333");

        mvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                //.header("token","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJ1c2VyTmFtZSI6IueUsOWIqeW9rCIsInVzZXJJZCI6IjEyMyIsImV4cCI6MTU2NDg4ODM0OH0.05riTU7HQFsbvlfr8gu77oxOGFDRM9N6H2SYo-U_RNU")
                .content(JSONObject.toJSONString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"resultCode\":1")));*/

    }

    /**
     * 用户登录
     * @throws Exception
     */
    //@Test
    public void userLogin() throws Exception {

        /*User user = new User();
        user.setUsername("田利彬1");
        user.setPassword("ttt123");

        MvcResult result = shiroMvc.perform(MockMvcRequestBuilders.post("/user/login/rest")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                //.header("token","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJ1c2VyTmFtZSI6IueUsOWIqeW9rCIsInVzZXJJZCI6IjEyMyIsImV4cCI6MTU2NDg4ODM0OH0.05riTU7HQFsbvlfr8gu77oxOGFDRM9N6H2SYo-U_RNU")
                .content(JSONObject.toJSONString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"resultCode\":1"))).andReturn();

        //获取登录后的token，用于后面接口用户权限校验
        String resultStr = result.getResponse().getContentAsString();
        JSONObject json = JSON.parseObject(resultStr);
        token = json.getJSONObject("data").getString("token");

        System.out.println("token= "+ token);*/

    }

    /**
     * 获取当前用户信息
     * @throws Exception
     */
    //@Test
    public void userCurrent() throws Exception {
        /*userLogin();

        shiroMvc.perform(MockMvcRequestBuilders.get("/user/current")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("token",token)
                .content(JSONObject.toJSONString(new Object())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"resultCode\":1")));*/

    }

    /**
     * 用户修改个人信息
     * @throws Exception
     */
/*    @Test
    public void userUpdateSelf() throws Exception {

        userLogin();

        User user = new User();
        user.setUsername("田利彬1");
        user.setEmail("1322222@qq.com");
        user.setPhone("13222223333");

        MvcResult result = shiroMvc.perform(MockMvcRequestBuilders.put("/user/updateSelf")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("token",token)
                .content(JSONObject.toJSONString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"resultCode\":1"))).andReturn();

    }

    *//**
     * 用户修改个人信息
     * @throws Exception
     *//*
    @Test
    public void userChangePassword() throws Exception {

        userLogin();

        UserEditPwdDto userEditPwdDto = new UserEditPwdDto();
        userEditPwdDto.setOldPassword("ttt1231");
        userEditPwdDto.setNewPassword("ttt123");

        shiroMvc.perform(MockMvcRequestBuilders.put("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("token",token)
                .content(JSONObject.toJSONString(userEditPwdDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //此处判断为失败，密码不随意修改
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"resultCode\":0")));
    }*/

    //@After
    public void after() {
        System.out.println("测试结束-----------------");
    }


}
