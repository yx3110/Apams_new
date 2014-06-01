package com.example.apams_newUtil;

public class assetItem {
	private String itemName;
	private String type;
	private String building;
	private String room;
	private byte[] pic;
	private String QRString;
	private int itemlvl;
	public assetItem(){
		
	}
	
	public String getItemName(){
		return this.itemName;
	}
	
	public String getItemType(){
		return this.type;
	}
	
	public String getBuilding(){
		return this.building;
	}
	
	public String getRoom(){
		return this.room;
	}
	
	public int getItemlvl(){
		return this.itemlvl;
	}
	public byte[] getPic(){
		return this.pic;
	}
	public String getQRString(){
		return this.QRString;
	}
	
	public void setItemName(String name){
		this.itemName = name;
	}
	public void setItemType(String type){
		this.type = type;
	}
	public void setBuilding(String building){
		this.building = building;
	}
	public void setRoom(String room){
		this.room = room;
	}
	public void setItemlvl(int itemlvl){
		this.itemlvl = itemlvl;
	}
	public void setPic(byte[] pic){
		this.pic = pic;
	}
	public void setQRString(String QRString){
		this.QRString = QRString;
	}
	
	
	
	
	
	

}
