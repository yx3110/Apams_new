package com.example.apams_newUtil;

public class apams_network_package_regisN extends apams_network_package {
	private String invite;
	private String time;
	public apams_network_package_regisN(String usernameStr,
			String password1Str, String cIDStr,String invite, String timeStamp) {
		super(usernameStr,password1Str,cIDStr,packageType.REGISTER_N);
		this.invite = invite;
		this.time = timeStamp;
	}
	public String getInvite(){
		return this.invite;
	}
	public String getTime(){
		return this.time;
	}

}
