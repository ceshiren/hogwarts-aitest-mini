package com.hogwartstest.aitestmini.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Restful方式登陆token
 * 
 */
@Data
public class Token implements Serializable {

	private static final long serialVersionUID = 4043470238789599973L;

	private String token;

	/**
	 * 到期时间
	 */
	private Date expireTime;


}
