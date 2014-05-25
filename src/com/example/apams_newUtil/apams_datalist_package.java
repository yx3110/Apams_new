package com.example.apams_newUtil;

import java.util.ArrayList;

public class apams_datalist_package extends apams_network_package {
	private ArrayList<String> datalist;
	public apams_datalist_package(String username,ArrayList<String> datalist) {
		super(username, packageType.DATALIST);
		this.datalist = datalist;
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<String> getDatalist(){
		return datalist;
	}

}
