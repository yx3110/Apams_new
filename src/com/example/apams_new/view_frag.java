package com.example.apams_new;

import java.util.ArrayList;

import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apams_network_package;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class view_frag extends Fragment implements OnTaskCompleted {
	private ArrayList<String> datalist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.view_frag, container, false);
		
		Spinner datalistSpinner = (Spinner) rootView
				.findViewById(R.id.inspect_spinner_data);
		
		Spinner sortBy = (Spinner) rootView
				.findViewById(R.id.inspect_spinner_sortby);

		TextView message = (TextView) rootView
				.findViewById(R.id.inspect_message);
		Bundle args = this.getArguments();
		boolean isAdmin = args.getBoolean("isAdmin");
		if (!isAdmin) {
			datalistSpinner.setVisibility(View.INVISIBLE);
			datalistSpinner.setEnabled(false);
			message.setText("Click confirm to inspect the items your are managing");
		} else {
			message.setText("Please select the database your want to inspect");
			if(this.datalist==null||this.datalist.size() == 0){
				popMsg("Please create a database first");
			}
			String[] dataarray = new String[this.datalist.size()];
			String[] sortByStr = new String[]{"Location","Type","Manufacturer","Broken","Missing"};
			dataarray = this.datalist.toArray(dataarray);
			ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(this.getActivity(),
					android.R.layout.simple_spinner_item, dataarray);
			ArrayAdapter<String> sortadapter = new ArrayAdapter<String>(this.getActivity(),
					android.R.layout.simple_spinner_item, sortByStr);
			datalistSpinner.setAdapter(dataadapter);
			sortBy.setAdapter(sortadapter);
		}
		return rootView;
	}

	public static view_frag newViewInstance(String mUsername, int i,
			boolean isAdmin, ArrayList<String> datalist) {
		view_frag fragment = new view_frag();
		fragment.datalist = datalist;
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

	@Override
	public void onTaskCompleted(String answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPackReceived(apams_network_package pack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popMsg(String string) {
		// TODO Auto-generated method stub
		((OnTaskCompleted) this.getActivity()).popMsg(string);
		
	}
}
