package com.ronguan.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.ronguan.R;
import com.ronguan.activity.VedioOnline;

public class SmallCoursewearContentFragment extends BaseFragment implements
		OnClickListener {
	private Context context;

	private TextView tvTittle;

	private WebView web;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		context = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.smallcoursewear_content,
				container, false);

		initView(view);

		return view;
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	void initView(View view) {
		tvTittle = (TextView) view.findViewById(R.id.tv_top_title);
		tvTittle.setText("微课件");

		web = (WebView) view.findViewById(R.id.web);
		WebSettings webSettings = web.getSettings();
		webSettings.setJavaScriptEnabled(true);

		web.loadUrl("http://so.100xuexi.com/QuestionSearch.aspx?wordKey="
				+ "图");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
