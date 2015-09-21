package com.template.serviceImpl;

import java.util.List;
import java.util.Map;

import com.template.dao.OrgDepartmentDao;
import com.template.model.Org_Department;
import com.template.service.OrgDepartmentService;

public class OrgDepartmentServiceImpl implements OrgDepartmentService {
	
	private OrgDepartmentDao orgDepartmentDao;

	public OrgDepartmentDao getOrgDepartmentDao() {
		return orgDepartmentDao;
	}
	public void setOrgDepartmentDao(OrgDepartmentDao orgDepartmentDao) {
		this.orgDepartmentDao = orgDepartmentDao;
	}
	
	/**
	 * @param args
	 * @return
	 * 列出查询信息
	 */
	public List<Org_Department> listDeparts(Map<String, String> args){
		return orgDepartmentDao.listDeparts(args);
	}

	/**
	 * @param args
	 * @return 
	 * 获取数量
	 */
	public int listCount(Map<String, String> args){
		return orgDepartmentDao.listCount(args);
	}
	
	/**
	 * @param department
	 * 修改或者保存部门信息
	 */
	public void saveOrUpdate(Org_Department department) {
		orgDepartmentDao.saveOrUpdate(department);
	}
	
	/**
	 * @param ids
	 * 删除部门信息
	 */
	public void deleteDeparts(String[] ids) {
		orgDepartmentDao.deleteDeparts(ids);
	}
	
}
