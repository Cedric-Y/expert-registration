package com.udiannet.tob.expertreg.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.udiannet.tob.expertreg.domain.Registration;
import com.udiannet.tob.expertreg.domain.RegistrationJobTitle;
import com.udiannet.tob.expertreg.mapper.RegistrationJobTitleMapper;
import com.udiannet.tob.expertreg.mapper.RegistrationMapper;
import com.udiannet.tob.expertreg.service.ExpertRegistration;

/**
 * 专家资料相关业务接口的实现类
 */
public class ExpertRegistrationImpl implements ExpertRegistration
{
	private SqlSession sqlSession = SessionFactoryManager.openSession();

	/**
	 * 根据专家输入的信息，进行新增或者更新
	 */
	@Override
	public int registration(Registration registration, List<RegistrationJobTitle> jobTitleList)
	{
		RegistrationMapper regMapper = sqlSession.getMapper(RegistrationMapper.class);
		RegistrationJobTitleMapper jobTitleMapper = sqlSession.getMapper(RegistrationJobTitleMapper.class);

		int reg_id = registration.getReg_id();
//		Registration reg = regMapper.findRegistrationByUserId(registration.getReg_u_id());
		int result = 0;
		// 先查看此登录用户是否已经建立了资料库
		// 还没有建立资料库，需要进行新增操作
		if (reg_id < 1)
		{
			// 1 先增加专家注册库的记录，然后获取新增后的记录ID
			regMapper.insertRegistration(registration);
			sqlSession.commit();
			reg_id = registration.getReg_id();
			System.out.println("新增专家注册返回ID：" + reg_id);
			// 2 循环增加专家注册-职称记录
			for (int i = 0; i < jobTitleList.size(); i++)
			{
				RegistrationJobTitle regJobTitle = jobTitleList.get(i);
				regJobTitle.setRjt_reg_id(reg_id);
				jobTitleMapper.insertRegJobTitle(regJobTitle);
			}
			sqlSession.commit();

			result = reg_id;
		}
		// 已经建立了资料库，需要进行更新操作
		else
		{
			// 1 更新专家注册的记录
//			registration.setReg_id(reg.getReg_id());
			result = regMapper.updateRegistration(registration);

			// 2 删除专家注册-职称记录
			jobTitleMapper.deleteRegJobTitleByRegId(reg_id);

			// 3 重新增加专家注册-职称记录
			for (int i = 0; i < jobTitleList.size(); i++)
			{
				RegistrationJobTitle regJobTitle = jobTitleList.get(i);
				regJobTitle.setRjt_reg_id(reg_id);
				jobTitleMapper.insertRegJobTitle(regJobTitle);
			}

			sqlSession.commit();
		}
		return result;
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

	/**
	 * 根据专家资料记录ID，查询职称信息
	 */
	@Override
	public List<RegistrationJobTitle> findJobTitleListByRegId(int reg_id)
	{
		RegistrationJobTitleMapper jobTitleMapper = sqlSession.getMapper(RegistrationJobTitleMapper.class);
		return (List<RegistrationJobTitle>) jobTitleMapper.findJobTitleListByRegId(reg_id);
	}

}
