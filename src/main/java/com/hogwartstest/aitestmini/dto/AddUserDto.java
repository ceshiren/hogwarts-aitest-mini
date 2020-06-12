package com.hogwartstest.aitestmini.dto;

import lombok.Data;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:49
 **/

@Data
public class AddUserDto {

    private String userName;
    private String password;
    private String email;

}
