package com.example.apams_newUtil;

public class apams_network_package_create extends apams_network_package {
	private int maxLvl;
	private String dataName;
	public apams_network_package_create(String username, String mPassword,int maxLvl,String dataName,
			packageType login) {
		super(username, mPassword, packageType.CREATE);
		this.maxLvl = maxLvl;
		this.dataName = dataName;
	}
	
	public int getMaxlvl(){
		return this.maxLvl;
	}
	public String getDataName(){
		return this.dataName;
	}

}
