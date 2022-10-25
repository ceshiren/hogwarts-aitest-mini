package com.hogwartstest.aitestmini.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hogwarts_test_user")
@Data
@ApiModel(value="测试用户对象")
public class HogwartsTestUser extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键",required=true)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    @ApiModelProperty(value="用户名",required=true)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码",required=true)
    private String password;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="创建时间",required=true)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value="更新时间",required=true)
    private Date updateTime;

}
