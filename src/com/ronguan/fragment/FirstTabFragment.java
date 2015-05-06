package com.ronguan.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ronguan.R;
import com.ronguan.view.JazzyViewPager;
import com.ronguan.view.JazzyViewPager.TransitionEffect;
import com.ronguan.view.RemoteImageView;

public class FirstTabFragment extends BaseFragment implements OnClickListener,
		OnPageChangeListener {

	private Context mContext;

	private static final int AD_W = 750;// 滚动广告的宽度

	private static final int AD_H = 305;// 滚动广告的长度

	private JazzyViewPager viewPager;

	private TextView tvTittle;

	private ArrayList<View> views;

	private GuideAdapter adapter;

	// 扫一扫按钮
	private Button btnSao;

	private int current;
	// 广告栏的小点
	private ImageView[] dots;
	// 广告的外布局
	private RelativeLayout relateLayout;
	// 广告栏小点的外布局
	private LinearLayout dotLl;
	// 控制广告的播放时间
	private Timer timer;

	private Handler mHandler;
	//提示扫描的tv
	private TextView tvTip;
	//课程表
	private LinearLayout lLayout_class;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		mHandler = new Handler(mContext.getMainLooper());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_firt_fragment, container,
				false);

		initView(view);

		initAd();

		createView();

		return view;
	}

	// 初始化布局
	@SuppressLint("NewApi") void initView(View view) {
		viewPager = (JazzyViewPager) view
				.findViewById(R.id.viewpager_first_fragment);
		tvTittle = (TextView) view.findViewById(R.id.tv_top_title);
		tvTittle.setText(getResources().getString(R.string.home_tittle));
		relateLayout = (RelativeLayout) view.findViewById(R.id.ad_rlayout_guid);
		dotLl = (LinearLayout) view.findViewById(R.id.guide_ll);
		btnSao = (Button) view.findViewById(R.id.btn_top_right);
		btnSao.setBackground(getResources().getDrawable(R.drawable.saoyisao));
		btnSao.setVisibility(View.VISIBLE);
		btnSao.setOnClickListener(this);
		
		tvTip=(TextView)view.findViewById(R.id.tv_tip);
		lLayout_class=(LinearLayout)view.findViewById(R.id.llayout_first_fragment);
		
		initAd();
	}

	// 广告栏
	void initAd() {
		// 推荐ViewPager
		viewPager.setTransitionEffect(TransitionEffect.Tablet);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度height = dm.heightPixels ;//高度
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) relateLayout
				.getLayoutParams();
		int height = (int) (AD_H * ((double) width / (double) AD_W));
		params.height = height;
		params.width = width;
		relateLayout.setLayoutParams(params);
	}

	class GuideAdapter extends PagerAdapter {

		// 销毁position位置
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			((ViewPager) container).removeView(views.get(position));
		}

		// 初始化position位置
		@Override
		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView(views.get(position), 0);
			return views.get(position);
		}

		// 获取界面数
		@Override
		public int getCount() {

			return views == null ? null : views.size();
		}

		// 判断是否有对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return (arg0 == arg1);
		}

	}

	/** 创建引导页面 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	private void createView() {
		final int paperSize = 2;
		views = new ArrayList<View>();
		dots = new ImageView[paperSize];
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		List<ImageView> imgList = new ArrayList<ImageView>();

		ImageView img1 = new ImageView(mContext);
		img1.setBackground(getResources().getDrawable(R.drawable.banner1));
		ImageView img2 = new ImageView(mContext);
		img2.setBackground(getResources().getDrawable(R.drawable.banner2));
		views.add(img1);
		views.add(img2);

		for (int i = 0; i < paperSize; i++) {

			ImageView dotIv = new ImageView(getActivity());
			dotIv.setLayoutParams(params);
			dotIv.setScaleType(ScaleType.CENTER);
			dotIv.setPadding(10, 2, 10, 2);
			dotIv.setImageResource(R.drawable.guide_dot);
			dotIv.setClickable(true);
			dots[i] = dotIv;
			dotIv.setEnabled(false);
			dotIv.setOnClickListener(this);
			dotIv.setTag(i);
			dotLl.addView(dotIv);
		}
		current = 0;
		dots[current].setEnabled(true);
		adapter = new GuideAdapter();
		adapter.notifyDataSetChanged();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);

		timer = new Timer();
		// createView(urls);
		TimerTask task = new TimerTask() {

			int position = 0;

			int count = paperSize;

			@Override
			public void run() {

				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {

					public void run() {

						position++;
						setViews(position);
						setDots(position);
						if (position >= count) {
							position = -1;
						}

					}
				});

			}
		};
		timer.schedule(task, 3000, 3000);
	}

	// 设置当前引导页
	private void setViews(int position) {

		if (position < 0 || position >= dots.length)
			return;
		try {
			viewPager.setCurrentItem(position);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}

	}

	// 设置当前小点
	private void setDots(int position) {

		if (position < 0 || position > dots.length - 1 || current == position) {
			return;
		}
		dots[position].setEnabled(true);
		dots[current].setEnabled(false);
		current = position;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_top_right:
			// 扫描二维码
			Intent intent = new Intent();
			intent.setClassName(mContext.getApplicationContext(),
					"com.shengcai.sweep.SweepActivity");
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, requestCode+"----"+resultCode+"----");
		tvTip.setVisibility(View.GONE);
		lLayout_class.setVisibility(View.VISIBLE);
		switch (resultCode) {
		case 1:
			tvTip.setVisibility(View.GONE);
			lLayout_class.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
}
