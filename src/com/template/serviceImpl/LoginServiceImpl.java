package com.template.serviceImpl;

import com.template.dao.LoginDao;
import com.template.model.Org_User;
import com.template.service.LoginService;

/**
 * @author JGW
 * 
 */
public class LoginServiceImpl implements LoginService{
	
	private LoginDao loginDao;
	public LoginDao getLoginDao() {
		return loginDao;
	}
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}


	/**
	 * @param username
	 * @param password
	 * @return 根据用户名密码得到权限用户
	 */
	public Org_User isExitAuthorizationUser(String username,String password){
		return loginDao.isExitAuthorizationUser(username, password);
	}
}
