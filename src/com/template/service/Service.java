package com.template.service;

import java.util.List;

/**
 * @author JGW
 *
 */
public interface Service {
	
	public void saveOrUpdate(Object obj);
	public void delete(Object obj);
	
	public List<Object> listByHql(String hql,int first,int max);
	public List<Object> listBySql(String sql);
	
	public String listChildByFid(String func,String tablename,String fid);
}
