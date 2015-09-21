package com.template.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;

import com.dzjk.model.DZJK_WuZi;
import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Org_Department;
import com.template.model.Org_User;
import com.template.model.Org_ZhiWei;
import com.template.service.Service;
import com.template.util.SysUtil;

public class MyUpload extends ActionSupport {

	private static final long serialVersionUID = -8066307209543067006L;
	public HttpServletResponse response = ServletActionContext.getResponse();

	private File urlName;
	private SysUtil sysUtil;
	private Service service;

	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public File getUrlName() {
		return urlName;
	}

	public void setUrlName(File urlName) {
		this.urlName = urlName;
	}
	public SysUtil getSysUtil() {
		return sysUtil;
	}
	public void setSysUtil(SysUtil sysUtil) {
		this.sysUtil = sysUtil;
	}

	/**
	 * 上传用户信息
	 */
	public void upload_usr() {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = null;
		try {
			// 上传文件
			String upload_path = SysUtil.getPageProperty("upload_path");
			FileUtils.copyFile(urlName, new File(upload_path
					+ "/upload_usr.xls"));

			inputStream = new FileInputStream(new File(upload_path
					+ "/upload_usr.xls"));

			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				//row和cell都是从0开始
				HSSFRow row = (HSSFRow) rows.next();
				int rowNum = row.getRowNum();
				if(rowNum==0){
					continue;
				}
				String desc = row.getCell(0).toString();
				String usrname = row.getCell(1).getRichStringCellValue().getString();
				int type = (int) row.getCell(2).getNumericCellValue();
				String ip = row.getCell(3).getRichStringCellValue().getString();
				String mac = row.getCell(4).getRichStringCellValue().getString();
				String gongwei = row.getCell(5).getRichStringCellValue().getString();
				String phoneNum = row.getCell(6).toString();
				int orgId = (int) row.getCell(7).getNumericCellValue();
				int weizhiId = (int) row.getCell(8).getNumericCellValue();
				
				BigDecimal db = new BigDecimal(phoneNum);
				phoneNum = db.toPlainString();
				
				Org_User usr = new Org_User();
				String hql = "from Org_User where username='"+usrname+"'";
				List<Object> list = service.listByHql(     hql, 0, 1);
				//用户不存在才导入
				if(list==null || list.size()==0){
					hql = "from Org_Department where id="+orgId;
					list = service.listByHql(hql, 0, 1);
					if(list!=null && list.size()>0){
						Org_Department department = (Org_Department) list.get(0);
						
						hql = "from Org_ZhiWei where id="+weizhiId;
						list = service.listByHql(hql, 0, 1);
						if(list!=null && list.size()>0){
							Org_ZhiWei zhiwei = (Org_ZhiWei) list.get(0);
							
							usr.setDescr(desc);
							usr.setGongwei(gongwei);
							usr.setIp(ip);
							usr.setLevel(zhiwei.getId());
							usr.setLevelName(zhiwei.getName());
							usr.setMac(mac);
							usr.setOrgId(department.getId());
							usr.setOrgName(department.getName());
							usr.setPassword("123");
							usr.setPhoneNum(phoneNum);
							usr.setType(Integer.valueOf(type));
							usr.setUsername(usrname);
							usr.setYouxianji(zhiwei.getLevel());
							
							service.saveOrUpdate(usr);
						}
						
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				map.put("success", "true");
				String json = JSONObject.fromObject(map).toString();
				response.getWriter().print(json);
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 上传物资信息
	 */
	public void upload_wuzi() {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = null;
		try {
			// 上传文件
			String upload_path = SysUtil.getPageProperty("upload_path");
			FileUtils.copyFile(urlName, new File(upload_path
					+ "/upload_wuzi.xls"));
			
			inputStream = new FileInputStream(new File(upload_path
					+ "/upload_wuzi.xls"));
			
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				//row和cell都是从0开始
				HSSFRow row = (HSSFRow) rows.next();
				int rowNum = row.getRowNum();
				if(rowNum==0){
					continue;
				}
				
				int wuziTypeId = (int) row.getCell(0).getNumericCellValue();
				String wuziTypeName = row.getCell(1).toString();
				String name = row.getCell(2).toString();
				String descr = row.getCell(3).toString();
				
				int tcount = (int) row.getCell(4).getNumericCellValue();
				String youxiaoqi = row.getCell(5).toString();
				int tipDays = (int) row.getCell(6).getNumericCellValue();
				int tipNum = (int) row.getCell(7).getNumericCellValue();
				
				DZJK_WuZi wuzi = new DZJK_WuZi();
				String hql = "from DZJK_WuZi where name='"+name+"'";
				List<Object> list = service.listByHql(hql, 0, 1);
				//用户不存在才导入
				if(list==null || list.size()==0){
					//wuzi.setId(id);
					wuzi.setWuziTypeId(wuziTypeId);
					wuzi.setWuziTypeName(wuziTypeName);
					wuzi.setName(name);
					wuzi.setDescr(descr);
					wuzi.setTcount(tcount);
					wuzi.setYouxiaoqi(youxiaoqi);
					wuzi.setTipDays(tipDays);
					wuzi.setTipDays(tipDays);
					wuzi.setTipNum(tipNum);
					
					service.saveOrUpdate(wuzi);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				map.put("success", "true");
				String json = JSONObject.fromObject(map).toString();
				response.getWriter().print(json);
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
