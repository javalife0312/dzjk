package com.template.daoImpl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.template.dao.RoleDao;
import com.template.model.Qxgl_Permission;

public class RoleDaoImpl implements RoleDao {
	
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
	 * 查询所有的权限
	 */
	@SuppressWarnings("unchecked")
	public List<Qxgl_Permission> listRoles(final List<String> args) {
		List<Qxgl_Permission> list = null;
		try {
			list = hibernateTemplate.executeFind(new HibernateCallback<List<Qxgl_Permission>>(){
				public List<Qxgl_Permission> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String hql = "from Qxgl_Permission where 1 = 1";
					int length = args.size();
					if(length>2){
						hql += " and fid = " + args.get(2);
					}
					Query query = session.createQuery(hql);
					if(length>=2){
						query.setFirstResult(Integer.parseInt(args.get(0)));
						query.setMaxResults(Integer.parseInt(args.get(1)));
					}
					return (List<Qxgl_Permission>)query.list();
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
	public int listRolesCount(List<String> args) {
		int result = 0;
		try {
			String hql = "select count(id) from Qxgl_Permission where 1 = 1";
			int length = args.size();
			if(length>2){
				hql += " and fid = " + args.get(2);
			}
			String count = hibernateTemplate.find(hql).get(0).toString();
			result = Integer.parseInt(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param role
	 * 保存或者更新Role
	 */
	public void saveOrUpdateRole(Qxgl_Permission role) {
		try {
			hibernateTemplate.saveOrUpdate(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param ids
	 * 删除Roles开始
	 * 判定是否有孩子,没有就删除
	 */
	private void deleteRoles(final String id) {
		try {
			String childCount = hibernateTemplate.find("select count(id) from Qxgl_Permission where fid="+id).get(0).toString();
			if(childCount.equals("0")){
				hibernateTemplate.execute(new HibernateCallback<Object>(){
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String sql = "delete from Qxgl_Permission where id =" + id;
						session.createQuery(sql).executeUpdate();
						return null;
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteRoles(final String[] ids) {
		try {
			for(int i=0;i<ids.length;i++){
				deleteRoles(ids[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***********************************************************************************************************************************/

}
