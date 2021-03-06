package com.example.apams_new;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.apams_newUtil.InviteInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * An activity representing a list of Invitation codes. This activity has
 * different presentations for handset and tablet-size devices. On handsets, the
 * activity presents a list of items, which when touched, lead to a
 * {@link InvitationcodeDetailActivity} representing item details. On tablets,
 * the activity presents the list of items and item details side-by-side using
 * two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link InvitationcodeListFragment} and the item details (if present) is a
 * {@link InvitationcodeDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link InvitationcodeListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class InvitationcodeListActivity extends FragmentActivity implements
		InvitationcodeListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitationcode_list);

		if (findViewById(R.id.invitationcode_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((InvitationcodeListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.invitationcode_list))
					.setActivateOnItemClick(true);
		}

	}

	/**
	 * Callback method from {@link InvitationcodeListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(InvitationcodeDetailFragment.ARG_ITEM_ID, id);
			InvitationcodeDetailFragment fragment = new InvitationcodeDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.invitationcode_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					InvitationcodeDetailActivity.class);
			Intent curIntent = this.getIntent();
			Log.e("id",id);
			@SuppressWarnings("unchecked")
			ArrayList<InviteInfo> inviteList = (ArrayList<InviteInfo>) curIntent.getSerializableExtra("inviteList");
			@SuppressWarnings("unchecked")
			HashMap<String,InviteInfo> inviteMap = (HashMap<String,InviteInfo>) curIntent.getSerializableExtra("inviteMap");
			detailIntent.putExtra(InvitationcodeDetailFragment.ARG_ITEM_ID, id);
			detailIntent.putExtra("inviteList", inviteList);
			detailIntent.putExtra("inviteMap", inviteMap);
			startActivity(detailIntent);
		}
	}
}
