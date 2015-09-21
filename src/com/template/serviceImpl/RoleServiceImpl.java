package com.template.serviceImpl;

import java.util.List;

import com.template.dao.RoleDao;
import com.template.model.Qxgl_Permission;
import com.template.service.RoleService;

public class RoleServiceImpl implements RoleService {
	
	private RoleDao roleDao;
	public RoleDao getRoleDao() {
		return roleDao;
	}
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	/**
	 * @param args
	 * @return
	 * 查询所有的权限
	 */
	public List<Qxgl_Permission> listRoles(List<String> args) {
		return roleDao.listRoles(args);
	}
	/**
	 * @param args
	 * @return
	 * 查询所有的权限数量
	 */
	public int listRolesCount(List<String> args) {
		return roleDao.listRolesCount(args);
	}
	
	/**
	 * @param role
	 * 保存或者更新Role
	 */
	public void saveOrUpdateRole(Qxgl_Permission role) {
		roleDao.saveOrUpdateRole(role);
	}
	
	/**
	 * @param ids
	 * 删除Roles
	 */
	public void deleteRoles(String[] ids) {
		roleDao.deleteRoles(ids);
	}
	
	
}
