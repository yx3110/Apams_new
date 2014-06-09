package com.example.apams_newUtil;


public class apams_update_package extends apams_network_package {

	private byte[] newPic;
	private int newlvl;
	private String newBuilding;
	private String newRoom;
	public apams_update_package(String username) {
		super(username, packageType.QRUPDATE);
		// TODO Auto-generated constructor stub
	}
	
	public void setNewPic(byte[] newPic){
		this.newPic = newPic;
	}
	public void setNewLvl(int lvl){
		this.newlvl = lvl;
	}
	public void setNewBuilding(String building){
		this.newBuilding = building;
	}
	public void setRoom(String room){
		this.newRoom = room;
	}
	public byte[] getNewPic(){
		return this.newPic;
	}
	public String getNewBuilding(){
		return this.newBuilding;
	}
	public String getNewRoom(){
		return this.newRoom;
	}
	public int getNewLvl(){
		return this.newlvl;
	}
}
