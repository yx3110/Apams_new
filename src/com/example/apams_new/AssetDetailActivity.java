package com.example.apams_new;

import java.util.HashMap;

import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apamsTCPclient;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package.packageType;
import com.example.apams_newUtil.assetItem;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * An activity representing a single Asset detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link AssetListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link AssetDetailFragment}.
 */
public class AssetDetailActivity extends FragmentActivity implements OnTaskCompleted {
	private assetItem mItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asset_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(false);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			this.mItem = ((HashMap<String, assetItem>) this.getIntent().getExtras()
					.getSerializable("assetMap")).get(getIntent().getStringExtra(
							AssetDetailFragment.ARG_ITEM_ID));
			arguments.putString(AssetDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(AssetDetailFragment.ARG_ITEM_ID));
			AssetDetailFragment fragment = new AssetDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.asset_detail_container, fragment).commit();
		}
	}

	public void showExtras(View view) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("Extra informations");
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.show_extras,
				(ViewGroup) findViewById(R.id.dialog));
		builder.setView(layout);
		builder.setNegativeButton("OK", null);
		String extra1 = null, extra2 = null, extra3 = null, extra4 = null, extra5 = null;
		try{
		extra1 = mItem.getExtras().get(0);
		extra2 = mItem.getExtras().get(1);
		extra3 = mItem.getExtras().get(2);
		extra4 = mItem.getExtras().get(3);
		extra5 = mItem.getExtras().get(4);

		}catch(Exception e){
			
		}
		if (extra1 != null) {
			((TextView) layout.findViewById(R.id.show_extra_1)).setText(extra1);
		}
		if (extra2 != null) {
			((TextView) layout.findViewById(R.id.show_extra_1)).setText(extra2);
		}
		if (extra3 != null) {
			((TextView) layout.findViewById(R.id.show_extra_1)).setText(extra3);
		}
		if (extra4 != null) {
			((TextView) layout.findViewById(R.id.show_extra_1)).setText(extra4);
		}
		if (extra5 != null) {
			((TextView) layout.findViewById(R.id.show_extra_1)).setText(extra5);
		}
	}

	public void reportMissing(View view){
		apams_network_package pack = new apams_network_package(mItem.getItemName(),mItem.getDatabase(),packageType.REPORTMISS);
		apamsTCPclient client = new apamsTCPclient(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
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
		
	}
}
