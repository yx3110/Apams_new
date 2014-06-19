package com.example.apams_newUtil;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.apams_newUtil.assetItem;;

public class apams_assetQuery_package extends apams_network_package {

	/**
	 * 
	 */
	private static final long serialVersionUID = 455991290752001493L;
	private ArrayList<assetItem> assetList;
	private HashMap<String, assetItem> assetMap;
	private String QRcode;
	private assetItem item;
	private boolean isAdmin;
	private String database;
	private assetItem.sortBy sortBy;

	public apams_assetQuery_package(String username, String QRcode,
			Boolean isAdmin) {
		super(username, packageType.QRQUERY);
		this.isAdmin = isAdmin;
		this.QRcode = QRcode;
	}

	public apams_assetQuery_package(String username, String database,
			assetItem.sortBy sortBy) {
		super(username, packageType.ASSETQUERY);
		this.database = database;
	}

	public apams_assetQuery_package(String username, assetItem item) {
		super(username, packageType.QRRESULT);
		this.item = item;
	}

	public apams_assetQuery_package(String username,
			ArrayList<assetItem> assetList, HashMap<String, assetItem> assetMap) {
		super(username, packageType.ASSETRESULT);
		this.assetList = assetList;
		this.assetMap = assetMap;
	}

	public assetItem getItem() {
		return this.item;
	}

	public ArrayList<assetItem> getItemList() {
		return this.assetList;
	}

	public HashMap<String, assetItem> getItemMap() {
		return this.assetMap;
	}

	public String getQR() {
		return this.QRcode;
	}

	public boolean isAdmin() {
		return this.isAdmin;
	}

	public String getDatabase() {
		return this.database;
	}

	public assetItem.sortBy getSortBy() {
		return this.sortBy;
	}

	public void setSortBy(assetItem.sortBy sortBy) {
		this.sortBy = sortBy;
	}

}
