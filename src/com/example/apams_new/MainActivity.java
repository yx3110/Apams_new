package com.example.apams_new;

import java.io.Serializable;

import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apams_acc_package;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package.packageType;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnTaskCompleted,
		Serializable {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private String mUsername;
	private boolean isAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mUsername = bundle.getString("username");
		isAdmin = bundle.getBoolean("isAdmin");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		switch (position) {
		case 0:
			Account_frag accFrag = Account_frag.newAccInstance(mUsername,
					position + 1, isAdmin, this);
			fragmentManager.beginTransaction().replace(R.id.container, accFrag)
					.commit();
			break;
		case 1:
			Addnew_frag addFrag = Addnew_frag.newAddInstance(position + 1);
			fragmentManager.beginTransaction().replace(R.id.container, addFrag)
					.commit();
			break;
		case 2:
			map_frag mapFrag = map_frag.newMapInstance(position + 1);
			fragmentManager.beginTransaction().replace(R.id.container, mapFrag)
					.commit();
			break;
		case 3:
			scan_frag scanFrag = scan_frag.newScanInstance(position + 1);
			fragmentManager.beginTransaction()
					.replace(R.id.container, scanFrag).commit();
			break;
		}

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_account);
			break;
		case 2:
			mTitle = getString(R.string.title_addNew);
			break;
		case 3:
			mTitle = getString(R.string.title_Map);
			break;
		case 4:
			mTitle = getString(R.string.title_scan);
			break;
		}

	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void newPic(View view) {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
	}
	
	public void getDatabase(View view){
		String databases =null;
		
		//TODO: implement database seeking.
		
		
		AlertDialog.Builder builder = new Builder(this);
		
		if(databases.equals(null)){
			databases = "No database is found under you management";
		}
		builder.setMessage(databases);
		builder.setTitle("Databases under you management");
		builder.setNegativeButton("ok", null);
		
		builder.create().show();

	}

	private final int RESULT_LOAD_IMAGE = 1;

	public void changeUserPic(View view) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			final String picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageButton imageButton = (ImageButton) findViewById(R.id.user_image);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
			float realWidth = options.outWidth;
			float realHeight = options.outHeight;
			int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
			if (scale <= 0) {
				scale = 1;
			}
			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(picturePath, options);
			imageButton.setImageBitmap(bitmap);

		}
	}

	public void chooseItemType(View view) {

		final String[] choices = new String[] { "PC", "Printer", "Mac",
				"Others" };

		new AlertDialog.Builder(this).setTitle("Choose Item type")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setItems(choices, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String itemType = choices[which];
						Button chooseButton = (Button) findViewById(R.id.addChooseType);
						chooseButton.setHint(itemType);
					}

				}).setNegativeButton("Cancel", null).show();

	}

	public void createDatabase(View view) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.acc_create,
				(ViewGroup) findViewById(R.id.dialog));

		new AlertDialog.Builder(this).setTitle("Create Database")
				.setView(layout).setNegativeButton("Cancel", null).show();
	}

	public void sendCreate(View view) {
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void onTaskCompleted(String answer) {

	}

	@Override
	public void popMsg(String msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void onPackReceived(apams_network_package pack) {
	}
}