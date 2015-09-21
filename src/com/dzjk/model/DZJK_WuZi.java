package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_wuzi")
public class DZJK_WuZi implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String descr;
	private Integer wuziTypeId;
	private String wuziTypeName;
	private String youxiaoqi;
	private Integer tipDays;
	private Integer type;
	private Integer tipNum;
	
	private String firstRecordDate;
	private Integer tcount; //剩余数量
	private Integer t_all_count; //总数
	private Integer t_feiqi_count; //废弃数量
	private Integer t_fafang_count; //已发放数量
	
	
	public String getFirstRecordDate() {
		return firstRecordDate;
	}
	public void setFirstRecordDate(String firstRecordDate) {
		this.firstRecordDate = firstRecordDate;
	}
	public Integer getT_all_count() {
		return t_all_count;
	}
	public void setT_all_count(Integer t_all_count) {
		this.t_all_count = t_all_count;
	}
	public Integer getT_feiqi_count() {
		return t_feiqi_count;
	}
	public void setT_feiqi_count(Integer t_feiqi_count) {
		this.t_feiqi_count = t_feiqi_count;
	}
	public Integer getT_fafang_count() {
		return t_fafang_count;
	}
	public void setT_fafang_count(Integer t_fafang_count) {
		this.t_fafang_count = t_fafang_count;
	}
	public Integer getTipNum() {
		return tipNum;
	}
	public void setTipNum(Integer tipNum) {
		this.tipNum = tipNum;
	}
	public String getWuziTypeName() {
		return wuziTypeName;
	}
	public void setWuziTypeName(String wuziTypeName) {
		this.wuziTypeName = wuziTypeName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getWuziTypeId() {
		return wuziTypeId;
	}
	public void setWuziTypeId(Integer wuziTypeId) {
		this.wuziTypeId = wuziTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
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
}