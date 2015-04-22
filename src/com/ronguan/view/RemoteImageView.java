package com.ronguan.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.RejectedExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.ronguan.utils.ImageUtils;
//加载显示网络图片的ImageView
public class RemoteImageView extends ImageView {
	private final String tag = "RemoteImageView ";
	public static HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

	private static final int MAX_FAIL_TIME = 5;
	private int mFails = 0;
	private boolean isBg = false;
	private String mUrl;
	private Context mContext;
	private int height=0,width=0;

	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void setDefaultImage(int resId) {
		this.setImageResource(resId);
	}

	public void setImageUrl(String url) {

		if (mUrl != null) {
			mFails++;
		} else {
			mFails = 0;
			mUrl = url;
		}

		if (mFails >= MAX_FAIL_TIME)
			return;

		mUrl = url;

		if (isCached(url))
			return;

		startDownload(url);
	}

	public void setImageUrl(String url, int size) {

		if (mUrl != null) {
			mFails++;
		} else {
			mFails = 0;
			mUrl = url;
		}

		if (mFails >= MAX_FAIL_TIME)
			return;

		mUrl = url;

		if (isCached(url))
			return;

		startDownload(url, size);
	}
	
	public String getImgUrl() {
	    return mUrl;
	}

	public boolean isCached(String url) {
		if (imageCache.containsKey(url)) {
			this.setImageBitmap(imageCache.get(url));

			return true;
		}

		return false;
	}

	private void startDownload(String url) {
		try {
			new DownloadTask().execute(url);
		} catch (RejectedExecutionException e) {
			// 捕获RejectedExecutionException同时加载的图片过多而导致程序崩溃
		}
	}

	private void startDownload(String url, int size) {
		try {
			new DownloadTask(size).execute(url);
		} catch (RejectedExecutionException e) {
			// 捕获RejectedExecutionException同时加载的图片过多而导致程序崩溃
		}
	}

	private void reDownload(String url) {
		setImageUrl(url);
	}

	class DownloadTask extends AsyncTask<String, Void, String> {
		int size;
		private String imageUrl;

		DownloadTask() {
		}

		DownloadTask(int size) {
			this.size = size;
		}

		@Override
		protected String doInBackground(String... params) {
			imageUrl = params[0];
			InputStream is = null;
			Bitmap bmp = null;

			try {

				Log.i(tag, "imageUrl : " + imageUrl);
				URL url = new URL(imageUrl);
				is = url.openStream();
				bmp = BitmapFactory.decodeStream(is);
				
				if (size != 0) {
					bmp = ImageUtils.resizeImage(mContext ,bmp, size);
				}
				if (bmp != null) {
					imageCache.put(imageUrl, bmp);
				} else {
					reDownload(imageUrl);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return imageUrl;
		}

		@Override
		protected void onPostExecute(String result) {
			Bitmap bmp = null;
			if (imageCache.containsKey(result)) {
				bmp = imageCache.get(result);
				height=bmp.getHeight();
				width=bmp.getWidth();
				Log.e("RemoteImageview", "width---"+width+"----height---"+height);
				if (!isBg()) {
					RemoteImageView.this.setImageBitmap(bmp);
				} else {
					BitmapDrawable bd = new BitmapDrawable(
							mContext.getResources(), bmp);
					RemoteImageView.this.setBackgroundDrawable(bd);
				}

			} else {
				reDownload(imageUrl);
			}

			super.onPostExecute(result);
		}

	}

	public boolean isBg() {
		return isBg;
	}

	public void setBg(boolean isBg) {
		this.isBg = isBg;
	}
	
	public int getH(){
		return height;
	}
	
	public int getW(){
		return width;
	}
}
