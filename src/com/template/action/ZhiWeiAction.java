package com.template.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Org_ZhiWei;
import com.template.service.Service;
import com.template.util.SysUtil;

public class ZhiWeiAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	private Service service;
	private SysUtil sysUtil;
	
	public SysUtil getSysUtil() {
		return sysUtil;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * 修改或者保存
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
				String level =  request.getParameter("level");
				
				Org_ZhiWei org_ZhiWei = new Org_ZhiWei();
				if(id != null && !"".equals(id)){
					org_ZhiWei.setId(Integer.parseInt(id));
				}
				org_ZhiWei.setName(name);
				org_ZhiWei.setDescr(descr);
				org_ZhiWei.setLevel(Integer.parseInt(level));
				
				service.saveOrUpdate(org_ZhiWei);
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
	 * 罗列职位信息
	 */
	public void listAll() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			List<Object> list = null;
			int count = 0;
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				
				
				String hql = "From Org_ZhiWei order by id";
				list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("success", "session");
			String json = JSONObject.fromObject(jsonMap).toString();
			try {
				response.getWriter().print(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 罗列职位信息
	 */
	public void listAllLevel() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			List<Object> list = null;
			response.setCharacterEncoding("utf-8");
			try {
				String hql = "From Org_ZhiWei order by id";
				list = service.listByHql(hql,0,1000);
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(list);
				String json = jsonArray.toString();
				json = json.substring(1, json.length()-1);
				response.getWriter().print(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("success", "session");
			String json = JSONObject.fromObject(jsonMap).toString();
			try {
				response.getWriter().print(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String[] ids = request.getParameter("ids").split(",");
				if(ids != null){
					for (String id : ids) {
						Org_ZhiWei org_ZhiWei = new Org_ZhiWei();
						org_ZhiWei.setId(Integer.valueOf(id));
						String hql = "From Org_User where level="+id;
						List<Object> users = service.listByHql(hql, 0, 2);
						if(users==null || users.size()==0){
							service.delete(org_ZhiWei);							
						}
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
	 * 检测用户名是否存在
	 */
	public void checkUsername(){
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> args = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				int count = 0;
				
				String username = request.getParameter("username");
				if(username != null){
					args.put("model","Org_User");
					args.put("username",username);
//					count = orgUserService.checkUsername(args);
				}
				map.put("success", "true");
				if(count > 0){
					map.put("msg", "true");
				}else{
					map.put("msg", "false");
				}
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
