package com.udiannet.tob.expertreg.service;

import com.udiannet.tob.expertreg.domain.User;

/**
 * 新用户注册相关业务
 */
public interface UserRegister
{
	/**
	 * 根据用户登录名，查询记录
	 */
	User findUserByLoginName(String loginname);
	/**
	 * 新用户注册：用户名、email、密码
	 */
	int userRegister(String loginname, String email, String password);
}
