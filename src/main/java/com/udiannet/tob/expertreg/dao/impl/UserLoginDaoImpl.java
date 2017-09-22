package com.udiannet.tob.expertreg.dao.impl;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import com.udiannet.tob.expertreg.dao.UserLoginDao;
import com.udiannet.tob.expertreg.domain.User;
/**
 * 负责用户登录的 DAO 层实现类
 */
public class UserLoginDaoImpl implements UserLoginDao
{
	/**
	 * 根据用户名或者手机号码以及密码，查找外网用户
	 */
	@Override
	public int findUserByUsernameAndPassword(@Param("username") String username, @Param("password")String password)
	{
		SqlSession session = SessionFactoryManager.openSession();
		try
		{
			User user = (User) session
					.selectOne("UserLogin.UserMapper.getUserById", 1);

		}
		finally
		{
			session.close();
		}
		return 0;
	}

}
