package com.ronguan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ronguan.R;
import com.ronguan.fragment.BaseFragment;
import com.ronguan.fragment.FirstTabFragment;
import com.ronguan.fragment.FouthTabFragment;
import com.ronguan.fragment.SecondeTabFragment;
import com.ronguan.fragment.ThirdTabFragment;
import com.ronguan.view.JazzyViewPager;

/**
 * Created by haloZh on 2015/4/21.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{
	private Context context;
	
	private JazzyViewPager mViewPager;
	
	private MyPagerAdapter adapter;
	//底部控件
	private ImageView tab1,tab2,tab3,tab4;
	
	private TextView tvTab1,tvTab2,tvTab3,tvTab4;
	
	private LinearLayout lLayoutTop;
	//当前fragment
	private BaseFragment currentFragment;
	
	private ArrayList<BaseFragment> mListFragment=new ArrayList<BaseFragment>();
	//viewpager 的页码
	private int vpIndex;
	
	private FirstTabFragment     TFragment1;
	private SecondeTabFragment   TFragment2;
	private ThirdTabFragment     TFragment3;
	private FouthTabFragment     TFragment4;
	
	private LinearLayout llayout_all,llayout_interaction,llayout_ebook,llayout_tk,llayout_sc;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        context=this;
        initData();
        initView();
    }
    
    void initView(){
    	tab1=(ImageView)findViewById(R.id.img1_main_tab);
    	tab2=(ImageView)findViewById(R.id.img2_main_tab);
    	tab3=(ImageView)findViewById(R.id.img3_main_tab);
    	tab4=(ImageView)findViewById(R.id.img4_main_tab);
    	tvTab1=(TextView)findViewById(R.id.tv1_main_tab);
    	tvTab2=(TextView)findViewById(R.id.tv2_main_tab);
    	tvTab3=(TextView)findViewById(R.id.tv3_main_tab);
    	tvTab4=(TextView)findViewById(R.id.tv4_main_tab);
    	
    	mViewPager=(JazzyViewPager)findViewById(R.id.main_viewpager);
    	adapter=new MyPagerAdapter(getSupportFragmentManager(), mListFragment);
    	mViewPager.setAdapter(adapter);
    	mViewPager.setCurrentItem(0);
    	mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    
    	llayout_all=(LinearLayout)findViewById(R.id.main_llayout_all);
		llayout_ebook=(LinearLayout)findViewById(R.id.main_llayout_ebook);
		llayout_tk=(LinearLayout)findViewById(R.id.main_llayout_tk);
		llayout_sc=(LinearLayout)findViewById(R.id.main_llayout_sc);
		
    	llayout_all.setOnClickListener(this);
		llayout_ebook.setOnClickListener(this);
		llayout_tk.setOnClickListener(this);
		llayout_sc.setOnClickListener(this);
    	
    }
    
    void initData(){
    	TFragment1=new FirstTabFragment();
    	TFragment2=new SecondeTabFragment();
    	TFragment3=new ThirdTabFragment();
    	TFragment4=new FouthTabFragment();
    	mListFragment.add(TFragment1);
    	mListFragment.add(TFragment2);
    	mListFragment.add(TFragment3);
    	mListFragment.add(TFragment4);
    	
    }
    
    /**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public List<BaseFragment> mListFragment;

		FragmentTransaction ft;

		public MyPagerAdapter(FragmentManager fm,
				ArrayList<BaseFragment> fragments) {

			super(fm);
			this.mListFragment = fragments;
			ft = fm.beginTransaction();
		}

		@Override
		public Fragment getItem(int position) {

			return mListFragment.get(position);
		}

		@Override
		public int getCount() {

			return mListFragment.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			ft.hide(mListFragment.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ft.show(mListFragment.get(position));
			mViewPager.setObjectForPosition(mListFragment.get(position),
					position);
			return super.instantiateItem(container, position);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(final int index) {
			vpIndex = index;
			tab1.setImageResource(R.drawable.index_icon_1);
			tab2.setImageResource(R.drawable.index_icon_2);
			tab3.setImageResource(R.drawable.index_icon_3);
			tab4.setImageResource(R.drawable.index_icon_4);
			
			tvTab1.setTextColor(Color.WHITE);
			tvTab2.setTextColor(Color.WHITE);
			tvTab3.setTextColor(Color.WHITE);
			tvTab4.setTextColor(Color.WHITE);
			
			switch (index) {
			case 0:
				tvTab1.setTextColor(getResources().getColor(R.color.tv_bottom_click));
				tab1.setImageResource(R.drawable.index_icon_1hover);
				break;
			case 1:
				tvTab2.setTextColor(getResources().getColor(R.color.tv_bottom_click));
				tab2.setImageResource(R.drawable.index_icon_2hover);
				break;	
			case 2:
				tvTab3.setTextColor(getResources().getColor(R.color.tv_bottom_click));
				tab3.setImageResource(R.drawable.index_icon_3hover);
				break;
			case 3:
				tvTab4.setTextColor(getResources().getColor(R.color.tv_bottom_click));
				tab4.setImageResource(R.drawable.index_icon_4hover);
				
				break;
			}
			currentFragment = (BaseFragment) adapter.getItem(index);
			setCurrentFragment(currentFragment);
		}
	}
    
	/**
	 * 回到初始两面 并移除已经打开的子fragment
	 */
	public void popAllTab1() {

		removeFrameChild(currentFragment);
	}

	public void setCurrentTab(int index) {

		mViewPager.setCurrentItem(index);
	}

	public void setCurrentFragment(BaseFragment fragment) {

		this.currentFragment = fragment;
	}

	public BaseFragment getCurrentFragment() {

		return currentFragment;
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		currentFragment.onFragmentBackPressed();
	}

	public void onFragmentResult(Bundle data) {

		currentFragment.onFragmentResult(data);
	}
	//移除fragment
	private void removeFrameChild(BaseFragment frame) {

		if (null == frame) {
			return;
		}
		if (frame.getChildFragmentManager() == null) {
			return;
		}
		FragmentManager fm = frame.getChildFragmentManager();
		fm.getBackStackEntryCount();
		for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
			fm.popBackStack();
		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//销毁fragment
		mListFragment.clear();
		mListFragment = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.main_llayout_all) {
			mViewPager.setCurrentItem(0);
			return;
		}
		if (v.getId() == R.id.main_llayout_ebook) {
			mViewPager.setCurrentItem(1);
			return;
		}
		if (v.getId() == R.id.main_llayout_tk) {
			mViewPager.setCurrentItem(2);
			return;
		}
		if (v.getId() == R.id.main_llayout_sc) {
			mViewPager.setCurrentItem(3);
			return;
		}
	}
    
}
