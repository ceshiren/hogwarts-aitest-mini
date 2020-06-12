package com.hogwartstest.aitestmini.entity;

import lombok.Data;

/**
 * @Author tlibn
 * @Date 2020/6/12 17:28
 **/
@Data
public class User {

    private Integer id;
    private String userName;
    private String password;
    private String nickName;
    private String email;

}
