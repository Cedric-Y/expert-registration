package com.udiannet.tob.expertreg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.udiannet.tob.expertreg.domain.RegistrationJobTitle;

/**
 * 专家注册-职称
 */
@Mapper
public interface RegistrationJobTitleMapper
{
	/**
	 * 新增专家职称记录
	 */
	int insertRegJobTitle(@Param("RegistrationJobTitle") RegistrationJobTitle regJobTitle);
	/**
	 * 删除专家职称记录
	 */
	int deleteRegJobTitleByRegId(@Param("reg_id")int reg_id);

}
