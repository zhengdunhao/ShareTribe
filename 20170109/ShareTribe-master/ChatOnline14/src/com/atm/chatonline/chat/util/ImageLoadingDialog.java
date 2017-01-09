package com.atm.chatonline.chat.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.studentsystem01.R;

/**
 * 
 */
public class ImageLoadingDialog extends Dialog {

	public ImageLoadingDialog(Context context) {
		super(context, R.style.ImageloadingDialogStyle);
		//setOwnerActivity((Activity) context);
	}

	private ImageLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_imageloading);
	}

}