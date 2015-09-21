package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_device_task")
public class DZJK_DeviceTask implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private Integer positionId;
	private String positionName;
	private Integer deviceId;
	private String deviceName;
	private Integer weijianId;
	private String weijianName;
	private Integer tipDays;
	private String updateTime;
	private String taskDate;
	private Integer status; //0-任务生成，1-已经巡检 
	private Integer deviceStatus;//0-异常，1-正常运行
	private String deal_note;
	
	public Integer getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public String getDeal_note() {
		return deal_note;
	}
	public void setDeal_note(String deal_note) {
		this.deal_note = deal_note;
	}
	public Integer getPositionId() {
		return positionId;
	}
	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getWeijianId() {
		return weijianId;
	}
	public void setWeijianId(Integer weijianId) {
		this.weijianId = weijianId;
	}
	public Integer getTipDays() {
		return tipDays;
	}
	public void setTipDays(Integer tipDays) {
		this.tipDays = tipDays;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getWeijianName() {
		return weijianName;
	}
	public void setWeijianName(String weijianName) {
		this.weijianName = weijianName;
	}
	
	
}