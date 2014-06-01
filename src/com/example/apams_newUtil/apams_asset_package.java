package com.example.apams_newUtil;

public class apams_asset_package extends apams_network_package {
	private assetItem item;
	public apams_asset_package(String username,assetItem item) {
		super(username, packageType.ADDASSET);
		this.item = item;
	}
	
	public assetItem getItem(){
		return this.item;
	}

}
