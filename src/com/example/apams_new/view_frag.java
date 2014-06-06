package com.example.apams_new;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class view_frag extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.view_frag, container, false);
		Spinner datalist = (Spinner) rootView
				.findViewById(R.id.inspect_spinner);
		TextView message = (TextView) rootView
				.findViewById(R.id.inspect_message);
		Bundle args = this.getArguments();
		boolean isAdmin = args.getBoolean("isAdmin");
		if (!isAdmin) {
			datalist.setVisibility(View.INVISIBLE);
			datalist.setEnabled(false);
			message.setText("Click confirm to inspect the items your are managing");
		} else {
			message.setText("Please select the database your want to inspect");
		}
		return rootView;
	}

	public static view_frag newViewInstance(String mUsername, int i,
			boolean isAdmin) {
		view_frag fragment = new view_frag();
		Bundle args = new Bundle();
		args.putInt("position", i);
		args.putString("username", mUsername);
		args.putBoolean("isAdmin", isAdmin);
		fragment.setArguments(args);
		return fragment;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				"position"));
	}
}
