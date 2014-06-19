package com.example.apams_new;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apams_newUtil.Contents;
import com.example.apams_newUtil.InviteInfo;
import com.example.apams_newUtil.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

/**
 * A fragment representing a single Invitation code detail screen. This fragment
 * is either contained in a {@link InvitationcodeListActivity} in two-pane mode
 * (on tablets) or a {@link InvitationcodeDetailActivity} on handsets.
 */
public class InvitationcodeDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private InviteInfo mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public InvitationcodeDetailFragment() {
	}

	private HashMap<String, InviteInfo> inviteMap;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getActivity().getIntent();
		this.inviteMap = (HashMap<String, InviteInfo>) intent
				.getSerializableExtra("inviteMap");

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = inviteMap.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_invitationcode_detail, container, false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		this.setView();
	}

	public void setView() {
		if (mItem != null) {
			View rootView = this.getView();
			ImageButton imgb = (ImageButton) rootView
					.findViewById(R.id.invite_detail_qr);
			imgb.setImageBitmap(this.QRencode(mItem.getCode(),
					imgb.getLayoutParams().width, imgb.getLayoutParams().height));

			TextView belonglvl = (TextView) rootView
					.findViewById(R.id.invite_belongandlvl);
			TextView date = (TextView) rootView
					.findViewById(R.id.invite_detail_date);
			TextView activation = (TextView) rootView
					.findViewById(R.id.invite_activation);
			belonglvl.setText("Database: " + mItem.getBelongto()
					+ ";\nPriority level: " + mItem.getLevel() + ";");
			date.setText("Date of issue: " + mItem.getTime() + ";");
			String yes = mItem.getActivated() ? "Yes" : "No";
			String activatedBy = mItem.getActivated() ? mItem.getActivatedBy()
					: "N/A";
			activation.setText("Activated: " + yes + ";\nActivated by: "
					+ activatedBy + ";");
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
