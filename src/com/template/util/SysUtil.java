package com.template.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class SysUtil {
	
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * @param code
	 * @param ModelName
	 * @param column
	 * @return
	 */
	public boolean isCodeExit(String code,String ModelName,String column){
		boolean flag = true;
		try {
			String hql = "select count(id) from " + ModelName + " where " + column + " = '" + code + "'";
			String count = hibernateTemplate.find(hql).get(0).toString();
			if(Integer.parseInt(count) == 0){
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @param map
	 * @param param
	 * 判定修改
	 * @return
	 */
	public static void searchMap(Map<String, String> map,String key,Object param){
		if(param != null && !"".equals(param.toString())){
			map.put(key, param.toString());
		}
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * Session检测
	 */
	public static boolean sessionCheck(HttpServletRequest request,HttpServletResponse response){
		boolean flag = false;
		try {
			if(request != null){
				HttpSession session = request.getSession();
				if(session != null){
					Object sessionObject = session.getAttribute("uid");
					if(sessionObject != null){
						String uid = sessionObject.toString();
						if(uid != null && uid.length() > 0){
							flag = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	/**
	 * @param name
	 * @return
	 * 获取属性文件的值
	 */
	public static String getProperty(String name){
		String result = "";
		try {    
			InputStream in = SysUtil.class.getResourceAsStream("/system.properties");
			Properties p = new Properties();
			p.load(in);
			result = p.getProperty(name);
		} catch (IOException e) {    
			e.printStackTrace();
		 }    
		return result;
	}
	
	/**
	 * @param name
	 * @return
	 * 获取属性文件的值
	 */
	public static String getPageProperty(String key){
		String result = "";
		try {    
			InputStream in = SysUtil.class.getResourceAsStream("/system.properties");
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			result = properties.getProperty(key);
		} catch (IOException e) {    
			e.printStackTrace();
		}    
		return result;
	}
	/**
	 * @param name
	 * @return
	 * 获取属性文件的值
	 */
	public static void setPageProperty(String path,String key,String value){
		try {
			path += "WEB-INF/classes/";
			InputStream in = SysUtil.class.getResourceAsStream("/system.properties");
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			
			OutputStream outputStream = new FileOutputStream(new File(path+"page.properties"));
			properties.setProperty(key, value);
			properties.store(outputStream, null);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {    
			e.printStackTrace();
		 }    
	}
	
	public static void main(String[] args) {
		System.out.println(SysUtil.getPageProperty("type1_beizhu_x"));
		SysUtil.setPageProperty("","type1_beizhu_x","55");
		System.out.println(SysUtil.getProperty("type1_beizhu_x"));
	}
	
	/**
	 * @param msg
	 * @return
	 * 文字超过10个字换行
	 */
	public static String resetStr(String msg){
		int num = Integer.parseInt(SysUtil.getProperty("rownum"));
		String result = "";
		int len = msg.length();
		int start_index = 0;
		for(int i=0;i<len;i+=num){
			start_index = i;
			int end_index = start_index + num;
			if(end_index>=len){
				end_index = len;
			}
			result += msg.substring(start_index,end_index) + "<br/>";
		}
		return result;
	}
	
	/**
	 * @param msg
	 * @return
	 * 文字超过10个字换行
	 */
	public static String resetSJRNameStr(String msg){
		int num = Integer.parseInt(SysUtil.getProperty("type23_sjr_rownum"));
		String result = "";
		int len = msg.length();
		int start_index = 0;
		for(int i=0;i<len;i+=num){
			start_index = i;
			int end_index = start_index + num;
			if(end_index>=len){
				end_index = len;
			}
			result += msg.substring(start_index,end_index) + "<br/>";
		}
		return result;
	}
	/**
	 * @param msg
	 * @return
	 * 文字超过10个字换行
	 */
	public static String resetType1BeizhuStr(String msg){
		int num = Integer.parseInt(SysUtil.getProperty("type1_beizhu_rownum"));
		String result = "";
		int len = msg.length();
		int start_index = 0;
		for(int i=0;i<len;i+=num){
			start_index = i;
			int end_index = start_index + num;
			if(end_index>=len){
				end_index = len;
			}
			result += msg.substring(start_index,end_index) + "<br/>";
		}
		return result;
	}
	
	/**
	 * @param num
	 * @return
	 * 根据数字填写中文
	 */
	public static String numToCN(String num){
		String result = "";
		if(num.equals("1")){
			result = "壹";
		}else if(num.equals("2")){
			result = "贰";
		}else if(num.equals("3")){
			result = "叁";
		}else if(num.equals("4")){
			result = "肆";
		}else if(num.equals("5")){
			result = "伍";
		}else if(num.equals("6")){
			result = "陆";
		}else if(num.equals("7")){
			result = "柒";
		}else if(num.equals("8")){
			result = "捌";
		}else if(num.equals("9")){
			result = "玖";
		}else {
			result = "零";
		}
		return result;
	}
	
	/**
	 * @param month
	 * @return
	 * 获取指定月的开始到结束的所日期
	 */
	public static List<String> getDaysOrMontOfDate(String tjDate,String type){
		List<String>  result = new ArrayList<String>();
		if(type.equals("month")){
			result.add(tjDate + "-01");
			result.add(tjDate + "-02");
			result.add(tjDate + "-03");
			result.add(tjDate + "-04");
			result.add(tjDate + "-05");
			result.add(tjDate + "-06");
			result.add(tjDate + "-07");
			result.add(tjDate + "-08");
			result.add(tjDate + "-09");
			result.add(tjDate + "-10");
			result.add(tjDate + "-11");
			result.add(tjDate + "-12");
		}
		if(type.equals("day")){
			List<String> list = getStart_EndOfMonth(tjDate);
			String start = list.get(0);
			String end = list.get(1);
			result.add(start);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			boolean flag = true;
			while(flag){
				try {
					Calendar calendar = Calendar.getInstance();
					Date tmpDate = dateFormat.parse(start);
					long time = tmpDate.getTime() + 24*60*60*1000;
					calendar.setTimeInMillis(time);
					String nextDay = dateFormat.format(calendar.getTime());
					if(nextDay.compareTo(end)>=0){
						flag = false;
					}else{
						result.add(nextDay);
					}
					start = nextDay;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			result.add(end);
		}
		
		return result;
		
	}
	/**
	 * @param month
	 * @return
	 * 获取指定月的开始和结束日期
	 * 规定:如果开始日期不是1号,跨月
	 */
	public static List<String> getStart_EndOfMonth(String month){
		List<String> list = new ArrayList<String>();
		String startDay = "01";
		String endDay = "31";
		try {
			if(!startDay.equals("01")){
				Calendar calendar = Calendar.getInstance();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date thisMonth = dateFormat.parse(month+"-01");
				long time = thisMonth.getTime() - 1;
				calendar.setTimeInMillis(time);
				String lasthMothLastDay = dateFormat.format(calendar.getTime());
				startDay = lasthMothLastDay.substring(0,8) + startDay;
			}else{
				startDay = month +"-"+ startDay;
			}
			//终止日期
			endDay = month + "-" + endDay;
			//获取本月最后一天
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date thisMonth = dateFormat.parse(month+"-01");
			calendar.setTime(thisMonth);
			int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		    calendar.add(Calendar.DAY_OF_MONTH, days-1);
		    String lastDate = dateFormat.format(calendar.getTime());
			if(endDay.compareTo(lastDate)>=0){
				endDay = lastDate;
			}
			list.add(startDay);
			list.add(endDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * @return
	 * 登录时判定软件使用期限检测
	 * 默认C:\Windows\System32\check.properties 中写入系统的安装日期和可使用天数,及其剩余使用天数
	 * 如果使用天数变为0则不能再登录使用
	 * loginDate 登录日期 格式2014-02-07
	 * leaveDays 剩余天数
	 */
	public static boolean loginGuoQiCheck(){
		boolean flag = false;
		Properties properties = new Properties();
		//String filePath = "C:\\Windows\\System32\\check.properties";
		String filePath = "C:\\check.properties";
		String systemDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		try {
			File file = new File(filePath);
			System.out.println("判定文件是否存在...........");
			if(file.exists()){
				InputStream inputStream = new FileInputStream(new File(filePath));
				properties.load(inputStream);
				inputStream.close(); // 关闭流
				String loginDate = properties.getProperty("loginDate");
				Integer leaveDays = Integer.parseInt(properties.getProperty("leaveDays"));
				System.out.println(loginDate + "--------------" + leaveDays);
				if(leaveDays>=1){
					if(!systemDate.equals(loginDate)){
						synchronized (systemDate) {
							OutputStream outputStream = new FileOutputStream(filePath);
							properties.setProperty("loginDate", systemDate);
							properties.setProperty("leaveDays", ""+(leaveDays-1));
							properties.store(outputStream,null);
							outputStream.close();
							flag = true;
						}
					}else{
						flag = true;
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
}
