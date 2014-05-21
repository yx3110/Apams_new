package com.example.apams_newUtil;

public class apams_network_package_regisN extends apams_network_package {
	private String invite;
	public apams_network_package_regisN(String usernameStr,
			String password1Str, String cIDStr,String invite) {
		super(usernameStr,password1Str,cIDStr,packageType.REGISTER_N);
		this.invite = invite;
	}
	public String getInvite(){
		return this.invite;
	}

}
