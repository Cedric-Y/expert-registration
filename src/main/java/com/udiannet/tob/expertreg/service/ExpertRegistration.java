package com.udiannet.tob.expertreg.service;

import java.util.List;

import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.domain.RegistrationJobTitle;

/**
 * 专家资料相关业务接口
 */
public interface ExpertRegistration
{
	/**
	 * 根据专家输入的信息，进行新增或者更新
	 */
	int registration(Registration registration, List<RegistrationJobTitle> jobTitleList);
	/**
	 * 根据登录用户的id，显示注册信息
	 */
	Registration showRegistration(int u_id);
	/**
	 * 根据登录用户 id，查询对应的专家资料
	 */
	Registration findRegistrationByUserId(int u_id);
	/**
	 * 根据专家资料记录ID，查询职称信息
	 */
	List<RegistrationJobTitle> findJobTitleListByRegId(int reg_id);
}
