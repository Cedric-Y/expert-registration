package com.udiannet.tob.expertreg.service.impl;

import org.apache.ibatis.session.SqlSession;

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
	public User userValidateForLogin(String loginname, String password)
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
		int resule = mapper.updateUserUpdateTime(user);
		sqlSession.commit();
//		sqlSession.close();
		return resule;
	}

	/**
	 * 忘记用户名或密码：根据输入的 Email 查询输入是否正确
	 */
	@Override
	public User findUserByEmail(String email)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByEmail(email);
		return user;
	}

	/**
	 * 根据用户实体，更新用户信息
	 */
	@Override
	public int updateUserByUser(User user)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int resule = mapper.updateUserByUser(user);
		sqlSession.commit();
		return resule;
	}

	/**
	 * 根据用户 id，email，邮件验证码来查询用户
	 */
	@Override
	public User userValidateFromReset(int id, String email, String validateCode, long currentTime)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByValidateCode(id, email, validateCode, currentTime, 3);
		return user;
	}

	/**
	 * 根据用户名，查找除开当前 u_id 之外的记录
	 */
	@Override
	public User findUserByLoginname(int u_id, String loginname)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = (User) mapper.findUserByLoginname(u_id, loginname);
		return user;
	}

	/**
	 * 根据用户 id，重置用户名
	 */
	@Override
	public int resetUserLoginname(int u_id, String loginname)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int resule = mapper.resetUserLoginname(u_id, loginname);
		sqlSession.commit();
		return resule;
	}

	/**
	 * 根据用户 id，重置密码
	 */
	@Override
	public int resetUserPassword(int u_id, String password)
	{
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int resule = mapper.resetUserPassword(u_id, password);
		sqlSession.commit();
		return resule;
	}

	
}
