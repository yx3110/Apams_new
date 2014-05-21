package com.example.apams_newUtil;

import java.sql.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class Server {

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Could not find JDBC driver" + e);
		}
		Connection conn = null;
		try {
			System.out
					.println("Establishing connection with Imperial psql database");
			conn = DriverManager.getConnection(
					"jdbc:postgresql://db.doc.ic.ac.uk/yx3110", "yx3110",
					"kyJZ7MyzRW");
			System.out.println("Connection with database made successfully");
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			ServerSocket serverSocket = new ServerSocket(8888);
			System.out.println("Server socket created");
			
			while(true){
				BackGroundRegister thread = new BackGroundRegister(serverSocket,conn);
				thread.run();
			}
			
		} catch (EOFException e) {
			System.out.println(e);
			main(args);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			main(args);
		}
	}
}
