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
		3	u_idcard (身份证：用户找回用户名或密码)	varchar(30)
		4	u_phone (手机号码)	varchar(20)
		5	u_password (密码)	varchar(50)
		6	u_create_time (首次注册成功时间)	datetime
		7	u_update_time (最近一次登录时间)	datetime
		8	u_type (用户类型)	smallint
	*/
	
	private int u_id;
	private String u_login_name;
	private String u_idcard;
	private String u_phone;
	private String u_password;
	private Date u_create_time;
	private Date u_update_time;
	private int u_type;
	
	@Override
	public String toString()
	{
		return "User [u_id=" + u_id + ", u_login_name=" + u_login_name + ", u_idcard=" + u_idcard + ", u_phone=" + u_phone + ", u_password="
				+ u_password + ", u_create_time=" + u_create_time + ", u_update_time=" + u_update_time + ", u_type=" + u_type + "]";
	}
}
