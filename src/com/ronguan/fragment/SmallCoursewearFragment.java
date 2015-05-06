package com.ronguan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ronguan.R;
import com.ronguan.activity.VedioOnline;

public class SmallCoursewearFragment extends BaseFragment implements OnClickListener {
	private Context context;

	private TextView tvTittle;
	
	private LinearLayout llayout;

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
		View view = inflater.inflate(R.layout.layout_fragment_smallcoursewear,
				container, false);
		
		initView(view);

		return view;
	}

	void initView(View view) {
		tvTittle=(TextView)view.findViewById(R.id.tv_top_title);
		tvTittle.setText("培训课件");
		
		llayout = (LinearLayout) view.findViewById(R.id.llayout);
		llayout.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.llayout:
			SmallCoursewearContentFragment coursewear = new SmallCoursewearContentFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.realtabcontent, coursewear);
			ft.addToBackStack(null);
			ft.commit();
			break;
		default:
			break;
		}
	}
}
