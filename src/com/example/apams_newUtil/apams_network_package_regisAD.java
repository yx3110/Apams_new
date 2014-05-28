package com.example.apams_newUtil;

public class apams_network_package_regisAD extends apams_network_package {
	private String time;

	public apams_network_package_regisAD(String username, String password,
			String CID, String timeStamp) {
		super(username, password, CID, packageType.REGISTER_AD);
		this.time = timeStamp;
	}
	
	public String getTime(){
		return this.time;
	}

}
