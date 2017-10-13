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
	User userValidateForLogin(String username, String password);
	/**
	 * 用户登录后，更新其登录时间
	 */
	int updateUserLoginTime(User user);	
	/**
	 * 忘记密码，需要先输入身份证号，匹配上了，才能重设密码
	 */
	//void resetPassword(String pwd);
	/**
	 * 忘记用户名或密码：根据输入的 Email 查询输入是否正确
	 */
	User findUserByEmail(String email);
	/**
	 * 根据用户实体，更新用户信息
	 */
	int updateUserByUser(User user);
	/**
	 * 根据用户 id，email，邮件验证码来查询用户
	 */
	User userValidateFromReset(int id, String email, String validateCode, long currentTime);
	/**
	 * 根据用户名，查找除开当前 u_id 之外的记录
	 */
	User findUserByLoginname(int u_id, String loginname);
	/**
	 * 根据用户 id，重置用户名
	 */
	int resetUserLoginname(int u_id, String loginname);
	/**
	 * 根据用户 id，重置密码
	 */
	int resetUserPassword(int u_id, String password);
}
