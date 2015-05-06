package com.ronguan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.ronguan.R;
import com.umeng.message.PushAgent;


/**
 * 加载页面
 * 
 * @author zhou
 * 
 */
public class LoadingActivity extends Activity {
	private static final int DELAYMILLIS = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loading);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉系统的标题栏
		Intent service = new Intent();
//		Intent.setClass(LoadingActivity.this, ShengCaiService.class);
		startService(service);
		PushAgent.getInstance(this).onAppStart();
		// 默认显示.5s 然后跳转到广告页面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(LoadingActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, DELAYMILLIS);
	}
}
