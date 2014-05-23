package com.example.apams_newUtil;

public class apams_acc_package extends apams_network_package {
	
	private String CID;
	private int priority;
	public apams_acc_package(String username,String CID,int priority) {
		super(username,packageType.ACC);
		this.CID = CID;
		this.priority = priority;
	}

}
