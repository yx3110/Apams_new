package com.example.apams_newUtil;

public class apams_network_package_regisAD extends apams_network_package {
	private String dataName;
	private int maxLvl;
	public apams_network_package_regisAD(String username, String password,
			String CID,String dataName,int maxLvl) {
		super(username, password, CID, packageType.REGISTER_AD);
		this.dataName = dataName;
		this.maxLvl = maxLvl;
	}
	
	public String getDataName(){
		return dataName;
	}
	public int getMaxLvl(){
		return maxLvl;
	}

}
