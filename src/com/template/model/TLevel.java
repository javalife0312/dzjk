package com.template.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tlevel")
public class TLevel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer kid;
	public Integer getKid() {
		return kid;
	}
	public void setKid(Integer kid) {
		this.kid = kid;
	}
	private Integer id;
	private Integer nlevel;
	private String scort;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNlevel() {
		return nlevel;
	}
	public void setNlevel(Integer nlevel) {
		this.nlevel = nlevel;
	}
	public String getScort() {
		return scort;
	}
	public void setScort(String scort) {
		this.scort = scort;
	}
}