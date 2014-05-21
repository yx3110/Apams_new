package com.example.apams_new;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class map_frag extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.map_frag, container, false);

		return rootView;
	}

	public static map_frag newMapInstance(int i) {
		map_frag fragment = new map_frag();
		Bundle args = new Bundle();
		args.putInt("position", i);
		fragment.setArguments(args);
		return fragment;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				"position"));
	}
}
