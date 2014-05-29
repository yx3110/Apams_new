package com.example.apams_newUtil;

import java.util.LinkedList;


public class apams_inviteManage_package extends apams_network_package {
	
	private LinkedList<InviteInfo> inviteList;

	public apams_inviteManage_package(String username,LinkedList<InviteInfo>invitelist) {
		super(username, packageType.INVITEMANAGE);
		this.inviteList = inviteList;
	}
	public LinkedList<InviteInfo> getInfo(){
		return this.inviteList;
	}

}
