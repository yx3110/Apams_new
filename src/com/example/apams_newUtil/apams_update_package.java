package com.example.apams_newUtil;

public class apams_update_package extends apams_network_package {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3893826563750163432L;
	private byte[] newPic;
	private int newlvl;
	private String newBuilding;
	private String newRoom;
	private boolean broken;
	private String database;
	private String itemName;

	public apams_update_package(String username) {
		super(username, packageType.QRUPDATE);
		this.newlvl = 0;
		this.newBuilding = null;
		this.newRoom = null;
	}

	public void setNewPic(byte[] newPic) {
		this.newPic = newPic;
	}

	public void setNewLvl(int lvl) {
		this.newlvl = lvl;
	}

	public void setNewBuilding(String building) {
		this.newBuilding = building;
	}

	public void setRoom(String room) {
		this.newRoom = room;
	}

	public byte[] getNewPic() {
		return this.newPic;
	}

	public String getNewBuilding() {
		return this.newBuilding;
	}

	public String getNewRoom() {
		return this.newRoom;
	}

	public int getNewLvl() {
		return this.newlvl;
	}

	public void setBroken(boolean b) {
		this.broken = b;
	}

	public boolean isBroken() {
		return this.broken;
	}

	public void setDatabase(String database) {
		// TODO Auto-generated method stub
		this.database = database;

	}

	public String getDatabase() {
		return this.database;
	}

	public void setItemName(String itemName) {
		// TODO Auto-generated method stub
		this.itemName = itemName;

	}

	public String getItemName() {
		return this.itemName;
	}
}
