package com.udiannet.tob.expertreg.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.mapper.RegistrationMapper;
import com.udiannet.tob.expertreg.service.ExpertRegistration;

public class ExpertRegistrationImpl implements ExpertRegistration
{
	private SqlSession sqlSession = SessionFactoryManager.openSession();

	@Override
	public void registration(Registration registration)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Registration showRegistration(int u_id)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据登录用户 id，查询对应的专家资料
	 */
	@Override
	public Registration findRegistrationByUserId(int u_id)
	{
		RegistrationMapper mapper = sqlSession.getMapper(RegistrationMapper.class);
		Registration reg = (Registration) mapper.findRegistrationByUserId(u_id);
		return reg;
	}

}
