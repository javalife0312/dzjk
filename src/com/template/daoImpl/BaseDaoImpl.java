package com.template.daoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.template.dao.BaseDao;
import com.template.model.Org_Department;
import com.template.model.Org_User;
import com.template.util.SysUtil;

/**
 * @author Administrator
 * 完成数据库的基本的crud
 */
public class BaseDaoImpl implements BaseDao{
	
	/*******************************************************************
	 * 			Spring 注入                           									   *
	 *******************************************************************/
	HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/*******************************************************************
	 * 			Spring 注入                           									   *
	 *******************************************************************/
	
	
	/**
	 * @param hql
	 * @param args 一般是分页信息,默认都包含first和limit信息
	 * @return
	 * 根据hql获取结果信息
	 */
	@SuppressWarnings("unchecked")
	public List<Object> listObjectsByHql(final String hql,final Map<String, String> args){
		List<Object> list = new ArrayList<Object>();
		list = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				
				if(args != null){
					if(args.containsKey("first") && args.containsKey("limit")){
						String first = args.get("first");
						String limit = args.get("limit");
						query.setFirstResult(Integer.valueOf(first));
						query.setMaxResults(Integer.valueOf(limit));
					}
				}
				return query.list();
			}
		});
		return list;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List listObjectArrByHql(final String hql,
			final Map<String, String> args) {
		List list = new ArrayList();
		list = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(hql);
				String first = "0";
				String limit = "100";
				if(args != null){
					if(args.containsKey("first")){
						first = args.get("first");
						query.setFirstResult(Integer.valueOf(first));
					}
					if(args.containsKey("limit")){
						limit = args.get("limit");
						query.setMaxResults(Integer.valueOf(limit));
					}
				}
				return query.list();
			}
		});
		return list;
	}
	
	/**
	 * @param hql
	 * @param args 一般是分页信息
	 * @return
	 * 根据hql获取结果信息
	 */
	@SuppressWarnings({ "rawtypes" })
	public int getTotalCountByHql(final String hql,Map<String, String> args){
		int result = 0;
		try {
			List list = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					return query.list();
				}
			});
			return Integer.valueOf(list.get(0).toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param object
	 * 保存修改Object
	 */
	public void saveOrUpdate(Object object){
		hibernateTemplate.saveOrUpdate(object);
	}
	
	/**
	 * @param object
	 * 删除对象
	 */
	public void deleteObject(Object object){
		hibernateTemplate.delete(object);
	}
	
	@SuppressWarnings("unchecked")
	public Org_Department getDeptmentByUid(String uid) {
		String hql = "from Org_User where id="+uid;
		List<Org_User> listUsers = hibernateTemplate.find(hql);
		if(listUsers != null && listUsers.size() > 0){
			hql = "from Org_Department where id = " + listUsers.get(0).getOrgId();
			List<Org_Department> listDepartments = hibernateTemplate.find(hql);
			if(listDepartments != null && listDepartments.size() > 0){
				return listDepartments.get(0);
			}
		}
		return null;
	}
}
