package com.dzjk.model;

public class DZJK_Report_XunJian implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String userdesc;
	private String orgName;
	private String zongji;  //接受的任务量
	private String weixunjian; //未巡检
	private String zhengchang; //正常
	private String yichang; //异常
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserdesc() {
		return userdesc;
	}
	public void setUserdesc(String userdesc) {
		this.userdesc = userdesc;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getZongji() {
		return zongji;
	}
	public void setZongji(String zongji) {
		this.zongji = zongji;
	}
	public String getWeixunjian() {
		return weixunjian;
	}
	public void setWeixunjian(String weixunjian) {
		this.weixunjian = weixunjian;
	}
	public String getZhengchang() {
		return zhengchang;
	}
	public void setZhengchang(String zhengchang) {
		this.zhengchang = zhengchang;
	}
	public String getYichang() {
		return yichang;
	}
	public void setYichang(String yichang) {
		this.yichang = yichang;
	}
	
}