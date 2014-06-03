package com.example.apams_newUtil;

public class apams_profile_package extends apams_network_package {
	/**
	 * 
	 */
	private static final long serialVersionUID = -542051879816188306L;
	private byte[] pic;

	public apams_profile_package(String username,byte[] pic) {
		super(username, packageType.PROFILE);
		this.pic = pic;
	}

	public byte[] getPic() {
		return this.pic;
	}

}
