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

import com.dzjk.conf.DZJK_Config;
import com.dzjk.model.DZJK_PersionWuZi;
import com.dzjk.model.DZJK_PersionWuZi_Temp;
import com.dzjk.model.DZJK_RunningWorks;
import com.dzjk.model.DZJK_ShenqingWuZi;
import com.dzjk.model.DZJK_WuZi;
import com.dzjk.model.DZJK_WuZiType;
import com.dzjk.model.DZJK_WuZi_BaoXiu;
import com.dzjk.model.DZJK_WuZi_Baofei;
import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Org_User;
import com.template.model.Sys_Message;
import com.template.service.Service;
import com.template.util.SysUtil;
import com.template.util.Type;
import com.template.util.UUIDUtils;

public class DZJK_PersionWuZiAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private DateFormat dateFormat = new SimpleDateFormat("YYYYMMdd HH:mm:ss");
	
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
	 * 获取用户信息
	 */
	public void shnqingUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String uid = request.getSession().getAttribute("uid").toString();
				
				String hql = "from Org_User where id="+uid;
				List<Object> list = service.listByHql(hql,0,1);
				for(Object tmp : list){
					Org_User org_User = (Org_User)tmp;
					map.put("utype", org_User.getType()+"");
					map.put("uid", org_User.getId()+"");
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
	 * 个人资产申请
	 */
	public void wuziShenqing() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String uid = request.getSession().getAttribute("uid").toString();
				String ids = request.getParameter("ids");
				String bghid = request.getParameter("bghid");
				if(bghid==null || bghid.equals("")){
					bghid = "0";
				}
				String shenqingliyou = request.getParameter("shenqingliyou");
				String applytype = request.getParameter("applytype");
				
				Org_User org_User = (Org_User) service.listByHql("from Org_User where id="+uid, 0, 2).get(0);
				
				String hql = "from DZJK_WuZiType where id in("+ids+")";
				List<Object> list = service.listByHql(hql, 0, Integer.MAX_VALUE);
				for(Object tmp : list){
					String uuid = UUIDUtils.nextCode();
					
					DZJK_WuZiType dzjk_WuZiType = (DZJK_WuZiType) tmp;
					// 物资申请信息
					DZJK_ShenqingWuZi dzjk_ShenqingWuZi = new DZJK_ShenqingWuZi();
					
					dzjk_ShenqingWuZi.setPersion_id(Integer.parseInt(uid));
					dzjk_ShenqingWuZi.setPersion_name(org_User.getDescr());
					dzjk_ShenqingWuZi.setPersion_phone(org_User.getPhoneNum());
					dzjk_ShenqingWuZi.setShenqingliyou(shenqingliyou);
				    dzjk_ShenqingWuZi.setStatus(0);  //提交申请
				    dzjk_ShenqingWuZi.setTcount(1);
				    dzjk_ShenqingWuZi.setPersion_orgName(org_User.getOrgName());
				    
				    dzjk_ShenqingWuZi.setWuziTypeId(dzjk_WuZiType.getId());
				    dzjk_ShenqingWuZi.setWuziTypeName(dzjk_WuZiType.getName());
				    
				    
				    if(applytype.equals("3")){
				    	DZJK_PersionWuZi persionWuZi = (DZJK_PersionWuZi) service.listByHql("from DZJK_PersionWuZi where id="+bghid, 0, 2).get(0);
				    	dzjk_ShenqingWuZi.setWuziId(persionWuZi.getWuziId());
				    	dzjk_ShenqingWuZi.setWuziDescr(persionWuZi.getWuziDescr());
				    	dzjk_ShenqingWuZi.setWuziName(persionWuZi.getWuziName());
				    	dzjk_ShenqingWuZi.setWuziCode(persionWuZi.getWuziCode());
				    }
				    
				    dzjk_ShenqingWuZi.setApplytype(Integer.parseInt(applytype));
				    dzjk_ShenqingWuZi.setBghid(Integer.parseInt(bghid));
				    dzjk_ShenqingWuZi.setUuid(uuid);
				    
				    service.saveOrUpdate(dzjk_ShenqingWuZi);
				    
				    
				    //生成消息
				    Sys_Message message = new Sys_Message();
				    message.setType(DZJK_Config.msg_type_WuZiShenQing);
				    message.setTypeDesc(DZJK_Config.msg_type_WuZiShenQing_Desc);
				    message.setStatus(DZJK_Config.msg_status_WuZiShenQing_Create);
				    message.setCreateDate(dateFormat.format(new Date()));
				    message.setLevel(org_User.getYouxianji());
				    message.setUuid(uuid);
				    message.setApply_note(shenqingliyou);
				    message.setApply_personName(org_User.getDescr());
				    message.setApply_personGongwei(org_User.getGongwei());
				    message.setApply_personPhone(org_User.getPhoneNum());
				    message.setApply_wuziMsg(dzjk_WuZiType.getName());
				    
				    service.saveOrUpdate(message);
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
	 * 个人资产审批
	 */
	public void wuziShenqingShenPi() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String shenqingId = request.getParameter("shenqingId");
				String wuziId = request.getParameter("wuziId");
				
				String hql = "from DZJK_WuZi where id=("+wuziId+")";
				List<Object> list = service.listByHql(hql, 0, 2);
				DZJK_WuZi dzjk_WuZi = (DZJK_WuZi)list.get(0);
				
				hql = "from DZJK_ShenqingWuZi where id=("+shenqingId+")";
				list = service.listByHql(hql, 0, 2);
				DZJK_ShenqingWuZi dzjk_ShenqingWuZi = (DZJK_ShenqingWuZi)list.get(0);
				
				dzjk_ShenqingWuZi.setYouxiaoqi(dzjk_WuZi.getYouxiaoqi());
				dzjk_ShenqingWuZi.setWuziId(dzjk_WuZi.getId());
				dzjk_ShenqingWuZi.setWuziName(dzjk_WuZi.getName());
				dzjk_ShenqingWuZi.setWuziDescr(dzjk_WuZi.getDescr());
				
				dzjk_ShenqingWuZi.setStatus(3);
				
				service.saveOrUpdate(dzjk_ShenqingWuZi);
				
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
	 * 个人资产发放
	 */
	public void wuziShenqingFaFang() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String shenqingId = request.getParameter("shenqingId");
				String wuziCode = request.getParameter("wuziCode");
				String dealtype = request.getParameter("dealtype");
				String applytype = request.getParameter("applytype");
				
				String hql = "from DZJK_ShenqingWuZi where id=("+shenqingId+")";
				List<Object> list = service.listByHql(hql, 0, 2);
				DZJK_ShenqingWuZi dzjk_ShenqingWuZi = (DZJK_ShenqingWuZi)list.get(0);
				
				if(applytype.equals("1") || applytype.equals("2")){
					hql = "from DZJK_WuZi where id=("+dzjk_ShenqingWuZi.getWuziId()+")";
					list = service.listByHql(hql, 0, 2);
					DZJK_WuZi dzjk_WuZi = (DZJK_WuZi)list.get(0);
					//更改总库存的数量 -- 没有同步机制，可能存在bug
					dzjk_WuZi.setTcount(dzjk_WuZi.getTcount()-dzjk_ShenqingWuZi.getTcount());
					dzjk_WuZi.setT_fafang_count(dzjk_WuZi.getT_fafang_count()+dzjk_ShenqingWuZi.getTcount());
					
					
					hql = "from DZJK_WuZiType where id=("+dzjk_ShenqingWuZi.getWuziTypeId()+")";
					list = service.listByHql(hql, 0, 2);
					DZJK_WuZiType dzjk_WuZiType = (DZJK_WuZiType)list.get(0);
					
					//更新申请记录的状态
					dzjk_ShenqingWuZi.setStatus(4);
					service.saveOrUpdate(dzjk_ShenqingWuZi);
					
					DZJK_PersionWuZi dzjk_PersionWuZi = new DZJK_PersionWuZi();
					dzjk_PersionWuZi.setPersion_id(dzjk_ShenqingWuZi.getPersion_id());
					dzjk_PersionWuZi.setPersion_name(dzjk_ShenqingWuZi.getPersion_name());
					
					dzjk_PersionWuZi.setTcount(dzjk_ShenqingWuZi.getTcount());
					dzjk_PersionWuZi.setTipDays(dzjk_WuZi.getTipDays());
					dzjk_PersionWuZi.setWuziDescr(dzjk_WuZi.getDescr());
					dzjk_PersionWuZi.setWuziId(dzjk_WuZi.getId());
					dzjk_PersionWuZi.setWuziName(dzjk_WuZi.getName());
					dzjk_PersionWuZi.setWuziTypeDescr(dzjk_WuZiType.getDescr());
					dzjk_PersionWuZi.setWuziTypeId(dzjk_WuZiType.getId());
					dzjk_PersionWuZi.setWuziTypeName(dzjk_WuZiType.getName());
					dzjk_PersionWuZi.setYouxiaoqi(dzjk_WuZi.getYouxiaoqi());
					dzjk_PersionWuZi.setWuziCode(wuziCode);
					dzjk_PersionWuZi.setStatus(1);
					dzjk_PersionWuZi.setType(dzjk_WuZiType.getType());
					
					//保存申请记录
					service.saveOrUpdate(dzjk_PersionWuZi);
					//更新库存量
					service.saveOrUpdate(dzjk_WuZi);
					int shengyuNum = dzjk_WuZi.getTcount();
					int tipsNum = dzjk_WuZi.getTipNum();
					if(shengyuNum<=tipsNum){
						map.put("tipsNum", shengyuNum+"");
					}else{
						map.put("tipsNum", 0+"");
					}
					//判断物资的数量少于tips就提醒
					
					//如果是更换的话，需要看是否讲原来的资产回库
					 if(applytype.equals("2")){
						 if(dealtype.equals("1")){//回库
							 //更新库存
							 hql = "from DZJK_WuZi where id=("+dzjk_ShenqingWuZi.getWuziId()+")";
							 list = service.listByHql(hql, 0, 2);
							 DZJK_WuZi dzjk_WuZi1 = (DZJK_WuZi)list.get(0);
							 dzjk_WuZi1.setTcount(dzjk_WuZi1.getTcount()+dzjk_ShenqingWuZi.getTcount());
							 dzjk_WuZi1.setT_fafang_count(dzjk_WuZi1.getT_fafang_count()-dzjk_ShenqingWuZi.getTcount());
							 service.saveOrUpdate(dzjk_WuZi1);
						 }else{ //报废
							 hql = "from DZJK_WuZi where id=("+dzjk_ShenqingWuZi.getWuziId()+")";
							 list = service.listByHql(hql, 0, 2);
							 DZJK_WuZi dealWuZi = (DZJK_WuZi)list.get(0);
							 
							 dealWuZi.setT_fafang_count(dealWuZi.getT_fafang_count()-dzjk_ShenqingWuZi.getTcount());
							 dealWuZi.setT_feiqi_count(dealWuZi.getT_feiqi_count()+dzjk_ShenqingWuZi.getTcount());
							 
							 DZJK_WuZi_Baofei baofei = new DZJK_WuZi_Baofei();
							 baofei.setDescr(dealWuZi.getDescr());
							 baofei.setName(baofei.getName());
							 baofei.setTcount(dzjk_ShenqingWuZi.getTcount());
							 baofei.setWuziCode(baofei.getWuziCode());
							 baofei.setWuziTypeId(baofei.getWuziTypeId());
							 baofei.setWuziTypeName(baofei.getWuziTypeName());
							 service.saveOrUpdate(baofei);
						 }
						//之前的记录失效
						 hql = "from DZJK_PersionWuZi where id=("+dzjk_ShenqingWuZi.getBghid()+")";
						 list = service.listByHql(hql, 0, 2);
						 DZJK_PersionWuZi persionWuZi = (DZJK_PersionWuZi)list.get(0);
						 persionWuZi.setStatus(0);
						 service.saveOrUpdate(persionWuZi);
					 }
					 
				}
				if(applytype.equals("3")){					
					//退回
					if(dealtype.equals("1")){//回库
						//更新库存
						hql = "from DZJK_WuZi where id=("+dzjk_ShenqingWuZi.getWuziId()+")";
						list = service.listByHql(hql, 0, 2);
						DZJK_WuZi dzjk_WuZi1 = (DZJK_WuZi)list.get(0);
						dzjk_WuZi1.setTcount(dzjk_WuZi1.getTcount()+dzjk_ShenqingWuZi.getTcount());
						dzjk_WuZi1.setT_fafang_count(dzjk_WuZi1.getT_fafang_count()-dzjk_ShenqingWuZi.getTcount());
						service.saveOrUpdate(dzjk_WuZi1);
						
						int shengyuNum = dzjk_WuZi1.getTcount();
						int tipsNum = dzjk_WuZi1.getTipNum();
						if(shengyuNum<=tipsNum){
							map.put("tipsNum", shengyuNum+"");
						}else{
							map.put("tipsNum", 0+"");
						}
						
					}else{ //报废
						hql = "from DZJK_WuZi where id=("+dzjk_ShenqingWuZi.getWuziId()+")";
						list = service.listByHql(hql, 0, 2);
						DZJK_WuZi dealWuZi = (DZJK_WuZi)list.get(0);
						
						dealWuZi.setT_fafang_count(dealWuZi.getT_fafang_count()-dzjk_ShenqingWuZi.getTcount());
						dealWuZi.setT_feiqi_count(dealWuZi.getT_feiqi_count()+dzjk_ShenqingWuZi.getTcount());
						
						DZJK_WuZi_Baofei baofei = new DZJK_WuZi_Baofei();
						baofei.setDescr(dealWuZi.getDescr());
						baofei.setName(dealWuZi.getName());
						baofei.setTcount(dzjk_ShenqingWuZi.getTcount());
						baofei.setWuziCode(dzjk_ShenqingWuZi.getWuziCode());
						baofei.setWuziTypeId(dealWuZi.getWuziTypeId());
						baofei.setWuziTypeName(dealWuZi.getWuziTypeName());
						service.saveOrUpdate(dealWuZi);
						
						int shengyuNum = dealWuZi.getTcount();
						int tipsNum = dealWuZi.getTipNum();
						if(shengyuNum<=tipsNum){
							map.put("tipsNum", shengyuNum+"");
						}else{
							map.put("tipsNum", 0+"");
						}
					}
					//之前的记录失效
					hql = "from DZJK_PersionWuZi where id=("+dzjk_ShenqingWuZi.getBghid()+")";
					list = service.listByHql(hql, 0, 2);
					DZJK_PersionWuZi persionWuZi = (DZJK_PersionWuZi)list.get(0);
					persionWuZi.setStatus(0);
					service.saveOrUpdate(persionWuZi);
					
					
					dzjk_ShenqingWuZi.setStatus(1);
					dzjk_ShenqingWuZi.setManyidu(1);
					service.saveOrUpdate(dzjk_ShenqingWuZi);
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
	 * 满意度调查
	 */
	public void wuziShenqingManyidu() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String ids = request.getParameter("ids");
				String manyidu = request.getParameter("manyidu");
				String manyidu_note = request.getParameter("manyidu_note");
				
				String hql = "from DZJK_ShenqingWuZi where id in("+ids+")";
				List<Object> list = service.listByHql(hql, 0, ids.split(",").length);
				for(Object tmp:list){
					DZJK_ShenqingWuZi dzjk_ShenqingWuZi = (DZJK_ShenqingWuZi)tmp;	
					
					//更新申请记录的状态
					dzjk_ShenqingWuZi.setManyidu(Integer.parseInt(manyidu));
					dzjk_ShenqingWuZi.setManyidu_note(manyidu_note);
					dzjk_ShenqingWuZi.setStatus(1);
					service.saveOrUpdate(dzjk_ShenqingWuZi);
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
	 * 个人资产申请撤销
	 */
	public void wuziShenqingCheXiao() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String uid = request.getSession().getAttribute("uid").toString();
				String ids = request.getParameter("ids");
				String status = request.getParameter("status");
				
				String hql = "from DZJK_ShenqingWuZi where id in("+ids+")";
				List<Object> list = service.listByHql(hql, 0, ids.split(",").length);
				for(Object tmp : list){
					DZJK_ShenqingWuZi dzjk_ShenqingWuZi = (DZJK_ShenqingWuZi) tmp;
					// 物资申请撤销
					if(dzjk_ShenqingWuZi.getStatus()==0){
						if(uid.equals(dzjk_ShenqingWuZi.getPersion_id()+"")){
							dzjk_ShenqingWuZi.setStatus(Integer.parseInt(status)); //本人撤销							
						}else{
							dzjk_ShenqingWuZi.setStatus(Integer.parseInt(status)); //审批人驳回							
						}
						service.saveOrUpdate(dzjk_ShenqingWuZi);
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
	 * 列出所有的申请信息 -- 个人的
	 */
	public void listShenqingAll() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String uid = request.getSession().getAttribute("uid").toString();
				
				String hql = "From Org_User where id="+uid+" order by id";
				List<Object> list = service.listByHql(hql,0,1);
				Org_User org_User = (Org_User)list.get(0);
				
				
				hql = "From DZJK_ShenqingWuZi where persion_id="+uid+" and status not in(-1,-2,1) order by id";
				if(org_User.getType()==0 || org_User.getType()==1){
					hql = "From DZJK_ShenqingWuZi where status not in(-2,-1,1) order by id";					
				}
				list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				int count = list.size();
				
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
	 * 列出所有的申请信息 -- 个人的
	 */
	public void listPersonInfoAll() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String uid = request.getSession().getAttribute("uid").toString();
				
				String hql = "From Org_User where id="+uid+" order by id";
				List<Object> list = service.listByHql(hql,0,1);
				Org_User org_User = (Org_User)list.get(0);
				
				hql = "From DZJK_PersionWuZi where persion_id="+uid+" and status=1 order by id";
				if(org_User.getType()==0 || org_User.getType()==1){
					hql = "From DZJK_PersionWuZi where status=1 order by id";					
				}
				list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				int count = list.size();
				
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
	 * 个人资产报修申请
	 */
	public void wuziBaoXiuShenqing() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String person_wuzi_id = request.getParameter("person_wuzi_id");
				String baoyou_yuanyin = request.getParameter("baoyou_yuanyin");
				String status = request.getParameter("status");
				
				String hql = "from DZJK_PersionWuZi where id="+person_wuzi_id;
				List<Object> list = service.listByHql(hql, 0, 1);
				for(Object tmp : list){
					String uuid = UUIDUtils.nextCode();
					
					DZJK_PersionWuZi dzjk_PersionWuZi = (DZJK_PersionWuZi)tmp;
					//打补丁，添上报修人的位置
					Org_User org_User = (Org_User) service.listByHql("from Org_User where id="+dzjk_PersionWuZi.getPersion_id(), 0, 1).get(0);
					// 物资申请信息
					DZJK_WuZi_BaoXiu wuZi_BaoXiu = new DZJK_WuZi_BaoXiu();
					wuZi_BaoXiu.setBaoyou_yuanyin(baoyou_yuanyin);
					wuZi_BaoXiu.setDate_baoxiao(dateFormat.format(new Date()));
					wuZi_BaoXiu.setPersion_id(dzjk_PersionWuZi.getPersion_id());
					wuZi_BaoXiu.setPersion_name(org_User.getDescr());
					wuZi_BaoXiu.setPersion_gongwei(org_User.getGongwei());
					
					wuZi_BaoXiu.setPersion_zichan_id(dzjk_PersionWuZi.getId());
					wuZi_BaoXiu.setStatus(Integer.parseInt(status));
					wuZi_BaoXiu.setTcount(1);
					wuZi_BaoXiu.setType(dzjk_PersionWuZi.getType());
					wuZi_BaoXiu.setWuziDescr(dzjk_PersionWuZi.getWuziDescr());
					wuZi_BaoXiu.setWuziId(dzjk_PersionWuZi.getWuziId());
					wuZi_BaoXiu.setWuziName(dzjk_PersionWuZi.getWuziName());
					wuZi_BaoXiu.setWuziTypeId(dzjk_PersionWuZi.getWuziTypeId());
					wuZi_BaoXiu.setWuziTypeName(dzjk_PersionWuZi.getWuziTypeName());
					wuZi_BaoXiu.setYouxiaoqi(dzjk_PersionWuZi.getYouxiaoqi());
					wuZi_BaoXiu.setUuid(uuid);
				    service.saveOrUpdate(wuZi_BaoXiu);
				    
				    hql = "from DZJK_WuZi_BaoXiu where uuid='"+uuid+"'";
				    DZJK_WuZi_BaoXiu baoXiu = (DZJK_WuZi_BaoXiu) service.listByHql(hql, 0, 1).get(0);
				    Sys_Message message = new Sys_Message();
				    message.setMsgId(baoXiu.getId());
				    message.setType(DZJK_Config.msg_type_BaoXiu);
				    message.setTypeDesc(DZJK_Config.msg_type_BaoXiu_Desc);
				    message.setStatus(DZJK_Config.msg_status_BaoXiu_Create);
				    message.setCreateDate(dateFormat.format(new Date()));
				    message.setLevel(org_User.getYouxianji());
				    message.setUuid(uuid);
				    message.setApply_note(baoyou_yuanyin);
				    message.setApply_personName(org_User.getDescr());
				    message.setApply_personGongwei(org_User.getGongwei());
				    message.setApply_personPhone(org_User.getPhoneNum());
				    message.setApply_wuziMsg(baoXiu.getWuziDescr());
				    
				    service.saveOrUpdate(message);
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
	 * 资产报修受理
	 */
	public void wuziBaoXiuShouLi() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String baoxiuId = request.getParameter("baoxiuId");
				String weijianId = request.getParameter("weijianId");
				
				String hql = "from DZJK_WuZi_BaoXiu where id=("+baoxiuId+")";
				List<Object> list = service.listByHql(hql, 0, 2);
				DZJK_WuZi_BaoXiu wuZi_BaoXiu = (DZJK_WuZi_BaoXiu)list.get(0);
				
				hql = "from Org_User where id="+weijianId;
				list = service.listByHql(hql, 0, 2);
				Org_User org_User = (Org_User)list.get(0);
				
				
				wuZi_BaoXiu.setDate_shouli(dateFormat.format(new Date()));
				wuZi_BaoXiu.setDeal_personId(org_User.getId());
				wuZi_BaoXiu.setDeal_personName(org_User.getDescr());
				wuZi_BaoXiu.setStatus(2);
				service.saveOrUpdate(wuZi_BaoXiu);
				
				//修改消息为已处理
				hql = "from Sys_Message where uuid='"+wuZi_BaoXiu.getUuid()+"'";
				Sys_Message message = (Sys_Message) service.listByHql(hql, 0, 1).get(0);
			    message.setStatus(1);
			    service.saveOrUpdate(message);
				
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
	 * 显示报修Msg
	 */
	public void listBaoXiuMsg() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
 			response.setCharacterEncoding("utf-8");
			try {
				String status = request.getParameter("status");
				
				String hql = "From Sys_Message where status!=1 and status in("+status+") order by level";
				System.out.println(hql);
				List<Object> list = service.listByHql(hql,0,1000);
				if(list != null && list.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(list);
					String json = jsonArray.toString();
					json = json.substring(1,json.length()-1);
					response.getWriter().print(json);
					System.out.println(json);
				}
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
	 * 列出所有的报修申请信息 -- 个人信息界面
	 */
	public void listBaoXiuAll_Person() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String uid = request.getSession().getAttribute("uid").toString();
				
				String hql = "";
				hql = "From DZJK_WuZi_BaoXiu where persion_id="+uid+" and status!=1 order by id";
				List<Object> list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				int count = list.size();
				
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
	 * 列出所有的报修申请信息 -- 维修记录页面
	 */
	public void listBaoXiuAll() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String uid = request.getSession().getAttribute("uid").toString();
				
				String hql = "From Org_User where id="+uid+" order by id";
				List<Object> list = service.listByHql(hql,0,1);
				Org_User org_User = (Org_User)list.get(0);
				
				hql = "From DZJK_WuZi_BaoXiu where deal_personId="+uid+" and status!=1 order by id";
				if(org_User.getType()==DZJK_Config.user_type_BAOXIU_维护 || org_User.getType()==DZJK_Config.user_type_BAOXIU_审批){
					hql = "From DZJK_WuZi_BaoXiu where status=2 and deal_personId="+uid+" order by id";					
				}
				if(org_User.getType()==DZJK_Config.user_type_SUPER){
					hql = "From DZJK_WuZi_BaoXiu where status=2 order by id";
				}
				list = service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				int count = list.size();
				
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
	 * 个人资产报修撤销
	 */
	public void wuziBaoXiuCheXiao() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String uid = request.getSession().getAttribute("uid").toString();
				String ids = request.getParameter("ids");
				String status = request.getParameter("status");
				
				String hql = "from DZJK_WuZi_BaoXiu where id in("+ids+")";
				List<Object> list = service.listByHql(hql, 0, ids.split(",").length);
				for(Object tmp : list){
					DZJK_WuZi_BaoXiu wuZi_BaoXiu = (DZJK_WuZi_BaoXiu) tmp;
					// 报修申请撤销
					if(wuZi_BaoXiu.getStatus()==0){
						if(uid.equals(wuZi_BaoXiu.getPersion_id()+"")){
							wuZi_BaoXiu.setStatus(Integer.parseInt(status)); //本人撤销							
						}else{
							wuZi_BaoXiu.setStatus(Integer.parseInt(status)); //审批人驳回		
						}
						service.saveOrUpdate(wuZi_BaoXiu);
						//修改消息为已处理
						hql = "from Sys_Message where uuid='"+wuZi_BaoXiu.getUuid()+"'";
						Sys_Message message = (Sys_Message) service.listByHql(hql, 0, 1).get(0);
						message.setStatus(1);
						service.saveOrUpdate(message);
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
	 * 列出维护人员名单
	 */
	public void listBaoXiuWeiHuAll() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String type = request.getParameter("msg_type");
				String hql = "";
				if(type.equals("1")){
					hql = "From Org_User where type="+DZJK_Config.user_type_BAOXIU_维护+" order by id";
				}
				if(type.equals("2")){
					hql = "From Org_User where type="+DZJK_Config.user_type_WUZI_审批+" order by id";
				}
				List<Object> list = service.listByHql(hql,Integer.parseInt(first),Integer.parseInt(limit));
				int count = list.size();
				
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
	 * 定检人员设置
	 */
	public void personSetting() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String baoxiuId = request.getParameter("baoxiuId");
				String weijianId = request.getParameter("weijianId");
				
				String hql = "from DZJK_WuZi_BaoXiu where id=("+baoxiuId+")";
				List<Object> list = service.listByHql(hql, 0, 2);
				
				hql = "from Org_User where id="+weijianId;
				list = service.listByHql(hql, 0, 2);
				Org_User org_User = (Org_User)list.get(0);
				
				DZJK_WuZi_BaoXiu wuZi_BaoXiu = (DZJK_WuZi_BaoXiu)list.get(0);
				
				wuZi_BaoXiu.setDate_shouli(dateFormat.format(new Date()));
				wuZi_BaoXiu.setDeal_personId(org_User.getId());
				wuZi_BaoXiu.setStatus(2);
				service.saveOrUpdate(wuZi_BaoXiu);
				
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
	 * 资产报修受理
	 */
	public void wuziBaoWeiXiu() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String baoxiuId = request.getParameter("baoxiuId");
				String deal_note = request.getParameter("deal_note");
				
				String deal_personId = request.getSession().getAttribute("uid").toString();
				
				String hql = "from DZJK_WuZi_BaoXiu where id=("+baoxiuId+")";
				List<Object> list = service.listByHql(hql, 0, 2);
				DZJK_WuZi_BaoXiu wuZi_BaoXiu = (DZJK_WuZi_BaoXiu)list.get(0);
				
				wuZi_BaoXiu.setDate_wancheng(dateFormat.format(new Date()));
				wuZi_BaoXiu.setDeal_personId(Integer.parseInt(deal_personId));
				wuZi_BaoXiu.setStatus(3);
				wuZi_BaoXiu.setDeal_note(deal_note);
				service.saveOrUpdate(wuZi_BaoXiu);
				
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
	 * 满意度调查
	 */
	public void wuziBaoXiuManyidu() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String ids = request.getParameter("ids");
				String manyidu = request.getParameter("manyidu");
				String manyidu_note = request.getParameter("manyidu_note");
				
				String hql = "from DZJK_WuZi_BaoXiu where id in("+ids+")";
				List<Object> list = service.listByHql(hql, 0, ids.split(",").length);
				for(Object tmp:list){
					DZJK_WuZi_BaoXiu wuZi_BaoXiu = (DZJK_WuZi_BaoXiu)tmp;	
					
					//更新申请记录的状态
					wuZi_BaoXiu.setManyidu(Integer.parseInt(manyidu));
					wuZi_BaoXiu.setManyidu_note(manyidu_note);
					wuZi_BaoXiu.setStatus(1);
					service.saveOrUpdate(wuZi_BaoXiu);
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
	 * 列出所有的申请信息 -- 个人的
	 */
	public void listPersonInfoAll_infos() {
		boolean sessionCheck = SysUtil.sessionCheck(request, response);
		if(sessionCheck){
			response.setCharacterEncoding("utf-8");
			try {
				String first = request.getParameter("start");
				String limit = request.getParameter("limit");
				String wuziId = request.getParameter("wuziId");
				
				String hql = "select a.persion_id,a.wuziId,a.tcount,b.orgId,a.wuziName,b.username,a.wuziCode,b.orgName From DZJK_PersionWuZi a ,Org_User b where a.persion_id=b.id and a.wuziId='"+wuziId+"' and a.status=1 order by a.id";
				List<Object> list =  service.listByHql(hql,Integer.valueOf(first),Integer.valueOf(limit));
				int count = list.size();
				List<DZJK_PersionWuZi_Temp> list_res = new ArrayList<DZJK_PersionWuZi_Temp>();
				for(Object tmp:list){
					Object[] obj_arr = (Object[]) tmp;
					tmp = trimNull(obj_arr);
					DZJK_PersionWuZi_Temp wuzi_temp = new DZJK_PersionWuZi_Temp();
					
					wuzi_temp.setPersion_id(Type.getInteger(obj_arr[0].toString()));
					wuzi_temp.setWuziId(Type.getInteger(obj_arr[1].toString()));
					wuzi_temp.setTcount(Type.getInteger(obj_arr[2].toString()));
					wuzi_temp.setOrgId(Type.getInteger(obj_arr[3].toString()));
					
					wuzi_temp.setWuziName(obj_arr[4].toString());
					wuzi_temp.setUsername(obj_arr[5].toString());
					wuzi_temp.setWuziCode(obj_arr[6].toString());
					wuzi_temp.setOrgName(obj_arr[7].toString());
					
					list_res.add(wuzi_temp);
				}
				
				String json = "{";
				if(list_res != null && list_res.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(list_res);
					String temp = jsonArray.toString();
					json += "root:["+temp.substring(2, temp.length()-2)+"],totalProperty:"+list_res.size()+"";				
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
	private Object[] trimNull(Object[] arr){
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]==null){
				arr[i] = "";
			}
		}
		return arr;
	}
	
	
	/**
	 * 资产报修受理
	 */
	public void messageBoxDeal() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String msgId = request.getParameter("msgId");
				String msgType = request.getParameter("msgType");
				String msgDealPersionID = request.getParameter("msgDealPersionID");
				
				//如果是资产申请择修改资产申请记录的状态
				if(msgType.equals(DZJK_Config.msg_type_WuZiShenQing+"")){
					String hql = "from Sys_Message where id="+msgId;
					List<Object> list = service.listByHql(hql, 0, 2);
					Sys_Message message = (Sys_Message)list.get(0);
					
					hql = "from DZJK_ShenqingWuZi where uuid='"+message.getUuid()+"'";
					list = service.listByHql(hql, 0, 2);
					DZJK_ShenqingWuZi wuZi = (DZJK_ShenqingWuZi)list.get(0);
					
					message.setStatus(1);
					wuZi.setStatus(2);
					
					service.saveOrUpdate(message);
					service.saveOrUpdate(wuZi);
				}
				
				//如果是资产报修申请
				if(msgType.equals(DZJK_Config.msg_type_BaoXiu+"")){
					String hql = "from Sys_Message where id="+msgId;
					List<Object> list = service.listByHql(hql, 0, 2);
					Sys_Message message = (Sys_Message)list.get(0);
					message.setStatus(1);
					
					hql = "from DZJK_WuZi_BaoXiu where uuid='"+message.getUuid()+"'";
					list = service.listByHql(hql, 0, 2);
					DZJK_WuZi_BaoXiu wuZi_BaoXiu = (DZJK_WuZi_BaoXiu)list.get(0);
					
					hql = "from Org_User where id="+msgDealPersionID;
					list = service.listByHql(hql, 0, 2);
					Org_User org_User = (Org_User)list.get(0);
					
					wuZi_BaoXiu.setDate_shouli(dateFormat.format(new Date()));
					wuZi_BaoXiu.setDeal_personId(org_User.getId());
					wuZi_BaoXiu.setDeal_personName(org_User.getDescr());
					wuZi_BaoXiu.setStatus(2);
					
					service.saveOrUpdate(message);
					service.saveOrUpdate(wuZi_BaoXiu);
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
	 * 个人资产报修申请
	 */
	public void wuziBaoXiuShenqing_other() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String baoyou_yuanyin = request.getParameter("baoyou_yuanyin");
				String status = request.getParameter("status");
				
				String person_id = request.getSession().getAttribute("uid").toString();
				

				String uuid = UUIDUtils.nextCode();
				
				//打补丁，添上报修人的位置
				Org_User org_User = (Org_User) service.listByHql("from Org_User where id="+person_id, 0, 1).get(0);
				// 物资申请信息
				DZJK_WuZi_BaoXiu wuZi_BaoXiu = new DZJK_WuZi_BaoXiu();
				wuZi_BaoXiu.setBaoyou_yuanyin(baoyou_yuanyin);
				wuZi_BaoXiu.setDate_baoxiao(dateFormat.format(new Date()));
				wuZi_BaoXiu.setPersion_id(Type.getInteger(person_id));
				wuZi_BaoXiu.setPersion_name(org_User.getDescr());
				wuZi_BaoXiu.setPersion_gongwei(org_User.getGongwei());
				
				wuZi_BaoXiu.setStatus(Integer.parseInt(status));
				wuZi_BaoXiu.setTcount(1);
				//100000 非个人资产的物资类型
				wuZi_BaoXiu.setType(100000);
				wuZi_BaoXiu.setWuziDescr("不存在的物资");
				wuZi_BaoXiu.setWuziName("不存在的物资");
				wuZi_BaoXiu.setWuziTypeName("不存在的物资类型");
				wuZi_BaoXiu.setUuid(uuid);
			    service.saveOrUpdate(wuZi_BaoXiu);
			    
			    String hql = "from DZJK_WuZi_BaoXiu where uuid='"+uuid+"'";
			    DZJK_WuZi_BaoXiu baoXiu = (DZJK_WuZi_BaoXiu) service.listByHql(hql, 0, 1).get(0);
			    Sys_Message message = new Sys_Message();
			    message.setMsgId(baoXiu.getId());
			    message.setType(DZJK_Config.msg_type_BaoXiu);
			    message.setTypeDesc(DZJK_Config.msg_type_BaoXiu_Desc);
			    message.setStatus(DZJK_Config.msg_status_BaoXiu_Create);
			    message.setCreateDate(dateFormat.format(new Date()));
			    message.setLevel(org_User.getYouxianji());
			    message.setUuid(uuid);
			    message.setApply_note(baoyou_yuanyin);
			    message.setApply_personName(org_User.getDescr());
			    message.setApply_personGongwei(org_User.getGongwei());
			    message.setApply_personPhone(org_User.getPhoneNum());
			    message.setApply_wuziMsg(baoXiu.getWuziDescr());
			    
			    service.saveOrUpdate(message);
							
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
	 * 列出维修人正在处理的任务
	 */
	public void listRunningWorks(){
		response.setCharacterEncoding("utf8");
		List<DZJK_RunningWorks> works = new ArrayList<DZJK_RunningWorks>();
		String sql = "SELECT usr.id, usr.descr, usr.phoneNum, bx.date_shouli, bx.persion_id, bx.persion_name, bx.persion_gongwei, bx.wuziName, bx.STATUS FROM dzjk_wuzi_baoxiu bx JOIN ls3x_org_user usr ON bx.deal_personId = usr.id WHERE usr.type IN (0, 3) AND bx. STATUS = 2";
		List<Object> list = service.listBySql(sql);
		for(Object tmp:list){
			Object[] obj_arr = (Object[]) tmp;
			tmp = trimNull(obj_arr);
			DZJK_RunningWorks work = new DZJK_RunningWorks();
			
			work.setWeixiu_person_id(obj_arr[0].toString());
			work.setWeixiu_persion_name(obj_arr[1].toString());
			work.setWeixiu_persion_phone(obj_arr[2].toString());
			work.setStart_time(obj_arr[3].toString());
			
			work.setBaoxiu_person_id(obj_arr[4].toString());
			work.setBaoxiu_persion_name(obj_arr[5].toString());
			work.setBaoxiu_persion_gongwei(obj_arr[6].toString());
			work.setBaoxiu_persion_wuziName(obj_arr[7].toString());
			
			work.setStatus(obj_arr[8].toString());
			works.add(work);
		}
		
		String json = "{";
		if(works != null && works.size() > 0) {
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(works);
			String temp = jsonArray.toString();
			json += "root:["+temp.substring(2, temp.length()-2)+"],totalProperty:"+works.size()+"";				
		}else {
			json += "root:[],totalProperty:"+works.size()+"";								
		}
		json += "}";
		try {
			System.out.println(json);
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
