package com.udiannet.tob.expertreg.controller;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.udiannet.tob.expertreg.util.TokenProccessor;

/**
 * 用户资料模块
 */
public class UserInfoServlet extends HttpServlet
{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 设置接收的信息的字符集
		request.setCharacterEncoding("UTF-8");

		// 设置字符编码为UTF-8
		response.setContentType("text/html;charset=utf-8");

		/* 设置响应头允许ajax跨域访问 */
		response.setHeader("Access-Control-Allow-Origin", "*");
		/* 星号表示所有的异域请求都可以接受 */
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");

		String reqMethod = request.getParameter("method");// 获取方法名
//				System.out.println("request method: "+reqMethod);

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
	 * 显示用户资料
	 */
	private void userInfoShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}
	
	/**
	 * 编辑用户资料
	 */
	private void userInfoEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String token = TokenProccessor.getInstance().makeToken();
		request.getSession().setAttribute("token", token); // 更新 token
		request.getRequestDispatcher("/userinfoedit.jsp").forward(request, response);
	}
	/**
	 * 编辑用户资料-提交
	 */
	private void userInfoEditSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}
}
