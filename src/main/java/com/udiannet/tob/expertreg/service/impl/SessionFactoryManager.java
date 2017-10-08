package com.udiannet.tob.expertreg.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * mybatis 的会话管理
 */
public class SessionFactoryManager
{
	private static SqlSessionFactory _sqlSessionFactory;

	static
	{
		SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
		_sqlSessionFactory = ssfb.build(SessionFactoryManager.class.getClassLoader().getResourceAsStream("mybatis.xml"));
	}

	public static SqlSession openSession()
	{
		return _sqlSessionFactory.openSession();
	}
}
