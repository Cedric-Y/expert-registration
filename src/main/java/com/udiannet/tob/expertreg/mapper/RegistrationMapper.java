package com.udiannet.tob.expertreg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.udiannet.tob.expertreg.domain.Registration;
/**
 * 专家注册
 */
@Mapper
public interface RegistrationMapper
{
	Registration getRegistrationById(int reg_id);
	/**
	 * 根据登录用户 id，查询对应的专家资料
	 */
	Registration findRegistrationByUserId(@Param("u_id") int u_id);
}
