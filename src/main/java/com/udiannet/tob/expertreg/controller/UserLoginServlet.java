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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.GsonBuilder;
import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.domain.User;
import com.udiannet.tob.expertreg.service.ExpertRegistration;
import com.udiannet.tob.expertreg.service.UserLogin;
import com.udiannet.tob.expertreg.service.UserRegister;
import com.udiannet.tob.expertreg.service.impl.ExpertRegistrationImpl;
import com.udiannet.tob.expertreg.service.impl.UserLoginImpl;
import com.udiannet.tob.expertreg.service.impl.UserRegisterImpl;
import com.udiannet.tob.expertreg.util.InputValidation;
import com.udiannet.tob.expertreg.util.MD5Encoder;
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
//		response.setContentType("text/html");
//		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		/* 设置响应头允许ajax跨域访问 */
		response.setHeader("Access-Control-Allow-Origin", "*");
		/* 星号表示所有的异域请求都可以接受 */
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");

		String reqMethod = request.getParameter("method");// 获取方法名
//		System.out.println("request method: "+reqMethod);

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
	 * token 检查校验
	 */
	private boolean checkToken(HttpServletRequest request)
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
//					case 2: // 生成汉字
//						String[] rBase =
//						{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
//						// 生成第一位区码
//						int r1 = random.nextInt(3) + 11;
//						String str_r1 = rBase[r1];
//						// 生成第二位区码
//						int r2;
//						if (r1 == 13)
//						{
//							r2 = random.nextInt(7);
//						}
//						else
//						{
//							r2 = random.nextInt(16);
//						}
//						String str_r2 = rBase[r2];
//						// 生成第一位位码
//						int r3 = random.nextInt(6) + 10;
//						String str_r3 = rBase[r3];
//						// 生成第二位位码
//						int r4;
//						if (r3 == 10)
//						{
//							r4 = random.nextInt(15) + 1;
//						}
//						else if (r3 == 15)
//						{
//							r4 = random.nextInt(15);
//						}
//						else
//						{
//							r4 = random.nextInt(16);
//						}
//						String str_r4 = rBase[r4];
//						// 将生成的机内码转换为汉字
//						byte[] bytes = new byte[2];
//						// 将生成的区码保存到字节数组的第一个元素中
//						String str_12 = str_r1 + str_r2;
//						int tempLow = Integer.parseInt(str_12, 16);
//						bytes[0] = (byte) tempLow;
//						// 将生成的位码保存到字节数组的第二个元素中
//						String str_34 = str_r3 + str_r4;
//						int tempHigh = Integer.parseInt(str_34, 16);
//						bytes[1] = (byte) tempHigh;
//						ctmp = new String(bytes);
//						break;
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
//			Graphics2D g2d_word = (Graphics2D) g;
//			AffineTransform trans = new AffineTransform();
//			trans.rotate((45) * 3.14 / 180, 15 * i + 8, 7);
//			/* 缩放文字 */
//			float scaleSize = random.nextFloat() + 0.8f;
//			if (scaleSize > 1f)
//				scaleSize = 1f;
//			trans.scale(scaleSize, scaleSize);
//			g2d_word.setTransform(trans);
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
		String token = TokenProccessor.getInstance().makeToken();
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
		if (!checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
//			if (session.getAttribute("method") == null)
//			{
			userLoginForm(request, response);
//			request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);

			return;
//			}
		}

		session.setAttribute("method", "userLoginSubmit"); // 记录当前 method

		String token = TokenProccessor.getInstance().makeToken();
		session.setAttribute("token", token); // 更新 token

//		Writer out = response.getWriter();

		// 页面传入的参数：用户名或者email
		String loginname = request.getParameter("loginname");
		// 页面传入的参数：密码
		String password = request.getParameter("password");
		boolean validate = true;
		String msg = null;
		// 用户名或密码不能为空
		if (validate && (loginname == null || loginname.trim().isEmpty() || password == null || password.trim().isEmpty()))
		{
			validate = false;
			msg = "请输入用户名和密码！";
		}

		// 进行验证码校验
		String checkCode = request.getParameter("checkCode");
//		System.out.println("session=" + session.getAttribute("checkCode") + " ; request=" + checkCode);

		// 验证码已过期（2分钟过期）
		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 2)))
		{
			validate = false;
			msg = "验证码已过期,请重新输入。";
		}

		// 验证码为空
		if (validate && (checkCode.equals("") || checkCode == null))
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

