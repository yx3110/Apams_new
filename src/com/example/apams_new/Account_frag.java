package com.example.apams_new;

import java.io.Serializable;

import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apamsTCPclient_package;
import com.example.apams_newUtil.apams_acc_package;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package.packageType;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Account_frag extends Fragment implements OnTaskCompleted {
	private View rootView;
	public Account_frag() {
	}

	public static Account_frag newAccInstance(String username, int position,
			boolean isAdmin, OnTaskCompleted listener) {
		Account_frag fragment = new Account_frag();
		Bundle args = new Bundle();
		args.putSerializable("listener", (Serializable) listener);
		args.putInt("position", position);
		args.putBoolean("isAdmin", isAdmin);
		args.putString("username", username);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.account_frag, container,
				false);
		this.rootView = rootView;
		Bundle args = this.getArguments();
		String CID = null;
		boolean isAdmin = args.getBoolean("isAdmin");
		String priority = isAdmin ? "Admin" : null;

		
		
		Button createData = (Button) rootView.findViewById(R.id.acc_addData);
		Button checkData = (Button) rootView.findViewById(R.id.acc_checkData);
		if (!isAdmin) {
			checkData.setEnabled(false);
			checkData.setVisibility(View.INVISIBLE);
			createData.setEnabled(false);
			createData.setVisibility(View.INVISIBLE);
		}else{
			rootView.findViewById(R.id.textView_database).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.textView_database).setEnabled(false);
		}

		apams_network_package pack = new apams_network_package(
				args.getString("username"), packageType.ACC);
		apamsTCPclient_package task = new apamsTCPclient_package(this);
		task.execute(pack);
		
		((TextView) rootView.findViewById(R.id.textView_username)).setText(args
				.getString("username"));
		rootView = this.rootView;
		return rootView;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				"position"));
	}

	@Override
	public void onTaskCompleted(String answer) {

	}

	@Override
	public void onPackReceived(apams_network_package pack) {
		apams_acc_package accPack = (apams_acc_package) pack;
		String CID = accPack.getCID();
		String priority = String.valueOf(accPack.getPriory());
		String database = accPack.getBelongto();
		Log.e("pack", CID+" "+ database);

		((TextView) rootView.findViewById(R.id.textView_CID)).setText(CID);
		if (this.getArguments().getBoolean("isAdmin")) {
			((TextView) rootView.findViewById(R.id.textView_priority))
					.setText("Admin");
			((TextView) rootView.findViewById(R.id.textView_CID)).setText(CID);
		} else {
			((TextView) rootView.findViewById(R.id.textView_CID)).setText(CID);
			((TextView) rootView.findViewById(R.id.textView_database)).setText(database);
			((TextView) rootView.findViewById(R.id.textView_priority))
					.setText(priority);
		}
	}

	@Override
	public void popMsg(String string) {
		((OnTaskCompleted) this.getArguments().getSerializable("listener")).popMsg(string);
	}

}
