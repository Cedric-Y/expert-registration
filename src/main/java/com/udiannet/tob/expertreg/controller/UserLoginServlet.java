package com.udiannet.tob.expertreg.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.GsonBuilder;
import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.domain.RegistrationJobTitle;
import com.udiannet.tob.expertreg.domain.User;
import com.udiannet.tob.expertreg.service.ExpertRegistration;
import com.udiannet.tob.expertreg.service.UserLogin;
import com.udiannet.tob.expertreg.service.UserRegister;
import com.udiannet.tob.expertreg.service.impl.ExpertRegistrationImpl;
import com.udiannet.tob.expertreg.service.impl.UserLoginImpl;
import com.udiannet.tob.expertreg.service.impl.UserRegisterImpl;
import com.udiannet.tob.expertreg.util.InputValidation;
import com.udiannet.tob.expertreg.util.MD5Encoder;
import com.udiannet.tob.expertreg.util.SendEmail;
import com.udiannet.tob.expertreg.util.TokenProccessor;

/**
 * 用户登录模块
 */
public class UserLoginServlet extends HttpServlet
{
	// 登录业务层实例
	private UserLogin userLoginService = new UserLoginImpl();

	// 新用户注册层实例
	private UserRegister userRegisterService = new UserRegisterImpl();

	// 专家资料层实例
	private ExpertRegistration expRegService = new ExpertRegistrationImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 设置接收的信息的字符集
		request.setCharacterEncoding("UTF-8");

		// 设置字符编码为UTF-8
		// response.setContentType("text/html");
		// response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		/* 设置响应头允许ajax跨域访问 */
		response.setHeader("Access-Control-Allow-Origin", "*");
		/* 星号表示所有的异域请求都可以接受 */
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");

		String reqMethod = request.getParameter("method");// 获取方法名
		// System.out.println("request method: "+reqMethod);

		if (reqMethod == null || reqMethod.isEmpty())
		{
			throw new RuntimeException("没有传递method参数,请给出你想调用的方法。");
		}
		Class c = this.getClass();// 获得当前类的Class对象

		Method classMethod = null; // class对象里的方法
		try
		{
			// 获得Class对象里的私有方法
			classMethod = c.getDeclaredMethod(reqMethod, HttpServletRequest.class, HttpServletResponse.class);
			// 抑制Java的访问控制检查
			classMethod.setAccessible(true);
		}
		catch (Exception e)
		{
			throw new RuntimeException("没有找到 【" + c.getName() + "." + reqMethod + "】 方法，请检查该方法是否存在。");
		}

		try
		{
			classMethod.invoke(this, request, response);// 反射调用方法
		}
		catch (Exception e)
		{
			System.out.println("你调用的方法 【" + c.getName() + "." + reqMethod + "】,内部发生了异常");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得随机生成的颜色，为验证码服务
	 */
	private Color getRandColor(int s, int e)
	{
		Random random = new Random();
		if (s > 255)
			s = 255;
		if (e > 255)
			e = 255;
		int r, g, b;
		r = s + random.nextInt(e - s); // 随机生成RGB颜色中的r值
		g = s + random.nextInt(e - s); // 随机生成RGB颜色中的g值
		b = s + random.nextInt(e - s); // 随机生成RGB颜色中的b值
		return new Color(r, g, b);
	}

	/**
	 * 验证码
	 */
	private void checkCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 设置不缓存图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应图片,一定不能缺少这句话,否则错误.
		response.setContentType("image/jpeg");
		int width = 120, height = 30; // 指定生成验证码的宽度和高度
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 创建BufferedImage对象,其作用相当于一图片
		Graphics g = image.getGraphics(); // 创建Graphics对象,其作用相当于画笔
		Graphics2D g2d = (Graphics2D) g; // 创建Grapchics2D对象
		Random random = new Random();
		Font mfont = new Font("Arial", Font.BOLD, 30); // 定义字体样式
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height); // 绘制背景
		g.setFont(mfont); // 设置字体
		g.setColor(getRandColor(180, 200));

