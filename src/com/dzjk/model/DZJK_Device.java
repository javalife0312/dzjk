package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_device")
public class DZJK_Device implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private Integer fid;
	private String position;
	private String name;
	private String descr;
	private Integer tipDays;
	private Integer executorId;
	private String executorName;
	private Integer status;
	
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTipDays() {
		return tipDays;
	}
	public void setTipDays(Integer tipDays) {
		this.tipDays = tipDays;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
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
	public Integer getExecutorId() {
		return executorId;
	}
	public void setExecutorId(Integer executorId) {
		this.executorId = executorId;
	}
	
}