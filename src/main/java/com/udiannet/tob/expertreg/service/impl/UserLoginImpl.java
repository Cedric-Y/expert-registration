package com.udiannet.tob.expertreg.service.impl;

import com.udiannet.tob.expertreg.dao.UserLoginDao;
import com.udiannet.tob.expertreg.dao.impl.UserLoginDaoImpl;
import com.udiannet.tob.expertreg.service.UserLogin;

/**
 * 用户登录接口实现类
 */
public class UserLoginImpl implements UserLogin
{
	// 登录 DAO 层实例
	private UserLoginDao dao = new UserLoginDaoImpl();

	@Override
	public int userValidate(String username, String password)
	{
		if (dao.findUserByUsernameAndPassword(username, password) != null)
			return 1;
		else
			return 0;
	}

	@Override
	public void resetUserName(String idcard)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resetPassword(String pwd)
	{
		// TODO Auto-generated method stub

	}

}