		// 绘制100条颜色和位置全部为随机产生的线条,该线条为2f
		for (int i = 0; i < 100; i++)
		{
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(6) + 1;
			int y1 = random.nextInt(12) + 1;
			BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL); // 定制线条样式
			Line2D line = new Line2D.Double(x, y, x + x1, y + y1);
			g2d.setStroke(bs);
			g2d.draw(line); // 绘制直线
		}

		// 输出由英文，数字，和中文随机组成的验证文字，具体的组合方式根据生成随机数确定。
		String sRand = "";
		String ctmp = "";
		int itmp = 0;
		// 制定输出的验证码为四位
		for (int i = 0; i < 4; i++)
		{
			switch (random.nextInt(2))
			{
			case 1: // 生成A-Z的字母
				itmp = random.nextInt(26) + 65;
				ctmp = String.valueOf((char) itmp);
				break;
			// case 2: // 生成汉字
			// String[] rBase =
			// { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b",
			// "c", "d", "e", "f" };
			// // 生成第一位区码
			// int r1 = random.nextInt(3) + 11;
			// String str_r1 = rBase[r1];
			// // 生成第二位区码
			// int r2;
			// if (r1 == 13)
			// {
			// r2 = random.nextInt(7);
			// }
			// else
			// {
			// r2 = random.nextInt(16);
			// }
			// String str_r2 = rBase[r2];
			// // 生成第一位位码
			// int r3 = random.nextInt(6) + 10;
			// String str_r3 = rBase[r3];
			// // 生成第二位位码
			// int r4;
			// if (r3 == 10)
			// {
			// r4 = random.nextInt(15) + 1;
			// }
			// else if (r3 == 15)
			// {
			// r4 = random.nextInt(15);
			// }
			// else
			// {
			// r4 = random.nextInt(16);
			// }
			// String str_r4 = rBase[r4];
			// // 将生成的机内码转换为汉字
			// byte[] bytes = new byte[2];
			// // 将生成的区码保存到字节数组的第一个元素中
			// String str_12 = str_r1 + str_r2;
			// int tempLow = Integer.parseInt(str_12, 16);
			// bytes[0] = (byte) tempLow;
			// // 将生成的位码保存到字节数组的第二个元素中
			// String str_34 = str_r3 + str_r4;
			// int tempHigh = Integer.parseInt(str_34, 16);
			// bytes[1] = (byte) tempHigh;
			// ctmp = new String(bytes);
			// break;
			default:
				itmp = random.nextInt(10) + 48;
				ctmp = String.valueOf((char) itmp);
				break;
			}
			sRand += ctmp;
			Color color = new Color(20 + random.nextInt(110), 20 + random.nextInt(110), random.nextInt(110));
			g.setColor(color);

			// 将生成的随机数进行随机缩放并旋转制定角度 PS.建议不要对文字进行缩放与旋转,因为这样图片可能不正常显示
			/* 将文字旋转制定角度 */
			// Graphics2D g2d_word = (Graphics2D) g;
			// AffineTransform trans = new AffineTransform();
			// trans.rotate((45) * 3.14 / 180, 15 * i + 8, 7);
			// /* 缩放文字 */
			// float scaleSize = random.nextFloat() + 0.8f;
			// if (scaleSize > 1f)
			// scaleSize = 1f;
			// trans.scale(scaleSize, scaleSize);
			// g2d_word.setTransform(trans);
			g.drawString(ctmp, 25 * i + 18, 25);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("checkCode", sRand);
		session.setAttribute("checkCodeTime", System.currentTimeMillis());// 保存这次生成验证码的时间
		g.dispose(); // 释放g所占用的系统资源
		ImageIO.write(image, "JPEG", response.getOutputStream()); // 输出图片
	}

	/**
	 * 进入登录页面
	 */
	private void userLoginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		session.setAttribute("method", "userLoginForm"); // 记录当前 method
		// 创建 token，并把 token 存进 session 传递过去
		String token = TokenProccessor.makeToken();
		System.out.println("loginForm-token: " + token);
		session.setAttribute("token", token);
		// 跳转到登录页面
		request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
	}

	/**
	 * 登录提交
	 */
	private void userLoginSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		// 是否重复提交
		if (!TokenProccessor.checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
			// if (session.getAttribute("method") == null)
			// {
			userLoginForm(request, response);
			// request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request,
			// response);

			return;
			// }
		}

		session.setAttribute("method", "userLoginSubmit"); // 记录当前 method

