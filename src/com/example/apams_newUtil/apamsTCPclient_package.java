package com.example.apams_newUtil;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class apamsTCPclient_package extends
		AsyncTask<apams_network_package, Integer, apams_network_package> {

	private OnTaskCompleted listener;

	public apamsTCPclient_package(OnTaskCompleted listener) {
		this.listener = listener;
	}

	public apamsTCPclient_package() {
	}

	public static String SERVERIP = "146.169.53.96";
	public static final int SERVERPORT = 8889;
	private apams_network_package answer;
	private Socket socket;

	private ObjectOutputStream Oout;
	private ObjectInputStream Oin;
/*
	public void changeIp(String ip){
		this.SERVERIP = ip;
	}
	*/
	@Override
	protected apams_network_package doInBackground(
			apams_network_package... pack) {
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			Log.e("TCP client", "C:Connecting...");
			try {
				socket = new Socket(serverAddr, SERVERPORT);
				Oout = new ObjectOutputStream(socket.getOutputStream());
				Oout.writeObject(pack[0]);
				Log.e("TCP", "package send");
				Oin = new ObjectInputStream(socket.getInputStream());
				Log.e("TCP", "");
				answer =(apams_network_package) Oin.readObject();
				Log.e("TCP", "answer package got");
			} catch (Exception e) {
				Log.e("TCP", "S:Error", e);
				
			} finally {
				socket.close();
				Oout.close();
				Oin.close();
			}
		} catch (Exception e) {
			Log.e("TCP", "S:Error", e);
		}
		return answer;
	}
	
	protected void onPostExecute(apams_network_package pack) {
		listener.onPackReceived(pack);;
	}

}
