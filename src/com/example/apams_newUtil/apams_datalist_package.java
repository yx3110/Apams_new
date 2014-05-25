package com.example.apams_newUtil;

public class apams_datalist_package extends apams_network_package {
	private String[] datalist;
	public apams_datalist_package(String username,String[] datalist) {
		super(username, packageType.DATALIST);
		this.datalist = datalist;
		// TODO Auto-generated constructor stub
	}
	
	public String[] getDatalist(){
		return datalist;
	}

}