//		String token = ;
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		// Writer out = response.getWriter();

		// 页面传入的参数：用户名或者email
		String loginname = request.getParameter("loginname");
		// 页面传入的参数：密码
		String password = request.getParameter("password");
		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");
		// 用户名或密码不能为空
		if (validate && (loginname == null || loginname.trim().isEmpty() || password == null || password.trim().isEmpty()))
		{
			validate = false;
			msg = "请输入用户名和密码！";
		}

		// 进行验证码校验
		String checkCode = request.getParameter("checkCode");
		// System.out.println("session=" + session.getAttribute("checkCode") + "
		// ; request=" + checkCode);

		// 验证码已过期（2分钟过期）
		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 2)))
		{
			validate = false;
			msg = "验证码已过期,请重新输入。";
		}

		// 验证码为空
		if (validate && ("".equals(checkCode) || checkCode == null))
		{
			validate = false;
			msg = "请输入验证码！";
		}

		// 验证码输入有误
		if (validate && !checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
		{
			validate = false;
			msg = "验证码不正确,请重新输入。";
		}

		// System.out.println("校验通过了。");
		// request.getRequestDispatcher("/result.jsp").forward(request,
		// response);
		if (validate)
		{
			// 用户是否存在
			// System.out.println("loginname="+loginname+";
			// password="+MD5Encoder.encoder(password));
			User user = userLoginService.userValidateForLogin(loginname, MD5Encoder.encoder(password));
			// (new GsonBuilder().create()).toJson(user, out);
			if (user != null) // 用户存在
			{
				// 已激活的用户
				if (user.getU_type() == 1)
				{
					// 登录成功后，更新其后台的登录时间
					int result = userLoginService.updateUserLoginTime(user);
					System.out.println("用户登录成功：" + result);
					// 把“u_id”传递过去
					session.setAttribute("u_id", user.getU_id());

					// 用户是否已经填写了专家资料表了
					Registration reg = expRegService.findRegistrationByUserId(user.getU_id());
					if (reg != null) // 用户已经填写过专家资料了，转向显示审核状态页面
					{
						System.out.println("转向资料显示页面，记录ID：" + reg.getReg_id());

						// 显示注册信息
						request.setAttribute("reg", (new GsonBuilder().create()).toJson(reg));
						// 显示职称信息
						List<RegistrationJobTitle> jobTitles = expRegService.findJobTitleListByRegId(reg.getReg_id());
						request.setAttribute("jobtitles", (new GsonBuilder().create()).toJson(jobTitles));

						request.getRequestDispatcher("/userinfo.jsp").forward(request, response);
					}
					else // 用户还没填写过专家资料，转向填写资料页面
					{
						System.out.println("转向资料编辑页面，用户ID：" + user.getU_id());
						request.getRequestDispatcher("/userinfoedit.jsp").forward(request, response);
					}
				}
				// 待激活的用户
				else if (user.getU_type() == 11)
				{
					System.out.println("此登录账号需激活。");
					Writer out = response.getWriter();
					out.write("<script type='text/javascript'>alert('此账号未激活，请输入您的注册邮箱进行激活！');</script>");
//					request.setAttribute("reset", 3);
					request.getRequestDispatcher("/UserLogin?method=userEmailForm&reset=3").forward(request, response);
					out.close();
				}
			}
			else // 用户不存在，继续登录
			{
				validate = false;
				msg = "用户名或密码错误,请重新输入。";
			}
		}

		if (!validate)
		{
			session.setAttribute("msg", msg);
			request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
		}
	}

	/**
	 * 进入新用户注册界面，需要传递 token 过去
	 */
	private void userRegForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		// session 超时后，要回到登录页面
		// if (session.getAttribute("method") == null)
		// {
		// userLoginForm(request,response);
		// request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request,
		// response);
		// return;
		// }

		session.setAttribute("method", "userRegForm"); // 记录当前 method

		// 创建 token，并把 token 存进 session 传递过去
		session.setAttribute("token", TokenProccessor.makeToken());
		// 跳转到新用户注册页面
		request.getRequestDispatcher("/userreg.jsp").forward(request, response);
	}

	/**
	 * 新用户注册，提交注册信息
	 */
	private void userRegSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		// 是否重复提交
		if (!TokenProccessor.checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
			if (session.getAttribute("method") == null)
				userLoginForm(request, response);
			else
				userRegForm(request, response);
			// request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request,
			// response);
			return;
		}
		session.setAttribute("method", "userRegSubmit"); // 记录当前 method