//		System.out.println("校验通过了。");
//		request.getRequestDispatcher("/result.jsp").forward(request, response);	
		if (validate)
		{
			// 用户是否存在
//			System.out.println("loginname="+loginname+"; password="+MD5Encoder.encoder(password));
			User user = userLoginService.userValidate(loginname, MD5Encoder.encoder(password));
//			(new GsonBuilder().create()).toJson(user, out);
			if (user != null) // 用户存在
			{
				// 登录成功后，更新其后台的登录时间
				int result = userLoginService.updateUserLoginTime(user);
				System.out.println("用户登录成功：" + result);

				// 用户是否已经填写了专家资料表了
				Registration reg = expRegService.findRegistrationByUserLogin(user);
				if (reg != null) // 用户已经填写过专家资料了，转向显示审核状态页面
				{
					Writer out = response.getWriter();
					// 发送 reg 的 json 到前端
					(new GsonBuilder().create()).toJson(reg, out);
					out.close();
					request.getRequestDispatcher("/userinfo.jsp").forward(request, response);
				}
				else // 用户还没填写过专家资料，转向填写资料页面
				{
					// 把“u_id”传递过去
					session.setAttribute("u_id", user.getU_id());
					request.getRequestDispatcher("/userinfoedit.jsp").forward(request, response);
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
//		if (session.getAttribute("method") == null)
//		{
//			userLoginForm(request,response);
//			request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
//			return;
//		}

		session.setAttribute("method", "userRegForm"); // 记录当前 method

		// 创建 token，并把 token 存进 session 传递过去
		session.setAttribute("token", TokenProccessor.getInstance().makeToken());
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
		if (!checkToken(request))
		{
			System.out.println("重复提交了。");
			// session 超时后，要回到登录页面
			if (session.getAttribute("method") == null)
				userLoginForm(request, response);
			else
				userRegForm(request, response);
//				request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
			return;
		}
		session.setAttribute("method", "userRegSubmit"); // 记录当前 method

		String token = TokenProccessor.getInstance().makeToken();
		session.setAttribute("token", token); // 更新 token

//		Writer out = response.getWriter();

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
		// 登录用户名校验
		if (validate && !InputValidation.isLegalLoginName(loginname))
		{
			validate = false;
			System.out.println("用户名错误。");
			msg = "请输入正确的用户名！";
//			request.getRequestDispatcher("/userreg.jsp").forward(request, response);
//			out.write("<script>alert('请输入正确的用户名！');</script>");
//			out.close();
		}

		// 注册用户登录名或者邮箱是否已存在
		User user = userRegisterService.findUserByLoginName(loginname);
//				(new GsonBuilder().create()).toJson(user, out);
		if (user != null) // 用户登录名已存在
		{
			validate = false;
			System.out.println("登录名或 email 重复。");
			msg = "此用户登录名或邮箱地址已存在！";
		}

		// email校验
		if (validate && !InputValidation.isLegalEmail(email))
		{
			validate = false;
			System.out.println("email格式错误。");
			msg = "请输入正确的email！";
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

		// 进行验证码校验
		String checkCode = request.getParameter("checkCode");
//		System.out.println("session=" + session.getAttribute("checkCode") + " ; request=" + checkCode);

		// 验证码已过期（2分钟过期）
		if (validate && (((String) session.getAttribute("checkCode")) == null || (session.getAttribute("checkCodeTime") != null
				&& (System.currentTimeMillis() - ((Long) session.getAttribute("checkCodeTime"))) / (1000 * 60) > 2)))
		{
			validate = false;
			System.out.println("验证码已过期。");
			msg = "验证码已过期,请重新输入！";
//				out.write("<script>alert('验证码已过期,请重新输入。');</script>");
		}

		// 验证码为空
		if (validate && (checkCode.equals("") || checkCode == null))
		{
			validate = false;
			System.out.println("验证码为空。");
			msg = "请输入验证码！";
//				out.write("<script>alert('请输入验证码！');</script>");
		}

		// 验证码输入有误
		if (validate && !checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
		{
			validate = false;
			System.out.println("验证码错误。");
			msg = "验证码不正确,请重新输入！";
//				out.write("<script>alert('验证码不正确,请重新输入。');</script>");
		}

		if (!validate)
		{
			session.setAttribute("msg", msg);

//			out.close();
			request.getRequestDispatcher("/userreg.jsp").forward(request, response);
			return;
		}

//		System.out.println("校验通过了。");
//		request.getRequestDispatcher("/result.jsp").forward(request, response);	

		// 所有的验证通过，可以进行注册了，注册成功后，将返回记录id
		int u_id = userRegisterService.userRegister(loginname, email, MD5Encoder.encoder(password1));
		System.out.println("注册成功：" + u_id);
		Writer out = response.getWriter();
		out.write("注册成功！请重新登录。");
		request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
		out.close();
	}
}
