package com.udiannet.tob.expertreg.domain;

import java.util.Date;

/**
 * 专家注册表：tbl_registration
 */
public class Registration
{
	/*
		1	reg_id (记录ID)		int
		2	reg_u_id (登录记录ID)		int
		3	reg_name (姓名)		varchar(50)
		4	reg_u_idcard (身份证)		varchar(30)
		5	reg_birthday (出生日期：可根据身份证读出)		date
		6	reg_telephone (联系电话)		varchar(50)
		7	reg_photo (一寸照：存放路径)		varchar(200)
		8	reg_education (最高学历)		varchar(100)
		9	reg_college (毕业院校)		varchar(100)
		10	reg_profession (从事行业)		varchar(100)
		11	reg_company (工作单位)		varchar(100)
		12	reg_company_address (单位地址)		varchar(200)
		13	reg_email (工作邮箱)		varchar(50)
		14	reg_resume (个人简历：存放路径)		varchar(200)
		15	reg_code (专家编号)		varchar(50)
		16	reg_hiring_start (聘用开始日期)		date
		17	reg_hiring_year (聘期：年)		tinyint
		18	reg_check_status_id (审核状态)		int
		19	reg_fail_reason (没通过审核原因)		varchar(200)
		20	reg_remarks (备注)		varchar(200)
		21	reg_create_time (首次注册时的时间)		datetime
		22	reg_update_time (最近一次修改时间)		datetime
		23	reg_record_status (记录状态)		tinyint
	*/
	
	private int reg_id;
	private int reg_u_id;
	private String reg_name;
	private String reg_idcard;
	private Date reg_birthday;
	private String reg_telephone;
	private String reg_photo;
	private String reg_education;
	private String reg_college;
	private String reg_profession;
	private String reg_company;
	private String reg_company_address;
	private String reg_email;
	private String reg_resume;
	private String reg_code;
	private Date reg_hiring_start;
	private int reg_hiring_time;
	private int reg_status;
	private String reg_fail_reason;
	private String reg_remarks;
	private Date reg_create_time;
	private Date reg_update_time;
	private int reg_record_status;
	
	@Override
	public String toString()
	{
		return "Registration [reg_id=" + reg_id + ", reg_u_id=" + reg_u_id + ", reg_name=" + reg_name + ", reg_idcard=" + reg_idcard
				+ ", reg_birthday=" + reg_birthday + ", reg_telephone=" + reg_telephone + ", reg_photo=" + reg_photo + ", reg_education="
				+ reg_education + ", reg_college=" + reg_college + ", reg_profession=" + reg_profession + ", reg_company=" + reg_company
				+ ", reg_company_address=" + reg_company_address + ", reg_email=" + reg_email + ", reg_resume=" + reg_resume + ", reg_code="
				+ reg_code + ", reg_hiring_start=" + reg_hiring_start + ", reg_hiring_time=" + reg_hiring_time + ", reg_status="
				+ reg_status + ", reg_fail_reason=" + reg_fail_reason + ", reg_remarks=" + reg_remarks + ", reg_create_time="
				+ reg_create_time + ", reg_update_time=" + reg_update_time + ", reg_record_status=" + reg_record_status + "]";
	}
	
}
