package com.example.apams_newUtil;

public class apams_network_package_create extends apams_network_package {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5237862608286593849L;
	private int maxLvl;
	private String dataName;
	public apams_network_package_create(String username,int maxLvl,String dataName) {
		super(username, packageType.CREATE);
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
