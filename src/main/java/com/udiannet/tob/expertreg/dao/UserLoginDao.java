package com.udiannet.tob.expertreg.dao;

import org.apache.ibatis.annotations.Param;

import com.udiannet.tob.expertreg.domain.User;

/**
 * 负责用户登录方面的 DAO 接口
 */
public interface UserLoginDao
{
	User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
