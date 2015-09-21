package com.template.service;

import java.util.List;
import java.util.Map;

import com.template.model.Org_User;

/**
 * @author JGW
 *
 */
public interface OrgUserService {
	
	/**
	 * @param args
	 * @return
	 * 列出查询信息
	 */
	public List<Org_User> listUsers(Map<String, String> map);

	/**
	 * @param args
	 * @return 
	 * 获取数量
	 */
	public int listCount(Map<String, String> map);

	/**
	 * @param department
	 * 修改或者保存信息
	 */
	public void saveOrUpdate(Org_User org_User);

	/**
	 * @param object
	 * 删除对象
	 */
	public void deleteObject(Object object);

	/**
	 * @param args
	 * @return
	 * 判定是否存在username
	 */
	public int checkUsername(Map<String, String> args);

	/**
	 * @param userid
	 * @return
	 * 根据id获取对象
	 */
	public Object getById(String userid);
}
