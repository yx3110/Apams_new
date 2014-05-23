package com.example.apams_new;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
 
public class Account_frag extends Fragment {
	public Account_frag(){}
	
	public static Account_frag newAccInstance(String username, int position,boolean isAdmin) {
		Account_frag fragment = new Account_frag();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putBoolean("isAdmin", isAdmin);
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
        boolean isAdmin = args.getBoolean("isAdmin");
        String priority = isAdmin?"Admin":null;
        
		Button createData = (Button) rootView.findViewById(R.id.acc_addData);
		if(!isAdmin){
			createData.setEnabled(false);
			createData.setVisibility(View.INVISIBLE);
		}
        
        ((TextView)rootView.findViewById(R.id.textView_CID)).setText(CID);
        ((TextView)rootView.findViewById(R.id.textView_priority)).setText(priority);        
        ((TextView)rootView.findViewById(R.id.textView_username)).setText(args.getString("username"));
        return rootView;
    }
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt("position"));
	}

}
