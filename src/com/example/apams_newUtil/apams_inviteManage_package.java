package com.example.apams_newUtil;

import java.util.ArrayList;
import java.util.LinkedList;


public class apams_inviteManage_package extends apams_network_package {
	
	private ArrayList<InviteInfo> inviteList;

	public apams_inviteManage_package(String username,ArrayList<InviteInfo>invitelist) {
		super(username, packageType.INVITEMANAGE);
		this.inviteList = inviteList;
	}
	public ArrayList<InviteInfo> getInfo(){
		return this.inviteList;
	}

}
