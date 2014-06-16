package com.example.apams_new;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.widget.Button;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apams_newUtil.assetItem;

/**
 * A fragment representing a single Asset detail screen. This fragment is either
 * contained in a {@link AssetListActivity} in two-pane mode (on tablets) or a
 * {@link AssetDetailActivity} on handsets.
 */
public class AssetDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private assetItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AssetDetailFragment() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			Log.e("id", getArguments().getString(ARG_ITEM_ID));

			mItem = ((HashMap<String, assetItem>) this.getActivity()
					.getIntent().getExtras().getSerializable("assetMap"))
					.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_asset_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			ImageView imgview = (ImageView) rootView
					.findViewById(R.id.asset_detail_image);
			byte[] pic = mItem.getPic();
			Log.e("pic", pic.toString());
			Bitmap bitpic = new BitmapFactory().decodeByteArray(pic, 0,
					pic.length);
			imgview.setImageBitmap(bitpic);
			((TextView) rootView.findViewById(R.id.asset_detail_broken))
			.setText("Item is broken: "+ mItem.isBroken());
			((Button) rootView.findViewById(R.id.asset_detail_missing))
			.setText("Item is missing: "+mItem.getMissing());
			((Button) rootView.findViewById(R.id.asset_detail_extras))
			.setText("Get extra information about item");
			((TextView) rootView.findViewById(R.id.asset_detail_name))
					.setText("Item name: "+mItem.getItemName());
			((TextView) rootView.findViewById(R.id.asset_detail_database))
					.setText("Database:"+mItem.getDatabase());
			((TextView) rootView.findViewById(R.id.asset_detail_priority))
					.setText("Asset level:" + mItem.getItemlvl());
			((TextView) rootView.findViewById(R.id.asset_detail_location))
					.setText("Stored in:" + mItem.getBuilding() + " at room:"
							+ mItem.getRoom());
			((TextView) rootView.findViewById(R.id.asset_detial_type))
					.setText("Item type:" + mItem.getItemType());
			((Button) rootView.findViewById(R.id.asset_detail_update))
					.setText("Last updated by: " + mItem.getUpdater()
							+ " At time:" + mItem.getUpdateTime());

		} else {
			Log.e("mItem", "null");
		}
		return rootView;
	}
}
