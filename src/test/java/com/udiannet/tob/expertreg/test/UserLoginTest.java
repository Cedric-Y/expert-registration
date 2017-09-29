package com.udiannet.tob.expertreg.test;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.udiannet.tob.expertreg.dao.UserLoginDao;
import com.udiannet.tob.expertreg.dao.impl.SessionFactoryManager;
import com.udiannet.tob.expertreg.domain.User;
import com.udiannet.tob.expertreg.mapper.UserMapper;
import com.udiannet.tob.expertreg.util.TokenProccessor;

public class UserLoginTest
{
	@Test
	public void getUserByIdTest()
	{
//		SqlSessionFactory sqlSessionFactory = null;
//		Reader reader;
//
//		try
//		{
//			reader = Resources.getResourceAsReader("mybatis.xml");
//			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		SqlSession session = sqlSessionFactory.openSession();

		SqlSession session = SessionFactoryManager.openSession();

		try
		{
			User user = (User) session.selectOne("UserLogin.getUserById", 1);
			System.out.println(user);
		}
		finally
		{
			session.close();
		}
	}

	@Test
	public void tokenIdTest()
	{
		System.out.println(TokenProccessor.getInstance().makeToken());
	}

	@Test
	public void findUserByUsernameAndPasswordTest()
	{
		SqlSession session = SessionFactoryManager.openSession();

		try
		{
			UserMapper mapper = session.getMapper(UserMapper.class);			
			User user = (User) mapper.findUserByUsernameAndPassword("test", "1");
			System.out.println(user);
		}
		finally
		{
			session.close();
		}
	}
	
	@Test
	public void md5Test()
	{
		String s = "1";
		
		try
		{
			MessageDigest md5=MessageDigest.getInstance("MD5");		
			String result = Base64.getEncoder().encodeToString(md5.digest(s.getBytes("utf-8")));
			System.out.println(result);
		}
		catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
