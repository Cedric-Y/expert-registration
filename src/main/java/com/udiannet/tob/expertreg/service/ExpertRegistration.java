package com.udiannet.tob.expertreg.service;

import com.udiannet.tob.expertreg.domain.Registration;

/**
 * 专家注册
 */
public interface ExpertRegistration
{
	/**
	 * 根据专家输入的信息，进行新增或者更新
	 */
	void registration(Registration registration);
	/**
	 * 根据登录用户的id，显示注册信息
	 */
	Registration showRegistration(int u_id);
	/**
	 * 根据登录用户 id，查询对应的专家资料
	 */
	Registration findRegistrationByUserId(int u_id);
}
