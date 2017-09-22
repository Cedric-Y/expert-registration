package com.udiannet.tob.expertreg.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.udiannet.tob.expertreg.domain.Registration;

@Mapper
public interface RegistrationMapper
{
	Registration getRegistrationById(int reg_id);
}
