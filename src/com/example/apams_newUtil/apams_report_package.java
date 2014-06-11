package com.example.apams_newUtil;

public class apams_report_package extends apams_network_package {

	private boolean value;

	public apams_report_package(String mEmail, String mPassword,boolean value,
			packageType login) {
		super(mEmail, mPassword, login);
		this.value = value;
		// TODO Auto-generated constructor stub
	}
	
	public boolean getBool(){
		return this.value;
	}

}
