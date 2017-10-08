package com.udiannet.tob.expertreg.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

/**
 * 生成 Token 的工具类
 */
public class TokenProccessor
{
	/**
	 * 使用内部静态类的单例模式
	 */
	/*
	private static class TokenProccessorInner
	{
		private static final TokenProccessor INSTANCE = new TokenProccessor();
	}

	private TokenProccessor()
	{
	}

	public static TokenProccessor getInstance()
	{
		return TokenProccessorInner.INSTANCE;
	}
	*/

	/**
	 * 生成Token
	 */
	public static String makeToken()
	{
		String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(token.getBytes());
			return Base64.getEncoder().encodeToString(md5);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * token 检查校验
	 */
	public static boolean checkToken(HttpServletRequest request)
	{
		String reqToken = request.getParameter("token");

		System.out.println("request-token: " + reqToken);
		// 1、如果用户提交的表单数据中没有 token，则用户是重复提交了表单
		if (reqToken == null)
		{
			return false;
		}

		// 取出存储在 Session 中的 token
		String sessionToken = (String) request.getSession().getAttribute("token");
		// 2、如果当前用户的 Session 中不存在 token，则用户是重复提交了表单
		if (sessionToken == null)
		{
			return false;
		}
		// 3、存储在 Session 中的 token 与表单提交的 token 不同，则用户是重复提交了表单
		if (!reqToken.equals(sessionToken))
		{
			return false;
		}

		return true;
	}
}
