package com.udiannet.tob.expertreg.dao.impl;

import org.apache.ibatis.session.SqlSession;

import com.udiannet.tob.expertreg.dao.UserLoginDao;
import com.udiannet.tob.expertreg.domain.User;
import com.udiannet.tob.expertreg.service.impl.SessionFactoryManager;

/**
 * 负责用户登录的 DAO 层实现类
 */
public class UserLoginDaoImpl implements UserLoginDao
{
	/**
	 * 根据用户名或者手机号码以及密码，查找外网用户
	 */
	@Override
	public User findUserByUsernameAndPassword(String username, String password)
	{
		SqlSession session = SessionFactoryManager.openSession();

		UserLoginDao dao = session.getMapper(UserLoginDao.class);
		User user = (User) dao.findUserByUsernameAndPassword(username, password);

		session.close();
		return user;
	}
}
