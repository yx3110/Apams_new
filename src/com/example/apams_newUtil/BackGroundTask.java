package com.example.apams_newUtil;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package.packageType;

public class BackGroundTask extends Thread {
	private ServerSocket ssocket;
	private Connection conn;

	public BackGroundTask(ServerSocket ssocket, Connection conn) {
		this.conn = conn;
		this.ssocket = ssocket;
	}

	public void run() {

		try {
			Socket socket = ssocket.accept();

			InputStream inputS = socket.getInputStream();
			ObjectInputStream OinputS = new ObjectInputStream(inputS);

			OutputStream outputs = socket.getOutputStream();
			ObjectOutputStream oOutputs = new ObjectOutputStream(outputs);

			BufferedWriter StrOut = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			while (true) {
				try {
					apams_network_package pack = (apams_network_package) OinputS
							.readObject();
					System.out.println("package got");

					String username = pack.getUsername();
					String password = pack.getPassword();
					String CID = pack.getCID();
					String replyStr = null;
					String time = pack.getTime();

					PreparedStatement insertpst;

					switch (pack.getType()) {

					case REPORTMISS:
						System.out.println("Package type = " + pack.getType());
						apams_report_package rpack = (apams_report_package) pack;
						String missName = username;
						String missDatabase = password;
						boolean bool = rpack.getBool();
						String missQuery = "UPDATE " + missDatabase
								+ " SET missing = ? WHERE name = ?";
						try {
							PreparedStatement missPst = conn
									.prepareStatement(missQuery);
							missPst.setBoolean(1, bool);
							missPst.setString(2, missName);
							missPst.executeUpdate();
							missPst.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						StrOut.write("MISSREPORTED");
						StrOut.flush();
						StrOut.close();
						run();
						break;
					case QRUPDATE:
						System.out.println("Package type = " + pack.getType());
						apams_update_package uppack = (apams_update_package) pack;
						String updateDatabase = uppack.getDatabase();
						String itemName = uppack.getItemName();
						if (uppack.getNewBuilding() != null) {
							String QRupdateQuery = "UPDATE " + updateDatabase
									+ " SET building = ? WHERE name = ?";
							try {
								PreparedStatement uppst = conn
										.prepareStatement(QRupdateQuery);
								uppst.setString(1, uppack.getNewBuilding());
								uppst.setString(2, itemName);
								uppst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (uppack.getNewRoom() != null) {
							String QRupdateQuery = "UPDATE " + updateDatabase
									+ " SET room = ? WHERE name = ?";
							try {
								PreparedStatement uppst = conn
										.prepareStatement(QRupdateQuery);
								uppst.setString(1, uppack.getNewRoom());
								uppst.setString(2, itemName);
								uppst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (uppack.getNewLvl() != 0) {
							String QRupdateQuery = "UPDATE " + updateDatabase
									+ " SET assetlvl = ? WHERE name = ?";
							try {
								PreparedStatement uppst = conn
										.prepareStatement(QRupdateQuery);
								uppst.setInt(1, uppack.getNewLvl());
								uppst.setString(2, itemName);
								uppst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						String QRupdateQuery = "UPDATE " + updateDatabase
								+ " SET broken = ? WHERE name = ?";
						try {
							PreparedStatement uppst = conn
									.prepareStatement(QRupdateQuery);
							uppst.setBoolean(1, uppack.isBroken());
							uppst.setString(2, itemName);
							uppst.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						StrOut.write("UPDATEDONE");
						StrOut.flush();
						StrOut.close();
						break;
					case FINDPW:
						System.out.println("Package type = " + pack.getType());
						String pwQuery = "SELECT password FROM user_information WHERE username = ? AND cid = ?";
						try {
							PreparedStatement pwpst = conn
									.prepareStatement(pwQuery);
							pwpst.setString(1, username);
							pwpst.setString(2, password);
							ResultSet rs = pwpst.executeQuery();
							String result = null;
							while (rs.next()) {
								result = rs.getString("password");
							}
							System.out.print(result);
							apams_network_package resultpack = new apams_network_package(
									result, packageType.FINDPW);
							oOutputs.writeObject(resultpack);
							System.out.println("return package sent");

							oOutputs.flush();
							StrOut.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						run();
						break;
					case ASSETQUERY:
						System.out.println("Package type = " + pack.getType());
						apams_assetQuery_package AQpack = (apams_assetQuery_package) pack;
						String database = AQpack.getDatabase();

						String lvlQuery = "SELECT priority FROM user_information WHERE username = ?";
						int prioritylvl = 0;
						try {
							PreparedStatement lvlpst = conn
									.prepareStatement(lvlQuery);
							lvlpst.setString(1, username);
							ResultSet rs = lvlpst.executeQuery();
							while (rs.next()) {
								prioritylvl = rs.getInt("priority");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println(database);
						String assetQuery = "SELECT * FROM " + database
								+ " WHERE assetlvl <= ?";
						ArrayList<assetItem> AQList = new ArrayList<assetItem>();
						HashMap<String, assetItem> AQMap = new HashMap<String, assetItem>();
						int AQid = 1;
						try {
							PreparedStatement AQpst = conn
									.prepareStatement(assetQuery);
							AQpst.setInt(1, prioritylvl);
							ResultSet rs = AQpst.executeQuery();
							while (rs.next()) {
								assetItem curItem = new assetItem();
								curItem.setBuilding(rs.getString("building"));
								curItem.setId(String.valueOf(AQid));
								curItem.setDatabase(database);
								curItem.setItemlvl(rs.getInt("assetlvl"));
								curItem.setItemName(rs.getString("name"));
								curItem.setItemType(rs.getString("type"));
								curItem.setPic(rs.getBytes("img"));
								curItem.setRoom(rs.getString("room"));
								curItem.setTime(rs.getString("time"));
								curItem.setUpdateTime(rs
										.getString("lastupdatetime"));
								curItem.setQRString(rs.getString("qrstring"));
								curItem.setUpdater(rs.getString("lastUpdater"));
								curItem.setManufacturer(rs
										.getString("manufacturer"));
								curItem.setModel(rs.getString("model"));
								curItem.setBroken(rs.getBoolean("broken"));
								curItem.setMissing(rs.getBoolean("missing"));
								curItem.setExtras(new ArrayList<String>(Arrays
										.asList((String[]) rs
												.getArray("extras").getArray())));
								AQList.add(curItem);
								AQMap.put(String.valueOf(AQid), curItem);
								AQid++;
							}
							apams_network_package resultpack = new apams_assetQuery_package(
									username, AQList, AQMap);
							oOutputs.writeObject(resultpack);
							oOutputs.close();
							StrOut.close();
							run();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case QRQUERY:
						System.out.println("Package type = " + pack.getType());
						apams_assetQuery_package QRpack = (apams_assetQuery_package) pack;
						String QRcode = QRpack.getQR();
						String getdataQuery;
						int QRpriority = 0;
						ArrayList<String> databases = new ArrayList<String>();
						if (QRpack.isAdmin()) {
							getdataQuery = "SELECT name FROM databases WHERE owner = ?";
							try {
								PreparedStatement gdpst = conn
										.prepareStatement(getdataQuery);
								gdpst.setString(1, username);
								ResultSet rs = gdpst.executeQuery();
								while (rs.next()) {
									databases.add(rs.getString("name"));
								}
								QRpriority = 1000;
								gdpst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							getdataQuery = "SELECT belongto,priority FROM user_information WHERE username = ?";
							try {
								PreparedStatement gdpst = conn
										.prepareStatement(getdataQuery);
								gdpst.setString(1, username);
								ResultSet rs = gdpst.executeQuery();
								while (rs.next()) {
									QRpriority = rs.getInt("priority");
									databases.add(rs.getString("belongto"));
								}
								gdpst.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						for (int i = 0; i < databases.size(); i++) {
							String curdata = databases.get(i);
							String curQuery = "SELECT * FROM " + curdata
									+ " WHERE qrstring = ?";
							try {
								PreparedStatement qrpst = conn
										.prepareStatement(curQuery);
								qrpst.setString(1, QRcode);
								ResultSet rs = qrpst.executeQuery();
								assetItem asset = new assetItem();
								int curItemlvl = 0;
								if (!rs.isBeforeFirst()) {
									continue;
								} else {
									while (rs.next()) {
										asset.setBuilding(rs
												.getString("building"));
										asset.setDatabase(curdata);
										asset.setItemlvl(rs.getInt("assetlvl"));
										asset.setItemName(rs.getString("name"));
										asset.setItemType(rs.getString("type"));
										asset.setPic(rs.getBytes("img"));
										asset.setQRString(rs
												.getString("qrstring"));
										asset.setRoom(rs.getString("room"));
										asset.setTime(rs.getString("time"));
										asset.setUpdater(rs
												.getString("lastupdater"));
										asset.setUpdateTime(rs
												.getString("lastupdatetime"));
										asset.setManufacturer(rs
												.getString("manufacturer"));
										asset.setModel(rs.getString("model"));
										asset.setBroken(rs.getBoolean("broken"));
										curItemlvl = rs.getInt("assetlvl");
										asset.setMissing(rs
												.getBoolean("missing"));
										asset.setExtras(new ArrayList<String>(
												Arrays.asList((String[]) rs
														.getArray("extras")
														.getArray())));
									}

									if (QRpriority > curItemlvl) {
										String updateQuery = "UPDATE "
												+ curdata
												+ " SET lastupdatetime = ?, lastupdater = ? WHERE qrstring = ?";
										try {
											PreparedStatement updatepst = conn
													.prepareStatement(updateQuery);
											updatepst.setString(1, time);
											updatepst.setString(2, username);
											updatepst.setString(3, QRcode);
											updatepst.executeUpdate();
											updatepst.close();
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
									apams_network_package resultpack = new apams_assetQuery_package(
											username, asset);
									oOutputs.writeObject(resultpack);
								}
								oOutputs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						run();

						break;
					case ADDASSET:
						System.out.println("Package type = " + pack.getType());
						String dataBase = null;
						int userPriority = 0;
						String infoQuery = "SELECT belongto,priority from user_information where username = ?";
						try {
							PreparedStatement infopst = conn
									.prepareStatement(infoQuery);
							infopst.setString(1, username);
							ResultSet rs = infopst.executeQuery();
							while (rs.next()) {
								userPriority = rs.getInt("priority");
								dataBase = rs.getString("belongto");
								System.out.println("User found in database");
							}
							infopst.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						assetItem curItem = ((apams_assetAdd_package) pack)
								.getItem();
						if (userPriority == 1000) {
							dataBase = curItem.getDatabase();
						}
						if (userPriority < curItem.getItemlvl()) {
							replyStr = "PriorityTooLow";
							StrOut.write(replyStr);
							System.out.println("return package sent");

							StrOut.flush();
							StrOut.close();
							run();
						} else {
							ArrayList<String> extrasAL = curItem.getExtras();
							String[] stringExtras = new String[extrasAL.size()];
							stringExtras = extrasAL.toArray(stringExtras);
							String addQuery = "INSERT INTO " + dataBase
									+ "(name," + "building," + "room,"
									+ "type," + "img," + "assetlvl,"
									+ "qrstring," + "time," + "lastupdatetime,"
									+ "lastupdater," + "manufacturer,"
									+ "model," + "broken," + "extras,"
									+ "missing)"
									+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							try {
								Array curArray = conn.createArrayOf("text",
										stringExtras);
								PreparedStatement addpst = conn
										.prepareStatement(addQuery);
								addpst.setString(1, curItem.getItemName());
								addpst.setString(2, curItem.getBuilding());
								addpst.setString(3, curItem.getRoom());
								addpst.setString(4, curItem.getItemType());
								addpst.setBytes(5, curItem.getPic());
								addpst.setInt(6, curItem.getItemlvl());
								addpst.setString(7, curItem.getQRString());
								addpst.setString(8, pack.getTime());
								addpst.setString(9, pack.getTime());
								addpst.setString(10, username);
								addpst.setString(11, curItem.getManufacturer());
								addpst.setString(12, curItem.getModel());
								addpst.setBoolean(13, false);
								addpst.setArray(14, curArray);
								addpst.setBoolean(15, false);
								int result = addpst.executeUpdate();

								if (result != 0) {
									replyStr = "ASSETADDED";
								} else {
									replyStr = "ERROR";
								}

							} catch (SQLException e) {
								e.printStackTrace();
							}
							StrOut.write(replyStr);
							StrOut.flush();
							StrOut.close();
							run();
						}

						break;
					case INVITEMANAGE:
						System.out.println("Package type = " + pack.getType());
						String IMquery = "SELECT code,level,belongto,time,activated,activated_by FROM invitelevel where creator =?";
						try {
							PreparedStatement IMpst = conn
									.prepareStatement(IMquery);
							IMpst.setString(1, username);
							ResultSet rs = IMpst.executeQuery();
							ArrayList<InviteInfo> resultList = new ArrayList<InviteInfo>();
							HashMap<String, InviteInfo> resultMap = new HashMap<String, InviteInfo>();
							int id = 0;
							if (!rs.isBeforeFirst()) {
								InviteInfo info = new InviteInfo();
								info.setCode("NOINVITE");
								System.out.print("No invite found");
								resultList.add(info);
							} else {
								while (rs.next()) {
									InviteInfo info = new InviteInfo();
									info.setId(String.valueOf(id));
									info.setCode(rs.getString("code"));
									info.setBelongto(rs.getString("belongto"));
									info.setLevel(rs.getInt("level"));
									info.setTime(rs.getString("time"));
									info.setActivated(rs
											.getBoolean("activated"));
									if (rs.getString("activated_by") != null) {
										info.setActivatedBy(rs
												.getString("activated_by"));
									}
									resultMap.put(info.getId(), info);
									resultList.add(info);
									id++;
								}
							}
							System.out.println(resultList.size());
							apams_network_package returnPack = new apams_inviteManage_package(
									username, resultList, resultMap);

							oOutputs.writeObject(returnPack);
							System.out.println("return package sent");
							oOutputs.flush();
							IMpst.close();
							run();

						} catch (SQLException e) {
							System.out.println(e);
						}
						break;
					case INVITECREATE:
						System.out.println("Package type = " + pack.getType());
						apams_inviteCreate_package ICpack = (apams_inviteCreate_package) pack;
						String ICquery = "INSERT INTO invitelevel(code,level,belongto,time,activated,creator)"
								+ "VALUES(?,?,?,?,?,?)";
						String code = ICpack.getCode();
						int level = ICpack.getLevel();
						String belongto = ICpack.getBelongto();

						try {
							PreparedStatement ICpst = conn
									.prepareStatement(ICquery);
							ICpst.setString(1, code);
							ICpst.setInt(2, level);
							ICpst.setString(3, belongto);
							ICpst.setString(4, time);
							ICpst.setBoolean(5, false);
							ICpst.setString(6, username);
							int result = ICpst.executeUpdate();
							if (result != 0) {
								replyStr = "INVITED";
							} else {
								replyStr = "invite error";
							}
							StrOut.write(replyStr);
							StrOut.flush();
							StrOut.close();
							ICpst.close();
							run();
						} catch (SQLException e) {
							System.out.println(e);
						}
						System.out.println("return String sent");

						break;
					case PROFILE:
						System.out.println("Package type = " + pack.getType());

						apams_profile_package proPack = (apams_profile_package) pack;
						byte[] byteArray = proPack.getPic();

						String profileQuery = "UPDATE user_information SET profilepic = ? WHERE username = ?";
						try {
							PreparedStatement profilepst = conn
									.prepareStatement(profileQuery);
							profilepst.setBytes(1, byteArray);
							profilepst.setString(2, username);
							int result = profilepst.executeUpdate();
							if (result != 0) {
								replyStr = "UPDATED";
							} else {
								replyStr = "Not updated";
							}
							StrOut.write(replyStr);
							StrOut.flush();
							System.out.println("return String sent");
							StrOut.close();
							profilepst.close();
							run();
						} catch (SQLException e) {
							System.out.println(e);
						}

						break;
					case DATALIST:
						System.out.println("Package type = " + pack.getType());

						String datalistQuery = "SELECT name,maxlvl FROM databases WHERE owner = ?";
						try {
							PreparedStatement datalistpst = conn
									.prepareStatement(datalistQuery);
							datalistpst.setString(1, username);
							ResultSet rs = datalistpst.executeQuery();
							ArrayList<String> resultAL = new ArrayList<String>();
							ArrayList<String> resultLVL = new ArrayList<String>();
							while (rs.next()) {
								String data = rs.getString("name");
								String lvl = String
										.valueOf(rs.getInt("maxlvl"));
								resultAL.add(data);
								resultLVL.add(lvl);
							}
							apams_network_package resultPack = new apams_datalist_package(
									username, resultAL, resultLVL);
							oOutputs.writeObject(resultPack);
							oOutputs.flush();
							datalistpst.close();
							System.out.println("return package sent");
							run();
						} catch (SQLException e) {
							System.out.println(e);
						}
						break;

					case ACC:
						System.out.println("Package type = " + pack.getType());

						String Accquery = "SELECT cid,priority,belongto,profilepic FROM user_information where username =? ";
						try {
							PreparedStatement accpst = conn
									.prepareStatement(Accquery);
							accpst.setString(1, username);
							ResultSet rs = accpst.executeQuery();
							String rscid = null;
							String rsBelong = null;
							int rsPriority = 0;
							byte[] profilepic = new byte[0];
							while (rs.next()) {
								rsBelong = rs.getString("belongto");
								rscid = rs.getString("cid");
								rsPriority = rs.getInt("priority");
								profilepic = rs.getBytes("profilepic");
							}

							apams_network_package accResult = new apams_acc_package(
									username, rscid, rsPriority, rsBelong,
									profilepic);
							oOutputs.writeObject(accResult);
							oOutputs.flush();
							accpst.close();
							System.out.println("return package sent");
							run();
						} catch (SQLException e) {
							System.out.println(e);
						}
						break;

					case CREATE:
						System.out.println("Package type = " + pack.getType());

						apams_network_package_create Cpack = (apams_network_package_create) pack;
						int maxLvl = Cpack.getMaxlvl();
						String databaseName = Cpack.getDataName();

						String createquery = "CREATE TABLE IF NOT EXISTS "
								+ databaseName
								+ "(name text NOT NULL PRIMARY KEY,"
								+ "building text," + "room text,"
								+ "type text," + "img bytea," + "assetlvl int,"
								+ "qrstring text UNIQUE," + "time text,"
								+ "lastupdatetime text," + "lastupdater text,"
								+ "manufacturer text," + "model text,"
								+ "broken boolean," + "extras text[],"
								+ "missing boolean)";
						try {
							PreparedStatement createpst = conn
									.prepareStatement(createquery);
							createpst.executeUpdate();
							createpst.close();
						} catch (SQLException e) {
							System.out.println(e);
							replyStr = "Database name already used";
						}

						String insertQuery = "INSERT INTO databases(name,owner,maxlvl,time)"
								+ "VALUES(?,?,?,?)";
						try {
							insertpst = conn.prepareStatement(insertQuery);
							insertpst.setString(1, databaseName);
							insertpst.setString(2, username);
							insertpst.setInt(3, maxLvl);
							insertpst.setString(4, time);
							int result = insertpst.executeUpdate();
							if (result != 0) {
								replyStr = "GOOD";
							} else {
								replyStr = "Sth wrong happened, database not updated";
							}
							insertpst.close();
						} catch (Exception e) {
							System.out.println(e);
							replyStr = "Insert into databases wrong";
						}
						StrOut.write(replyStr);
						StrOut.flush();
						System.out.println("Reply String sent" + replyStr);
						StrOut.close();
						run();
						break;

					case REGISTER_AD:
						System.out.println("Package type = " + pack.getType());

						String ADquery = "INSERT INTO user_information (username, password,CID,profilepic,priority,belongto,time)"
								+ "VALUES(?,?,?,?,?,?,?)";

						try {
							byte[] emptypic = new byte[100];
							insertpst = conn.prepareStatement(ADquery);
							insertpst.setString(1, username);
							insertpst.setString(2, password);
							insertpst.setString(3, CID);
							insertpst.setBytes(4, emptypic);
							insertpst.setInt(5, 1000);
							insertpst.setString(6, "Admin");
							insertpst.setString(7,
									((apams_network_package_regisAD) pack)
											.getTime());

							try {
								int result = insertpst.executeUpdate();
								System.out
										.println("Register request updated in database");
								if (result != 0) {
									replyStr = "GOOD";
								} else {
									replyStr = "Sth wrong happened, database not updated";
								}
							} catch (SQLException e) {
								System.out.println(e);
								replyStr = "Username already exist";
							}
							insertpst.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						StrOut.write(replyStr);
						StrOut.flush();
						System.out.println("Reply String sent" + replyStr);
						StrOut.close();
						run();

						break;
					case REGISTER_N:
						System.out.println("Package type = " + pack.getType());

						apams_network_package_regisN regisNpack = (apams_network_package_regisN) pack;

						String invite = regisNpack.getInvite();

						int priority = 0;
						String belongTo = null;
						boolean activated = true;
						try {
							String Nquery = "SELECT level,belongto,activated FROM invitelevel WHERE code = ? ";
							PreparedStatement querypst = conn
									.prepareStatement(Nquery);
							querypst.setString(1, invite);

							ResultSet rs = querypst.executeQuery();
							if (!rs.isBeforeFirst()) {
								replyStr = "Invite code not found";
							}
							while (rs.next()) {
								priority = rs.getInt("level");
								belongTo = rs.getString("belongto");
								activated = rs.getBoolean("activated");
							}
							if (activated) {
								replyStr = "CODEUSED";
								StrOut.write(replyStr);
								StrOut.flush();
								System.out.println("Reply String sent"
										+ replyStr);
								StrOut.close();
								querypst.close();
								run();
								break;
							} else {

								querypst.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						String NIquery = "INSERT INTO user_information (username, password,CID,profilepic,priority,belongto,time)"
								+ "VALUES(?,?,?,?,?,?,?)";

						try {
							insertpst = conn.prepareStatement(NIquery);
							insertpst.setString(1, username);
							insertpst.setString(2, password);
							insertpst.setString(3, CID);
							insertpst.setBytes(4, new byte[0]);
							insertpst.setInt(5, priority);
							insertpst.setString(6, belongTo);
							insertpst.setString(7, regisNpack.getTime());

							try {
								int result = insertpst.executeUpdate();
								System.out
										.println("Register request updated in database");
								if (result != 0) {
									replyStr = "GOOD";
								} else {
									replyStr = "Sth wrong happened, database not updated";
								}
							} catch (SQLException e) {
								replyStr = "Username already exist";
								System.out.println(e);
							}
							insertpst.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						String modInviteQuery = "UPDATE invitelevel SET activated = ?, activated_by = ? WHERE code = ?";
						try {
							PreparedStatement MIpst = conn
									.prepareStatement(modInviteQuery);
							MIpst.setBoolean(1, true);
							MIpst.setString(2, username);
							MIpst.setString(3, invite);
							MIpst.executeUpdate();
						} catch (SQLException e) {
							System.out.println();
							replyStr = "Error";
						}
						StrOut.write(replyStr);
						StrOut.flush();
						System.out.println("Reply String sent" + replyStr);
						StrOut.close();
						run();
						break;
					case LOGIN:
						System.out.println("Package type = " + pack.getType()
								+ " " + "username = " + username);

						String Lquery = "SELECT password,priority FROM user_information WHERE username = ?";

						try {
							insertpst = conn.prepareStatement(Lquery);
							insertpst.setString(1, username);
							try {
								ResultSet rs = insertpst.executeQuery();
								System.out
										.println("ResultSet got from database");
								if (!rs.isBeforeFirst()) {
									replyStr = "User not found";
								}
								while (rs.next()) {
									if (rs.getString("password").equals(
											password)) {
										replyStr = "GOOD";
										if (rs.getInt("priority") == 1000) {
											replyStr = "ISADMIN";
										}
									} else {
										replyStr = "Wrong password";
									}
								}
								insertpst.close();
								rs.close();
								System.out.println(replyStr);
							} catch (SQLException e) {
								e.printStackTrace();
								replyStr = "Bug!!";
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						StrOut.write(replyStr);
						StrOut.flush();
						System.out.println("Reply String sent" + replyStr);
						StrOut.close();
						run();
						break;
					default:
						run();
						break;
					}
				} catch (EOFException e) {
					run();
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			run();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			run();
		}
	}

}