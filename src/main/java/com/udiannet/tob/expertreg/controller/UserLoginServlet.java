package com.udiannet.tob.expertreg.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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

import com.udiannet.tob.expertreg.service.UserLogin;
import com.udiannet.tob.expertreg.service.impl.UserLoginImpl;
import com.udiannet.tob.expertreg.util.TokenProccessor;

public class UserLoginServlet extends HttpServlet
{
	// 登录业务层实例
	private UserLogin userLogin = new UserLoginImpl();
	/**
	 * token 检查校验
	 */
	private boolean checkToken(HttpServletRequest request)
	{
		String reqToken = request.getParameter("token");

		System.out.println("token: " + reqToken);
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String reqMethod = request.getParameter("method");// 获取方法名
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
	 * 登录
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 设置字符编码为UTF-8
//		response.setContentType("text/html");
//		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		/* 设置响应头允许ajax跨域访问 */
		response.setHeader("Access-Control-Allow-Origin", "*");
		/* 星号表示所有的异域请求都可以接受 */
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		Writer out = response.getWriter();

//		//是否重复提交
//		if (!checkToken(request))
//		{
//			System.out.println("重复提交了。");
//			return;
//		}
//		request.getSession().removeAttribute("token");// 移除session中的token

		// 进行验证码校验
		HttpSession session = request.getSession();
		String checkCode = request.getParameter("checkCode");
		System.out.println("session=" + session.getAttribute("checkCode") + " ; request=" + checkCode);

		// 验证码已过期
		if (((String) session.getAttribute("checkCode")) == null)
		{
			out.write("<script>alert('验证码已过期,请重新输入。');</script>");
			request.getRequestDispatcher("/userlogin.jsp").forward(request, response);
			return;
		}

		// 验证码为空
		if (checkCode.equals("") || checkCode == null)
		{
			out.write("<script>alert('请输入验证码！');</script>");
			return;
		}

		//验证码输入有误
		if (!checkCode.equalsIgnoreCase((String) session.getAttribute("checkCode")))
		{
			out.write("<script>alert('验证码不正确,请重新输入。');history.back(-1);</script>");
			return;
		}

//		System.out.println("校验通过了。");
//		request.getRequestDispatcher("/result.jsp").forward(request, response);

		// 页面传入的参数：用户名或者手机号码
		String userName = request.getParameter("username");
		// 页面传入的参数：密码
		String password = request.getParameter("password");
		//用户是否存在
		int result = userLogin.userValidate(userName, password);
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
		session.setMaxInactiveInterval(60); // 验证码1分钟过期
//		session.setAttribute("sessionId", session.getId());
		session.setAttribute("checkCode", sRand);
		g.dispose(); // 释放g所占用的系统资源
		ImageIO.write(image, "JPEG", response.getOutputStream()); // 输出图片
	}

}
