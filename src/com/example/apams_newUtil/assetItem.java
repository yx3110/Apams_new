package com.example.apams_newUtil;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

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
	private String manufacturer;
	private String model;
	private ArrayList<String> extras;
	private boolean broken;
	private boolean missing;
	private sortBy sortBy;
	private byte[] locMap;

	public static enum sortBy {
		LOCATION, TYPE, BROKEN, MISSING, MANUFACTURER
	}

	public assetItem() {
		this.extras = new ArrayList<String>();
	}
	public byte[] getLocMap(){
		return this.locMap;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public boolean isBroken() {
		return this.broken;
	}

	public void setExtras(ArrayList<String> extras) {
		this.extras = extras;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}
	public sortBy getSortBy(){
		return this.sortBy;
	}
	public void setSortBy(sortBy sortBy){
		this.sortBy = sortBy;
	}

	public String getModel() {
		return this.model;
	}

	public ArrayList<String> getExtras() {
		return this.extras;
	}

	public void setManufacturer(String string) {
		this.manufacturer = string;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void addExtra(String extra) {
		this.extras.add(extra);
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

	public String getUpdateTime() {
		return this.updateTime;
	}

	@Override
	public String toString() {
		switch (this.getSortBy()) {
		case TYPE:
			return  "Type: "
					+ this.getItemType() + ",Name: " + this.getItemName() +",Level: " + this.getItemlvl();
		case LOCATION:
			return "Building:" + this.getBuilding() + ", Room:"
					+ this.getRoom() + ", Name:" + this.getItemName();
		case BROKEN:
			return "Name: " + this.getItemName() + ", Is Broken: "+ this.isBroken();
		case MISSING:
			return "Name: " + this.getItemName() + ", Is Missing: "+this.getMissing();
		case MANUFACTURER:
			return "Manufacturer:" + this.getManufacturer() + ", Model:"
					+ this.getModel() + ", Type:" + this.getItemType()
					+ ", Name" + this.getItemName();
		default:
			return null;
		}

	}

	public void setMissing(boolean boolean1) {
		this.missing = boolean1;

	}

	public boolean getMissing() {
		return this.missing;
	}


	public void setLocMap(byte[] byteArray) {
		this.locMap = byteArray;
	}
}