//		String token = ;
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		// Writer out = response.getWriter();

		// 页面传入的参数：用户名
		String loginname = request.getParameter("loginname");
		// 页面传入的参数：email
		String email = request.getParameter("email");
		// 页面传入的参数：密码
		String password1 = request.getParameter("password1");
		// 页面传入的参数：确认密码
		String password2 = request.getParameter("password2");

		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");
		// 登录用户名格式校验
		if (validate && !InputValidation.isLegalLoginName(loginname))
		{
			validate = false;
			System.out.println("用户名错误。");
			msg = "请输入正确的用户名！";
			// request.getRequestDispatcher("/userreg.jsp").forward(request,
			// response);
			// out.write("<script>alert('请输入正确的用户名！');</script>");
			// out.close();
		}

		// 检查用户名是否已存在
		if (validate)
		{
			User user = userRegisterService.userValidateLoginname(loginname);
			if (user != null) // 用户登录名已存在
			{
				validate = false;
				System.out.println("登录名重复。");
				msg = "此用户登录名已存在！";
			}
		}

		// email 格式校验
		if (validate && !InputValidation.isLegalEmail(email))
		{
			validate = false;
			System.out.println("email格式错误。");
			msg = "请输入正确的email！";
		}

		// 检查 Email 是否已使用
		if (validate)
		{
			User user = userRegisterService.userValidateEmail(email);
			if (user != null) // email 已存在
			{
				validate = false;
				System.out.println("email 重复。");
				msg = "此邮箱地址已存在！";
			}
		}

		// 校对密码
		if (validate && !password1.equals(password2))
		{
			validate = false;
			System.out.println("两次输入的密码不一样。");
			msg = "请保证两次输入的密码一样！";
		}

		// 验证密码强度
		if (validate)
		{
			validate = false;
			switch (InputValidation.isLegalPassword(password1))
			{
			case 0: // 验证通过
				validate = true;
				break;
			case 1: // 长度不满足要求
				System.out.println("密码长度不满足要求。");
				msg = "密码长度为3到15位！";
				break;

			case 2:// 纯数字
				System.out.println("密码是纯数字。");
				msg = "请确保密码至少包含字母与数字！";
				break;
			case 3:// 纯字母
				System.out.println("密码是纯字母。");
				msg = "请确保密码至少包含字母与数字！";
				break;

			case 4:// 纯特殊字符
				System.out.println("密码是纯特殊字符。");
				msg = "请确保密码至少包含字母与数字！";
				break;
			}
		}

