package com.template.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dzjk.model.DZJK_DevicePosition;
import com.dzjk.model.DZJK_WuZiType;
import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Org_Department;
import com.template.model.Org_User;
import com.template.model.Qxgl_Permission;
import com.template.service.OrgUserService;
import com.template.service.TreeService;
import com.template.util.SysUtil;

public class TreeAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/*********************************************************************************
	* Spring注入
	*********************************************************************************/
	private TreeService treeService;
	private OrgUserService orgUserService;
	public TreeService getTreeService() {
		return treeService;
	}
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	public OrgUserService getOrgUserService() {
		return orgUserService;
	}
	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}	
	/*********************************************************************************
	 * Spring注入
	 *********************************************************************************/

	/**
	 * 权限树
	 */
	public void sysTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				Qxgl_Permission role = null;
				boolean leaf = true;
				
				String id = request.getParameter("id");
				String json = "[";
				List<Object> list = new ArrayList<Object>();
				list = treeService.listNodesByParent(id,"Qxgl_Permission");
				if(list != null) {
					for(int i=0;i<list.size();i++){
						role = (Qxgl_Permission)list.get(i);
						leaf = treeService.hasChild(role.getId()+"");
						json += "{id:"+role.getId()+",text:'"+role.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",url:'"+role.getUrl()+"'},";
					}
					if(json.length() > 1) {
						json = json.substring(0, json.length()-1);
					}
				}
				json += "]";
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
	 * 登陆树
	 */
	public void loginTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				Object uid = request.getSession().getAttribute("uid");
				if(uid != null){
					Org_User org_User = (Org_User) orgUserService.getById(uid+"");
					List<String> pms = new ArrayList<String>();
					if(org_User.getPermissions() != null){
						String[] qxs = org_User.getPermissions().split(",");
						for (String pm : qxs) {
							pms.add(pm);
						}
					}
					Qxgl_Permission role = null;
					boolean leaf = true;
					boolean checked = false;
					String psId = request.getParameter("id");
					String json = "[";
					List<Object> list = new ArrayList<Object>();
					list = treeService.listNodesByParent(psId,"Qxgl_Permission");
					if(list != null) {
						for(int i=0;i<list.size();i++){
							role = (Qxgl_Permission)list.get(i);
							leaf = treeService.hasChild(role.getId()+"");
							checked = pms.contains(role.getId()+"");
							if(checked){
								json += "{id:"+role.getId()+",text:'"+role.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",url:'"+role.getUrl()+"'},";
							}
						}
						if(json.length() > 1) {
							json = json.substring(0, json.length()-1);
						}
					}
					json += "]";
					response.getWriter().print(json);
				}
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
	 * 组织部门树
	 */
	public void orgTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				Org_Department role = null;
				String id = request.getParameter("id");
				String json = "[";
				List<Object> list = new ArrayList<Object>();
				list = treeService.listNodesByParent(id,"Org_Department");
				if(list != null) {
					for(int i=0;i<list.size();i++){
						role = (Org_Department)list.get(i);
						boolean leaf = true;
						if(role.getIsleaf() == 1){
							leaf = false;
						}
						json += "{id:"+role.getId()+",text:'"+role.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",path:'"+role.getPath()+"'},";
					}
					if(json.length() > 1) {
						json = json.substring(0, json.length()-1);
					}
				}
				json += "]";
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
	 * 分配权限树
	 */
	public void sysCheckBoxTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String userid = request.getParameter("userid");

				boolean leaf = true;
				boolean checked = false;
				
				String id = request.getParameter("id");
				String json = "[";
				List<Object> list = new ArrayList<Object>();
				list = treeService.listNodesByParent(id,"Qxgl_Permission");
				if(list != null) {
					Org_User org_User = (Org_User)orgUserService.getById(userid);
					List<String> pms = new ArrayList<String>();
					if(org_User != null){
						if(org_User.getPermissions() != null){
							String[] pmses = org_User.getPermissions().split(",");
							for (String vl : pmses) {
								pms.add(vl);
							}
						}
					}
					for(int i=0;i<list.size();i++){
						Qxgl_Permission role = (Qxgl_Permission)list.get(i);
						leaf = treeService.hasChild(role.getId()+"");
						checked = pms.contains(role.getId()+"");
						json += "{id:"+role.getId()+",text:'"+role.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",url:'"+role.getUrl()+"',checked:"+checked+"},";
					}
					if(json.length() > 1) {
						json = json.substring(0, json.length()-1);
					}
				}
				json += "]";
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
	 * 物资类别树
	 */
	public void wuziTypeTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			DZJK_WuZiType wuZiType = null;
			boolean leaf = true;
			
			String id = request.getParameter("id");
			String json = "[";
			List<Object> list = new ArrayList<Object>();
			list = treeService.listNodesByParent(id,"DZJK_WuZiType");
			if(list != null) {
				for(int i=0;i<list.size();i++){
					wuZiType = (DZJK_WuZiType)list.get(i);
					if(wuZiType.getIsleaf()==0){
						leaf = true;
					}else{
						leaf = false;
					}
					json += "{id:"+wuZiType.getId()+",text:'"+wuZiType.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",isleaf:'"+wuZiType.getIsleaf()+"'},";
				}
				if(json.length() > 1) {
					json = json.substring(0, json.length()-1);
				}
			}
			json += "]";
			response.getWriter().print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 物资类别树
	 */
	public void wuziTypeCheckBoxTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			DZJK_WuZiType wuZiType = null;
			boolean leaf = true;
			
			String id = request.getParameter("id");
			String json = "[";
			List<Object> list = new ArrayList<Object>();
			list = treeService.listNodesByParent(id,"DZJK_WuZiType");
			if(list != null) {
				for(int i=0;i<list.size();i++){
					wuZiType = (DZJK_WuZiType)list.get(i);
					if(wuZiType.getIsleaf()==0){
						leaf = true;
						json += "{id:"+wuZiType.getId()+",text:'"+wuZiType.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",isleaf:'"+wuZiType.getIsleaf()+"',checked:false},";
					}else{
						leaf = false;
						json += "{id:"+wuZiType.getId()+",text:'"+wuZiType.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",isleaf:'"+wuZiType.getIsleaf()+"'},";
					}
				}
				if(json.length() > 1) {
					json = json.substring(0, json.length()-1);
				}
			}
			json += "]";
			response.getWriter().print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 定检点树
	 */
	public void devicePositionTree() {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			DZJK_DevicePosition obj = null;
			boolean leaf = true;
			
			String id = request.getParameter("id");
			String json = "[";
			List<Object> list = new ArrayList<Object>();
			list = treeService.listNodesByParent(id,"DZJK_DevicePosition");
			if(list != null) {
				for(int i=0;i<list.size();i++){
					obj = (DZJK_DevicePosition)list.get(i);
					if(obj.getIsleaf()==0){
						leaf = true;
					}else{
						leaf = false;
					}
					json += "{id:"+obj.getId()+",text:'"+obj.getName()+"',icon:'img/tree/subsystem.png',leaf:"+leaf+",isleaf:'"+obj.getIsleaf()+"'},";
				}
				if(json.length() > 1) {
					json = json.substring(0, json.length()-1);
				}
			}
			json += "]";
			response.getWriter().print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
