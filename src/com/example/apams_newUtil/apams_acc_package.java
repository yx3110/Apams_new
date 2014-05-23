package com.example.apams_newUtil;

public class apams_acc_package extends apams_network_package {
	
	private String CID;
	private int priority;
	private String belongto;
	public apams_acc_package(String username,String CID,int priority,String belongto) {
		super(username,packageType.ACC);
		this.CID = CID;
		this.priority = priority;
		this.belongto = belongto;
	}
	public int getPriory(){
		return this.priority;
	}
	public String getCID(){
		return this.CID;
	}
	public String getBelongto(){
		return this.belongto;
	}

}
