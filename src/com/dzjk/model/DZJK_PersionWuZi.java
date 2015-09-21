package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_persion_wuzi")
public class DZJK_PersionWuZi implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private Integer persion_id;
	private String persion_name;
	private Integer wuziId;
	private String wuziName; 
	private String wuziDescr; 
	private Integer wuziTypeId;
	private String wuziTypeName; 
	private String wuziTypeDescr; 
	private Integer tcount;
	private String youxiaoqi;
	private Integer tipDays;
	private String wuziCode;
	private Integer type;  //物资的类型  软件 / 硬件  / 等(目前仅有软件和硬件)
	private Integer status; //1 有效 0 无效
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPersion_name() {
		return persion_name;
	}
	public void setPersion_name(String persion_name) {
		this.persion_name = persion_name;
	}
	public String getWuziCode() {
		return wuziCode;
	}
	public void setWuziCode(String wuziCode) {
		this.wuziCode = wuziCode;
	}
	public Integer getWuziTypeId() {
		return wuziTypeId;
	}
	public void setWuziTypeId(Integer wuziTypeId) {
		this.wuziTypeId = wuziTypeId;
	}
	public String getWuziTypeName() {
		return wuziTypeName;
	}
	public void setWuziTypeName(String wuziTypeName) {
		this.wuziTypeName = wuziTypeName;
	}
	public String getWuziTypeDescr() {
		return wuziTypeDescr;
	}
	public void setWuziTypeDescr(String wuziTypeDescr) {
		this.wuziTypeDescr = wuziTypeDescr;
	}
	public String getWuziDescr() {
		return wuziDescr;
	}
	public void setWuziDescr(String wuziDescr) {
		this.wuziDescr = wuziDescr;
	}
	public String getWuziName() {
		return wuziName;
	}
	public void setWuziName(String wuziName) {
		this.wuziName = wuziName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Integer getTcount() {
		return tcount;
	}
	public void setTcount(Integer tcount) {
		this.tcount = tcount;
	}
	public String getYouxiaoqi() {
		return youxiaoqi;
	}
	public void setYouxiaoqi(String youxiaoqi) {
		this.youxiaoqi = youxiaoqi;
	}
	public Integer getTipDays() {
		return tipDays;
	}
	public void setTipDays(Integer tipDays) {
		this.tipDays = tipDays;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}