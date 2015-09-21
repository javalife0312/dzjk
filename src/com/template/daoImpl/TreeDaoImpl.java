package com.template.daoImpl;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.template.dao.TreeDao;
import com.template.model.Qxgl_Permission;

public class TreeDaoImpl implements TreeDao {
	
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * @param node
	 * @return
	 * 根据父节点得到所有的孩子节点
	 */
	@SuppressWarnings("unchecked")
	public List<Object> listNodesByParent(String node,String model) {
		List<Object> list = null;
		try {
			String hql = "from " + model + " where fid = " + node + " order by id";
			list = hibernateTemplate.find(hql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * @param node
	 * @return
	 * 根据父节点得到所有的孩子节点
	 */
	@SuppressWarnings("unchecked")
	public boolean hasChild(String node) {
		boolean leaf = true;
		List<Qxgl_Permission> list = null;
		try {
			String hql = "from Qxgl_Permission where fid = " + node;
			list = hibernateTemplate.find(hql);
			if(list != null && list.size() > 0) {
				leaf = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leaf;
	}
	
	/**
	 * @param id
	 * @param groupId
	 * @return
	 * 检测权限组是否有此权限
	 */
	public boolean isChecked(String groupId, Integer id) {
		boolean flag = false;
		try {
			
			String hql = "select count(id) from Qxgl_Group_Permission where groupId = " + groupId + " and roleId = " + id;
			int count = Integer.parseInt(hibernateTemplate.find(hql).get(0).toString());
			if(count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
