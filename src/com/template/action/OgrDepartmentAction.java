package com.template.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Org_Department;
import com.template.service.OrgDepartmentService;
import com.template.service.Service;
import com.template.util.SysUtil;

public class OgrDepartmentAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	private OrgDepartmentService orgDepartmentService;
	private SysUtil sysUtil;
	private Service service;
	
	public SysUtil getSysUtil() {
		return sysUtil;
	}
	public OrgDepartmentService getOrgDepartmentService() {
		return orgDepartmentService;
	}
	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	/**
	 * 修改或者保存Depart
	 */
	public void saveOrUpdate() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				String descr = request.getParameter("descr");
				String isleaf =  request.getParameter("isleaf");
				String path = request.getParameter("path");
				String fid = request.getParameter("fid");
				
				
				Org_Department department = new Org_Department();
				if(id != null && !"".equals(id)){
					String hql = "from Org_Department where id="+id;
					List<Object> list = service.listByHql(hql,0,1);
					department = (Org_Department) list.get(0);
				}
				department.setName(name);
				department.setDescr(descr);
				if(isleaf.equals("true")){
					//更新，原来不是叶子节点，
					if(department.getIsleaf()!=null && department.getIsleaf()==1){
						//当下边没有子节点可以更新
						String hql = "from Org_Department where fid="+id;
						List<Object> list = service.listByHql(hql,0,1);
						if(list==null || list.size()==0){
							department.setIsleaf(0);	
						}
					}else{
						//新增时直接处理
						department.setIsleaf(0);						
					}
				}else{
					//更新时
					if(department.getIsleaf()!=null && department.getIsleaf()==0){
						//没有人员信息才可以变
						String hql = "from Org_User where orgId="+id;
						List<Object> list = service.listByHql(hql,0,1);
						if(list==null || list.size()==0){
							department.setIsleaf(1);	
						}
					}else{						
						department.setIsleaf(1);
					}
				}
				department.setFid(Integer.parseInt(fid));
				department.setPath(path);
				
				orgDepartmentService.saveOrUpdate(department);
				
				map.put("success", "true");
				String json = JSONObject.fromObject(map).toString();
				response.getWriter().print(json);
			}else {
				Map<String, String> jsonMap = new HashMap<String, String>();
				jsonMap.put("success", "session");
				String json = JSONObject.fromObject(jsonMap).toString();
				response.getWriter().print(json);
			}
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
		}finally {}
	}
	
	/**
	 * 删除departs
	 */
	public void deleteDeparts() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String[] idstr = request.getParameter("ids").split(",");
				for(String fid: idstr){
					String ids = service.listChildByFid("showtreenodes","ls3x_org_department", fid);
					String hql = "From Org_User where orgId in("+ids+") order by id";
					List<Object> list = service.listByHql(hql,0,1);
					if(list==null || list.size()==0){
						Org_Department department = new Org_Department();
						department.setId(Integer.parseInt(fid));
						service.delete(department);
					}
				}
				map.put("success", "true");
				map.put("msg", "true");
				String json = JSONObject.fromObject(map).toString();
				response.getWriter().print(json);
			}else {
				Map<String, String> jsonMap = new HashMap<String, String>();
				jsonMap.put("success", "session");
				String json = JSONObject.fromObject(jsonMap).toString();
				response.getWriter().print(json);
			}
			
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
		}finally {}
	}

	/**
	 * 查询departs
	 */
	public void listAllDeparts() {
		int count = 0;
		response.setCharacterEncoding("utf-8");
		try {
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String fid = request.getParameter("id");
				
				String ids = service.listChildByFid("showtreenodes","ls3x_org_department", fid);
				String hql = "From Org_Department where id in("+ids+") order by id";
				List<Object> list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				count = list.size();
				
				
				String json = "{";
				if(list != null && list.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(list);
					String temp = jsonArray.toString();
					json += "root:["+temp.substring(2, temp.length()-2)+"],totalProperty:"+count+"";				
				}else {
					json += "root:[],totalProperty:"+count+"";								
				}
				json += "}";
				response.getWriter().print(json);
			}else {
				Map<String, String> jsonMap = new HashMap<String, String>();
				jsonMap.put("success", "session");
				String json = JSONObject.fromObject(jsonMap).toString();
				response.getWriter().print(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
