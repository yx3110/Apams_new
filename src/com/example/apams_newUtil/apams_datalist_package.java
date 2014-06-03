package com.example.apams_newUtil;

import java.util.ArrayList;

public class apams_datalist_package extends apams_network_package {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5651418393757086094L;
	private ArrayList<String> datalist;
	private ArrayList<String> resultLVL;
	public apams_datalist_package(String username,ArrayList<String> datalist, ArrayList<String> resultLVL) {
		super(username, packageType.DATALIST);
		this.datalist = datalist;
		this.resultLVL = resultLVL;
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<String> getDatalist(){
		return datalist;
	}
	public ArrayList<String> getLvllist(){
		return this.resultLVL;
	}

}
