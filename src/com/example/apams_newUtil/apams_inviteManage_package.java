package com.example.apams_newUtil;

import java.util.ArrayList;
import java.util.HashMap;


public class apams_inviteManage_package extends apams_network_package {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1766266076159097053L;
	private ArrayList<InviteInfo> inviteList;
	private HashMap<String,InviteInfo> inviteMap;

	public apams_inviteManage_package(String username,ArrayList<InviteInfo> invitelist, HashMap<String, InviteInfo> resultMap) {
		super(username, packageType.INVITEMANAGE);
		this.inviteList = invitelist;
		this.inviteMap = resultMap;
	}
	public ArrayList<InviteInfo> getInfo(){
		return this.inviteList;
	}
	public HashMap<String,InviteInfo> getMap() {
		return this.inviteMap;
	}

}
