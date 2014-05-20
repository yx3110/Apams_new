package com.example.apams_new;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class Account_frag extends Fragment {
	public Account_frag(){}
	
	public static Account_frag newAccInstance(String username, int position) {
		Account_frag fragment = new Account_frag();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putString("username", username);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.account_frag, container, false);
        Bundle args = this.getArguments();
        String CID = null;
        
        ((TextView)rootView.findViewById(R.id.textView_username)).setText(CID);
        ((TextView)rootView.findViewById(R.id.textView_username)).setText(args.getString("username"));
        return rootView;
    }
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt("position"));
	}

}
