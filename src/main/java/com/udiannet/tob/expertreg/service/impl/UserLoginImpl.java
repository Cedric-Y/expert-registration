package com.udiannet.tob.expertreg.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.udiannet.tob.expertreg.dao.impl.SessionFactoryManager;
import com.udiannet.tob.expertreg.domain.User;
import com.udiannet.tob.expertreg.mapper.UserMapper;
import com.udiannet.tob.expertreg.service.UserLogin;

/**
 * 用户登录相关业务接口实现类
 */
public class UserLoginImpl implements UserLogin
{
	private SqlSession sqlSession = SessionFactoryManager.openSession();

	/**
	 * 根据用户输入的用户名和密码进行登录校验，还得包括校验码
	 */
	@Override
	public User userValidate(String loginname, String password)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByLoginnameAndPassword(loginname, password);
//		sqlSession.close();
		return user;
	}

	/**
	 * 用户登录后，更新其登录时间
	 */
	@Override
	public int updateUserLoginTime(User user)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int resule = mapper.updateUser(user);
		sqlSession.commit();
//		sqlSession.close();
		return resule;

	}

}
