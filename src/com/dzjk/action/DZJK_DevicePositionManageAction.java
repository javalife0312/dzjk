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

import com.dzjk.model.DZJK_DevicePosition;
import com.opensymphony.xwork2.ActionSupport;
import com.template.service.Service;
import com.template.util.SysUtil;

public class DZJK_DevicePositionManageAction extends ActionSupport {

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
				
				DZJK_DevicePosition obj = new DZJK_DevicePosition();
				if(id != null && !"".equals(id)){
					obj.setId(Integer.parseInt(id));
				}
				obj.setFid(Integer.parseInt(fid));
				obj.setName(name);
				obj.setDescr(descr);
				if(isleaf.equals("")){
					obj.setIsleaf(0);					
				}else{
					obj.setIsleaf(Integer.parseInt(isleaf));
				}
				
				service.saveOrUpdate(obj);
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
	 * 设备位置信息
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
				String ids = service.listChildByFid("showtreenodes","dzjk_device_position", fid);
				
				String hql = "From DZJK_DevicePosition where id in ("+ids+") order by id";
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
						DZJK_DevicePosition devicePosition = new DZJK_DevicePosition();
						devicePosition.setId(Integer.valueOf(id));
						
						String hql = "From DZJK_Device where fid="+id;
						List<Object> childs = service.listByHql(hql, 0, 2);
						if(childs==null || childs.size()==0){
							service.delete(devicePosition);							
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
