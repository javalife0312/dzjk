package com.template.daoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.template.dao.Dao;

/**
 * @author JGW
 */
public class DaoImpl implements Dao{
	
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@Override
	public void saveOrUpdate(Object obj) {
		hibernateTemplate.saveOrUpdate(obj);
	}
	@Override
	public List<Object> listByHql(final String hql,final int first,final int max) {
		List<Object> result = new ArrayList<Object>();
		result = hibernateTemplate.execute(new HibernateCallback<List<Object>>() {
			@SuppressWarnings({ "unchecked",})
			@Override
			public List<Object> doInHibernate(Session session) throws HibernateException,
					SQLException {
				List<Object> tmp = new ArrayList<Object>();
				Query query = session.createQuery(hql);
				query.setFirstResult(first);
				query.setMaxResults(max);
				tmp = (List<Object>)query.list();
				return tmp;
			}
		});
		return result;
	}
	@Override
	public void delete(Object obj) {
		hibernateTemplate.delete(obj);
	}
	
	@Override
	public String listChildByFid(final String func,final String tablename, final String fid) {
		String result = hibernateTemplate.execute(new HibernateCallback<Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public String doInHibernate(Session session)
					throws HibernateException, SQLException {
				String rs = "-1";
				String sql = "call " + func + "("+fid+",'"+tablename+"')";
				Query query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				for(Object tmp : list){
					rs += "," + tmp.toString();
				}
				return rs;
			}
		}).toString();
		return result;
	}
	
	@Override
	public List<Object> listBySql(final String sql) {
		List<Object> result = new ArrayList<Object>();
		result = hibernateTemplate.execute(new HibernateCallback<List<Object>>() {
			@SuppressWarnings({ "unchecked",})
			@Override
			public List<Object> doInHibernate(Session session) throws HibernateException,
					SQLException {
				List<Object> tmp = new ArrayList<Object>();
				Query query = session.createSQLQuery(sql);
				tmp = (List<Object>)query.list();
				return tmp;
			}
		});
		return result;
	}
}
