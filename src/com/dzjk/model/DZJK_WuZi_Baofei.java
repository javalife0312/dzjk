package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_wuzi_baofei")
public class DZJK_WuZi_Baofei implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String descr;
	private Integer wuziTypeId;
	private String wuziTypeName;
	private Integer tcount;
	private String wuziCode;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
}