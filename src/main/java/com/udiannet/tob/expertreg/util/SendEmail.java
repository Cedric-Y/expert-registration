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
	public static final String HOST = AppProperties.getPropertiesValueByKey("SendEmail.host");
	public static final String PROTOCOL = AppProperties.getPropertiesValueByKey("SendEmail.protocol");
	public static final int PORT = Integer.parseInt(AppProperties.getPropertiesValueByKey("SendEmail.port"));
	public static final String FROM = AppProperties.getPropertiesValueByKey("SendEmail.from");// 发件人的email
	public static final String PWD = AppProperties.getPropertiesValueByKey("SendEmail.pwd");// 发件人密码
	public static final String CONTENT_HOST = AppProperties.getPropertiesValueByKey("SendEmail.content.host");// 邮件里的链接的host

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

//		try
//		{
//			//QQ邮箱需要下面这段代码，163邮箱不需要 
//			MailSSLSocketFactory sf = new MailSSLSocketFactory();
//			sf.setTrustAllHosts(true);
//			props.put("mail.smtp.ssl.enable", "true");
//			props.put("mail.smtp.ssl.socketFactory", sf);
//		}
//		catch (GeneralSecurityException e)
//		{
//			e.printStackTrace();
//		}

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
	 * 
	 * @param toEmail 目的地
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public static void send(String toEmail, String subject, String content)
	{
		Session session = getSession();
		try
		{
			System.out.println("--开始发送邮件--");
			// 创建邮件对象
			Message msg = new MimeMessage(session);

			// 设置发送参数、内容等
			msg.setFrom(new InternetAddress(FROM));
			InternetAddress[] address =
			{ new InternetAddress(toEmail) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setContent(content, "text/html;charset=utf-8");

			// 发送邮件
			Transport.send(msg);
			System.out.println("--发送邮件成功--");
		}
		catch (MessagingException mex)
		{
			System.out.println("--发送邮件失败--");
			mex.printStackTrace();
		}
	}
}
