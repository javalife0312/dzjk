package com.template.serviceImpl;

import java.util.List;

import com.template.dao.Dao;
import com.template.service.Service;

/**
 * @author JGW
 * 
 */
public class ServiceImpl implements Service{
	
	private Dao dao;
	public Dao getDao() {
		return dao;
	}
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	
	@Override
	public void saveOrUpdate(Object obj) {
		dao.saveOrUpdate(obj);
	}
	
	@Override
	public List<Object> listByHql(String hql, int first, int max) {
		return dao.listByHql(hql, first, max);
	}
	
	@Override
	public void delete(Object obj) {
		dao.delete(obj);
	}
	@Override
	public String listChildByFid(String func,String tablename, String fid) {
		return dao.listChildByFid(func,tablename,fid);
	}
	@Override
	public List<Object> listBySql(String sql) {
		return dao.listBySql(sql);
	}
}
