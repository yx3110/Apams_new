package com.example.apams_new;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.apams_newUtil.Contents;
import com.example.apams_newUtil.InviteInfo;
import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.QRCodeEncoder;
import com.example.apams_newUtil.apamsTCPclient;
import com.example.apams_newUtil.apamsTCPclient_package;
import com.example.apams_newUtil.apams_assetAdd_package;
import com.example.apams_newUtil.apams_assetQuery_package;
import com.example.apams_newUtil.apams_datalist_package;
import com.example.apams_newUtil.apams_inviteCreate_package;
import com.example.apams_newUtil.apams_inviteManage_package;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_update_package;
import com.example.apams_newUtil.apams_network_package.packageType;
import com.example.apams_newUtil.apams_network_package_create;
import com.example.apams_newUtil.apams_profile_package;
import com.example.apams_newUtil.assetItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

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
import android.support.v4.print.PrintHelper;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	private String database;

	private View createLayout;
	private View inviteLayout;
	private View QRresultLayout;

	private assetItem curItem;

	private View addExtraLayout;

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
		this.updatePack = new apams_update_package(mUsername);

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
			Addnew_frag addFrag = Addnew_frag.newAddInstance(position + 1,
					isAdmin);
			fragmentManager.beginTransaction().replace(R.id.container, addFrag)
					.commit();
			break;
		case 2:
			view_frag viewFrag = view_frag.newViewInstance(mUsername,
					position + 1, isAdmin, datalist);
			fragmentManager.beginTransaction()
					.replace(R.id.container, viewFrag).commit();
			break;

		case 3:
			scan_frag scanFrag = scan_frag.newScanInstance(position + 1);
			fragmentManager.beginTransaction()
					.replace(R.id.container, scanFrag).commit();
			break;
		/*
		 * case 4: map_frag mapFrag = map_frag.newMapInstance(position + 1);
		 * fragmentManager.beginTransaction().replace(R.id.container, mapFrag)
		 * .commit(); break;
		 */
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
			mTitle = getString(R.string.title_View);
			break;
		case 4:
			mTitle = getString(R.string.title_scan);
			break;
		case 5:
			mTitle = getString(R.string.title_Map);
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

	public void setDatabase(String database) {
		this.database = database;
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

	public void assetQuery(View view) {
		String queryDatabase;
		if (this.isAdmin) {
			Spinner dataSpinner = (Spinner) this
					.findViewById(R.id.inspect_spinner);
			queryDatabase = (String) dataSpinner.getSelectedItem();
		} else {
			queryDatabase = this.database;
		}
		apams_network_package pack = new apams_assetQuery_package(
				this.mUsername, queryDatabase);
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

		if (!isEmpty((EditText) this.findViewById(R.id.addManufacturer))) {
			curItem.setManufacturer(((EditText) this
					.findViewById(R.id.addManufacturer)).getText().toString());
		} else {
			this.popMsg("Please enter the manufacturer of the asset.");
			return;
		}

		if (!isEmpty((EditText) this.findViewById(R.id.addModel))) {
			curItem.setModel(((EditText) this.findViewById(R.id.addModel))
					.getText().toString());
		} else {
			this.popMsg("Please enter the model of the asset.");
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
		} else {
			this.popMsg("Please enter your CID number.");
			return;
		}
		if (!isEmpty((EditText) this.findViewById(R.id.addNewName))) {
			curItem.setItemName(((EditText) this.findViewById(R.id.addNewName))
					.getText().toString());
		} else {
			this.popMsg("Please enter your Item name.");
			return;
		}
		if (curItem.getPic() == null) {
			this.popMsg("Please take a picture of the asset.");
		}else{
			((Button) this.findViewById(R.id.addTakePicture)).setHint(R.string.addPic);
		}
		if (curItem.getItemType() == null) {
			this.popMsg("Please choose a type of the asset.");
		}else{
			((Button) this.findViewById(R.id.addChooseType)).setHint(R.string.addType);
		}
		if (curItem.getQRString() == null) {
			this.popMsg("Please generate a QR code for the asset.");
		}else{
			((Button) this.findViewById(R.id.add_generateQR)).setHint(R.string.add_generateQR);
		}
		if (this.isAdmin && curItem.getDatabase() == null) {
			this.popMsg("Please select a target database for the item.");
		}else{
			((Button) this.findViewById(R.id.add_chooseData)).setHint(R.string.add_chooseData);
		}

		apams_network_package pack = new apams_assetAdd_package(mUsername,
				curItem);
		apamsTCPclient client = new apamsTCPclient(this);
		client.execute(pack);

	}

	// Setting extra informations when add new asset item;
	public void setExtras(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Maximum 5 Extra informations are allowed");
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.add_extras,
				(ViewGroup) findViewById(R.id.dialog));
		this.addExtraLayout = layout;
		builder.setView(layout);
		builder.setNegativeButton("Cancel", null);
		builder.show();
	}

	// confirm extra setting;

	public void confirmExtra(View view) {
		int counter = 0;
		if (!isEmpty((EditText) addExtraLayout.findViewById(R.id.extras_1))) {
			curItem.addExtra(((EditText) addExtraLayout.findViewById(R.id.extras_1))
					.getText().toString());
			counter++;
		}
		if (!isEmpty((EditText) addExtraLayout.findViewById(R.id.extras_2))) {
			curItem.addExtra(((EditText) addExtraLayout.findViewById(R.id.extras_2))
					.getText().toString());
			counter++;
		}
		if (!isEmpty((EditText) addExtraLayout.findViewById(R.id.extras_3))) {
			curItem.addExtra(((EditText) addExtraLayout.findViewById(R.id.extras_3))
					.getText().toString());
			counter++;
		}
		if (!isEmpty((EditText) addExtraLayout.findViewById(R.id.extras_4))) {
			curItem.addExtra(((EditText) addExtraLayout.findViewById(R.id.extras_4))
					.getText().toString());
			counter++;
		}
		if (!isEmpty((EditText) addExtraLayout.findViewById(R.id.extras_5))) {
			curItem.addExtra(((EditText) addExtraLayout.findViewById(R.id.extras_5))
					.getText().toString());
			counter++;
		}
		this.popMsg("In total " + counter + " extra informations added");
	}

	public void chooseTargetData(View view) {
		String[] choices = new String[this.datalist.size()];
		choices = this.datalist.toArray(choices);
		final String[] fchoices = choices;

		new AlertDialog.Builder(this).setTitle("Choose Item type")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setItems(fchoices, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String itemType = fchoices[which];
						Button chooseButton = (Button) findViewById(R.id.add_chooseData);
						Log.e("Button", "Button selected");
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
		((Button) this.findViewById(R.id.add_generateQR))
				.setHint("QR generated,touch to generate again");
		this.popMsg("New QR code have been generated");
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
			this.popMsg("No database is found under you management.");
			return;
		}
		builder.setMessage(databases);
		builder.setTitle("Databases under you management");
		builder.setNegativeButton("ok", null);

		builder.create().show();

	}

	private final int RESULT_LOAD_IMAGE = 1;
	private final int RESULT_TAKE_PIC = 2;
	private final int RESULT_QR_SCAN = 3;
	private final int RESULT_UPDATE_PIC = 4;

	public void apams_scan_barcode(View view) {
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
					"QR_CODE_MODE");
			startActivityForResult(intent, RESULT_QR_SCAN);
		} catch (Exception e) {
			this.popMsg("You need to install the Zxing barcode scanner first");
			Intent goToMarket = new Intent(Intent.ACTION_VIEW)
					.setData(Uri
							.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android"));
			startActivity(goToMarket);
		}
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
		if (requestCode == RESULT_UPDATE_PIC && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			this.updatePack.setNewPic(byteArray);
			QRresultLayout.findViewById(R.id.qrquery_confirm_update)
					.setVisibility(View.VISIBLE);
			QRresultLayout.findViewById(R.id.qrquery_confirm_update)
					.setEnabled(true);
		}
		if (requestCode == RESULT_QR_SCAN && resultCode == RESULT_OK) {
			String contents = data.getStringExtra("SCAN_RESULT");
			apams_network_package pack = new apams_assetQuery_package(
					mUsername, contents, isAdmin);
			apamsTCPclient_package task = new apamsTCPclient_package(this);
			task.execute(pack);
		}

		if (requestCode == RESULT_TAKE_PIC && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			((Button) this.findViewById(R.id.addTakePicture))
					.setHint("Picture taken,touch here to take again");
			;
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

	protected void printDialog(final Bitmap bitmap) {
		AlertDialog.Builder builder = new Builder(this);
		final Activity main = this;
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.singleqr,
				(ViewGroup) findViewById(R.id.dialog));

		builder.setMessage("Do you want to print the QR code for this item now?");
		builder.setTitle("Print");
		builder.setView(layout);
		builder.setNegativeButton("No", null);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				PrintHelper photoPrinter = new PrintHelper(main);
				photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
				photoPrinter.printBitmap(
						((EditText) findViewById(R.id.addNewName)).getText()
								.toString(), bitmap);
			}

		});
		((ImageView) layout.findViewById(R.id.singleqr)).setImageBitmap(bitmap);
		builder.create().show();

	}

	@Override
	public void onTaskCompleted(String answer) {
		if (answer.contains("UPDATEDONE")) {
			this.popMsg("Updated,Please re-scan code for new item details");
		} else if (answer.contains("PriorityTooLow")) {
			this.popMsg("Priority too low, please lower the priority of this asset");
			findViewById(R.id.add_itemlvl).requestFocus();
		} else if (answer.contains("ASSETADDED")) {
			popMsg("New Item added into database");
			Bitmap bitmap = this.QRencode(curItem.getQRString(), 45, 45);
			this.printDialog(bitmap);

			Button typeButton = (Button) findViewById(R.id.addChooseType);
			typeButton.setText(R.string.add_chooseData);
			Button pictureItem = (Button) findViewById(R.id.addTakePicture);
			pictureItem.setText(R.string.addPic);
			Button QRButton = (Button) findViewById(R.id.add_generateQR);
			QRButton.setText(R.string.add_generateQR);
			this.curItem = new assetItem();
		} else if (answer.contains("GOOD")) {
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

	// QR SCAN RESULT BUTTON FUNCTIONS
	private apams_update_package updatePack;
	private assetItem curQRresult;

	public void reportBroken(View view) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("Set Item Broken");
		builder.setMessage("Is this item Broken?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				updatePack.setBroken(true);
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				updatePack.setBroken(false);
			}
		});
		builder.show();
		QRresultLayout.findViewById(R.id.qrquery_confirm_update).setVisibility(
				View.VISIBLE);
		QRresultLayout.findViewById(R.id.qrquery_confirm_update).setEnabled(
				true);
	}

	public void qrImage(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, RESULT_UPDATE_PIC);
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
		extra1 = this.curQRresult.getExtras().get(0);
		extra2 = this.curQRresult.getExtras().get(1);
		extra3 = this.curQRresult.getExtras().get(2);
		extra4 = this.curQRresult.getExtras().get(3);
		extra5 = this.curQRresult.getExtras().get(4);
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
		builder.show();
	}

	public void qrBuilding(View view) {
		final EditText building = new EditText(this);

		building.setHint("New Building");
		AlertDialog.Builder builder = new Builder(this);
		builder.setNegativeButton("Cancel", null);
		builder.setView(building);
		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!isEmpty(building)) {
							updatePack.setNewBuilding(building.getText()
									.toString());
							QRresultLayout.findViewById(
									R.id.qrquery_confirm_update).setVisibility(
									View.VISIBLE);
							QRresultLayout.findViewById(
									R.id.qrquery_confirm_update).setEnabled(
									true);

						}
					}
				});
		builder.show();
	}

	public void qrRoom(View view) {
		final EditText room = new EditText(this);

		room.setHint("New Building");
		AlertDialog.Builder builder = new Builder(this);
		builder.setNegativeButton("Cancel", null);
		builder.setView(room);
		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!isEmpty(room)) {
							updatePack
									.setNewBuilding(room.getText().toString());
							QRresultLayout.findViewById(
									R.id.qrquery_confirm_update).setVisibility(
									View.VISIBLE);
							QRresultLayout.findViewById(
									R.id.qrquery_confirm_update).setEnabled(
									true);
						}
					}
				});
		builder.show();
	}

	public void qrPriority(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final EditText priority = new EditText(this);
		priority.setHint("New Priority Level");

		builder.setNegativeButton("Cancel", null);
		builder.setView(priority);
		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!isEmpty(priority)) {
							updatePack.setNewLvl(Integer.parseInt(priority
									.getText().toString()));
							QRresultLayout.findViewById(
									R.id.qrquery_confirm_update).setVisibility(
									View.VISIBLE);
							QRresultLayout.findViewById(
									R.id.qrquery_confirm_update).setEnabled(
									true);
						}
					}
				});
		builder.show();
	}

	public void qrConfirmUpdate(View view) {
		this.updatePack.setDatabase(this.curQRresult.getDatabase());
		this.updatePack.setItemName(this.curQRresult.getItemName());
		apams_network_package pack = this.updatePack;
		apamsTCPclient task = new apamsTCPclient(this);
		task.execute(pack);
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
			ArrayList<InviteInfo> inviteInfoList = ((apams_inviteManage_package) pack)
					.getInfo();
			HashMap<String, InviteInfo> inviteInfoMap = ((apams_inviteManage_package) pack)
					.getMap();
			if (inviteInfoList.size() == 1
					&& inviteInfoList.get(0).getCode().contains("NOINVITE")) {
				this.popMsg("No invite is found");
				return;
			}
			Log.e("mapSize",
					inviteInfoMap.size() + "" + "code" + inviteInfoMap.get("1"));
			Intent intent = new Intent(this, InvitationcodeListActivity.class);
			intent.putExtra("inviteList", inviteInfoList);
			intent.putExtra("inviteMap", inviteInfoMap);
			this.startActivity(intent);
			break;
		case QRRESULT:
			assetItem item = ((apams_assetQuery_package) pack).getItem();

			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.asset_detail,
					(ViewGroup) findViewById(R.id.dialog));
			this.QRresultLayout = layout;
			ImageView img = (ImageView) layout.findViewById(R.id.qrquery_pic);
			byte[] byteapic = item.getPic();
			Bitmap bitpic = new BitmapFactory().decodeByteArray(byteapic, 0,
					byteapic.length);
			img.setImageBitmap(bitpic);
			this.curQRresult = item;

			((Button) layout.findViewById(R.id.qrquery_broken)).setText("Item broken:"+item
					.isBroken());
			((Button) layout.findViewById(R.id.qrquery_missing)).setText("Item missing:"+item
					.getMissing());

			Button assettype = (Button) layout.findViewById(R.id.qrquery_type);
			Button database = (Button) layout
					.findViewById(R.id.qrquery_database);
			Button building = (Button) layout
					.findViewById(R.id.qrquery_building);
			Button room = (Button) layout.findViewById(R.id.qrquery_room);
			Button itemlvl = (Button) layout.findViewById(R.id.qrquery_itemlvl);
			Button time = (Button) layout.findViewById(R.id.qrquery_time);
			Button confirm = (Button) layout
					.findViewById(R.id.qrquery_confirm_update);
			confirm.setVisibility(View.INVISIBLE);
			confirm.setEnabled(false);
			assettype.setText("Type:" + item.getItemType());
			database.setText("Database:" + item.getDatabase());
			building.setText("Located in building:" + item.getBuilding());
			room.setText("Located in room:" + item.getRoom());
			itemlvl.setText("Priority level:" + item.getItemlvl());
			time.setText("Last time updated: " + item.getUpdateTime()
					+ "\nLast Updater: " + item.getUpdater());
			new AlertDialog.Builder(this).setTitle(item.getItemName())
					.setView(layout).setNegativeButton("OK", null).show();
			break;
		case ASSETRESULT:
			Intent AssetIntent = new Intent(this, AssetListActivity.class);
			apams_assetQuery_package RSpack = (apams_assetQuery_package) pack;
			AssetIntent.putExtra("assetList", RSpack.getItemList());
			AssetIntent.putExtra("assetMap", RSpack.getItemMap());
			this.startActivity(AssetIntent);
			break;

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

	public Bitmap QRencode(String content, int width, int height) {
		int smallerDimension = width < height ? width : height;
		smallerDimension = smallerDimension * 3 / 4;
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(content, null,
				Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
				smallerDimension);
		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;

	}
}