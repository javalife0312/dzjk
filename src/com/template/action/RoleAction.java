package com.template.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Qxgl_Permission;
import com.template.service.RoleService;
import com.template.util.SysUtil;

public class RoleAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	private RoleService roleService;
	private SysUtil sysUtil;
	
	public SysUtil getSysUtil() {
		return sysUtil;
	}
	public void setSysUtil(SysUtil sysUtil) {
		this.sysUtil = sysUtil;
	}
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 查询Roles
	 */
	public void listAllRoles() {
		List<Qxgl_Permission> list = null;
		int count = 0;
		response.setCharacterEncoding("utf-8");
		try {
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				List<String> args = new ArrayList<String>();
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String id = request.getParameter("id");
				String json = "{";
				args.add(first);
				args.add(limit);
				if(id != null && !"".equals(id)){
					args.add(id);
				}
				list = roleService.listRoles(args);
				count = roleService.listRolesCount(args);
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
	
	/**
	 * 修改或者保存Roles
	 */
	public void saveOrUpdateRole() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String id = request.getParameter("id");
				String code = request.getParameter("code");
				String name = request.getParameter("name");
				String url =  request.getParameter("url");
				String fid = request.getParameter("fid");
				
				Qxgl_Permission role = new Qxgl_Permission();
				if(id != null && !"".equals(id)){
					role.setId(Integer.parseInt(id));
				}
				role.setCode(code);
				role.setName(name);
				role.setUrl(url);
				role.setFid(Integer.parseInt(fid));
				
				roleService.saveOrUpdateRole(role);
				
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
	 * 验证编码是否重复
	 */
	public void isCodeExit() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String code = request.getParameter("code");
				boolean flag = sysUtil.isCodeExit(code, "Qxgl_Permission", "code");
				map.put("success", "true");
				map.put("msg", flag+"");
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
	 * 删除Roles
	 */
	public void deleteRoles() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String[] ids = request.getParameter("ids").split(",");
				roleService.deleteRoles(ids);
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
	
}
