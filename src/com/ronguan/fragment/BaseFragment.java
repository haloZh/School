package com.ronguan.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.example.ronguan.R;
import com.ronguan.activity.MainActivity;
import com.ronguan.utils.DialogUtil;

/**
 * 页面的基类
 * 
 * 主要实现loading弹窗 退出弹窗
 * 
 * @author zhoujian
 * 
 */
public class BaseFragment extends Fragment implements FragmentListener {

	// private BaseFragment tempFragment;
	public ProgressDialog pd;

	private Dialog alert;

	String TAG = "BaseFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 初始化loading弹窗
		pd = new ProgressDialog(getActivity());
		pd.setCancelable(true);
	}

	/**
	 * 退出弹窗
	 */
	public void showExit() {

//		alert = DialogUtil.showAlert(getActivity(), "温馨提示", "是否退出圣才学霸?", "退出", "取消", new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				alert.dismiss();
//				getActivity().finish();
//			}
//		}, new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				alert.dismiss();
//			}
//		});
	}

	/**
	 * fragment中返回键调用方法
	 */
	@Override
	public void onFragmentBackPressed() {

//		Activity activity = getActivity();
//		if (activity instanceof MainActivity) {
//			BaseFragment bf = ((MainActivity) getActivity()).getCurrentFragment();
//			if (bf instanceof MainType1Fragment || bf instanceof DownloadFragment || bf instanceof UserFragment||bf instanceof InteractionFragment) {
//				if (bf.getFragmentManager() != null) {
//					if (bf.getFragmentManager().getBackStackEntryCount() > 0) {
//						bf.getFragmentManager().popBackStack();
//					} else {
//						if (bf.getChildFragmentManager().getBackStackEntryCount() > 0) {
//							bf.getChildFragmentManager().popBackStack();
//						} else {
//							showExit();
//						}
//					}
//				} else {
//					showExit();
//				}
//			} else {
//				showExit();
//			}
//		} else if (activity instanceof PayActivity) {
//			getActivity().finish();
//		}
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void onFragmentResult(Bundle data) {

	}

	public void hideKeyboard() {

		if (getActivity() != null) {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				View focusView = getActivity().getCurrentFocus();
				if (focusView != null)
					imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}
	}

	public void showKeyboard() {

		if (getActivity() != null) {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			View focusView = getActivity().getCurrentFocus();
			if (imm != null && focusView != null) {
				imm.showSoftInput(focusView, 0);
			}
		}
	}

}
