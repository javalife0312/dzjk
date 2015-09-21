package com.dzjk.model;

import java.io.Serializable;


public class DZJK_PersionWuZi_Temp implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	private String wuziName; 
	private String wuziCode;
	private String username;
	private String orgName;
	
	private Integer persion_id;
	private Integer wuziId;
	private Integer tcount;
	private int orgId;
	
	public Integer getPersion_id() {
		return persion_id;
	}
	public void setPersion_id(Integer persion_id) {
		this.persion_id = persion_id;
	}
	public Integer getWuziId() {
		return wuziId;
	}
	public void setWuziId(Integer wuziId) {
		this.wuziId = wuziId;
	}
	public String getWuziName() {
		return wuziName;
	}
	public void setWuziName(String wuziName) {
		this.wuziName = wuziName;
	}
	public Integer getTcount() {
		return tcount;
	}
	public void setTcount(Integer tcount) {
		this.tcount = tcount;
	}
	public String getWuziCode() {
		return wuziCode;
	}
	public void setWuziCode(String wuziCode) {
		this.wuziCode = wuziCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}