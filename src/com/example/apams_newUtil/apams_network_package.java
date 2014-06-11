package com.example.apams_newUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class apams_network_package implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -584169835216472545L;
	public enum packageType{
		REGISTER_AD, REGISTER_N, LOGIN, QUERY, CREATE, 
		ACC, DATALIST, PROFILE, INVITECREATE, INVITEMANAGE, ADDASSET, ASSETQUERY, ASSETRESULT, QRQUERY, QRRESULT, FINDPW, FRESHCHECK, QRUPDATE, REPORTMISS, REPORTBROKEN
	}
	private packageType type;
	private String username;
	private String password;
	private String CID;
	private String time;
	
	public apams_network_package(String username,packageType type){
		this.username = username;
		this.type = type;
		this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

	}
	public apams_network_package(String username,String password,String CID,packageType type){
		this.type = type;
		this.username = username;
		this.password = password;
		this.CID = CID;
		this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

	}
	public apams_network_package(String mEmail, String mPassword,
			packageType login) {
		this.username =mEmail;
		this.password = mPassword;
		this.type = login;
		this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	}
	public packageType getType(){
		return this.type;
	}
	public String getTime(){
		return this.time;
	}
	public void setType(packageType type){
		this.type = type;
	}
	public String getUsername(){
		return this.username;
	}
	public String getPassword(){
		return this.password;
	}
	public String getCID(){
		return this.CID;
	}

}
