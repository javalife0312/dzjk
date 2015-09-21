package com.dzjk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.dzjk.model.DZJK_Report_WeiXiu;
import com.dzjk.model.DZJK_Report_XunJian;
import com.opensymphony.xwork2.ActionSupport;
import com.template.service.Service;
import com.template.util.SysUtil;

public class DZJK_ReportAction extends ActionSupport {

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
	
	
	/**************************************************************************************
	 * 					维修记录统计	     													  *
	 *************************************************************************************/
	private String addSum(String num1,String num2){
		return "" + (Integer.parseInt(num1)+Integer.parseInt(num2));
	}
	private Object[] trimNull(Object[] arr){
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]==null){
				arr[i] = "";
			}
		}
		return arr;
	}
	public void weixiuTJ() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				String orgIg = request.getParameter("orgIg");
				String username = request.getParameter("username");
				String ids = service.listChildByFid("showtreenodes","ls3x_org_department", orgIg);
				
				List<DZJK_Report_WeiXiu> list_res = new ArrayList<DZJK_Report_WeiXiu>();
				DZJK_Report_WeiXiu huizong = new DZJK_Report_WeiXiu();
				huizong.setUsername("总计");
				huizong.setUserdesc("总计");
				huizong.setOrgName("总计");
				huizong.setZongji("0");
				huizong.setChulizhong("0");
				huizong.setDaipingji("0");
				huizong.setManyi1("0");
				huizong.setManyi2("0");
				huizong.setManyi3("0");
				
				
				String sql = "select " +
								"a.deal_personId," +
								"b.orgId," +
								"b.username," +
								"b.descr," +
								"b.orgName," +
								"count(a.id) as zongji," +
								"count(if(a.status=2,1,null)) as chulizhong," +
								"count(if(a.status=3,1,null)) as daipingji," +
								"count(if(a.manyidu=1,1,null)) as manyi1," +
								"count(if(a.manyidu=2,1,null)) as manyi2, " +
								"count(if(a.manyidu=3,1,null)) as manyi3 " +
							"from " +
								"dzjk_wuzi_baoxiu a " +
							"join " +
								"ls3x_org_user b " +
							"on " +
								"a.deal_personId=b.id " +
							"where 1=1 ";
							if(startDate!=null && !"".equals(startDate) && endDate!=null && !"".equals(endDate)){
								sql += " and substr(a.date_baoxiao,1,8) between '"+startDate+"' and '"+endDate+"' ";
							}
							if(orgIg!=null && !"".equals(orgIg)){
								sql += " and b.orgId in("+ids+")";
							}
							if(username!=null && !"".equals(username)){
								sql += " and b.descr='"+username+"'";
							}
							sql += 
							" group by " +
								"a.deal_personId,b.username,b.descr,b.orgName,b.orgId;";
							System.out.println(sql);
				List<Object> list = service.listBySql(sql);
				for(Object tmp:list){
					Object[] obj_arr = (Object[]) tmp;
					tmp = trimNull(obj_arr);
					DZJK_Report_WeiXiu dzjk_Report_WeiXiu = new DZJK_Report_WeiXiu();
					dzjk_Report_WeiXiu.setUsername(obj_arr[2].toString());
					dzjk_Report_WeiXiu.setUserdesc(obj_arr[3].toString());
					dzjk_Report_WeiXiu.setOrgName(obj_arr[4].toString());
					
					String zongji = obj_arr[5].toString();
					String chulizhong = obj_arr[6].toString();
					String daipingji = obj_arr[7].toString();
					String manyi1 = obj_arr[8].toString();
					String manyi2 = obj_arr[9].toString();
					String manyi3 = obj_arr[10].toString();
					
					dzjk_Report_WeiXiu.setZongji(zongji);
					dzjk_Report_WeiXiu.setChulizhong(chulizhong);
					dzjk_Report_WeiXiu.setDaipingji(daipingji);
					dzjk_Report_WeiXiu.setManyi1(manyi1);
					dzjk_Report_WeiXiu.setManyi2(manyi2);
					dzjk_Report_WeiXiu.setManyi3(manyi3);
					list_res.add(dzjk_Report_WeiXiu);
					
					huizong.setZongji(addSum(zongji,huizong.getZongji()));
					huizong.setChulizhong(addSum(chulizhong, huizong.getChulizhong()));
					huizong.setDaipingji(addSum(daipingji, huizong.getDaipingji()));
					huizong.setManyi1(addSum(manyi1, huizong.getManyi1()));
					huizong.setManyi2(addSum(manyi2, huizong.getManyi2()));
					huizong.setManyi3(addSum(manyi3, huizong.getManyi3()));
				}
				list_res.add(huizong);
				
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
			}
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
		}finally {}
	}
	
	/**************************************************************************************
	 * 					巡检记录统计	     													  *
	 *************************************************************************************/
	public void xunjianTJ() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			boolean sessionCheck = SysUtil.sessionCheck(request, response);
			if(sessionCheck){
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				String orgIg = request.getParameter("orgIg");
				String username = request.getParameter("username");
				String ids = service.listChildByFid("showtreenodes","ls3x_org_department", orgIg);
				
				List<DZJK_Report_XunJian> list_res = new ArrayList<DZJK_Report_XunJian>();
				DZJK_Report_XunJian huizong = new DZJK_Report_XunJian();
				huizong.setUsername("总计");
				huizong.setUserdesc("总计");
				huizong.setOrgName("总计");
				huizong.setZongji("0");
				huizong.setWeixunjian("0");
				huizong.setZhengchang("0");
				huizong.setYichang("0");
				
				
				String sql = "select " +
								"a.weijianId," +
								"b.orgId," +
								"b.username," +
								"b.descr," +
								"b.orgName," +
								"count(a.id) as zongji," +
								"count(if(a.deviceStatus=0,1,null)) as weixunjian," +
								"count(if(a.deviceStatus=1,1,null)) as zhengchang," +
								"count(if(a.deviceStatus=2,1,null)) as yichang " +
							"from " +
								"dzjk_device_task a " +
							"join " +
								"ls3x_org_user b " +
							"on " +
								"a.weijianId=b.id " +
							"where 1=1 ";
							if(startDate!=null && !"".equals(startDate) && endDate!=null && !"".equals(endDate)){
								sql += " and substr(a.taskDate,1,8) between '"+startDate+"' and '"+endDate+"' ";
							}
							if(orgIg!=null && !"".equals(orgIg)){
								sql += " and b.orgId in("+ids+")";
							}
							if(username!=null && !"".equals(username)){
								sql += " and b.descr='"+username+"'";
							}
							sql += 
							" group by " +
								"a.weijianId,b.username,b.descr,b.orgName,b.orgId;";
							System.out.println(sql);
				List<Object> list = service.listBySql(sql);
				for(Object tmp:list){
					Object[] obj_arr = (Object[]) tmp;
					tmp = trimNull(obj_arr);
					DZJK_Report_XunJian dzjk_Report_WeiXiu = new DZJK_Report_XunJian();
					dzjk_Report_WeiXiu.setUsername(obj_arr[2].toString());
					dzjk_Report_WeiXiu.setUserdesc(obj_arr[3].toString());
					dzjk_Report_WeiXiu.setOrgName(obj_arr[4].toString());
					
					String zongji = obj_arr[5].toString();
					String weixun = obj_arr[6].toString();
					String zhengchang = obj_arr[7].toString();
					String yichang = obj_arr[8].toString();
					
					dzjk_Report_WeiXiu.setZongji(zongji);
					dzjk_Report_WeiXiu.setWeixunjian(weixun);
					dzjk_Report_WeiXiu.setZhengchang(zhengchang);
					dzjk_Report_WeiXiu.setYichang(yichang);
					
					list_res.add(dzjk_Report_WeiXiu);
					
					huizong.setZongji(addSum(zongji,huizong.getZongji()));
					huizong.setWeixunjian(addSum(weixun, huizong.getWeixunjian()));
					huizong.setZhengchang(addSum(zhengchang, huizong.getZhengchang()));
					huizong.setYichang(addSum(yichang, huizong.getYichang()));
				}
				list_res.add(huizong);
				
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
			}
		} catch (Exception e) {
			map.put("success", "false");
			e.printStackTrace();
		}finally {}
	}
}
