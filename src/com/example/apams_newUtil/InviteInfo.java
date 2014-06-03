package com.example.apams_newUtil;

import java.io.Serializable;


public class InviteInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4702782612699173127L;
	private String code;
	private int level;
	private String belongto;
	private String time;
	private boolean activated;
	private String activated_by;
	private String id;
	
	public InviteInfo(){		
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getCode(){
		return this.code;
	}
	public void setLevel(int level){
		this.level = level;
	}
	public int getLevel(){
		return this.level;
	}
	public void setBelongto(String belongto){
		this.belongto = belongto;
	}
	public String getBelongto(){
		return this.belongto;
	}
	public void setTime(String time){
		this.time = time;
	}
	public String getTime(){
		return this.time;
	}
	public void setActivated(boolean a){
		this.activated = a;
	}
	public boolean getActivated(){
		return this.activated;
	}
	public void setActivatedBy(String acti){
		this.activated_by = acti;
	}
	public String getActivatedBy(){
		return this.activated_by;
	}
	public String getId(){
		return this.id;
	}
	@Override
	public String toString() {
		return "Code: "+this.code+",Belongs to database: "+this.belongto;
	}
		
}
