package com.example.apams_new;

import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apamsTCPclient;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package_regisAD;
import com.example.apams_newUtil.apams_network_package_regisN;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Apams_register extends Activity implements OnTaskCompleted {

	private CheckBox manager;
	private EditText dataName, invite, maxLvl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_apams_register);

		dataName = (EditText) findViewById(R.id.regis_dataName);
		invite = (EditText) findViewById(R.id.regis_invite);
		maxLvl = (EditText) findViewById(R.id.editText_regisLvl);

		dataName.setEnabled(false);
		maxLvl.setEnabled(false);
		dataName.setVisibility(100);
		maxLvl.setVisibility(100);

		manager = (CheckBox) findViewById(R.id.checkBox_owner);
		manager.setOnCheckedChangeListener(new ApamsRegisBoxListener(dataName,
				invite));
	}

	class ApamsRegisBoxListener implements OnCheckedChangeListener {
		private EditText dataName, invite;

		public ApamsRegisBoxListener(EditText dataName, EditText invite) {
			this.dataName = dataName;
			this.invite = invite;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int inviteVis = isChecked ? 100 : 0;
			int dataVis = isChecked ? 0 : 100;

			dataName.setEnabled(isChecked);
			maxLvl.setEnabled(isChecked);
			invite.setEnabled(!isChecked);
			dataName.setVisibility(dataVis);
			maxLvl.setVisibility(dataVis);
			invite.setVisibility(inviteVis);
		}

	}

	public void confirmRegister(View view) {
		EditText username = (EditText) findViewById(R.id.editText_username);
		EditText password1 = (EditText) findViewById(R.id.editText_passwordFirst);
		EditText password2 = (EditText) findViewById(R.id.editText_passwordSecond);
		EditText CID = (EditText) findViewById(R.id.editText_CID);

		String CIDStr = CID.getText().toString();
		String usernameStr = username.getText().toString();
		String password1Str = password1.getText().toString();
		String password2Str = password2.getText().toString();
		apams_network_package pack = null;
		if (manager.isChecked()) {
			String databaseName = dataName.getText().toString();
			int lvlStr = Integer.parseInt(maxLvl.getText().toString());
			pack = new apams_network_package_regisAD(usernameStr, password1Str,
					CIDStr, databaseName, lvlStr);
		} else {
			String inviteStr = this.invite.getText().toString();
			pack = new apams_network_package_regisN(usernameStr, password1Str,
					CIDStr, inviteStr);
		}
		if (password1Str.length() == 0 || usernameStr.length() == 0
				|| CIDStr.length() == 0) {
			popMsg("Please fill in all the fields!");
		}
		if (!password1Str.equals(password2Str)) {
			popMsg("Password not match!");
		} else if (!usernameStr.contains("@")) {
			popMsg("Username has to be a valid email!");
		} else if (usernameStr.length() > 40 || password1Str.length() > 40) {
			popMsg("Length of password or username must be shorter than 40 characters!");
		} else if (password1Str.length() < 8) {
			popMsg("Password needs to be at least 8 characters long");
		} else if (!(CIDStr.length() == 8)) {
			popMsg("CID has to be a valid Imperial CID with 8 digits long");
		} else {
			try {
				new apamsTCPclient(this).execute(pack);
			} catch (Exception e) {
				Log.e("exception", e.getMessage());
			}
		}
	}

	@Override
	public void onTaskCompleted(String answer) {
		if (answer.equals("GOOD")) {
			popMsg("Registration done!Login using this account.");
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("username",
					((EditText) findViewById(R.id.editText_username))
							.toString());
			startActivity(intent);
			finish();
		} else if (answer.equals("Username already exist")) {
			popMsg("Username or CID already exist,please change username or CID!");
			EditText username = (EditText) findViewById(R.id.editText_username);
			username.requestFocus();
		} else if (answer.equals("Error occurred during connection")) {
			popMsg("Connection failed. Server not online");
		} else {
			popMsg("Please try again!" + " " + answer);
		}
	}

	@Override
	public void popMsg(String msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
