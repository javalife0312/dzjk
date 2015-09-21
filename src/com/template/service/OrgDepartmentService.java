package com.template.service;

import java.util.List;
import java.util.Map;

import com.template.model.Org_Department;

/**
 * @author JGW
 *
 */
public interface OrgDepartmentService {
	
	/**
	 * @param args
	 * @return
	 * 列出查询信息
	 */
	public List<Org_Department> listDeparts(Map<String, String> args);

	/**
	 * @param args
	 * @return 
	 * 获取数量
	 */
	public int listCount(Map<String, String> args);

	/**
	 * @param department
	 * 修改或者保存部门信息
	 */
	public void saveOrUpdate(Org_Department department);

	/**
	 * @param ids
	 * 删除部门信息
	 */
	public void deleteDeparts(String[] ids);
}
