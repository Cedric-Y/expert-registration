package com.udiannet.tob.expertreg.controller;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.domain.RegistrationJobTitle;
import com.udiannet.tob.expertreg.service.ExpertRegistration;
import com.udiannet.tob.expertreg.service.impl.ExpertRegistrationImpl;
import com.udiannet.tob.expertreg.util.IDCardValidation;
import com.udiannet.tob.expertreg.util.InputValidation;
import com.udiannet.tob.expertreg.util.TokenProccessor;

/**
 * 用户资料模块
 */
public class UserInfoServlet extends HttpServlet
{
	// 专家资料相关业务实例
	private ExpertRegistration expRegService = new ExpertRegistrationImpl();

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
	 * 退出登录
	 */
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
	}

	/**
	 * 显示用户资料
	 */
	private void userInfoForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	/**
	 * 编辑用户资料
	 */
	private void userInfoEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getSession().setAttribute("method", "userRegForm"); // 记录当前 method
		request.getSession().setAttribute("token", TokenProccessor.makeToken()); // 更新 token
		request.getRequestDispatcher("/userinfoedit.jsp").forward(request, response);
	}

	/**
	 * 编辑用户资料-提交
	 */
	private void userInfoEditSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
				request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
			else
				userInfoEditForm(request, response);
			return;
		}

		session.setAttribute("method", "userInfoEditSubmit"); // 记录当前 method
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		// Writer out = response.getWriter();

		/*
		 * 1 reg_id (记录ID) int
		 * 2 reg_u_id (登录记录ID) int
		 * 3 reg_name (姓名) varchar(50)
		 * 4 reg_idcard (身份证) varchar(30)
		 * 5 reg_birthday (出生日期：可根据身份证读出) date
		 * 6 reg_phone (联系电话) varchar(50)
		 * 7 reg_photo (一寸照：存放路径) varchar(200)
		 * 8 reg_education (最高学历) varchar(100)
		 * 9 reg_college (毕业院校) varchar(100)
		 * 10 reg_profession_kind (从事行业类别) varchar(100)
		 * 11 reg_profession (从事行业) varchar(100)
		 * 12 reg_company (工作单位) varchar(100)
		 * 13 reg_company_address (单位地址) varchar(200)
		 * 14 reg_email (工作邮箱) varchar(50)
		 * 15 reg_resume (个人简历：存放路径) varchar(200)
		 */

		// 页面传入的参数：2 reg_u_id (登录记录ID)
		int reg_u_id = Integer.parseInt(request.getParameter("u_id"));
		// 页面传入的参数：3 reg_name (姓名)
		String reg_name = request.getParameter("name");
		// 页面传入的参数：4 reg_u_idcard (身份证)
		String reg_idcard = request.getParameter("idcard");
		// 5 reg_birthday (出生日期：可根据身份证读出)
		Date reg_birthday = null;
		// 页面传入的参数：6 reg_phone (联系电话)
		String reg_phone = request.getParameter("phone");
		// 页面传入的参数：7 reg_photo (一寸照：存放路径)
		String reg_photo = request.getParameter("photo");
		// 页面传入的参数：8 reg_education (最高学历)
		String reg_education = request.getParameter("education");
		// 页面传入的参数：9 reg_college (毕业院校)
		String reg_college = request.getParameter("college");
		// 页面传入的参数：10 reg_profession_kind (从事行业类别)
		String reg_profession_kind = request.getParameter("profession_kind");
		// 页面传入的参数：11 reg_profession (从事行业)
		String reg_profession = request.getParameter("profession");
		// 页面传入的参数：12 reg_company (工作单位)
		String reg_company = request.getParameter("company");
		// 页面传入的参数：13 reg_company_address (单位地址)
		String reg_company_address = request.getParameter("company_address");
		// 页面传入的参数：14 reg_email (工作邮箱)
		String reg_email = request.getParameter("email");
		// 页面传入的参数：15 reg_resume (个人简历：存放路径)
		String reg_resume = request.getParameter("resume");

		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");
		// 姓名校验
		if (validate && !InputValidation.isLegalName(reg_name))
		{
			validate = false;
			System.out.println("姓名错误。");
			msg = "请输入正确的姓名！";
		}

		// 身份证校验
		IDCardValidation idcard = new IDCardValidation(reg_idcard);
		if (validate && !idcard.validate())
		{
			validate = false;
			System.out.println("身份证格式错误。");
			msg = "请输入正确的身份证号码！";
		}
		else
		{
			// 从身份证号码读出生日
			reg_birthday = idcard.getBirthDate();
		}

		// 电话号码验证
		if (validate && !InputValidation.isLegalPhone(reg_phone))
		{
			validate = false;
			System.out.println("电话格式错误。");
			msg = "请输入正确的电话号码！";
		}

		// email校验
		if (validate && !InputValidation.isLegalEmail(reg_email))
		{
			validate = false;
			System.out.println("email格式错误。");
			msg = "请输入正确的工作电子邮箱！";
		}

		// 其余的必填项不能为空
		if (validate && (reg_education == null || reg_education.trim().length() == 0 || reg_profession_kind == null
				|| reg_profession_kind.trim().length() == 0 || reg_profession == null || reg_profession.trim().length() == 0
				|| reg_resume == null || reg_resume.trim().length() == 0))
		{
			validate = false;
			System.out.println("必填项有些为空。");
			msg = "所有的【必填项】都不能为空！";
		}

		// 错误信息回显
		if (!validate)
		{
			session.setAttribute("msg", msg);

			// out.close();
			request.getRequestDispatcher("/userinfoedit.jsp").forward(request, response);
			return;
		}

		// 所有的验证通过了，可以进行更新或新增记录了
		Registration reg = new Registration();
		reg.setReg_u_id(reg_u_id); // 2 reg_u_id (登录记录ID)
		reg.setReg_name(reg_name); // 3 reg_name (姓名)
		reg.setReg_idcard(reg_idcard); // 4 reg_idcard (身份证)
		reg.setReg_birthday(reg_birthday); // 5 reg_birthday (出生日期：可根据身份证读出)
		reg.setReg_phone(reg_phone); // 6 reg_phone (联系电话)
		reg.setReg_photo(reg_photo == null ? "" : reg_photo); // 7 reg_photo (一寸照：存放路径)
		reg.setReg_education(reg_education); // 8 reg_education (最高学历)
		reg.setReg_college(reg_college == null ? "" : reg_college); // 9 reg_college (毕业院校)
		reg.setReg_profession_kind(reg_profession_kind); // 10 reg_profession_kind (从事行业类别)
		reg.setReg_profession(reg_profession); // 11 reg_profession (从事行业)
		reg.setReg_company(reg_company == null ? "" : reg_company); // 12 reg_company (工作单位)
		reg.setReg_company_address(reg_company_address == null ? "" : reg_company_address); // 13 reg_company_address (单位地址)
		reg.setReg_email(reg_email == null ? "" : reg_email); // 14 reg_email (工作邮箱)
		reg.setReg_resume(reg_resume); // 15 reg_resume (个人简历：存放路径)

		// 职称证书
		List<RegistrationJobTitle> jobTitleList = new ArrayList<RegistrationJobTitle>();
		String job_title_name = null;
		for (int i = 1; i < 10; i++)
		{
			job_title_name = request.getParameter("job_title_name" + i);
			if (job_title_name != null && job_title_name.trim().length() > 0)
			{
				RegistrationJobTitle rjt = new RegistrationJobTitle();
				rjt.setRjt_name(job_title_name);
				rjt.setRjt_level(
						request.getParameter("job_title_level" + i) == null ? "" : request.getParameter("job_title_level" + i).trim());
				try
				{
					rjt.setRjt_date(request.getParameter("job_title_date" + i) == null ? null
							: (new SimpleDateFormat("yyyy-MM-dd ").parse(request.getParameter("job_title_date" + i))));
				}
				catch (ParseException e)
				{
					rjt.setRjt_date(null);
					e.printStackTrace();
				}
				rjt.setRjt_organization(request.getParameter("job_title_organization" + i) == null ? ""
						: request.getParameter("job_title_organization" + i).trim());
				jobTitleList.add(rjt);
			}
		}

		int result = expRegService.registration(reg, jobTitleList);
		if (result > 0)
		{
			System.out.println("资料更新成功：" + result);
			Writer out = response.getWriter();
			out.write("信息已保存。");
			request.getRequestDispatcher("/userinfo.jsp").forward(request, response);
			out.close();
		}
		else
		{
			System.out.println("资料更新失败：" + result);
			Writer out = response.getWriter();
			out.write("信息保存失败！请稍后重试。");
			out.close();
		}
	}
}
