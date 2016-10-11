package com.atm.chatonline.bbs.activity.login;
/**
 * 该类用于，生成注册界面的Agreement
 * 2015.7.21,atm--墩
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.studentsystem01.R;

public class RegisterAgreementView extends Activity{
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_agreement_view);
		
	}
}
