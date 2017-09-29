package com.udiannet.tob.expertreg.service;

import com.udiannet.tob.expertreg.domain.User;

/**
 * 用户登录相关业务
 */
public interface UserLogin
{
	/**
	 * 根据用户输入的用户名和密码进行登录校验，还得包括校验码
	 */
	User userValidate(String username, String password);
	/**
	 * 用户登录后，更新其登录时间
	 */
	int updateUserLoginTime(User user);
	/**
	 * 忘记用户名，要先输入身份证号，匹配上了，才能重新修改用户名和密码
	 */
	//void resetUserName(String idcard);
	/**
	 * 忘记密码，需要先输入身份证号，匹配上了，才能重设密码
	 */
	//void resetPassword(String pwd);	
}
