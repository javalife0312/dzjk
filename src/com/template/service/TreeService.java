package com.template.service;

import java.util.List;


public interface TreeService {
	
	/**
	 * @param node
	 * @return
	 * 根据父节点得到所有的孩子节点
	 */
	public List<Object> listNodesByParent(String node,String Model);

	/**
	 * @param node
	 * @return
	 * 检测该节点下是否有孩子节点
	 */
	public boolean hasChild(String node);

	/**
	 * @param id
	 * @param groupId
	 * @return
	 * 检测权限组是否有此权限
	 */
	public boolean isChecked(String groupId,Integer id);
}
