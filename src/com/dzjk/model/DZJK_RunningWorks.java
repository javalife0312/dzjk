package com.dzjk.model;

import java.io.Serializable;

public class DZJK_RunningWorks implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	//维修人信息
	private String weixiu_person_id;
	private String weixiu_persion_name;
	private String weixiu_persion_phone;
	private String start_time;
		
	//报修人信息
	private String baoxiu_person_id;
	private String baoxiu_persion_name;
	private String baoxiu_persion_gongwei;
	private String baoxiu_persion_wuziName;
	
	//报修单状态
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWeixiu_person_id() {
		return weixiu_person_id;
	}
	public void setWeixiu_person_id(String weixiu_person_id) {
		this.weixiu_person_id = weixiu_person_id;
	}
	public String getWeixiu_persion_name() {
		return weixiu_persion_name;
	}
	public void setWeixiu_persion_name(String weixiu_persion_name) {
		this.weixiu_persion_name = weixiu_persion_name;
	}
	public String getWeixiu_persion_phone() {
		return weixiu_persion_phone;
	}
	public void setWeixiu_persion_phone(String weixiu_persion_phone) {
		this.weixiu_persion_phone = weixiu_persion_phone;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getBaoxiu_person_id() {
		return baoxiu_person_id;
	}
	public void setBaoxiu_person_id(String baoxiu_person_id) {
		this.baoxiu_person_id = baoxiu_person_id;
	}
	public String getBaoxiu_persion_name() {
		return baoxiu_persion_name;
	}
	public void setBaoxiu_persion_name(String baoxiu_persion_name) {
		this.baoxiu_persion_name = baoxiu_persion_name;
	}
	public String getBaoxiu_persion_gongwei() {
		return baoxiu_persion_gongwei;
	}
	public void setBaoxiu_persion_gongwei(String baoxiu_persion_gongwei) {
		this.baoxiu_persion_gongwei = baoxiu_persion_gongwei;
	}
	public String getBaoxiu_persion_wuziName() {
		return baoxiu_persion_wuziName;
	}
	public void setBaoxiu_persion_wuziName(String baoxiu_persion_wuziName) {
		this.baoxiu_persion_wuziName = baoxiu_persion_wuziName;
	}
}