//		// 进行验证码校验
//		String checkCode = request.getParameter("checkCode");
//		// System.out.println("session=" + session.getAttribute("checkCode") + "
//		// ; request=" + checkCode);
//
//		// 验证码已过期（5分钟过期）
//		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
//				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 5)))
//		{
//			validate = false;
//			System.out.println("验证码已过期。");
//			msg = "验证码已过期,请重新输入！";
//			// out.write("<script>alert('验证码已过期,请重新输入。');</script>");
//		}
//
//		// 验证码为空
//		if (validate && (checkCode.equals("") || checkCode == null))
//		{
//			validate = false;
//			System.out.println("验证码为空。");
//			msg = "请输入验证码！";
//			// out.write("<script>alert('请输入验证码！');</script>");
//		}
//
//		// 验证码输入有误
//		if (validate && !checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
//		{
//			validate = false;
//			System.out.println("验证码错误。");
//			msg = "验证码不正确,请重新输入！";
//			// out.write("<script>alert('验证码不正确,请重新输入。');</script>");
//		}		

		if (!validate)
		{
			session.setAttribute("msg", msg);
			request.getRequestDispatcher("/userreg.jsp").forward(request, response);
			return;
		}

		// 所有的验证通过
		User user = new User();
		user.setU_login_name(loginname); // 用户名
		user.setU_email(email); // email
		user.setU_password(MD5Encoder.encoder(password1));// 密码
		String validateCode = UUID.randomUUID().toString();
		user.setU_validate_code(validateCode); // 邮件验证码
		Date currentTime = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long expireTimestamp = currentTime.getTime() + 30 * 60 * 1000;
		user.setU_expire_time(expireTimestamp); // 验证码到期时间
		// 新增用户
		userRegisterService.userRegister(user);
		// 获取到新增用户的记录id
		int u_id = user.getU_id();

		if (u_id > 0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("请在 ");
			sb.append(df.format(new Date(expireTimestamp)));
			sb.append(" 之前，点击下面链接激活您的注册账户。</br>");
			sb.append("<a href=\"");
			sb.append(SendEmail.CONTENT_HOST);
			sb.append("/expertreg/UserLogin?method=userRegActivation");
			sb.append("&reset=3&id=");
			sb.append(u_id);
			sb.append("&email=");
			sb.append(email);
			sb.append("&validateCode=");
			sb.append(validateCode);
			sb.append("\">激活注册账户</a>");
			SendEmail.send(email, "深圳市职业能力专家库-激活注册账户", sb.toString());

			System.out.println("注册成功：" + u_id);
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('注册成功！请到您的邮箱 " + email + " 打开激活邮件里的链接，激活您的账户。');</script>");
			request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
			out.close();
		}
		else
		{
			System.out.println("注册失败：" + u_id);
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('注册失败！请稍后重试。');</script>");
			request.getRequestDispatcher("/userreg.jsp").forward(request, response);
			out.close();
		}
	}

	/**
	 * 新用户注册，提交注册信息后，从邮件里的链接进行激活
	 */
	private void userRegActivation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));

		// 记录当前 method
		session.setAttribute("method", "userRegActivation");

		// 页面传入的参数：reset 类型
		String reset = request.getParameter("reset");
		// 页面传入的参数：id
		String id = request.getParameter("id");
		// 页面传入的参数：email
		String email = request.getParameter("email");
		// 页面传入的参数：邮件校验码
		String validateCode = request.getParameter("validateCode");

		// 激活类型不匹配
		if (reset == null || !("3".equals(reset)))
		{
			System.out.println("激活类型有误。");
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('激活链接有误！');</script>");
			out.flush();
			out.close();
			return;
		}

		// 对 id 进行判断
		int u_id = -1;

		try
		{
			u_id = Integer.parseInt(id);
		}
		catch (NumberFormatException e)
		{
			System.out.println("传入的 id 有误。");
			e.printStackTrace();
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('激活链接有误！');</script>");
			out.flush();
			out.close();
			return;
		}

		// 根据页面参数以及当前时间，判断是否满足条件去激活登录账号
		long currentTime = System.currentTimeMillis();
		int result = userRegisterService.userValidateFromActivation(u_id, email, validateCode, currentTime);
		if (result == 0)
		{
			System.out.println("激活账号时找不到这样的记录。");
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('激活链接有误，或者已经失效！');</script>");
			out.flush();
			out.close();
			return;
		}

		// 激活成功，转入登录界面
		userLoginForm(request, response);
	}

	/**
	 * 重置用户名或密码前，需提供注册时所填写的 Email
	 */
	private void userEmailForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		// System.out.println("session-token: " +
		// session.getAttribute("token"));
		// session 超时后，要回到登录页面
		// if (session.getAttribute("method") == null)
		// {
		// userLoginForm(request,response);
		// request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request,
		// response);
		// return;
		// }

		session.setAttribute("method", "userEmailForm"); // 记录当前 method

		// 重置类型：1 重置用户名；2 重置密码；3 激活账号
		session.setAttribute("reset", request.getParameter("reset"));

		// 创建 token，并把 token 存进 session 传递过去
		session.setAttribute("token", TokenProccessor.makeToken());
		// 跳转到填写邮箱的页面
		request.getRequestDispatcher("/useremail.jsp").forward(request, response);
	}

	/**
	 * 发送验证邮件
	 */
	private void userSendEmailSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		// 是否重复提交
		if (!TokenProccessor.checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
			if (session.getAttribute("method") == null)
				userLoginForm(request, response);
			else
				userEmailForm(request, response);
			return;
		}
		session.setAttribute("method", "userSendEmail"); // 记录当前 method

//		String token = ;
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		// Writer out = response.getWriter();

		// 页面传入的参数：email
		String email = request.getParameter("email");
		// 页面传入的参数：重置类型
		String reset = request.getParameter("reset");
		// 页面传入的参数：验证码
		String checkCode = request.getParameter("checkCode");

