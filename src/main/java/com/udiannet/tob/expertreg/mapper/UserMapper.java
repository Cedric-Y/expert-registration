package com.udiannet.tob.expertreg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.udiannet.tob.expertreg.domain.User;

@Mapper
public interface UserMapper
{
	/**
	 * 根据用户名或者email以及密码，查询记录
	 */
	User findUserByLoginnameAndPassword(@Param("loginname") String loginname, @Param("password") String password);

	/**
	 * 用户登录后，更新其登录时间
	 */
	int updateUserUpdateTime(@Param("User") User user);

	/**
	 * 根据用户登录名或者 email，查询记录
	 */
	User findUserByLoginnameOrEmail(@Param("loginname") String loginname, @Param("email") String email);

	/**
	 * 新用户注册：用户名、身份证、手机号码、密码
	 */
	int insertUser(@Param("loginname") String loginname, @Param("email") String email,
			@Param("password") String password);

	/**
	 * 忘记用户名：根据输入的 Email 查询输入是否正确
	 */
	User findUserByEmail(@Param("email") String email);

	/**
	 * 根据用户实体，更新用户信息
	 */
	int updateUserByUser(@Param("User") User user);

	/**
	 * 根据用户 id，email，邮件验证码来查询用户
	 */
	User findUserByValidateCode(@Param("id") int id, @Param("email") String email,
			@Param("validatecode") String validateCode, @Param("currenttime") long currentTime);

	/**
	 * 根据用户名，查找除开当前 u_id 之外的记录
	 */
	User findUserByLoginname(@Param("id") int u_id, @Param("loginname") String loginname);
	/**
	 * 根据用户 id，重置用户名
	 */
	int resetUserLoginname(@Param("u_id") int u_id, @Param("loginname") String loginname);
	/**
	 * 根据用户 id，重置密码
	 */
	int resetUserPassword(@Param("u_id") int u_id, @Param("password") String password);
}
