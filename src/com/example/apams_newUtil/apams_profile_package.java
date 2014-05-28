package com.example.apams_newUtil;

import android.graphics.Bitmap;

public class apams_profile_package extends apams_network_package {
	private Bitmap pic;

	public apams_profile_package(String username,Bitmap pic) {
		super(username, packageType.PROFILE);
		this.pic = pic;
	}

	public Bitmap getPic() {
		return this.pic;
	}

}