//		System.out.println("reset: "+reset);

		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");

		// email校验
		if (validate && !InputValidation.isLegalEmail(email))
		{
			validate = false;
			msg = "请输入正确的email！";
		}

		// 验证码已过期（2分钟过期）
		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 2)))
		{
			validate = false;
			msg = "验证码已过期,请重新输入。";
		}

		// 验证码为空
		if (validate && ("".equals(checkCode) || checkCode == null))
		{
			validate = false;
			msg = "请输入验证码！";
		}

		// 验证码输入有误
		if (validate && !checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
		{
			validate = false;
			msg = "验证码不正确,请重新输入。";
		}

		User user = null;
		if (validate)
		{
			// 此 Email 是否存在
			user = userLoginService.findUserByEmail(email);
			if (user == null) // Email 不存在
			{
				validate = false;
				msg = "此 Email 不存在。";
			}
		}

		if (!validate)
		{
			session.setAttribute("msg", msg);
			System.out.println(msg);
			request.getRequestDispatcher("/useremail.jsp").forward(request, response);
			return;
		}

		// 所有的验证全部通过，就发送 Email
		StringBuffer sb = new StringBuffer();

		// 发送用户名到用户邮箱里
		if ("1".equals(reset))
		{
			sb.append("您的登录用户名为：");
			sb.append(user.getU_login_name());
			SendEmail.send(email, "深圳市职业能力专家库-重置用户名", sb.toString());

			System.out.println("发送用户名邮件成功。");
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('邮件发送成功！请登录您的邮箱查看您的登录用户名。');</script>");
		}

		else
		{
			Date currentTime = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			long expireTimestamp = currentTime.getTime() + 30 * 60 * 1000;
			String validateCode = UUID.randomUUID().toString();
			int u_id = user.getU_id();

			// 设置用户验证码
			user.setU_validate_code(validateCode);
			// 设置验证码过期时间
			user.setU_expire_time(expireTimestamp);
			// 把验证码、过期时间，存进数据库里
			int result = userLoginService.updateUserByUser(user);

			// 重置密码
			if ("2".equals(reset))
			{
				// 更新数据库成功
				if (result > 0)
				{
					System.out.println("更新用户数据成功：" + result);
//				msg = "邮件发送成功！请登录邮箱完成验证操作。";
//				session.setAttribute("msg", msg);
//				request.getRequestDispatcher("/useremail.jsp").forward(request, response);

					sb.append("请在 ");
					sb.append(df.format(new Date(expireTimestamp)));
					sb.append(" 之前，点击下面链接重置密码。</br>");
					sb.append("<a href=\"");
					sb.append(SendEmail.CONTENT_HOST);
					sb.append("/expertreg/UserLogin?method=userResetForm");
					sb.append("&reset=2");
					sb.append("&id=");
					sb.append(u_id);
					sb.append("&email=");
					sb.append(email);
					sb.append("&validateCode=");
					sb.append(validateCode);
					sb.append("\">重置密码链接</a>");
					SendEmail.send(email, "深圳市职业能力专家库-重置密码", sb.toString());

					Writer out = response.getWriter();
					out.write("<script type='text/javascript'>alert('邮件发送成功！请登录邮箱完成验证操作。');</script>");
//				request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
					out.flush();
					out.close();
				}
				// 更新数据库失败
				else
				{
					System.out.println("更新用户数据失败：" + result);
//				msg = "后台数据更新失败，请稍后重试。";
//				session.setAttribute("msg", msg);
//				request.getRequestDispatcher("/useremail.jsp").forward(request, response);

					Writer out = response.getWriter();
					out.write("<script type='text/javascript'>alert('服务器异常，请稍后重试。');</script>");
					out.flush();
					out.close();
				}
			}
			// 登录账号需激活
			else if ("3".equals(reset))
			{
				// TODO
				// 更新数据库成功
				if (result > 0)
				{
					sb.append("请在 ");
					sb.append(df.format(new Date(expireTimestamp)));
					sb.append(" 之前，点击下面链接激活您的注册账户。</br>");
					sb.append("<a href=\"");
					sb.append(SendEmail.CONTENT_HOST);
					sb.append("/expertreg/UserLogin?method=userRegActivation");
					sb.append("&reset=3&id=");
					sb.append(u_id);
					sb.append("&email=");
					sb.append(email);
					sb.append("&validateCode=");
					sb.append(validateCode);
					sb.append("\">激活注册账户</a>");
					SendEmail.send(email, "深圳市职业能力专家库-激活注册账户", sb.toString());

					System.out.println("等待激活：" + u_id);
					Writer out = response.getWriter();
					out.write("<script type='text/javascript'>alert('邮件发送成功！请到您的邮箱 " + email + " 打开激活邮件里的链接，激活您的账户。');</script>");
					request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
					out.close();
				}
				// 更新数据库失败
				else
				{
					System.out.println("激活账号前更新表遇到错误：" + u_id);
					Writer out = response.getWriter();
					out.write("<script type='text/javascript'>alert('激活异常，请稍后重试。');</script>");
					request.getRequestDispatcher("/userreg.jsp").forward(request, response);
					out.close();
				}
			}
		}
	}

	/**
	 * 重置用户名或者密码界面
	 */
	private void userResetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		// System.out.println("session-token: " +
		// session.getAttribute("token"));
		// session 超时后，要回到登录页面
		// if (session.getAttribute("method") == null)
		// {
		// userLoginForm(request,response);
		// request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request,
		// response);
		// return;
		// }

		// 记录当前 method
		session.setAttribute("method", "userResetForm");

		// 页面传入的参数：reset 类型
		String reset = request.getParameter("reset");
		// 页面传入的参数：id
		String id = request.getParameter("id");
		// 页面传入的参数：email
		String email = request.getParameter("email");
		// 页面传入的参数：邮件校验码
		String validateCode = request.getParameter("validateCode");

		// 重置类型不匹配
		if (reset == null || (!"1".equals(reset) && !"2".equals(reset)))
		{
			System.out.println("重置类型有误。");
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置链接有误！');</script>");
			out.flush();
			out.close();
			return;
		}

		// 对 id 进行判断
		int u_id = -1;

		try
		{
			u_id = Integer.parseInt(id);
		}
		catch (NumberFormatException e)
		{
			System.out.println("传入的 id 有误。");
			e.printStackTrace();
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置链接有误！');</script>");
			out.flush();
			out.close();
			return;
		}

		// 根据页面参数以及当前时间，判断是否满足条件去重置用户名
		long currentTime = System.currentTimeMillis();
