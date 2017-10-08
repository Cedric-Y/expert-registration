package com.udiannet.tob.expertreg.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.udiannet.tob.expertreg.domain.User;
import com.udiannet.tob.expertreg.mapper.UserMapper;
import com.udiannet.tob.expertreg.service.UserRegister;

/**
 * 注册业务接口实现类
 */
public class UserRegisterImpl implements UserRegister
{
	private SqlSession sqlSession = SessionFactoryManager.openSession();

	/**
	 * 根据用户登录名或者 email，查询记录
	 */
	@Override
	public User findUserByLoginnameOrEmail(String loginname, String email)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByLoginnameOrEmail(loginname, email);
//		sqlSession.close();
		return user;
	}

	/**
	 * 新用户注册：用户名、身份证、手机号码、密码
	 */
	@Override
	public int userRegister(String loginname, String email, String password)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int resule = mapper.insertUser(loginname, email, password);
		sqlSession.commit();
//		sqlSession.close();
		return resule;
	}
}