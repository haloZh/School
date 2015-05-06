package com.ronguan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ronguan.R;

public class SecondeTabFragment extends BaseFragment implements OnClickListener {
	private Context context;

	private TextView tvTittle, tv1, tv2;

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
		View view = inflater.inflate(R.layout.layout_second_fragment,
				container, false);

		initView(view);

		return view;
	}

	void initView(View view) {
		tvTittle = (TextView) view.findViewById(R.id.tv_top_title);
		tvTittle.setText(getResources().getString(R.string.tittle_kejian));
		tv1 = (TextView) view.findViewById(R.id.tv_sfragment_coursewear);
		tv2 = (TextView) view.findViewById(R.id.tv_sfragment_coursewear_small);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_sfragment_coursewear:
			CoursewearFragment select = new CoursewearFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.realtabcontent, select);
			ft.addToBackStack(null);
			ft.commit();
			return;
		case R.id.tv_sfragment_coursewear_small:
			SmallCoursewearFragment coursewear = new SmallCoursewearFragment();
			FragmentTransaction ft1 = getFragmentManager().beginTransaction();
			ft1.add(R.id.realtabcontent, coursewear);
			ft1.addToBackStack(null);
			ft1.commit();
			break;
		default:
			break;
		}
	}
}