//		System.out.println("u_id=" + u_id + ", email=" + email + ", validateCode=" + validateCode + ", currentTime="
//				+ currentTime);
		User user = userLoginService.userValidateFromReset(u_id, email, validateCode, currentTime);
		if (user == null)
		{
			System.out.println("找不到这样的记录。");
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置链接有误，或者已经失效！');</script>");
			out.flush();
			out.close();
			return;
		}

		// 创建 token，并把 token 存进 session 传递过去
		session.setAttribute("token", TokenProccessor.makeToken());
		// 保存将要重置用户名的记录 id
		session.setAttribute("u_id", u_id);
		switch (reset)
		{
		case "1":
			// 跳转到重置用户名界面
			request.getRequestDispatcher("/userresetloginname.jsp").forward(request, response);
			break;
		case "2":
			// 跳转到重置密码界面
			request.getRequestDispatcher("/userresetpassword.jsp").forward(request, response);
			break;
		}
	}

	/**
	 * 重置用户名界面-提交
	 */
	private void userResetLoginnameSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		// 是否重复提交
		if (!TokenProccessor.checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
			if (session.getAttribute("method") == null)
				userLoginForm(request, response);
			// session 没超时，则回到当前页面
			else
			{
				session.setAttribute("method", "userResetLoginnameSubmit"); // 记录当前 method
				session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token
				request.getRequestDispatcher("/userresetloginname.jsp").forward(request, response);
			}
			return;
		}

		session.setAttribute("method", "userResetLoginnameSubmit"); // 记录当前 method

//		String token = ;
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		// Writer out = response.getWriter();

		// 页面传入的参数：新用户名
		String loginname = request.getParameter("loginname");
		// 用户id
		int u_id = (int) session.getAttribute("u_id");

		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");
		// 登录用户名校验
		if (validate && !InputValidation.isLegalLoginName(loginname))
		{
			validate = false;
			System.out.println("用户名错误。");
			msg = "请输入正确的用户名！";
		}

		// 进行验证码校验
		String checkCode = request.getParameter("checkCode");
		// System.out.println("session=" + session.getAttribute("checkCode") + "
		// ; request=" + checkCode);

		// 验证码已过期（2分钟过期）
		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 2)))
		{
			validate = false;
			System.out.println("验证码已过期。");
			msg = "验证码已过期,请重新输入！";
			// out.write("<script>alert('验证码已过期,请重新输入。');</script>");
		}

		// 验证码为空
		if (validate && ("".equals(checkCode) || checkCode == null))
		{
			validate = false;
			System.out.println("验证码为空。");
			msg = "请输入验证码！";
			// out.write("<script>alert('请输入验证码！');</script>");
		}

		// 验证码输入有误
		if (validate && !checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
		{
			validate = false;
			System.out.println("验证码错误。");
			msg = "验证码不正确,请重新输入！";
			// out.write("<script>alert('验证码不正确,请重新输入。');</script>");
		}

		if (validate)
		{
			// 注册用户登录名是否已存在
			User user = userLoginService.findUserByLoginname(u_id, loginname);
			// (new GsonBuilder().create()).toJson(user, out);
			if (user != null) // 用户名已存在
			{
				validate = false;
				System.out.println("此用户名重复。");
				msg = "此用户名已存在！";
			}
		}

		if (!validate)
		{
			session.setAttribute("msg", msg);

			// out.close();
			request.getRequestDispatcher("/userresetloginname.jsp").forward(request, response);
			return;
		}

		// 所有的验证通过，可以进行用户名重置了
		int result = userLoginService.resetUserLoginname(u_id, loginname);
		if (result > 0)
		{
			System.out.println("重置用户名成功：" + result);
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置用户名成功！请重新登录。');</script>");
			out.flush();
			request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
			out.close();
		}
		else
		{
			System.out.println("重置用户名失败：" + result);
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置用户名失败！请稍后重试。');</script>");
			out.flush();
//			request.getRequestDispatcher("/userreg.jsp").forward(request, response);
			out.close();
		}
	}

	/**
	 * 重置密码界面-提交
	 */
	private void userResetPasswordSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));
		// 是否重复提交
		if (!TokenProccessor.checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
			if (session.getAttribute("method") == null)
				userLoginForm(request, response);
			// session 没超时，则回到当前页面
			else
			{
				session.setAttribute("method", "userResetPasswordSubmit"); // 记录当前 method
				session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token
				request.getRequestDispatcher("/userresetpassword.jsp").forward(request, response);
			}
			return;
		}

		session.setAttribute("method", "userResetPasswordSubmit"); // 记录当前 method

