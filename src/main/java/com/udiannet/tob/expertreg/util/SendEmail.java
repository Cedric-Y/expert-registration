package com.udiannet.tob.expertreg.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 发送 Email 工具类
 */
public class SendEmail
{
	public static final String HOST = "smtp.163.com";
	public static final String PROTOCOL = "smtp";
	public static final int PORT = 25;
	public static final String FROM = "qiuch_mm@163.com";// 发件人的email
	public static final String PWD = "***";// 发件人密码

	/**
	 * 获取Session
	 */
	private static Session getSession()
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);// 设置服务器地址
		props.put("mail.store.protocol", PROTOCOL);// 设置协议
		props.put("mail.smtp.port", PORT);// 设置端口
		props.put("mail.smtp.auth", true);// 需要请求认证

		Authenticator authenticator = new Authenticator()
		{

			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(FROM, PWD);
			}

		};
		
		Session session = Session.getDefaultInstance(props, authenticator);

		return session;
	}

	/**
	 * 发送邮件
	 * @param toEmail 目的地
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public static void send(String toEmail, String type)
	{
		Session session = getSession();
		try
		{
			System.out.println("--send--");
			// 创建邮件对象
			Message msg = new MimeMessage(session);

			// 设置发送参数、内容等
			msg.setFrom(new InternetAddress(FROM));
			InternetAddress[] address =
			{ new InternetAddress(toEmail) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(setEmailSubject(type));
			msg.setSentDate(new Date());
			msg.setContent(setEmailContent(type), "text/html;charset=utf-8");

			// 发送邮件
			Transport.send(msg);
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
		}
	}
	
	private static String setEmailSubject(String type)
	{
		switch (type)
		{
		case "reset-loginname":
			return "深圳市职业能力专家库-重置用户名";

		case "reset-password":
			return "深圳市职业能力专家库-重置密码";
		}
		
		return null;
	}
	private static String setEmailContent(String type)
	{
		switch (type)
		{
		case "reset-loginname":
			return "深圳市职业能力专家库-重置用户名";
			
		case "reset-password":
			return "深圳市职业能力专家库-重置密码";
		}
		
		return null;
	}
}
