package com.udiannet.tob.expertreg.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

/**
 * 生成 Token 的工具类
 */
public class TokenProccessor
{
	/**
	 * 使用内部静态类的单例模式
	 */
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

	/**
	 * 生成Token
	 */
	public String makeToken()
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
}
