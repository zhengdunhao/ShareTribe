package com.atm.chatonline.bbs.activity.login;
/**
 * 该类用于，生成注册页面，选择用户为“老师”或“学生”
 * 2015.7.21,atm--墩
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.studentsystem01.R;

public class RegisterChooseView extends Activity {
	private Button btnStudent;
	private Button btnTeacher;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_choose_view);
		
		btnTeacher=(Button)findViewById(R.id.btn_teacher);
		btnStudent=(Button)findViewById(R.id.btn_student);
		
		Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.in_from_down); //这里我使用/res/layout/anim里的文件
		btnTeacher.startAnimation(translateAnimation);
		btnStudent.startAnimation(translateAnimation);
		
		btnTeacher.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent01=new Intent(RegisterChooseView.this,RegisterTeacherView.class);
				startActivity(intent01);
			}
		});
		btnStudent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent02=new Intent(RegisterChooseView.this,RegisterStudentView.class);
				startActivity(intent02);
			}
		});
		
	}
}




