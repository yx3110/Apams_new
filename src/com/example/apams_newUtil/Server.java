package com.example.apams_newUtil;

import java.sql.*;
import java.net.*;
import java.util.*;
import java.io.*;


public class Server {

	public static void main(String[] args) {
		try{
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e){
			System.out.println("Could not find JDBC driver"+e);
		}
		Connection conn = null;
		try{
			System.out.println("Establishing connection with Imperial psql database");
			conn = DriverManager.getConnection("jdbc:postgresql://db.doc.ic.ac.uk/yx3110","yx3110","kyJZ7MyzRW");
			System.out.println("Connection with database made successfully");
		}catch (Exception e){
			System.out.println(e);
		}
		try{
			ServerSocket serverSocket = new ServerSocket(8888);
			System.out.println("Server socket created");
			
			Socket socket = serverSocket.accept();
			
			InputStream inputS = socket.getInputStream();
			ObjectInputStream OinputS = new ObjectInputStream(inputS);
			BufferedWriter StrOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(true){
				apams_network_package pack = (apams_network_package) OinputS.readObject();
				switch(pack.getType()){
					case REGISTER:
						String username = pack.getUsername();
						String password = pack.getPassword();
						String CID = pack.getCID();
						String query = "INSERT INTO user_information (username, password,CID)"+"VALUES(?,?,?)";
						
						PreparedStatement pst = conn.prepareStatement(query);
						pst.setString(2, password);
						pst.setString(1, username);
						pst.setString(3, CID);
						int result =pst.executeUpdate();
						String replyStr;
						if(result == 0){
							replyStr = "Please try again!";
						}else{
							replyStr = "GOOD";
						}
						StrOut.write(replyStr);
						StrOut.flush();
						break;
					default:
						return;
				}
			}
			
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
