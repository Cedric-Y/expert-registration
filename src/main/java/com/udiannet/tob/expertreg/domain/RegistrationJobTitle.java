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
}
