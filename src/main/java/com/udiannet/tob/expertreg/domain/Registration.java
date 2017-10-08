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
		4	reg_idcard (身份证)		varchar(30)
		5	reg_birthday (出生日期：可根据身份证读出)		date
		6	reg_phone (联系电话)		varchar(50)
		7	reg_photo (一寸照：存放路径)		varchar(200)
		8	reg_education (最高学历)		varchar(100)
		9	reg_college (毕业院校)		varchar(100)
		10	reg_profession_kind (从事行业类别)		varchar(100)
		11	reg_profession (从事行业)		varchar(100)
		12	reg_company (工作单位)		varchar(100)
		13	reg_company_address (单位地址)		varchar(200)
		14	reg_email (工作邮箱)		varchar(50)
		15	reg_resume (个人简历：存放路径)		varchar(200)
		16	reg_code (专家编号)		varchar(50)
		17	reg_hiring_start (聘用开始日期)		date
		18	reg_hiring_year (聘期：年)		tinyint
		19	reg_check_status_id (审核状态)		int
		20	reg_fail_reason (没通过审核原因)		varchar(200)
		21	reg_remarks (备注)		varchar(200)
		22	reg_create_time (首次注册时的时间)		datetime
		23	reg_update_time (最近一次修改时间)		datetime
		24	reg_record_status (记录状态)		tinyint
	*/
	
	private int reg_id;
	private int reg_u_id;
	private String reg_name;
	private String reg_idcard;
	private Date reg_birthday;
	private String reg_phone;
	private String reg_photo;
	private String reg_education;
	private String reg_college;
	private String reg_profession_kind;
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
	
	public int getReg_id()
	{
		return reg_id;
	}
	public void setReg_id(int reg_id)
	{
		this.reg_id = reg_id;
	}
	public int getReg_u_id()
	{
		return reg_u_id;
	}
	public void setReg_u_id(int reg_u_id)
	{
		this.reg_u_id = reg_u_id;
	}
	public String getReg_name()
	{
		return reg_name;
	}
	public void setReg_name(String reg_name)
	{
		this.reg_name = reg_name;
	}
	public String getReg_idcard()
	{
		return reg_idcard;
	}
	public void setReg_idcard(String reg_idcard)
	{
		this.reg_idcard = reg_idcard;
	}
	public Date getReg_birthday()
	{
		return reg_birthday;
	}
	public void setReg_birthday(Date reg_birthday)
	{
		this.reg_birthday = reg_birthday;
	}
	public String getReg_phone()
	{
		return reg_phone;
	}
	public void setReg_phone(String reg_phone)
	{
		this.reg_phone = reg_phone;
	}
	public String getReg_photo()
	{
		return reg_photo;
	}
	public void setReg_photo(String reg_photo)
	{
		this.reg_photo = reg_photo;
	}
	public String getReg_education()
	{
		return reg_education;
	}
	public void setReg_education(String reg_education)
	{
		this.reg_education = reg_education;
	}
	public String getReg_college()
	{
		return reg_college;
	}
	public void setReg_college(String reg_college)
	{
		this.reg_college = reg_college;
	}
	public String getReg_profession_kind()
	{
		return reg_profession_kind;
	}
	public void setReg_profession_kind(String reg_profession_kind)
	{
		this.reg_profession_kind = reg_profession_kind;
	}
	public String getReg_profession()
	{
		return reg_profession;
	}
	public void setReg_profession(String reg_profession)
	{
		this.reg_profession = reg_profession;
	}
	public String getReg_company()
	{
		return reg_company;
	}
	public void setReg_company(String reg_company)
	{
		this.reg_company = reg_company;
	}
	public String getReg_company_address()
	{
		return reg_company_address;
	}
	public void setReg_company_address(String reg_company_address)
	{
		this.reg_company_address = reg_company_address;
	}
	public String getReg_email()
	{
		return reg_email;
	}
	public void setReg_email(String reg_email)
	{
		this.reg_email = reg_email;
	}
	public String getReg_resume()
	{
		return reg_resume;
	}
	public void setReg_resume(String reg_resume)
	{
		this.reg_resume = reg_resume;
	}
	public String getReg_code()
	{
		return reg_code;
	}
	public void setReg_code(String reg_code)
	{
		this.reg_code = reg_code;
	}
	public Date getReg_hiring_start()
	{
		return reg_hiring_start;
	}
	public void setReg_hiring_start(Date reg_hiring_start)
	{
		this.reg_hiring_start = reg_hiring_start;
	}
	public int getReg_hiring_time()
	{
		return reg_hiring_time;
	}
	public void setReg_hiring_time(int reg_hiring_time)
	{
		this.reg_hiring_time = reg_hiring_time;
	}
	public int getReg_status()
	{
		return reg_status;
	}
	public void setReg_status(int reg_status)
	{
		this.reg_status = reg_status;
	}
	public String getReg_fail_reason()
	{
		return reg_fail_reason;
	}
	public void setReg_fail_reason(String reg_fail_reason)
	{
		this.reg_fail_reason = reg_fail_reason;
	}
	public String getReg_remarks()
	{
		return reg_remarks;
	}
	public void setReg_remarks(String reg_remarks)
	{
		this.reg_remarks = reg_remarks;
	}
	public Date getReg_create_time()
	{
		return reg_create_time;
	}
	public void setReg_create_time(Date reg_create_time)
	{
		this.reg_create_time = reg_create_time;
	}
	public Date getReg_update_time()
	{
		return reg_update_time;
	}
	public void setReg_update_time(Date reg_update_time)
	{
		this.reg_update_time = reg_update_time;
	}
	public int getReg_record_status()
	{
		return reg_record_status;
	}
	public void setReg_record_status(int reg_record_status)
	{
		this.reg_record_status = reg_record_status;
	}
	
	@Override
	public String toString()
	{
		return "Registration [reg_id=" + reg_id + ", reg_u_id=" + reg_u_id + ", reg_name=" + reg_name + ", reg_idcard="
				+ reg_idcard + ", reg_birthday=" + reg_birthday + ", reg_phone=" + reg_phone + ", reg_photo="
				+ reg_photo + ", reg_education=" + reg_education + ", reg_college=" + reg_college
				+ ", reg_profession_kind=" + reg_profession_kind + ", reg_profession=" + reg_profession
				+ ", reg_company=" + reg_company + ", reg_company_address=" + reg_company_address + ", reg_email="
				+ reg_email + ", reg_resume=" + reg_resume + ", reg_code=" + reg_code + ", reg_hiring_start="
				+ reg_hiring_start + ", reg_hiring_time=" + reg_hiring_time + ", reg_status=" + reg_status
				+ ", reg_fail_reason=" + reg_fail_reason + ", reg_remarks=" + reg_remarks + ", reg_create_time="
				+ reg_create_time + ", reg_update_time=" + reg_update_time + ", reg_record_status=" + reg_record_status
				+ "]";
	}
}
