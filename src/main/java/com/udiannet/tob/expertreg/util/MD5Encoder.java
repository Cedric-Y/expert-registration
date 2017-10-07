package com.udiannet.tob.expertreg.util;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 对字符串进行 MD5 加密
 */
public class MD5Encoder
{
	public static String encoder(String str)
	{
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return Base64.getEncoder().encodeToString(md5.digest(str.getBytes("utf-8")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
