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
	int updateUser(@Param("User") User user);
	/**
	 * 根据用户登录名，查询记录
	 */
	User findUserByLoginName(@Param("loginname") String loginname);
	/**
	 * 新用户注册：用户名、身份证、手机号码、密码
	 */
	int insertUser(@Param("loginname") String loginname,
			@Param("email") String email,
			@Param("password") String password);
}	
