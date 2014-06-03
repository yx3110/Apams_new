package com.example.apams_newUtil;


public class apams_inviteCreate_package extends apams_network_package {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4456660882540513101L;
	private String code;
	private String belongto;
	private int level;

	public apams_inviteCreate_package(String username, String code, String belongto, int level) {
		super(username, packageType.INVITECREATE);
		this.code = code;
		this.belongto = belongto;
		this.level = level;
	}
	public String getCode(){
		return code;
	}
	public String getBelongto(){
		return belongto;
	}
	public int getLevel(){
		return level;
	}

}
