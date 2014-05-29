package com.example.apams_newUtil;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.apams_newUtil.apams_network_package;

public class BackGroundRegister extends Thread {
	private ServerSocket ssocket;
	private Connection conn;

	public BackGroundRegister(ServerSocket ssocket, Connection conn) {
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
					case INVITEMANAGE:
						System.out.println("Package type = " + pack.getType());
						String IMquery = "SELECT code,level,belongto,time,activated,activated_by FROM invitelevel where creator =?";
						try {
							PreparedStatement IMpst = conn
									.prepareStatement(IMquery);
							IMpst.setString(1, username);
							ResultSet rs = IMpst.executeQuery();
							if (!rs.isBeforeFirst()) {
								replyStr = "NOINVITE";
								StrOut.write(replyStr);
								StrOut.flush();
								StrOut.close();
								IMpst.close();
								run();
							} else {
								ArrayList<InviteInfo> resultList = new ArrayList<InviteInfo>();
								while (rs.next()) {
									InviteInfo info = new InviteInfo();
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
									resultList.add(info);
								}
								System.out.println(resultList.size());
								apams_network_package returnPack = new apams_inviteManage_package(
										username, resultList);
								oOutputs.writeObject(returnPack);
								System.out.println("return package sent");
								oOutputs.close();
								IMpst.close();
								run();
							}

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
							oOutputs.close();
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
							oOutputs.close();
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

						String createquery = "CREATE TABLE ? (" + "name text,"
								+ "building text," + "room text,"
								+ "type text," + "img bytea," + "assetlvl int,"
								+ "time text" + "PRIMARY KEY(name))";
						try {
							PreparedStatement createpst = conn
									.prepareStatement(createquery);
							createpst.setString(1, databaseName);
							createpst.execute();
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
							if(activated){
								replyStr = "CODEUSED";
								StrOut.write(replyStr);
								StrOut.flush();
								System.out.println("Reply String sent" + replyStr);
								StrOut.close();
								querypst.close();
								run();
								break;
							}else{

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
