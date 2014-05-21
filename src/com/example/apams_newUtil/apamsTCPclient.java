package com.example.apams_newUtil;
import java.io.*;
import java.net.*;

import com.example.apams_new.R;

import android.os.AsyncTask;
import android.util.*;

public class apamsTCPclient extends
		AsyncTask<apams_network_package, Integer, String> {
	private OnTaskCompleted listener;

	public apamsTCPclient(OnTaskCompleted listener) {
		this.listener = listener;
	}
	public apamsTCPclient() {
	}

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

	protected void onPostExecute(String answer) {
		listener.onTaskCompleted(answer);
	}
}