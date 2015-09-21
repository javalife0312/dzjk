package com.template.daoImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.template.dao.OrgDepartmentDao;
import com.template.model.Org_Department;

public class OrgDepartmentDaoImpl implements OrgDepartmentDao {
	
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * @param args
	 * @return
	 * 查询所有的部门信息
	 */
	@SuppressWarnings("unchecked")
	public List<Org_Department> listDeparts(final Map<String, String> args) {
		List<Org_Department> list = null;
		try {
			list = hibernateTemplate.executeFind(new HibernateCallback<List<Org_Department>>(){
				public List<Org_Department> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String hql = "from Org_Department where 1 = 1";
					if(args.containsKey("path")){
						hql += " and path like '" + args.get("path") + ",%'";
					}
					Query query = session.createQuery(hql);
					if(args.containsKey("first") && args.containsKey("limit")){
						query.setFirstResult(Integer.parseInt(args.get("first")));
						query.setMaxResults(Integer.parseInt(args.get("limit")));
					}
					return (List<Org_Department>)query.list();
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * @param args
	 * @return
	 * 查询所有的权限数量
	 */
	public int listCount(Map<String, String> args) {
		int result = 0;
		try {
			String hql = "select count(id) from Org_Department where 1 = 1";
			if(args.containsKey("path")){
				hql += " and path like '" + args.get("path") + ",%'";
			}
			String count = hibernateTemplate.find(hql).get(0).toString();
			result = Integer.parseInt(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param department
	 * 修改或者保存部门信息
	 */
	public void saveOrUpdate(Org_Department department) {
		try {
			hibernateTemplate.saveOrUpdate(department);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param ids
	 * 删除部门信息
	 */
	public void deleteDeparts(String[] ids) {
		try {
			for(int i=0;i<ids.length;i++){
				int child_count = Integer.valueOf(hibernateTemplate.find("select count(id) from Org_Department where fid="+ids[i]).get(0).toString());
				if(child_count == 0){
					Object object = hibernateTemplate.get(Org_Department.class, Integer.valueOf(ids[i]));
					hibernateTemplate.delete(object);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
