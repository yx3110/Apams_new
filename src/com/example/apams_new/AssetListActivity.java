package com.example.apams_new;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apamsTCPclient_package;
import com.example.apams_newUtil.apams_assetQuery_package;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.assetItem;
import com.example.apams_newUtil.apams_network_package.packageType;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a list of Assets. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link AssetDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link AssetListFragment} and the item details (if present) is a
 * {@link AssetDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link AssetListFragment.Callbacks} interface to listen for item selections.
 */
public class AssetListActivity extends FragmentActivity implements
		AssetListFragment.Callbacks, OnTaskCompleted {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private AssetListFragment listFrag;
	public ArrayList<assetItem> assetList;
	private HashMap<String, assetItem> assetMap;
	ArrayAdapter<assetItem> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		apams_network_package pack = (apams_network_package) this.getIntent()
				.getExtras().get("pack");
		apamsTCPclient_package task = new apamsTCPclient_package(this);
		task.execute(pack);

		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						sleep(10000);
					}
				} catch (InterruptedException ex) {
					Log.e("", ex.getMessage());
				}
			}
		};

		thread.start();
		
		Log.e("list", this.assetList.size()+"");

		if (findViewById(R.id.asset_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.

			this.listFrag = ((AssetListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.asset_list));
			listFrag.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link AssetListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(AssetDetailFragment.ARG_ITEM_ID, id);
			AssetDetailFragment fragment = new AssetDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.asset_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, AssetDetailActivity.class);
			detailIntent.putExtra(AssetDetailFragment.ARG_ITEM_ID, id);
			detailIntent.putExtra("assetList", this.assetList);
			detailIntent.putExtra("assetMap", this.assetMap);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onTaskCompleted(String answer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPackReceived(apams_network_package pack) {
		packageType type = pack.getType();
		switch (type) {
		case ASSETRESULT:
			apams_assetQuery_package AQpack = (apams_assetQuery_package) pack;
			this.assetList = AQpack.getItemList();
			this.assetMap = AQpack.getItemMap();
			
			this.arrayAdapter = new ArrayAdapter<assetItem>(this,
					android.R.layout.simple_list_item_activated_1,
					android.R.id.text1, this.assetList);
			break;
		default:
			break;
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
