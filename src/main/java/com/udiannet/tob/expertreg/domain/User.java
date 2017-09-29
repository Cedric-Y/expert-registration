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
	*/
	
	private int u_id;
	private String u_login_name;
	private String u_email;
	private String u_password;
	private Date u_create_time;
	private Date u_update_time;
	private int u_type;
	
	public final int getU_id()
	{
		return u_id;
	}

	@Override
	public String toString()
	{
		return "User [u_id=" + u_id + ", u_login_name=" + u_login_name + ", u_email=" + u_email + ", u_password=" + u_password
				+ ", u_create_time=" + u_create_time + ", u_update_time=" + u_update_time + ", u_type=" + u_type + "]";
	}
}
