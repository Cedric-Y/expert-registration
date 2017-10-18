package com.udiannet.tob.expertreg.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.GsonBuilder;
import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.domain.RegistrationJobTitle;
import com.udiannet.tob.expertreg.service.ExpertRegistration;
import com.udiannet.tob.expertreg.service.impl.ExpertRegistrationImpl;
import com.udiannet.tob.expertreg.util.AppProperties;
import com.udiannet.tob.expertreg.util.IDCardValidation;
import com.udiannet.tob.expertreg.util.InputValidation;
import com.udiannet.tob.expertreg.util.TokenProccessor;

/**
 * 用户资料模块
 */
//标识Servlet支持文件上传
@MultipartConfig
@WebServlet(name = "UserInfoServlet", urlPatterns = "/UserInfo")
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
		// session超时，返回到登录界面
		if (request.getSession().getAttribute("method") == null)
		{
			System.out.println("session超时。");
			request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
			return;
		}

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
		// session超时，返回到登录界面
//		if(request.getSession().getAttribute("method")==null)
//		{
//			request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
//			return;
//		}

		request.getSession().setAttribute("method", "userInfoForm"); // 记录当前 method
		request.getSession().setAttribute("token", TokenProccessor.makeToken()); // 更新 token
		
		Registration reg = expRegService.findRegistrationByUserId((int) request.getSession().getAttribute("u_id"));
		// 显示注册信息
		request.setAttribute("reg", (new GsonBuilder().create()).toJson(reg));
		// 显示职称信息
		List<RegistrationJobTitle> jobTitles = expRegService.findJobTitleListByRegId(reg.getReg_id());
		request.setAttribute("jobtitles", (new GsonBuilder().create()).toJson(jobTitles));
		
		request.getRequestDispatcher("/userinfo.jsp").forward(request, response);
	}

	/**
	 * 编辑用户资料
	 */
	private void userInfoEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getSession().setAttribute("method", "userInfoEditForm"); // 记录当前 method
		request.getSession().setAttribute("token", TokenProccessor.makeToken()); // 更新 token
		request.getRequestDispatcher("/userinfoedit.jsp").forward(request, response);
	}

	/**
	 * 根据请求头解析出文件名
	 * 请求头的格式：火狐和google浏览器下：form-data; name="file"; filename="snmp4j--api.zip"
	 * IE浏览器下：form-data; name="file"; filename="E:\snmp4j--api.zip"
	 * @param header 请求头
	 * @return 文件名
	 */
	private String getUploadFileNameFromHeader(String header)
	{
		/**
		 * String[] tempArr1 = header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
		 * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
		 * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
		 */
		String[] tempArr1 = header.split(";");
		/**
		 * 火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
		 * IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
		 */
		String[] tempArr2 = tempArr1[2].split("=");
		// 获取文件名，兼容各种浏览器的写法
		String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
		return fileName;
	}

	/**
	 * 编辑用户资料-提交
	 */
	private void userInfoEditSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String uploadPath = AppProperties.getPropertiesValueByKey("upload.path");
		if (uploadPath == null)
		{
			System.out.println("没有配置上传文件的存放路径。");
			return;
		}

		System.out.println("前：" + uploadPath);

		// 检查路径配置是否已“/”符号结尾

		if (uploadPath.charAt(uploadPath.length() - 1) != '/')
		{
			uploadPath += "/";
		}

		System.out.println("后：" + uploadPath);

		File uploadDir = new File(uploadPath);
		// 如果目录不存在则创建
		if (!uploadDir.exists())
		{
			uploadDir.mkdir();
		}
		if (!uploadDir.canWrite())
		{
			System.out.println("上传目录没有写权限。");
			return;
		}

		// 上传个人照片时需要用到的参数配置检查
		String uploadImgTypeList = AppProperties.getPropertiesValueByKey("upload.imgtype");
		String uploadImgSize = AppProperties.getPropertiesValueByKey("upload.imgsize");
		if (uploadImgTypeList == null || uploadImgSize == null || !uploadImgSize.matches("^[1-9]\\d+$"))
		{
			System.out.println("没有配置上传照片支持的文件类型或者大小。");
			return;
		}

		// 上传个人简历时需要用到的参数配置检查
		String uploadResumeTypeList = AppProperties.getPropertiesValueByKey("upload.resumetype");
		String uploadResumeSize = AppProperties.getPropertiesValueByKey("upload.resumesize");
		if (uploadResumeTypeList == null || uploadResumeSize == null || !uploadResumeSize.matches("^[1-9]\\d+$"))
		{
			System.out.println("没有配置上传简历支持的文件类型或者大小。");
			return;
		}

		HttpSession session = request.getSession();
		System.out.println("session-method: " + session.getAttribute("method"));
		System.out.println("session-token: " + session.getAttribute("token"));

		// 是否重复提交
		if (!TokenProccessor.checkToken(request))
		{
			// session 超时后，要回到登录页面
//			if (session.getAttribute("method") == null)
//			{
//				System.out.println("session超时。");
//				request.getRequestDispatcher("/UserLogin?method=userLoginForm").forward(request, response);
//			}
//			else
//			{
			System.out.println("重复提交了。");
			userInfoEditForm(request, response);
//			}
			return;
		}

		session.setAttribute("method", "userInfoEditSubmit"); // 记录当前 method
		session.setAttribute("token", TokenProccessor.makeToken()); // 更新 token

		boolean validate = true;
		String msg = null;
		session.removeAttribute("msg");

		/*
		 * 1 reg_id (记录ID) int
		 * 2 reg_u_id (登录记录ID) int
		 * 3 reg_name (姓名) varchar(50)
		 * 4 reg_idcard (身份证) varchar(30)
		 * 5 reg_birthday (出生日期：可根据身份证读出) date
		 * 6 reg_gender (性别) tinyint
		 * 7 reg_political_status (政治面貌) varchar(50)
		 * 8 reg_phone (联系电话) varchar(50)
		 * 9 reg_photo (一寸照：存放路径) varchar(200)
		 * 10 reg_education (最高学历) varchar(100)
		 * 11 reg_college (毕业院校) varchar(100)
		 * 12 reg_profession_kind (从事行业类别) varchar(100)
		 * 13 reg_profession (从事行业) varchar(100)
		 * 14 reg_company (工作单位) varchar(100)
		 * 15 reg_company_address (单位地址) varchar(200)
		 * 16 reg_email (工作邮箱) varchar(50)
		 * 17 reg_resume (个人简历：存放路径) varchar(200)
		 */

		// 页面传入的参数：1 reg_id (记录ID)
		int reg_id = Integer.parseInt(request.getParameter("reg_id"));
		// 页面传入的参数：2 reg_u_id (登录记录ID)
		int reg_u_id = Integer.parseInt(request.getParameter("u_id"));
		// 页面传入的参数：3 reg_name (姓名)
		String reg_name = request.getParameter("name");
		// 页面传入的参数：4 reg_u_idcard (身份证)
		String reg_idcard = request.getParameter("idcard");
		// 5 reg_birthday (出生日期：可根据身份证读出)
		Date reg_birthday = null;
		// 6 reg_gender (性别：可根据身份证读出)
		int reg_gender = -1;
		// 页面传入的参数：7 reg_political_status (政治面貌)
		String reg_political_status = request.getParameter("political_status");
		// 页面传入的参数：8 reg_phone (联系电话)
		String reg_phone = request.getParameter("phone");
		// 页面传入的参数：9 reg_photo (一寸照：存放路径)
