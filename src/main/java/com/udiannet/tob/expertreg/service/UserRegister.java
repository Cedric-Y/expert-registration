package com.udiannet.tob.expertreg.service;

import com.udiannet.tob.expertreg.domain.User;

/**
 * 新用户注册相关业务
 */
public interface UserRegister
{
	/**
	 * 根据用户登录名或者 email，查询记录
	 */
//	User findUserByLoginnameOrEmail(String loginname, String email);
	/**
	 * 新用户注册，返回新增记录后产生的记录ID
	 */
	int userRegister(User user);
	/**
	 * 新用户注册，激活注册账号
	 */
	int userValidateFromActivation(int u_id, String email, String validateCode, long currentTime);
	/**
	 * 用户注册校验：根据用户登录名，查询记录
	 */
	User userValidateLoginname(String loginname);	
	/**
	 * 用户注册校验：根据 email，查询记录
	 */
	User userValidateEmail(String email);
}
