package com.dzjk.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.dzjk.model.DZJK_WuZi;
import com.dzjk.model.DZJK_WuZiType;
import com.opensymphony.xwork2.ActionSupport;
import com.template.service.Service;
import com.template.util.SysUtil;

public class DZJK_WuZiAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
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
				String descr =  request.getParameter("descr");
				String wuziTypeId = request.getParameter("wuziTypeId");
				String tcount =  request.getParameter("tcount");
				String youxiaoqi =  request.getParameter("youxiaoqi");
				String tipDays =  request.getParameter("tipDays");
				String tipNum =  request.getParameter("tipNum");
				
				
				DZJK_WuZiType wuZiType = new DZJK_WuZiType();
				DZJK_WuZi dzjk_WuZi = new DZJK_WuZi();
				if(id != null && !"".equals(id)){
					String hql = "from DZJK_WuZi where id="+id;
					dzjk_WuZi = (DZJK_WuZi) service.listByHql(hql, 0, 2).get(0);
					dzjk_WuZi.setId(Integer.parseInt(id));
					
					int sysl = dzjk_WuZi.getTcount();
					int new_add = Integer.parseInt(tcount)-sysl;
					dzjk_WuZi.setT_all_count(dzjk_WuZi.getT_all_count()+new_add);
				}else{
					dzjk_WuZi.setFirstRecordDate(dateFormat.format(new Date()));
					dzjk_WuZi.setT_all_count(Integer.parseInt(tcount));
					dzjk_WuZi.setT_fafang_count(0);
					dzjk_WuZi.setT_feiqi_count(0);
				}
				wuZiType = (DZJK_WuZiType) service.listByHql("from DZJK_WuZiType where id="+wuziTypeId, 0, 2).get(0);
				dzjk_WuZi.setName(name);
				dzjk_WuZi.setDescr(descr);
				dzjk_WuZi.setWuziTypeId(Integer.parseInt(wuziTypeId));
				dzjk_WuZi.setWuziTypeName(wuZiType.getName());
				
				dzjk_WuZi.setTcount(Integer.parseInt(tcount));
				dzjk_WuZi.setT_all_count(Integer.parseInt(tcount));
				dzjk_WuZi.setYouxiaoqi(youxiaoqi);
				dzjk_WuZi.setTipDays(Integer.parseInt(tipDays));
				dzjk_WuZi.setTipNum(Integer.parseInt(tipNum));
				
				service.saveOrUpdate(dzjk_WuZi);
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
	 * 根据物资类别列出所有的物资信息
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
				String ids = service.listChildByFid("showtreenodes","dzjk_wuzitype", fid);
				
				String hql = "From DZJK_WuZi where wuziTypeID in("+ids+") order by id";
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
						DZJK_WuZi dzjk_WuZi = new DZJK_WuZi();
						dzjk_WuZi.setId(Integer.valueOf(id));
						
						String hql = "From DZJK_WuZi where tcount>0 and id="+id;
						List<Object> childs = service.listByHql(hql, 0, 2);
						if(childs==null || childs.size()==0){
							service.delete(dzjk_WuZi);							
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