//		String reg_photo = request.getParameter("photo");
		Part reg_photoPart = request.getPart("photo");
		// 页面传入的参数：10 reg_education (最高学历)
		String reg_education = request.getParameter("education");
		// 页面传入的参数：11 reg_college (毕业院校)
		String reg_college = request.getParameter("college");
		// 页面传入的参数：12 reg_profession_kind (从事行业类别)
		String reg_profession_kind = request.getParameter("profession_kind");
		// 页面传入的参数：13 reg_profession (从事行业)
		String reg_profession = request.getParameter("profession");
		// 页面传入的参数：14 reg_company (工作单位)
		String reg_company = request.getParameter("company");
		// 页面传入的参数：15 reg_company_address (单位地址)
		String reg_company_address = request.getParameter("company_address");
		// 页面传入的参数：16 reg_email (工作邮箱)
		String reg_email = request.getParameter("email");
		// 页面传入的参数：17 reg_resume (个人简历：存放路径)
//		String reg_resume = request.getParameter("resume");
		Part reg_resumePart = request.getPart("resume");

		// 个人照片文件名
		String photoFileName = getUploadFileNameFromHeader(reg_photoPart.getHeader("content-disposition"));
		// 个人照片文件保存路径
		String photoFileSavePath = uploadPath + reg_u_id + "/" + photoFileName;
		// 个人简历文件名
		String resumeFileName = getUploadFileNameFromHeader(reg_resumePart.getHeader("content-disposition"));
		// 个人简历文件保存路径
		String resumeFileSavePath = uploadPath + reg_u_id + "/" + resumeFileName;
		// 在磁盘上建立个人目录
		uploadDir = new File(uploadPath + reg_u_id);
		// 如果目录不存在则创建
		if (!uploadDir.exists())
		{
			uploadDir.mkdir();
		}
		if (!uploadDir.canWrite())
		{
			System.out.println("上传目录没有写权限。");
			return;
		}

