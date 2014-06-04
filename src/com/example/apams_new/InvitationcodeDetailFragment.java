package com.example.apams_new;

import java.io.File;
import java.util.ArrayList;
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

import com.example.apams_newUtil.InviteInfo;
import com.example.apams_newUtil.QRencoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
	private static final String IMAGE_FORMAT = "png";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private InviteInfo mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public InvitationcodeDetailFragment() {
	}

	private ArrayList<InviteInfo> inviteList;
	private HashMap<String, InviteInfo> inviteMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getActivity().getIntent();
		this.inviteList = (ArrayList<InviteInfo>) intent
				.getSerializableExtra("inviteList");
		this.inviteMap = (HashMap<String, InviteInfo>) intent
				.getSerializableExtra("inviteMap");

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = inviteMap.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_invitationcode_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			ImageButton imgb = (ImageButton) rootView
					.findViewById(R.id.invite_detail_qr);
			try {
				Bitmap bitmap = QRencoder.encodeAsBitmap(mItem.getCode(), "",
						BarcodeFormat.QR_CODE, imgb.getLayoutParams().width,
						imgb.getLayoutParams().height);
				imgb.setImageBitmap(bitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
			
			TextView belonglvl = (TextView) rootView.findViewById(R.id.invite_belongandlvl);
			TextView date = (TextView) rootView.findViewById(R.id.invite_detail_date);
			TextView activation = (TextView) rootView.findViewById(R.id.invite_activation);
			belonglvl.setText(mItem.getBelongto()+mItem.getLevel());
			date.setText("Date of issue: "+mItem.getTime());
			String yes = mItem.getActivated()?"Yes":"No";
			String activatedBy = mItem.getActivated()?mItem.getActivatedBy():"N/A";
			activation.setText("Activated: "+ yes+" Activated by: "+activatedBy);


		}

		return rootView;
	}
}
