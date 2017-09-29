package com.udiannet.tob.expertreg.util;

import java.util.regex.Pattern;

/**
 * 输入校验
 */
public class InputValidation
{
	/**
	 * 登录用户名合法性： 由字母、数字、下划线组成，且开头必须是字母，3到15位
	 */
	public static boolean isLegalLoginName(String loginName)
	{
		Pattern p = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{2,14}$");
		return p.matcher(loginName).matches();
	}

	/**
	 * 姓名合法性：全汉字
	 */
	public static boolean isLegalName(String name)
	{
		Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
		return p.matcher(name).matches();
	}

	/**
	 * 手机号码合法性：全数字，长度为11位
	 */
	public static boolean isLegalPhone(String phone)
	{
		Pattern p = Pattern.compile("^[0-9]{11}$");
		return p.matcher(phone).matches();
	}

	/**
	 * 邮箱合法性：必须包含@符号，必须包含点，点和@之间必须有字符
	 */
	public static boolean isLegalEmail(String email)
	{
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		return p.matcher(email).matches();
	}

	/**
	 * 密码强度判断：3到15位，至少是数字与字母的结合
	 */
	public static int isLegalPassword(String password)
	{
		// 1、长度的判断
		if (password == null || password.length() < 3 || password.length() > 15)
			return 1;

		Pattern p = null;

		// 2、纯数字的判断
		p = Pattern.compile("^\\d+$");
		if (p.matcher(password).matches())
			return 2;
		
		//3、纯字母的判断
		p = Pattern.compile("^[a-zA-Z]+$");
		if (p.matcher(password).matches())
			return 3;
		
		//4、纯特殊字符的判断
		p = Pattern.compile("^[`~!@#$%^&*()+=|{}:;',\\\\\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]$");
		if (p.matcher(password).matches())
			return 4;

		return 0;
	}
}
