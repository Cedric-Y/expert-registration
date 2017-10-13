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
//	@Override
//	public User findUserByLoginnameOrEmail(String loginname, String email)
//	{
//		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//		User user = (User) mapper.findUserByLoginnameOrEmail(loginname, email);
//		return user;
//	}

	/**
	 * 新用户注册，返回新增记录后产生的记录ID
	 */
	@Override
	public int userRegister(User user)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		mapper.insertUser(user);
		sqlSession.commit();
		return user.getU_id();
	}

	/**
	 * 新用户注册，激活注册账号
	 */
	@Override
	public int userValidateFromActivation(int u_id, String email, String validateCode, long currentTime)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByValidateCode(u_id, email, validateCode, currentTime, 2);
		//找到用户后，需要立即更新它的“用户类型”为“1”
		if(user!=null)
		{
			int result = mapper.updateUserTypeById(u_id, 1);
			sqlSession.commit();
			if (result>0)
				return 1;
			else
				return 0;
		}
		
		return 0;
	}

	/**
	 * 用户注册校验：根据用户登录名，查询记录
	 */
	@Override
	public User userValidateLoginname(String loginname)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByLoginnameForReg(loginname);
		return user;
	}

	/**
	 * 用户注册校验：根据 email，查询记录
	 */
	@Override
	public User userValidateEmail(String email)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByEmailForReg(email);
		return user;
	}
}