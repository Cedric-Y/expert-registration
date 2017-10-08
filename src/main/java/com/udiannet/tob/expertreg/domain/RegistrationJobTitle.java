package com.udiannet.tob.expertreg.domain;

import java.util.Date;

/**
 * 专家注册-职称：tbl_registration_job_title
 */
public class RegistrationJobTitle
{
	/*
		1	rjt_id (职称记录ID)	int
		2	rjt_reg_id (专家注册表记录ID)	int
		3	rjt_name (职称证书名称)	varchar(100)
		4	rjt_level (职称证书级别)	varchar(50)
		5	rjt_date (获得证书时间)	date
		6	rjt_organization (发证机构)	varchar(100)
	*/
	private int rjt_id;
	private int rjt_reg_id;
	private String rjt_name;
	private String rjt_level;
	private Date rjt_date;
	private String rjt_organization;
	
	public int getRjt_id()
	{
		return rjt_id;
	}
	public void setRjt_id(int rjt_id)
	{
		this.rjt_id = rjt_id;
	}
	public int getRjt_reg_id()
	{
		return rjt_reg_id;
	}
	public void setRjt_reg_id(int rjt_reg_id)
	{
		this.rjt_reg_id = rjt_reg_id;
	}
	public String getRjt_name()
	{
		return rjt_name;
	}
	public void setRjt_name(String rjt_name)
	{
		this.rjt_name = rjt_name;
	}
	public String getRjt_level()
	{
		return rjt_level;
	}
	public void setRjt_level(String rjt_level)
	{
		this.rjt_level = rjt_level;
	}
	public Date getRjt_date()
	{
		return rjt_date;
	}
	public void setRjt_date(Date rjt_date)
	{
		this.rjt_date = rjt_date;
	}
	public String getRjt_organization()
	{
		return rjt_organization;
	}
	public void setRjt_organization(String rjt_organization)
	{
		this.rjt_organization = rjt_organization;
	}
	
	@Override
	public String toString()
	{
		return "RegistrationJobTitle [rjt_id=" + rjt_id + ", rjt_reg_id=" + rjt_reg_id + ", rjt_name=" + rjt_name
				+ ", rjt_level=" + rjt_level + ", rjt_date=" + rjt_date + ", rjt_organization=" + rjt_organization
				+ "]";
	}
}
