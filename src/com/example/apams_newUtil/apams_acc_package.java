package com.example.apams_newUtil;

import android.graphics.Bitmap;

public class apams_acc_package extends apams_network_package {
	
	private int priority;
	private String belongto;
	private String CID;
	private byte[] pic;
	public apams_acc_package(String username,String CID,int priority,String belongto,byte[] profilepic) {
		super(username,CID,packageType.ACC);
		this.priority = priority;
		this.CID = CID;
		this.belongto = belongto;
		this.pic=profilepic;
	}
	public int getPriory(){
		return this.priority;
	}
	@Override
	public String getCID(){
		return this.CID;
	}
	public byte[] getPic(){
		return this.pic;
	}
	public String getBelongto(){
		return this.belongto;
	}

}
