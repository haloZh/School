package com.ronguan.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.ronguan.R;

public class ChargeDialog extends Dialog implements OnClickListener {
	private Button bt_buynow;
	private ChargeDialogListener listener;
	public ChargeDialog(Context context, int theme,ChargeDialogListener chargeDialogListener) {
		super(context, theme);
		this.listener=chargeDialogListener;
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		listener.onClick(v);
	}

	public interface ChargeDialogListener {
		public void onClick(View view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_dialog);
		initwidget();// 初始化控件
	}

	private void initwidget() {
		// TODO 自动生成的方法存根
		bt_buynow=(Button)findViewById(R.id.bt_buynow);  
		bt_buynow.setOnClickListener(this);
	}
	
}
