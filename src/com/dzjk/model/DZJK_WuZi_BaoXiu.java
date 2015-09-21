package com.dzjk.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dzjk_wuzi_baoxiu")
public class DZJK_WuZi_BaoXiu implements Serializable {
	private static final long serialVersionUID = 3020597504908395252L;

	@Id
	@GeneratedValue
	private Integer id;
	private Integer persion_zichan_id;  //个人资产信息 id
	private Integer persion_id;         //个人的组织结构ID
	private String persion_name;       //个人的组织结构名字
	private String persion_gongwei;       //个人的组织结构名字

	private Integer wuziId;
	private String wuziName; 
	private String wuziDescr; 
	private String youxiaoqi;
	private Integer type;
	
	private Integer wuziTypeId;
	private String wuziTypeName;

	private Integer tcount;
	private String baoyou_yuanyin;
	private Integer status;
	
	private Integer manyidu;
	private String manyidu_note;
	
	private String deal_note;
	private Integer deal_personId;
	private String deal_personName;
	
	private String date_baoxiao;
	private String date_shouli;
	private String date_wancheng;
	
	private String uuid;
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPersion_gongwei() {
		return persion_gongwei;
	}
	public void setPersion_gongwei(String persion_gongwei) {
		this.persion_gongwei = persion_gongwei;
	}
	public String getDeal_personName() {
		return deal_personName;
	}
	public void setDeal_personName(String deal_personName) {
		this.deal_personName = deal_personName;
	}
	public String getPersion_name() {
		return persion_name;
	}
	public void setPersion_name(String persion_name) {
		this.persion_name = persion_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPersion_zichan_id() {
		return persion_zichan_id;
	}
	public void setPersion_zichan_id(Integer persion_zichan_id) {
		this.persion_zichan_id = persion_zichan_id;
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
	public String getYouxiaoqi() {
		return youxiaoqi;
	}
	public void setYouxiaoqi(String youxiaoqi) {
		this.youxiaoqi = youxiaoqi;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public String getBaoyou_yuanyin() {
		return baoyou_yuanyin;
	}
	public void setBaoyou_yuanyin(String baoyou_yuanyin) {
		this.baoyou_yuanyin = baoyou_yuanyin;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getDeal_note() {
		return deal_note;
	}
	public void setDeal_note(String deal_note) {
		this.deal_note = deal_note;
	}
	public Integer getDeal_personId() {
		return deal_personId;
	}
	public void setDeal_personId(Integer deal_personId) {
		this.deal_personId = deal_personId;
	}
	public String getDate_baoxiao() {
		return date_baoxiao;
	}
	public void setDate_baoxiao(String date_baoxiao) {
		this.date_baoxiao = date_baoxiao;
	}
	public String getDate_shouli() {
		return date_shouli;
	}
	public void setDate_shouli(String date_shouli) {
		this.date_shouli = date_shouli;
	}
	public String getDate_wancheng() {
		return date_wancheng;
	}
	public void setDate_wancheng(String date_wancheng) {
		this.date_wancheng = date_wancheng;
	}
}