package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_shenqing_wuzi")
public class DZJK_ShenqingWuZi implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private Integer persion_id;
	private String persion_name;
	private String persion_phone;
	private String persion_orgName;
	private Integer tcount;

	private Integer wuziId;
	private String wuziName; 
	private String wuziDescr; 
	
	private Integer wuziTypeId;
	private String wuziTypeName;

	private String shenqingliyou; 
	private String youxiaoqi;
	
	private Integer manyidu;
	private String manyidu_note;
	private Integer status;
	private Integer applytype; //1 物资申请 2 更换 3 退还
	private Integer bghid;     //需要换新的个人资产ID
	private String wuziCode;
	private String uuid;  //用于区分是哪条消息
	   
	
//	if(value=='0') {
//		return "提交申请";
//	}
//	if(value=='-1') {
//		return "申请人撤销";
//	}
//	if(value=='-2') {
//		return "审批人驳回";
//	}
//	if(value=='1') {
//		return "已经完成";
//	}
//	if(value=='2') {
//		return "审批天通过-可以领取";
//	}
//	if(value=='3') {
//		return "已经发放-等待评论";
//	}
	
	public String getPersion_name() {
		return persion_name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPersion_orgName() {
		return persion_orgName;
	}
	public void setPersion_orgName(String persion_orgName) {
		this.persion_orgName = persion_orgName;
	}
	public String getWuziCode() {
		return wuziCode;
	}
	public void setWuziCode(String wuziCode) {
		this.wuziCode = wuziCode;
	}
	public Integer getBghid() {
		return bghid;
	}
	public void setBghid(Integer bghid) {
		this.bghid = bghid;
	}
	public String getPersion_phone() {
		return persion_phone;
	}
	public void setPersion_phone(String persion_phone) {
		this.persion_phone = persion_phone;
	}
	public Integer getApplytype() {
		return applytype;
	}
	public void setApplytype(Integer applytype) {
		this.applytype = applytype;
	}
	public void setPersion_name(String persion_name) {
		this.persion_name = persion_name;
	}
	public Integer getManyidu() {
		return manyidu;
	}
	public void setManyidu(Integer manyidu) {
		this.manyidu = manyidu;
	}
	public String getManyidu_note() {
		return manyidu_note;
	}
	public void setManyidu_note(String manyidu_note) {
		this.manyidu_note = manyidu_note;
	}
	public String getYouxiaoqi() {
		return youxiaoqi;
	}
	public void setYouxiaoqi(String youxiaoqi) {
		this.youxiaoqi = youxiaoqi;
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
	public String getShenqingliyou() {
		return shenqingliyou;
	}
	public void setShenqingliyou(String shenqingliyou) {
		this.shenqingliyou = shenqingliyou;
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
	public String getWuziName() {
		return wuziName;
	}
	public void setWuziName(String wuziName) {
		this.wuziName = wuziName;
	}
	public String getWuziDescr() {
		return wuziDescr;
	}
	public void setWuziDescr(String wuziDescr) {
		this.wuziDescr = wuziDescr;
	}
	public Integer getTcount() {
		return tcount;
	}
	public void setTcount(Integer tcount) {
		this.tcount = tcount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}