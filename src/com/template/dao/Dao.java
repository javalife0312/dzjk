package com.template.dao;

import java.util.List;

public interface Dao {
	
	public void saveOrUpdate(Object obj);
	public void delete(Object obj);
	
	public List<Object> listByHql(String hql,final int first,final int max);
	public List<Object> listBySql(String sql);
	public String listChildByFid(String func,String tablename,String fid);

}
