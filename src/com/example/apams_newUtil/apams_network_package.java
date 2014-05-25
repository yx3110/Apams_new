package com.example.apams_newUtil;

import java.io.Serializable;


public class apams_network_package implements Serializable {
	public enum packageType{
		REGISTER_AD,REGISTER_N,LOGIN,QUERY,CREATE, ACC, DATALIST
	}
	private packageType type;
	private String username;
	private String password;
	private String CID;
	
	public apams_network_package(String username,packageType type){
		this.username = username;
		this.type = type;
	}
	public apams_network_package(String username,String password,String CID,packageType type){
		this.type = type;
		this.username = username;
		this.password = password;
		this.CID = CID;
	}
	public apams_network_package(String mEmail, String mPassword,
			packageType login) {
		this.username =mEmail;
		this.password = mPassword;
		this.type = login;
	}
	public packageType getType(){
		return this.type;
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
