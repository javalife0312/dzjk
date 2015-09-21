package com.dzjk.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dzjk.model.DZJK_WuZiType;
import com.opensymphony.xwork2.ActionSupport;
import com.template.service.Service;
import com.template.util.SysUtil;

public class DZJK_WuZiTypeAction extends ActionSupport {

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
				String fid = request.getParameter("fid");
				String name = request.getParameter("name");
				String descr =  request.getParameter("descr");
				String isleaf =  request.getParameter("isleaf");
				String type =  request.getParameter("type");
				if(type==null || type.equals("")){
					type = "1";
				}
				
				DZJK_WuZiType dzjk_WuZiType = new DZJK_WuZiType();
				if(id != null && !"".equals(id)){
					dzjk_WuZiType.setId(Integer.parseInt(id));
				}
				dzjk_WuZiType.setFid(Integer.parseInt(fid));
				dzjk_WuZiType.setName(name);
				dzjk_WuZiType.setDescr(descr);
				dzjk_WuZiType.setType(Integer.parseInt(type));
				if(isleaf.equals("")){
					dzjk_WuZiType.setIsleaf(0);					
				}else{
					dzjk_WuZiType.setIsleaf(Integer.parseInt(isleaf));
				}
				
				service.saveOrUpdate(dzjk_WuZiType);
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
		String fid = request.getParameter("fid");
		if(sessionCheck){
			List<Object> list = null;
			int count = 0;
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String ids = service.listChildByFid("showtreenodes","DZJK_WuZiType", fid);
				
				String hql = "From DZJK_WuZiType where fid in("+ids+") order by id";
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
				System.out.println(json);
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
						DZJK_WuZiType dzjk_WuZiType = new DZJK_WuZiType();
						dzjk_WuZiType.setId(Integer.valueOf(id));
						
						String hql = "From DZJK_WuZiType where fid="+id;
						List<Object> childs = service.listByHql(hql, 0, 2);
						if(childs==null || childs.size()==0){
							service.delete(dzjk_WuZiType);							
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
}