//		String token = ;
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		// Writer out = response.getWriter();

		// 页面传入的参数：密码
		String password1 = request.getParameter("password1");
		// 页面传入的参数：确认密码
		String password2 = request.getParameter("password2");
		// 用户id
		int u_id = (int) session.getAttribute("u_id");

		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");

		// 校对密码
		if (validate && !password1.equals(password2))
		{
			validate = false;
			System.out.println("两次输入的密码不一样。");
			msg = "请保证两次输入的密码一样！";
		}

		// 验证密码强度
		if (validate)
		{
			validate = false;
			switch (InputValidation.isLegalPassword(password1))
			{
			case 0: // 验证通过
				validate = true;
				break;
			case 1: // 长度不满足要求
				System.out.println("密码长度不满足要求。");
				msg = "密码长度为3到15位！";
				break;

			case 2:// 纯数字
				System.out.println("密码是纯数字。");
				msg = "请确保密码至少包含字母与数字！";
				break;
			case 3:// 纯字母
				System.out.println("密码是纯字母。");
				msg = "请确保密码至少包含字母与数字！";
				break;

			case 4:// 纯特殊字符
				System.out.println("密码是纯特殊字符。");
				msg = "请确保密码至少包含字母与数字！";
				break;
			}
		}

		// 进行验证码校验
		String checkCode = request.getParameter("checkCode");
		// System.out.println("session=" + session.getAttribute("checkCode") + "
		// ; request=" + checkCode);

		// 验证码已过期（2分钟过期）
		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 2)))
		{
			validate = false;
			System.out.println("验证码已过期。");
			msg = "验证码已过期,请重新输入！";
			// out.write("<script>alert('验证码已过期,请重新输入。');</script>");
		}

		// 验证码为空
		if (validate && ("".equals(checkCode) || checkCode == null))
		{
			validate = false;
			System.out.println("验证码为空。");
			msg = "请输入验证码！";
			// out.write("<script>alert('请输入验证码！');</script>");
		}

		// 验证码输入有误
		if (validate && !checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
		{
			validate = false;
			System.out.println("验证码错误。");
			msg = "验证码不正确,请重新输入！";
			// out.write("<script>alert('验证码不正确,请重新输入。');</script>");
		}

		if (!validate)
		{
			session.setAttribute("msg", msg);

			// out.close();
			request.getRequestDispatcher("/userresetpassword.jsp").forward(request, response);
			return;
		}

		// 所有的验证通过，可以进行密码重置了
		int result = userLoginService.resetUserPassword(u_id, MD5Encoder.encoder(password1));
		if (result > 0)
		{
			System.out.println("重置密码成功：" + result);
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置密码成功！请重新登录。');</script>");
//			out.flush();
			request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
			out.close();
		}
		else
		{
			System.out.println("重置密码失败：" + result);
			Writer out = response.getWriter();
			out.write("<script type='text/javascript'>alert('重置密码失败！请稍后重试。');</script>");
			out.flush();
//			request.getRequestDispatcher("/userreg.jsp").forward(request, response);
			out.close();
		}
	}
}
