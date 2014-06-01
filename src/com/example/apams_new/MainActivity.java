package com.example.apams_new;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;

import com.example.apams_newUtil.InviteInfo;
import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.apamsTCPclient;
import com.example.apams_newUtil.apamsTCPclient_package;
import com.example.apams_newUtil.apams_asset_package;
import com.example.apams_newUtil.apams_datalist_package;
import com.example.apams_newUtil.apams_inviteCreate_package;
import com.example.apams_newUtil.apams_inviteManage_package;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package.packageType;
import com.example.apams_newUtil.apams_network_package_create;
import com.example.apams_newUtil.apams_profile_package;
import com.example.apams_newUtil.assetItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnTaskCompleted {

	/**
	 * Fragment managing the behaviours, interactions and presentation of the
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
	private ArrayList<String> datalist;
	private ArrayList<String> lvllist;
	private ArrayList<InviteInfo> inviteInfos;

	private View createLayout;
	private View inviteLayout;

	private assetItem curItem;

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
		datalist = new ArrayList<String>();
		lvllist = new ArrayList<String>();
		this.curItem = new assetItem();

		apams_network_package pack = new apams_network_package(mUsername,
				packageType.DATALIST);
		apamsTCPclient_package task = new apamsTCPclient_package(this);
		task.execute(pack);

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
					position + 1, isAdmin);
			fragmentManager.beginTransaction().replace(R.id.container, accFrag)
					.commit();
			break;
		case 1:
			Addnew_frag addFrag = Addnew_frag.newAddInstance(position + 1,isAdmin);
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

	public void createInvite(View view) {

		if (this.datalist.size() == 0) {
			popMsg("You need to create a database first");
			return;
		}
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.acc_invitecreate,
				(ViewGroup) findViewById(R.id.dialog));

		this.inviteLayout = layout;
		Spinner spinner = (Spinner) layout
				.findViewById(R.id.invite_dataspinner);
		String[] dataarray = new String[datalist.size()];
		dataarray = datalist.toArray(dataarray);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dataarray);
		spinner.setAdapter(adapter);
		new AlertDialog.Builder(this).setTitle("Create new APAMS Invite code")
				.setView(layout).show();
		this.addListenerToInviteSpinner(lvllist, layout);

	}

	private void addListenerToInviteSpinner(ArrayList<String> lvllist,
			final View layout) {
		Spinner spinner = (Spinner) this.inviteLayout
				.findViewById(R.id.invite_dataspinner);

		final ArrayList<String> flvllist = lvllist;
		final View flayout = layout;

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				((TextView) flayout.findViewById(R.id.invite_maxlvl))
						.setText("Max priority level = " + flvllist.get(pos));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
	}

	public void generateCode(View view) {
		SecureRandom random = new SecureRandom();
		String answer = new BigInteger(200, random).toString(32).substring(0,
				14);
		((Button) view).setText(answer);
		this.popMsg("Code generated but not activiated,press the same button to generate again");
	}

	public void confirmInvite(View view) {
		View layout = this.inviteLayout;
		String code = ((Button) layout.findViewById(R.id.invite_generate))
				.getText().toString();
		String belongto = (String) ((Spinner) layout
				.findViewById(R.id.invite_dataspinner)).getSelectedItem();
		int maxLvl = Integer.parseInt(this.lvllist.get(((Spinner) layout
				.findViewById(R.id.invite_dataspinner))
				.getSelectedItemPosition()));
		int level = Integer.parseInt(((EditText) layout
				.findViewById(R.id.invite_lvl)).getText().toString());
		if (level > maxLvl) {
			popMsg("Current priority level is higher than max priority level");
			layout.findViewById(R.id.invite_lvl).requestFocus();
		} else {
			apams_network_package pack = new apams_inviteCreate_package(
					mUsername, code, belongto, level);
			apamsTCPclient task = new apamsTCPclient(this);
			task.execute(pack);
		}
	}

	public void shareInvite(View view) {
		String invite = ((Button) this.inviteLayout
				.findViewById(R.id.invite_generate)).getText().toString();
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_TEXT, invite);
		startActivity(Intent.createChooser(share, "Share invite code"));
	}

	public void manageInvite(View view) {
		apams_network_package pack = new apams_network_package(mUsername,
				packageType.INVITEMANAGE);
		apamsTCPclient_package task = new apamsTCPclient_package(this);
		task.execute(pack);

	}

	public void confirmAddItem(View view) {
		assetItem curItem = this.curItem;
		if (!isEmpty((EditText) this.findViewById(R.id.addBuilding))) {
			curItem.setBuilding(((EditText) this.findViewById(R.id.addBuilding))
					.getText().toString());
		} else {
			this.popMsg("Please enter the building containing the asset.");
			return;
		}
		if (!isEmpty((EditText) this.findViewById(R.id.add_itemlvl))) {
			curItem.setItemlvl(Integer.parseInt(((EditText) this
					.findViewById(R.id.add_itemlvl)).getText().toString()));
		} else {
			this.popMsg("Please enter the Apams priority level the asset.");
			return;
		}

		if (!isEmpty((EditText) this.findViewById(R.id.addRoom))) {
			curItem.setRoom(((EditText) this.findViewById(R.id.addRoom))
					.getText().toString());
		} else {
			this.popMsg("Please enter the room containing the asset.");
			return;
		}
		if (!isEmpty((EditText) this.findViewById(R.id.addCID))) {
			curItem.setBuilding(((EditText) this.findViewById(R.id.addCID))
					.getText().toString());
		} else {
			this.popMsg("Please enter your CID number.");
			return;
		}
		if(curItem.getPic() == null){
			this.popMsg("Please take a picture of the asset.");
		}
		if(curItem.getItemType()==null){
			this.popMsg("Please choose a type of the asset.");
		}
		if(curItem.getQRString() == null){
			this.popMsg("Please generate a QR code for the asset.");
		}
		if(this.isAdmin&&curItem.getDatabase() == null){
			this.popMsg("Please select a target database for the item.");
		}
		
		apams_network_package pack = new apams_asset_package(mUsername,curItem);
		apamsTCPclient client = new apamsTCPclient(this);
		client.execute(pack);
		
	}
	public void chooseTargetData(View view){
		String[] choices = new String[this.datalist.size()];
		choices = this.datalist.toArray(choices);
		final String[] fchoices = choices;
		
		new AlertDialog.Builder(this).setTitle("Choose Item type")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setItems(fchoices, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				String itemType = fchoices[which];
				Button chooseButton = (Button) findViewById(R.id.addChooseType);
				chooseButton.setHint(itemType);
				curItem.setDatabase(itemType);
			}

		}).setNegativeButton("Cancel", null).show();
	}

	private boolean isEmpty(EditText ET) {
		String test = ET.getText().toString().trim();
		if (test.isEmpty() || test.length() == 0 || test.equals("")
				|| test == null) {
			return true;
		} else
			return false;
	}

	public void generateQR(View view) {
		SecureRandom random = new SecureRandom();
		String codeString = new BigInteger(200, random).toString(32).substring(
				0, 14);
		curItem.setQRString(codeString);
	}

	public void getDatabase(View view) {
		String databases = "Name of databases under management: \n";

		Log.e("length", "" + this.datalist.size());

		for (int i = 0; i < datalist.size(); i++) {
			databases = databases + datalist.get(i) + "," + "Max Lvl:"
					+ lvllist.get(i) + "\n";
		}

		AlertDialog.Builder builder = new Builder(this);

		if (this.datalist.isEmpty()) {
			databases = "No database is found under you management.";
		}
		builder.setMessage(databases);
		builder.setTitle("Databases under you management");
		builder.setNegativeButton("ok", null);

		builder.create().show();

	}

	private final int RESULT_LOAD_IMAGE = 1;
	private final int RESULT_TAKE_PIC = 2;
	private final int RESULT_QR = 3;

	public void apams_scan_barcode(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, RESULT_QR);
	}

	public void newPic(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, RESULT_TAKE_PIC);
		}
	}

	public void changeUserPic(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_QR && resultCode == RESULT_OK) {
			// TODO: QR code reading handle;
			
		}

		if (requestCode == RESULT_TAKE_PIC && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			this.curItem.setPic(byteArray);
		}
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
			int targetW = imageButton.getWidth();
			int targetH = imageButton.getHeight();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picturePath, options);
			int photoW = options.outWidth;
			int photoH = options.outHeight;
			int scale = Math.min(photoW / targetW, photoH / targetH);

			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
			imageButton.setImageBitmap(bitmap);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			apams_network_package pack = new apams_profile_package(mUsername,
					byteArray);
			apamsTCPclient task = new apamsTCPclient(this);
			task.execute(pack);
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
						curItem.setItemType(itemType);
					}

				}).setNegativeButton("Cancel", null).show();

	}

	public void createDatabase(View view) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.acc_create,
				(ViewGroup) findViewById(R.id.dialog));

		this.createLayout = layout;

		new AlertDialog.Builder(this).setTitle("Create Database")
				.setView(layout).setNegativeButton("Quit creating", null)
				.show();
	}

	public void sendCreate(View view) {
		String dataname = ((TextView) this.createLayout
				.findViewById(R.id.ET_acc_dataName)).getText().toString();
		int maxLvl = Integer.parseInt(((TextView) this.createLayout
				.findViewById(R.id.ET_acc_maxLvl)).getText().toString());
		apams_network_package pack = new apams_network_package_create(
				mUsername, maxLvl, dataname);
		apamsTCPclient task = new apamsTCPclient(this);
		task.execute(pack);
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
		
		if(answer.contains("ASSETADDED")){
			popMsg("New Item added into database");
			this.curItem = new assetItem();
		}
		if (answer.contains("GOOD")) {
			popMsg("Database created!Stay in this dialog to create more or click quit if you are finished.");
			apams_network_package pack = new apams_network_package(mUsername,
					packageType.DATALIST);
			apamsTCPclient_package task = new apamsTCPclient_package(this);
			task.execute(pack);
		} else if (answer.contains("UPDATED")) {
			popMsg("Profile pic updated in database");
		} else if (answer.contains("INVITED")) {
			Button share = (Button) this.inviteLayout
					.findViewById(R.id.invite_share);
			share.setVisibility(View.VISIBLE);
			share.setEnabled(true);
			popMsg("Invite code created.Stay in this dialog to create more or click quit if you are finished.");
		} else {
			popMsg("Error");
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

	@Override
	public void onPackReceived(apams_network_package pack) {
		packageType type = pack.getType();
		switch (type) {
		case DATALIST:
			apams_datalist_package dataPack = (apams_datalist_package) pack;
			this.datalist = dataPack.getDatalist();
			this.lvllist = dataPack.getLvllist();
			break;
		case INVITEMANAGE:
			ArrayList<InviteInfo> inviteInfos = ((apams_inviteManage_package) pack)
					.getInfo();
			Log.e("listSize", inviteInfos.size() + "");
			Intent intent = new Intent(this, InvitationcodeListActivity.class);
			intent.putExtra("inviteinfos", inviteInfos);
			this.startActivity(intent);
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Quitting APAMS")
					.setMessage(
							"Are you sure you want to log out and quit APAMS?")
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// Stop the activity
									MainActivity.this.finish();
								}

							}).setNegativeButton("Cancel", null).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}