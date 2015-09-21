package com.template.serviceImpl;

import java.util.List;

import com.template.dao.TreeDao;
import com.template.service.TreeService;

public class TreeServiceImpl implements TreeService {
	
	private TreeDao treeDao;
	public TreeDao getTreeDao() {
		return treeDao;
	}
	public void setTreeDao(TreeDao treeDao) {
		this.treeDao = treeDao;
	}


	/**
	 * @param node
	 * @return
	 * 根据父节点得到所有的孩子节点
	 */
	public List<Object> listNodesByParent(String node,String model) {
		return treeDao.listNodesByParent(node,model);
	}
	
	/**
	 * @param node
	 * @return
	 * 检测该节点下是否有孩子节点
	 */
	public boolean hasChild(String node) {
		return treeDao.hasChild(node);
	}
	
	/**
	 * @param id
	 * @param groupId
	 * @return
	 * 检测权限组是否有此权限
	 */
	public boolean isChecked(String groupId, Integer id) {
		return treeDao.isChecked(groupId,id);
	}

}
