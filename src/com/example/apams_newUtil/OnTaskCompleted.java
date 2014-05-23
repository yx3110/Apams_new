package com.example.apams_newUtil;

public interface OnTaskCompleted {
	
	void onTaskCompleted(String answer);
	
	void onPackReceived(apams_network_package pack);

	void popMsg(String string);

}
