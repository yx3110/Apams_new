package com.example.apams_newUtil;

import java.io.Serializable;

public class assetItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6941727429564381212L;
	private String itemName;
	private String type;
	private String building;
	private String room;
	private byte[] pic;
	private String QRString;
	private int itemlvl;
	private String database;
	private String time;
	private String id;
	private String updateTime;
	private String updater;

	public assetItem() {

	}

	public String getUpdater() {
		return this.updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getTime() {
		return this.time;
	}

	public String getItemType() {
		return this.type;
	}

	public String getBuilding() {
		return this.building;
	}

	public String getRoom() {
		return this.room;
	}

	public int getItemlvl() {
		return this.itemlvl;
	}

	public byte[] getPic() {
		return this.pic;
	}

	public String getQRString() {
		return this.QRString;
	}

	public void setItemName(String name) {
		this.itemName = name;
	}

	public void setItemType(String type) {
		this.type = type;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public void setItemlvl(int itemlvl) {
		this.itemlvl = itemlvl;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public void setQRString(String QRString) {
		this.QRString = QRString;
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setUpdateTime(String string) {
		this.updateTime = string;
	}
	public String getUpdateTime(){
		return this.updateTime;
	}
	@Override
	public String toString() {
		return "Name: "+this.getItemName()+",Belongs to database: "+this.database;
	}
}
