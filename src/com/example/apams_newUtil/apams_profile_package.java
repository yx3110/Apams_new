package com.example.apams_newUtil;

import android.graphics.Bitmap;

public class apams_profile_package extends apams_network_package {
	private byte[] pic;

	public apams_profile_package(String username,byte[] pic) {
		super(username, packageType.PROFILE);
		this.pic = pic;
	}

	public byte[] getPic() {
		return this.pic;
	}

}
