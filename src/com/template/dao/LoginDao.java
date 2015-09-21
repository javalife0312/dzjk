package com.template.dao;

import com.template.model.Org_User;

/**
 * @author JGW
 *
 */
public interface LoginDao {
	
	/**
	 * @param username
	 * @param password
	 * @return
	 * 根据用户名密码得到权限用户
	 */
	public Org_User isExitAuthorizationUser(String username,String password);
}
