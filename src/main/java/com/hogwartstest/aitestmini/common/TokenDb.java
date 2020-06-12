package com.hogwartstest.aitestmini.common;

import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:36
 **/

@Component
public class TokenDb {

    //key是用户id，用户名和密码的md5值，
    private Map<String, TokenDto> tokenMap = new HashMap<>();

    //用户数据，暂存
    private Map<Integer, User> userMap = new HashMap<>();


    public int getTokenMapSize() {
        return tokenMap.size();
    }

    public int getUserMapSize() {
        return userMap.size();
    }

    public TokenDto getTokenDto(String token){
        return tokenMap.get(token);
    }

    //也可以实现成登录用户互踢，2种方式，1是id前后缀，2是id-token=map的key-value
    public TokenDto addTokenDto(String token,TokenDto tokenDto){

        if(Objects.isNull(tokenDto)){
            return tokenDto;
        }

        return tokenMap.put(token,tokenDto);
    }

    public boolean isLogin(String token){
        return tokenMap.get(token)!=null;
    }




    public User getUser(Integer id){
        return userMap.get(id);
    }

    public User addUser(User user){
        Integer id = user.getId();
        if(Objects.isNull(id)){
            throw new RuntimeException("用户id不能为空");
        }
        return userMap.put(id,user);
    }



}
