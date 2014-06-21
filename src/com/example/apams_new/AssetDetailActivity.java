package com.example.apams_new;

import java.util.HashMap;

import com.example.apams_newUtil.Contents;
import com.example.apams_newUtil.OnTaskCompleted;
import com.example.apams_newUtil.QRCodeEncoder;
import com.example.apams_newUtil.apamsTCPclient;
import com.example.apams_newUtil.apamsTCPclient_package;
import com.example.apams_newUtil.apams_network_package;
import com.example.apams_newUtil.apams_network_package.packageType;
import com.example.apams_newUtil.apams_report_package;
import com.example.apams_newUtil.assetItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a single Asset detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link AssetListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link AssetDetailFragment}.
 */
public class AssetDetailActivity extends FragmentActivity implements
		OnTaskCompleted {
	private assetItem mItem;
	private AssetDetailFragment frag;

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
			this.mItem = ((HashMap<String, assetItem>) this.getIntent()
					.getExtras().getSerializable("assetMap")).get(getIntent()
					.getStringExtra(AssetDetailFragment.ARG_ITEM_ID));
			arguments.putString(AssetDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(AssetDetailFragment.ARG_ITEM_ID));
			AssetDetailFragment fragment = new AssetDetailFragment();
			fragment.setArguments(arguments);
			this.frag = fragment;
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
		try {
			extra1 = mItem.getExtras().get(0);
			extra2 = mItem.getExtras().get(1);
			extra3 = mItem.getExtras().get(2);
			extra4 = mItem.getExtras().get(3);
			extra5 = mItem.getExtras().get(4);

		} catch (Exception e) {

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

	public void reportMissing(View view) {
		apams_network_package pack = new apams_report_package(
				mItem.getItemName(), mItem.getDatabase(), !mItem.getMissing(),
				packageType.REPORTMISS);
		apamsTCPclient client = new apamsTCPclient(this);
		client.execute(pack);
	}

	public void printQR(View view) {
		Bitmap bitmap = this.QRencode(mItem.getQRString(), 45, 45);
		Log.e("QRSTRING", mItem.getQRString());
		this.printDialog(bitmap);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	@Override
	public void onTaskCompleted(String answer) {
		if (answer.contains("MISSREPORTED")) {
			this.popMsg("Miss is reported");
			this.mItem.setMissing(!mItem.getMissing());
			((Button) this.frag.getView().findViewById(
					R.id.asset_detail_missing)).setText("Item is missing: "
					+ mItem.getMissing());
		}
	}

	@Override
	public void onPackReceived(apams_network_package pack) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Location map");
		ImageView imgview = new ImageView(this);
		byte[] pic = pack.getPic();
		Bitmap bitpic = new BitmapFactory().decodeByteArray(pic, 0, pic.length);
		imgview.setImageBitmap(bitpic);
		builder.setView(imgview);
		builder.setNegativeButton("OK", null);
		builder.show();
	}

	@Override
	public void popMsg(String msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void showLocMap(View view){
		
		apams_network_package pack = new apams_network_package(
				mItem.getItemName(),mItem.getDatabase(), packageType.GETLOCPIC);
		apamsTCPclient_package task = new apamsTCPclient_package(this);
		task.execute(pack);
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
						mItem.getItemName(), bitmap);
			}

		});
		((ImageView) layout.findViewById(R.id.singleqr)).setImageBitmap(bitmap);
		builder.create().show();

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
