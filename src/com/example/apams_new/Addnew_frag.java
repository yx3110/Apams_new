package com.example.apams_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Addnew_frag extends Fragment {

	public Addnew_frag() {
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.addnew_frag, container, false);
		
		
		if(!(this.getArguments().getBoolean("isAdmin"))){
			((Button) rootView.findViewById(R.id.add_chooseData)).setVisibility(View.INVISIBLE);
			((Button) rootView.findViewById(R.id.add_chooseData)).setEnabled(false);
		}

		return rootView;
	}

	public static Addnew_frag newAddInstance( int i,boolean isAdmin) {
		Addnew_frag fragment = new Addnew_frag();
		Bundle args = new Bundle();
		args.putBoolean("isAdmin", isAdmin);
		args.putInt("position", i);
		fragment.setArguments(args);
		return fragment;
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt("position"));
	}
}
