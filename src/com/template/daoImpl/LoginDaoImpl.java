package com.template.daoImpl;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.template.dao.LoginDao;
import com.template.model.Org_User;

/**
 * @author JGW
 * 
 */
public class LoginDaoImpl implements LoginDao{
	
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * @param username
	 * @param password
	 * @return 根据用户名密码得到权限用户
	 */
	@SuppressWarnings("unchecked")
	public Org_User isExitAuthorizationUser(String username,String password){
		Org_User authorizationUser = null;
		List<Org_User> list = null;
		try {
			String hql = "from Org_User where username = '"+username+"' and password = '"+password+"'";
			list = hibernateTemplate.find(hql);
			if(list != null && list.size() > 0) {
				authorizationUser = list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorizationUser;
	}
}