//		System.out.println("个人照片文件名："+reg_photoPart.getSubmittedFileName());
//		System.out.println("个人照片文件名："+getFileName(reg_photoPart.getHeader("content-disposition")));
//		System.out.println("个人照片header："+reg_photoPart.getHeader("content-disposition"));
//		System.out.println("个人照片文件名后缀："+reg_photoPart.getSubmittedFileName().substring(reg_photoPart.getSubmittedFileName().lastIndexOf('.')+1));

		// 姓名校验
		if (validate && !InputValidation.isLegalName(reg_name))
		{
			validate = false;
			System.out.println("姓名错误。");
			msg = "请输入正确的姓名！";
		}

		// 身份证校验
		if (validate)
		{
			IDCardValidation idcard = new IDCardValidation(reg_idcard);
			if (!idcard.validate())
			{
				validate = false;
				System.out.println("身份证格式错误。");
				msg = "请输入正确的身份证号码！";
			}
			else
			{
				// 从身份证号码读出生日
				reg_birthday = idcard.getBirthDate();
				// 从身份证号码读出性别（0 女，1 男）
				reg_gender = idcard.isMale() ? 1 : 0;
			}
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

		// 假如有上传了个人照片，则需要对文件的格式以及大小进行判断
		if (validate && reg_photoPart.getSize() > 0)
		{
			// 文件大小判断
			if (reg_photoPart.getSize() > Integer.parseInt(uploadImgSize) * 1024)
			{
				validate = false;
				System.out.println("个人照片大小过大。");
				msg = "个人照片的大小不能超过" + uploadImgSize + "KB！";
			}
			// 文件后缀名判断
			else
			{
				validate = false;
				String imgType = photoFileName.substring(photoFileName.lastIndexOf('.') + 1);
				String[] imgTypeArray = uploadImgTypeList.split(",");
//				Arrays.asList(uploadImgTypeList.split(",")).contains(imgType);
				for (int i = 0; i < imgTypeArray.length; i++)
				{
					if (imgTypeArray[i].equalsIgnoreCase(imgType))
					{
						validate = true;
						break;
					}
				}

				if (!validate)
				{
					System.out.println("个人照片文件格式有误。");
					msg = "个人照片文件格式只能是 " + uploadImgTypeList + "。";
				}
			}
		}

		// 简历是否有上传
		if (validate && reg_resumePart.getSize() == 0)
		{
			validate = false;
			System.out.println("个人简历未上传。");
			msg = "请上传个人简历！";
		}

		if (validate && reg_resumePart.getSize() > 0)
		{
			// 文件大小判断
			if (reg_resumePart.getSize() > Integer.parseInt(uploadResumeSize) * 1024)
			{
				validate = false;
				System.out.println("个人照片大小过大。");
				msg = "个人照片的大小不能超过" + uploadResumeSize + "KB！";
			}
			// 文件后缀名判断
			else
			{
				validate = false;
				String resumeType = resumeFileName.substring(resumeFileName.lastIndexOf('.') + 1);
				String[] resumeTypeArray = uploadResumeTypeList.split(",");
//				Arrays.asList(uploadImgTypeList.split(",")).contains(imgType);
				for (int i = 0; i < resumeTypeArray.length; i++)
				{
					if (resumeTypeArray[i].equalsIgnoreCase(resumeType))
					{
						validate = true;
						break;
					}
				}

				if (!validate)
				{
					System.out.println("简历文件格式有误。");
					msg = "个人简历文件格式只能是 " + uploadImgTypeList + "。";
				}
			}
		}

		// 其余的必填项不能为空
		if (validate && (reg_political_status == null || reg_political_status.trim().length() == 0 // 政治面貌
				|| reg_education == null || reg_education.trim().length() == 0 // 最高学历
				|| reg_profession_kind == null || reg_profession_kind.trim().length() == 0 // 从事行业类型
				|| reg_profession == null || reg_profession.trim().length() == 0 // 从事行业
		))
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

		// 上传个人照片
		if (reg_photoPart.getSize() > 0)
		{
			reg_photoPart.write(photoFileSavePath);
		}
		// 上传个人简历
		if (reg_resumePart.getSize() > 0)
		{
			reg_resumePart.write(resumeFileSavePath);
		}

		// 所有的验证通过了，可以进行更新或新增记录了
		Registration reg = new Registration();
		reg.setReg_id(reg_id); // 1 reg_id (记录ID)
		reg.setReg_u_id(reg_u_id); // 2 reg_u_id (登录记录ID)
		reg.setReg_name(reg_name); // 3 reg_name (姓名)
		reg.setReg_idcard(reg_idcard); // 4 reg_idcard (身份证)
		reg.setReg_birthday(reg_birthday); // 5 reg_birthday (出生日期：可根据身份证读出)
		reg.setReg_gender(reg_gender); // 6 reg_gender (性别：可根据身份证读出)
		reg.setReg_political_status(reg_political_status); // 7 reg_political_status (政治面貌)
		reg.setReg_phone(reg_phone); // 8 reg_phone (联系电话)
		reg.setReg_photo(reg_photoPart.getSize() > 0 ? photoFileSavePath : ""); // 9 reg_photo (一寸照：存放路径)
		reg.setReg_education(reg_education); // 10 reg_education (最高学历)
		reg.setReg_college(reg_college == null ? "" : reg_college); // 11 reg_college (毕业院校)
		reg.setReg_profession_kind(reg_profession_kind); // 12 reg_profession_kind (从事行业类别)
		reg.setReg_profession(reg_profession); // 13 reg_profession (从事行业)
		reg.setReg_company(reg_company == null ? "" : reg_company); // 14 reg_company (工作单位)
		reg.setReg_company_address(reg_company_address == null ? "" : reg_company_address); // 15 reg_company_address (单位地址)
		reg.setReg_email(reg_email == null ? "" : reg_email); // 16 reg_email (工作邮箱)
		reg.setReg_resume(reg_resumePart.getSize() > 0 ? resumeFileSavePath : ""); // 17 reg_resume (个人简历：存放路径)

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
					System.out.println("[" + request.getParameter("job_title_date" + i) + "]");
					rjt.setRjt_date(request.getParameter("job_title_date" + i) == null
							|| request.getParameter("job_title_date" + i).trim().isEmpty() ? null
									: (new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("job_title_date" + i).trim())));

				}
				catch (Exception e)
				{
					rjt.setRjt_date(null);
					e.printStackTrace();
				}
				rjt.setRjt_organization(request.getParameter("job_title_organization" + i) == null ? ""
						: request.getParameter("job_title_organization" + i).trim());
				System.out.println(rjt);
				jobTitleList.add(rjt);
			}
		}

		int result = expRegService.registration(reg, jobTitleList);
		if (result > 0)
		{
			System.out.println("资料更新成功：" + result);
			Writer out = response.getWriter();
			out.write("信息已保存。");
//			request.getRequestDispatcher("/UserInfo?method=userInfoForm").forward(request, response);
			userInfoForm(request,response);
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
