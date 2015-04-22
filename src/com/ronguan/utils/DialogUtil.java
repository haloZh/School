package com.ronguan.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class DialogUtil {

	// 展示土司提示--联网失败的提示
	public static void showHttpError(final Activity activity) {
		showToast(activity, "连接服务器失败,请稍后重试");
	}

	// 展示提示信息
	public static void showToast(final Activity activity, final String text) {
		try {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
					toast(activity, text);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void toast(Activity activity,String msg) {
		if(activity == null) return;
		Toast toast = Toast.makeText(activity, null, Toast.LENGTH_SHORT);
		LinearLayout layout = (LinearLayout) toast.getView();
		layout.setBackgroundColor(Color.GREEN);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		TextView tv = new TextView(activity);
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(Color.parseColor("#ffffffff"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setPadding(0, 0, 0, 0);
		tv.setText(msg);
		layout.addView(tv);
		toast.show();
	}

	// 展示广告
	/*public static MainActivity	activity;

	public static void showAd(final Context context, Handler handler, final Bitmap bitmap, final String url, final String id) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.dialog_ad, null);
					ImageView close = (ImageView) view.findViewById(R.id.ad_close);
					ImageView img = (ImageView) view.findViewById(R.id.ad_img);
					if (bitmap != null) {
						img.setBackgroundDrawable(new BitmapDrawable(bitmap));
					}
					final Dialog alert = new Dialog(context, R.style.alertdialog);
					alert.setContentView(view);
					alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					alert.show();
					close.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							alert.dismiss();
						}
					});
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							alert.dismiss();
							if (url == null || "".equals(url)) {
								if (activity != null) {
									activity.openBook(id, "推荐");
								}
							} else {
								Uri uri = Uri.parse(url);
								Intent intent = new Intent(Intent.ACTION_VIEW, uri);
								context.startActivity(intent);
							}
						}
					});
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/

	/*// 展示弹窗
	public static Dialog showAlert(Context context, String title, String content, String yesText, String noText, OnClickListener yesClick, OnClickListener noClick) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_alert, null);
		TextView titleView = (TextView) view.findViewById(R.id.alert_title);
		titleView.setText(title);
		TextView contentView = (TextView) view.findViewById(R.id.alert_content);
		contentView.setText(content);
		Button yes = (Button) view.findViewById(R.id.alert_yes);
		Button no = (Button) view.findViewById(R.id.alert_no);
		Dialog alert = new Dialog(context, R.style.alertdialog);
		alert.setContentView(view);
		if (yesText == null || "".equals(yesText)) {
			yes.setVisibility(View.GONE);
		} else {
			yes.setText(yesText);
			yes.setOnClickListener(yesClick);
		}
		no.setText(noText);
		no.setOnClickListener(noClick);
		alert.show();
		return alert;
	}*/

	// 展示弹窗
	/*public static Dialog showPhoneAlert(Context context, String title, String yesText, String noText, OnClickListener yesClick, OnClickListener noClick) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_alert, null);
		TextView titleView = (TextView) view.findViewById(R.id.alert_title);
		titleView.setText(title);
		TextView contentView = (TextView) view.findViewById(R.id.alert_content);
		contentView.setVisibility(View.GONE);
		EditText editView = (EditText) view.findViewById(R.id.alert_content_edit);
		editView.setVisibility(View.VISIBLE);
		Button yes = (Button) view.findViewById(R.id.alert_yes);
		Button no = (Button) view.findViewById(R.id.alert_no);
		Dialog alert = new Dialog(context, R.style.alertdialog);
		alert.setContentView(view);
		alert.show();
		if (yesText == null || "".equals(yesText)) {
			yes.setVisibility(View.GONE);
		} else {
			yes.setText(yesText);
			yes.setOnClickListener(yesClick);
		}
		no.setText(noText);
		no.setOnClickListener(noClick);
		return alert;
	}*/
	
	public static void showLoadingDialog(Context context) {
		
	}
}
