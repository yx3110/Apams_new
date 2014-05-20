package com.example.apams_newUtil;

import java.io.Serializable;


public class apams_network_package implements Serializable {
	public enum packageType{
		REGISTER,LOGIN,QUERY,IMAGE
	}
	private packageType type;
	private String username;
	private String password;
	private String CID;
	public apams_network_package(String username,String password,String CID,packageType type){
		this.username = username;
		this.password = password;
		this.CID = CID;
		this.type = type;
	}
	
	public apams_network_package(String username,String password,packageType type){
		this.username = username;
		this.password = password;
		this.type = type;
	}
	
	public packageType getType(){
		return this.type;
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
