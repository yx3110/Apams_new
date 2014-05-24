package com.example.apams_newUtil;

public class apams_acc_package extends apams_network_package {
	
	private int priority;
	private String belongto;
	private String CID;
	public apams_acc_package(String username,String CID,int priority,String belongto) {
		super(username,CID,packageType.ACC);
		this.priority = priority;
		this.CID = CID;
		this.belongto = belongto;
	}
	public int getPriory(){
		return this.priority;
	}
	@Override
	public String getCID(){
		return CID;
	}

	public String getBelongto(){
		return this.belongto;
	}

}
