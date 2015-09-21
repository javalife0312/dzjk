package com.template.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ls3x_sys_message")
public class Sys_Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	private Integer type;
	private String typeDesc;
	private Integer status;
	private Integer msgId;
	private String createDate;
	private String uuid;
	
	private Integer level;
	private String apply_note;
	private String apply_personName;
	private String apply_personGongwei;
	private String apply_personPhone;
	private String apply_wuziMsg;
	
	
	public String getApply_note() {
		return apply_note;
	}
	public void setApply_note(String apply_note) {
		this.apply_note = apply_note;
	}
	public String getApply_personName() {
		return apply_personName;
	}
	public void setApply_personName(String apply_personName) {
		this.apply_personName = apply_personName;
	}
	public String getApply_personGongwei() {
		return apply_personGongwei;
	}
	public void setApply_personGongwei(String apply_personGongwei) {
		this.apply_personGongwei = apply_personGongwei;
	}
	public String getApply_personPhone() {
		return apply_personPhone;
	}
	public void setApply_personPhone(String apply_personPhone) {
		this.apply_personPhone = apply_personPhone;
	}
	public String getApply_wuziMsg() {
		return apply_wuziMsg;
	}
	public void setApply_wuziMsg(String apply_wuziMsg) {
		this.apply_wuziMsg = apply_wuziMsg;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getMsgId() {
		return msgId;
	}
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}
	
	
}
