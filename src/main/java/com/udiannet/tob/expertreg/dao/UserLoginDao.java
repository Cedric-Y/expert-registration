package com.udiannet.tob.expertreg.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 负责用户登录方面的 DAO 接口
 */
public interface UserLoginDao
{
	int findUserByUsernameAndPassword(@Param("username") String username, @Param("password")String password);
}
