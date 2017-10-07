package com.udiannet.tob.expertreg.domain;

import java.util.Date;

/**
 * 登录用户管理：tbl_user
 */
public class User
{
	/*
		1	u_id (记录ID)	numeric
		2	u_login_name (登录名称)	varchar(50)
		3	u_email (emial：用户找回用户名或密码)	varchar(30)
		4	u_password (密码)	varchar(50)
		5	u_create_time (首次注册成功时间)	datetime
		6	u_update_time (最近一次登录时间)	datetime
		7	u_type (用户类型)	smallint
		8	u_validate_code (重置登录名或者密码的验证码)	varchar(100)
		9	u_expire_time (验证码到期时间)	timestamp
	*/
	
	private int u_id;
	private String u_login_name;
	private String u_email;
	private String u_password;
	private Date u_create_time;
	private Date u_update_time;
	private int u_type;
	private String u_validate_code;
	private long u_expire_time;
	
	public final int getU_id()
	{
		return u_id;
	}

	public String getU_login_name()
	{
		return u_login_name;
	}

	public void setU_login_name(String u_login_name)
	{
		this.u_login_name = u_login_name;
	}

	public String getU_email()
	{
		return u_email;
	}

	public void setU_email(String u_email)
	{
		this.u_email = u_email;
	}

	public String getU_password()
	{
		return u_password;
	}

	public void setU_password(String u_password)
	{
		this.u_password = u_password;
	}

	public Date getU_create_time()
	{
		return u_create_time;
	}

	public void setU_create_time(Date u_create_time)
	{
		this.u_create_time = u_create_time;
	}

	public Date getU_update_time()
	{
		return u_update_time;
	}

	public void setU_update_time(Date u_update_time)
	{
		this.u_update_time = u_update_time;
	}

	public int getU_type()
	{
		return u_type;
	}

	public void setU_type(int u_type)
	{
		this.u_type = u_type;
	}

	public String getU_validate_code()
	{
		return u_validate_code;
	}

	public void setU_validate_code(String u_validate_code)
	{
		this.u_validate_code = u_validate_code;
	}

	public long getU_expire_time()
	{
		return u_expire_time;
	}

	public void setU_expire_time(long u_expire_time)
	{
		this.u_expire_time = u_expire_time;
	}

	@Override
	public String toString()
	{
		return "User [u_id=" + u_id + ", u_login_name=" + u_login_name + ", u_email=" + u_email + ", u_password="
				+ u_password + ", u_create_time=" + u_create_time + ", u_update_time=" + u_update_time + ", u_type="
				+ u_type + ", u_validate_code=" + u_validate_code + ", u_expire_time=" + u_expire_time + "]";
	}
}
