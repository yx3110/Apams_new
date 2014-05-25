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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

					PreparedStatement insertpst;

					switch (pack.getType()) {

					case DATALIST:
						System.out.println("Package type = " + pack.getType());

						String datalistQuery = "SELECT name FROM databases WHERE owner = ?";
						try {
							PreparedStatement datalistpst = conn
									.prepareStatement(datalistQuery);
							datalistpst.setString(1, username);
							ResultSet rs = datalistpst.executeQuery();
							ArrayList<String> resultAL = new ArrayList<String>();
							while (rs.next()) {
								String data = rs.getString("name");
								resultAL.add(data);
							}
							apams_network_package resultPack = new apams_datalist_package(
									username, resultAL);
							oOutputs.writeObject(resultPack);
							oOutputs.close();
							System.out.println("return package sent");
							run();
						} catch (SQLException e) {
							System.out.println(e);
						}
						break;

					case ACC:
						System.out.println("Package type = " + pack.getType());

						String Accquery = "SELECT cid,priority,belongto FROM user_information where username =? ";
						try {
							PreparedStatement accpst = conn
									.prepareStatement(Accquery);
							accpst.setString(1, username);
							ResultSet rs = accpst.executeQuery();
							String rscid = null;
							String rsBelong = null;
							int rsPriority = 0;
							while (rs.next()) {
								rsBelong = rs.getString("belongto");
								rscid = rs.getString("cid");
								rsPriority = rs.getInt("priority");
							}

							apams_network_package accResult = new apams_acc_package(
									username, rscid, rsPriority, rsBelong);
							oOutputs.writeObject(accResult);
							oOutputs.close();
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
								+ "PRIMARY KEY(name))";
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

						String insertQuery = "INSERT INTO databases(name,owner,maxlvl)"
								+ "VALUES(?,?,?)";
						try {
							insertpst = conn.prepareStatement(insertQuery);
							insertpst.setString(1, databaseName);
							insertpst.setString(2, username);
							insertpst.setInt(3, maxLvl);
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

						String ADquery = "INSERT INTO user_information (username, password,CID,profilepic,priority,belongto)"
								+ "VALUES(?,?,?,?,?,?)";

						try {
							byte[] emptypic = new byte[100];
							insertpst = conn.prepareStatement(ADquery);
							insertpst.setString(1, username);
							insertpst.setString(2, password);
							insertpst.setString(3, CID);
							insertpst.setBytes(4, emptypic);
							insertpst.setInt(5, 1000);
							insertpst.setString(6, "Admin");

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

						try {
							String Nquery = "SELECT level,belongto FROM invitelevel WHERE code = ? ";
							PreparedStatement querypst = conn
									.prepareStatement(Nquery);
							querypst.setString(1, invite);
							try {
								ResultSet rs = querypst.executeQuery();
								if (!rs.isBeforeFirst()) {
									replyStr = "Invite code not found";
								}
								while (rs.next()) {
									priority = rs.getInt("level");
									belongTo = rs.getString("belongto");
								}

								querypst.close();

							} catch (SQLException e) {
								System.out.println(e);

								replyStr = "Error during finding invite code";
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						String NIquery = "INSERT INTO user_information (username, password,CID,profilepic,priority,belongto)"
								+ "VALUES(?,?,?,?,?,?)";

						try {
							insertpst = conn.prepareStatement(NIquery);
							insertpst.setString(1, username);
							insertpst.setString(2, password);
							insertpst.setString(3, CID);
							insertpst.setByte(4, (byte) 0);
							insertpst.setInt(5, priority);
							insertpst.setString(6, belongTo);

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
