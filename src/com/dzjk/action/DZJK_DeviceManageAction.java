package com.dzjk.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dzjk.model.DZJK_Device;
import com.dzjk.model.DZJK_DevicePosition;
import com.dzjk.model.DZJK_DeviceTask;
import com.opensymphony.xwork2.ActionSupport;
import com.template.service.Service;
import com.template.util.SysUtil;
import com.template.util.Type;

public class DZJK_DeviceManageAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private DateFormat dateFormat = new SimpleDateFormat("YYYYMMdd HH:mm:ss");
	private DateFormat dateFormat_ymd = new SimpleDateFormat("YYYYMMdd");
	
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
	 * 巡检完成更改状态
	 */
	public void complateXJ() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String ids = request.getParameter("ids");
				String deviceStatus = request.getParameter("deviceStatus");
				String deal_note = request.getParameter("deal_note");
				
				String hql = "From DZJK_DeviceTask where id in("+ids+")";
				List<Object> objs = service.listByHql(hql, 0, Integer.MAX_VALUE);
				for(Object obj : objs){
					DZJK_DeviceTask deviceTask = (DZJK_DeviceTask)obj;
					if(deviceTask.getStatus()!=1){
						deviceTask.setStatus(1);
						deviceTask.setDeviceStatus(Integer.parseInt(deviceStatus));
						deviceTask.setDeal_note(deal_note);
						deviceTask.setUpdateTime(dateFormat.format(new Date()));
						service.saveOrUpdate(deviceTask);
					}
				}
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
				String tipDays =  request.getParameter("tipDays");
				
				DZJK_Device obj = new DZJK_Device();
				if(id != null && !"".equals(id)){
					String hql = "from DZJK_Device where id="+id;
					obj = (DZJK_Device)service.listByHql(hql, 0, 1).get(0);
				}
				String hql = "from DZJK_DevicePosition where id="+fid;
				DZJK_DevicePosition devicePosition = (DZJK_DevicePosition) service.listByHql(hql, 0, 1).get(0);
				obj.setFid(Integer.parseInt(fid));
				obj.setPosition(devicePosition.getName());
				obj.setName(name);
				obj.setDescr(descr);
				obj.setTipDays(Integer.parseInt(tipDays));
				
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
				
				String hql = "From DZJK_Device where fid in("+ids+") order by id";
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
	
	
	public void listDeviceTaskAllgroup() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			List<Object> list = null;
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				
				String taskDate = request.getParameter("taskDate");
				String weijianName = request.getParameter("weijianName");
				String deviceStatus = request.getParameter("deviceStatus");
				
				//ids 此部门下的所有子部门
				String fid = request.getParameter("fid");
				String department_ids = service.listChildByFid("showtreenodes","ls3x_org_department", fid);
				String hql = 
				"select a.id,a.positionId,a.positionName,a.deviceId,a.deviceName,a.weijianId,a.weijianName,a.tipDays,a.updateTime,a.taskDate," +
				" a.status,a.deviceStatus,a.deal_note from DZJK_DeviceTask a ,Org_User b where a.weijianId=b.id and b.orgId in ("+department_ids+") ";
				if (taskDate!=null && !"".equals(taskDate)){
					hql += " and a.taskDate='"+taskDate+"'";
				}
				if (weijianName!=null && !"".equals(weijianName)){
					hql += " and a.weijianName='"+weijianName+"'";
				}
				if (deviceStatus!=null && !"".equals(deviceStatus)){
					hql += " and a.deviceStatus='"+deviceStatus+"'";
				}
				System.out.println(hql);
				list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				
				List<DZJK_DeviceTask> list_res = new ArrayList<DZJK_DeviceTask>();
				for(Object tmp:list){
					Object[] obj_arr = (Object[]) tmp;
					tmp = trimNull(obj_arr);
					DZJK_DeviceTask deviceTask = new DZJK_DeviceTask();
					
					deviceTask.setId(Type.getInteger(obj_arr[0].toString()));
					deviceTask.setPositionId(Type.getInteger(obj_arr[1].toString()));
					deviceTask.setPositionName(obj_arr[2].toString());
					deviceTask.setDeviceId(Type.getInteger(obj_arr[3].toString()));
					deviceTask.setDeviceName(obj_arr[4].toString());
					deviceTask.setWeijianId(Type.getInteger(obj_arr[5].toString()));
					deviceTask.setWeijianName(obj_arr[6].toString());
					deviceTask.setTipDays(Type.getInteger(obj_arr[7].toString()));
					deviceTask.setUpdateTime(obj_arr[8].toString());
					deviceTask.setTaskDate(obj_arr[9].toString());
					deviceTask.setStatus(Type.getInteger(obj_arr[10].toString()));
					deviceTask.setDeviceStatus(Type.getInteger(obj_arr[11].toString()));
					deviceTask.setDeal_note(obj_arr[12].toString());
					
					list_res.add(deviceTask);
				}
				
				String json = "{";
				if(list_res != null && list_res.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(list_res);
					String temp = jsonArray.toString();
					json += "root:["+temp.substring(2, temp.length()-2)+"],totalProperty:"+list_res.size()+"";				
				}else {
					json += "root:[],totalProperty:"+list_res.size()+"";								
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
	private Object[] trimNull(Object[] arr){
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]==null){
				arr[i] = "";
			}
		}
		return arr;
	}
	/**
	 * 列出个人的巡检任务
	 */
	public void listDeviceTaskAll() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			List<Object> list = null;
			int count = 0;
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String uid = request.getSession().getAttribute("uid").toString();
				
				String showDate = dateFormat_ymd.format(new Date());
				String hql = "From DZJK_DeviceTask where weijianId="+uid+" and taskDate='"+showDate+"' order by weijianId,deviceStatus";
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
						String hql = "From DZJK_Device where id="+id;
						List<Object> list = service.listByHql(hql,0,1);
						service.delete(list.get(0));	
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
	
	public void deviceTaskHandle() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String deviceId = request.getParameter("deviceId");
				String weijianId = request.getParameter("weijianId");
				String weijianName = request.getParameter("weijianName");
			
				//同步修改设备表中的任务人
				String hql = "From DZJK_Device where id="+deviceId;
				List<Object> list = service.listByHql(hql,0,1);
				DZJK_Device device  = (DZJK_Device) list.get(0);
				device.setExecutorId(Integer.parseInt(weijianId));
				device.setExecutorName(weijianName);
				service.saveOrUpdate(device);
				
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
