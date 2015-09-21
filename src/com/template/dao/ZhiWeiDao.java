package com.template.dao;

import java.util.List;

import com.template.model.Org_ZhiWei;
import com.template.model.Qxgl_Permission;

public interface ZhiWeiDao {
	
	/**
	 * @param args
	 * @return
	 * 查询所有的权限
	 */
	public List<Qxgl_Permission> listRoles(List<String> args);

	/**
	 * @param args
	 * @return
	 * 查询所有的权限数量
	 */
	public int listRolesCount(List<String> args);

	/**
	 * @param role
	 * 保存或者更新Role
	 */
	public void saveOrUpdate(Org_ZhiWei zhiWei);

	/**
	 * @param ids
	 * 删除Roles
	 */
	public void deleteRoles(String[] ids);
}
