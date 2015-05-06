package com.ronguan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ronguan.R;

public class FouthTabFragment extends BaseFragment implements OnClickListener {
	private Context context;

	private TextView tvTittle, tv1, tv2,tv3,tv4;

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
		View view = inflater.inflate(R.layout.layout_fouth_fragment,
				container, false);

		initView(view);

		return view;
	}

	void initView(View view) {
		tvTittle = (TextView) view.findViewById(R.id.tv_top_title);
		tvTittle.setText(getResources().getString(R.string.tittle_kejian));
		tv1 = (TextView) view.findViewById(R.id.tv_fofragment_test);
		tv2 = (TextView) view.findViewById(R.id.tv_fofragment_smalltest);
		tv3 = (TextView) view.findViewById(R.id.tv_fofragment_rank);
		tv4 = (TextView) view.findViewById(R.id.tv_fofragment_question);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_sfragment_coursewear:

			break;
		case R.id.tv_sfragment_coursewear_small:

			break;
		default:
			break;
		}
	}
}
