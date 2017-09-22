package com.udiannet.tob.expertreg.service;

import java.util.List;

/**
 * 获取基础数据
 */
public interface BaseData
{
	/**
	 * 获取“基础数据：聘期（年）”列表
	 */
	List<String> getHiringYearList();
	/**
	 * 获取“基础数据：学历”列表
	 */
	List<String> getEducationList();
	/**
	 * 获取“基础数据：审核状态”列表
	 */
	List<String> getCheckStatusList();
	/**
	 * 获取“基础数据：从事行业”列表
	 */
	List<String> getProfessionList();
}
