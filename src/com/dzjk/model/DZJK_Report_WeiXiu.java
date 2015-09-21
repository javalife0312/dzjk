package com.dzjk.model;

public class DZJK_Report_WeiXiu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String userdesc;
	private String orgName;
	private String zongji;  //接受的任务量
	private String chulizhong; //正在处理的任务
	private String daipingji; //处理完成等待评价的数量
	private String manyi1;    //非常满意数量
	private String manyi2;    //基本满意数量
	private String manyi3;    //非常不满意数量
	
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
	public String getChulizhong() {
		return chulizhong;
	}
	public void setChulizhong(String chulizhong) {
		this.chulizhong = chulizhong;
	}
	public String getDaipingji() {
		return daipingji;
	}
	public void setDaipingji(String daipingji) {
		this.daipingji = daipingji;
	}
	public String getManyi1() {
		return manyi1;
	}
	public void setManyi1(String manyi1) {
		this.manyi1 = manyi1;
	}
	public String getManyi2() {
		return manyi2;
	}
	public void setManyi2(String manyi2) {
		this.manyi2 = manyi2;
	}
	public String getManyi3() {
		return manyi3;
	}
	public void setManyi3(String manyi3) {
		this.manyi3 = manyi3;
	}
}