package com.example.apams_newUtil;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

					String query;

					PreparedStatement insertpst;

					switch (pack.getType()) {

					case REGISTER_AD:
						System.out.println("Package type = " + pack.getType());

						apams_network_package_regisAD regisADpack = (apams_network_package_regisAD) pack;
						int maxLvl = regisADpack.getMaxLvl();
						String databaseName = regisADpack.getDataName();

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
							replyStr = "Database name already used";
						}

						query = "INSERT INTO user_information (username, password,CID,profilepic,priority,belongto)"
								+ "VALUES(?,?,?,?,?,?)";
						
						try {
							insertpst = conn.prepareStatement(query);
							insertpst.setString(1, username);
							insertpst.setString(2, password);
							insertpst.setString(3, CID);
							insertpst.setByte(4, (byte) 0);
							insertpst.setInt(5, maxLvl);
							insertpst.setString(6, databaseName);

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
					case REGISTER_N:
						System.out.println("Package type = " + pack.getType());

						apams_network_package_regisN regisNpack = (apams_network_package_regisN) pack;

						String invite = regisNpack.getInvite();

						int priority = 0;
						String belongTo = null;

						try {
							query = "SELECT level,belongto FROM invitelevel WHERE code = ? ";
							PreparedStatement querypst = conn
									.prepareStatement(query);
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
								replyStr = "Error during finding invite code";
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						query = "INSERT INTO user_information (username, password,CID,profilepic,priority,belongto)"
								+ "VALUES(?,?,?,?,?,?)";

						try {
							insertpst = conn.prepareStatement(query);
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

						query = "SELECT password FROM user_information WHERE username = ?";

						try {
							insertpst = conn.prepareStatement(query);
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
