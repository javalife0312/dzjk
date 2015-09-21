package com.template.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.template.dao.BaseDao;
import com.template.model.Org_User;
import com.template.service.OrgUserService;

public class OrgUserServiceImpl implements OrgUserService{
	
	private BaseDao baseDao;
	public BaseDao getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	/**
	 * @param args
	 * @return
	 * 列出查询信息
	 */
	public List<Org_User> listUsers(Map<String, String> map){
		
		List<Object> objects = new ArrayList<Object>();
		List<Org_User> list = new ArrayList<Org_User>();
		String hql = "from " + map.get("model").toString() + " where 1=1";
		if(map.containsKey("orgId")){
			hql += " and orgId = "+map.get("orgId").toString();
		}
		objects = baseDao.listObjectsByHql(hql, map);
		for (Object object : objects) {
			Org_User org_User = (Org_User)object;
			list.add(org_User);
		}
		return list;
	}

	/**
	 * @param args
	 * @return 
	 * 获取数量
	 */
	public int listCount(Map<String, String> map){
		String hql = "select count(*) from " + map.get("model").toString() + " where 1=1";
		if(map.containsKey("uid")){
			hql += " and orgId = "+map.get("orgId").toString();
		}
		int result = baseDao.getTotalCountByHql(hql, map);
		return result;
	}
	
	/**
	 * @param department
	 * 修改或者保存用户信息
	 */
	public void saveOrUpdate(Org_User org_User) {
		baseDao.saveOrUpdate(org_User);
	}
	
	/**
	 * @param ids
	 * 删除用户信息
	 */
	public void deleteObject(Object object) {
		baseDao.deleteObject(object);
	}
	
	/**
	 * @param username
	 * @return
	 * 判定是否存在username
	 */
	public int checkUsername(Map<String, String> map) {
		String hql = "select count(*) from " + map.get("model").toString() + " where 1=1";
		if(map.containsKey("username")){
			hql += " and username = '"+map.get("username") + "'";
		}
		int result = baseDao.getTotalCountByHql(hql, map);
		return result;
	}
	
	/**
	 * @param userid
	 * @return
	 * 根据id获取对象
	 */
	public Object getById(String userid) {
		String hql = "from Org_User where id = " + userid;
		List<Object> objects = baseDao.listObjectsByHql(hql,null);
		if(objects != null && objects.size()>0){
			return objects.get(0);
		}
		return null;
	}
	
}
