package com.template.dao;

import java.util.List;
import java.util.Map;

import com.template.model.Org_Department;

/**
 * @author Administrator
 * 完成数据库的基本的crud
 */
public interface BaseDao {
	
	/**
	 * @param hql
	 * @param args 一般是分页信息
	 * @return
	 * 根据hql获取结果信息
	 */
	public List<Object> listObjectsByHql(String hql,Map<String, String> args);
	
	public List<Object[]> listObjectArrByHql(String hql,Map<String, String> args);
	
	/**
	 * @param hql
	 * @param args 一般是分页信息
	 * @return
	 * 根据hql获取结果信息
	 */
	public int getTotalCountByHql(String hql,Map<String, String> args);
	
	/**
	 * @param object
	 * 保存修改Object
	 */
	public void saveOrUpdate(Object object);
	
	/**
	 * @param object
	 * 删除对象
	 */
	public void deleteObject(Object object);

	public Org_Department getDeptmentByUid(String uid);
}
