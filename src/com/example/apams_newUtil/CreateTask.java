package com.example.apams_newUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class CreateTask extends AsyncTask<apams_network_package, Integer, String> {
	public static final String SERVERIP = "146.169.53.22";
	public static final int SERVERPORT = 8888;
	private String answer;
	private Socket socket;

	private ObjectOutputStream Oout;
	private BufferedReader in;
	@Override
	protected String doInBackground(apams_network_package... pack) {
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			Log.e("TCP client", "C:Connecting...");
			try {
				socket = new Socket(serverAddr, SERVERPORT);
				Oout = new ObjectOutputStream(socket.getOutputStream());
				Oout.writeObject(pack[0]);
				Log.e("TCP", "package send");
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				Log.e("TCP", "" + in.ready());
				answer = in.readLine();
				Log.e("TCP", "answer got");
			} catch (Exception e) {
				answer = "Error occurred during connections";
				Log.e("TCP", "S:Error", e);
			}finally{
				socket.close();
				Oout.close();
				in.close();
			}
		} catch (Exception e) {
			answer = "Error occurred during connection"+e;
		}
		return answer;		
	}

}
