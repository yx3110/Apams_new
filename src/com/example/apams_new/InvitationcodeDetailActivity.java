package com.example.apams_new;

import java.util.HashMap;

import com.example.apams_newUtil.InviteInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

/**
 * An activity representing a single Invitation code detail screen. This
 * activity is only used on handset devices. On tablet-size devices, item
 * details are presented side-by-side with a list of items in a
 * {@link InvitationcodeListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link InvitationcodeDetailFragment}.
 */
public class InvitationcodeDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitationcode_detail);

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
			arguments.putString(
					InvitationcodeDetailFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(
							InvitationcodeDetailFragment.ARG_ITEM_ID));
			InvitationcodeDetailFragment fragment = new InvitationcodeDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.invitationcode_detail_container, fragment)
					.commit();
		}
	}

	public void shareQR(View view) {
		@SuppressWarnings("unchecked")
		String invite = ((HashMap<String, InviteInfo>) (this.getIntent()
				.getSerializableExtra("inviteMap"))).get(
				getIntent().getStringExtra(
						InvitationcodeDetailFragment.ARG_ITEM_ID)).getCode();
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_TEXT, invite);
		startActivity(Intent.createChooser(share, "Share invite code"));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}
}
