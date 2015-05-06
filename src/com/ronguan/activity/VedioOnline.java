package com.ronguan.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ronguan.R;
import com.ronguan.utils.DialogUtil;
import com.ronguan.view.ChargeDialog;
import com.ronguan.view.VerticalSeekBar;
import com.umeng.message.PushAgent;

public class VedioOnline extends Activity implements OnClickListener {

	protected static final int AUTO_PLAY = 20;
	private boolean isbuy, bttype = true, isSeek = true, flag = true, isShow = false, isPrepared = false, loadingshow = false, touch,isfinish=false;// 购买状态，按钮状态,首次播放,视频线程监听标志
	private ImageButton bt_play, bt_pause;// 播放，暂停，按钮
	private ImageView bt_stop;// 返回按钮
	private TextView tv_time, tv_light, tv_voice, tv_connnect, tv_vedioname;// 时间，亮度，声音的显示
	private SeekBar sb_progress;// 播放进度滚动条
	private VerticalSeekBar sb_light, sb_voice;// 亮度和声音滚动条
	private SurfaceView sv_vedio, sv_image;// 视频视图和图片视图
	private ImageView iv_sound, iv_light, iv_seek, iv_playmessage;
	private int maxVolume, currentVolume, currentLight, currentPosition = 0, bufferPosition = 0, paycount = 600000, max, clicktime = 0, leavetime = 0;// 最大声音，当前声音，当前亮度,当前视频进度
	private AudioManager audiomanager; // 系统音量控制类
	private Handler handler;// Handler接收类
	private MediaPlayer mediaplayer = null;// 多媒体播放器
	private ChargeDialog alert;
	private ProgressDialog pd;
	private SurfaceHolder mSurfaceHolder = null;
	private Uri uri = null;
	private Timer timer;
	private TimerTask task;
	private LinearLayout ll_sound, ll_light, ll_seek, ll_back;
	private TranslateAnimation mHiddenLeft, mShowLeft, mHiddenRight, mShowRight, mHiddenBottom, mShowBottom, mHiddenTop, mShowTop;
	private Bitmap bitmap;
	private Activity mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		mContext=this;
		PushAgent.getInstance(mContext).onAppStart();
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉程序标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉系统的标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 不再自动锁屏
		setContentView(R.layout.vedioonline);
		initwidget();// 初始化控件
		initdate();// 初始化数据
		initstate();// 初始化控件状态
		//开启一个线程向服务器提交用户足迹 观看视频
		UserActive();
	}

	private void UserActive() {
		new Thread(new Runnable() {

			@Override
			public void run() {
//				String userID=SharedUtil.getFriendId(mContext);
//				String longitude=SharedUtil.getLongitude(mContext);
//				String latitude=SharedUtil.getLatitude(mContext);
//				String position=SharedUtil.getPosition(mContext);
//				String street=ParserJson.getStreet(position);
//				if(userID!=null&&longitude!=null&&street!=null){
//					String productPlat="1";
//					String productID=bookbean.getId();
//					String trackType="2";
//					String result=NetUtil.UserActive(mContext,userID,productPlat,productID,longitude,latitude,street,trackType);
//					String[] code = ParserJson.getCreateGroupResult(result);
//					if(result!=null&&code!=null){
//						if(code[0].equals("1")){
//							Log.i("VedioOnline", "阅读记录成功");
//						}
//					}
//				}
			}
		}).start();	
	}

	private int getbrightnessValue() {
		// 获取屏幕当前亮度,最小0，最大255,但是亮度为0时程序将不可见，会锁屏黑掉，所以这里我们默认最小值为1
		int value = 0;
		ContentResolver cr = this.getContentResolver();
		try {
			value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {

		}
		return value;
	}

	private void changebtn() {
		// 切换播放与暂停按钮图标按钮
		if (bttype == true) {
			bt_play.setVisibility(View.INVISIBLE);
			bt_pause.setVisibility(View.VISIBLE);
			bttype = false;
			iv_playmessage.setVisibility(View.GONE);
			// System.out.println("bttype变为" + bttype + "开始播放视频");
			return;
		}
		if (bttype == false) {
			bt_play.setVisibility(View.VISIBLE);
			bt_pause.setVisibility(View.INVISIBLE);
			bttype = true;
			iv_playmessage.setVisibility(View.VISIBLE);
			// System.out.println("bttype变为" + bttype + "暂停");
			return;
		}
	}

	private void showPd(ProgressDialog pd2, String string) {
		// 显示等候消息
		pd2 = new ProgressDialog(VedioOnline.this);
		pd2.setMessage(string);
		pd2.setCancelable(false);
		pd2.show();
	}

	private Boolean isLeft(float mPosX2) {
		// TODO 判断是否触摸的屏幕左侧
		float width = (float) sv_image.getWidth();
		if (mPosX2 > (width / 2)) {
			return false;
		} else {
			return true;
		}

	}

	private void changeLight(float f, int height) {
		// 根据滑动距离变更亮度条,正值向下，负值向上
		float change = f / (float) height;// 根据滑动距离计算变动长度,正值向下，复制向上
		int changei = (int) (change * 255);
		int light = sb_light.getProgress();
		sb_light.setProgress(light - changei);

	}

	private void changeVoice(float f, int height) {
		// 根据滑动距离变更音量条,正值向下，负值向上
		float change = f / (float) height;
		int changei = (int) (change * 100);
		int voice = sb_voice.getProgress();
		sb_voice.setProgress(voice - changei);
	}

	private void changeSeekbar(float f, int width) {
		// 根据滑动距离变更视频进度条,正值向右，负值向左
		if (sb_progress.getMax() == 100) {
			return;
		} else {
			int progress = sb_progress.getProgress();// 当前位置
			int max = sb_progress.getMax();// 最大长度
			float change = f / (float) width;// 根据滑动距离计算变动长度，正值向右，负值向左
			sb_progress.setProgress(progress + (int) (max * change * 0.5));// 移动mediaplayer，滑动速度减半
			// if (mediaplayer!=null) {
			// // 如果视频进度条发生变化，发消息更新时间显示栏
			// if(sb_progress.getMax()>100){
			// if (Math.abs(sb_progress.getProgress() - currentPosition) >
			// 1000){
			// if(isSeek==true){
			// isSeek=false;
			// mediaplayer.pause();
			// System.out.println("mzy滚动条改变mediaplayer "+"-->" + progress);
			// currentPosition=progress;
			// new AsyncTask<Void, Void, Void>(){
			//
			// @Override
			// protected Void doInBackground(Void... params) {
			// // TODO 自动生成的方法存根
			// mediaplayer.seekTo(sb_progress.getProgress());
			// return null;
			// }}.execute(new Void[]{});
			//
			// }
			// }
			// }
			// }
			// System.out.println("mzy进度条变化:" + max * change);
		}
	}

	private int getVoice(int progress) {
		// 根据进度条获取实际音量值0-maxVolume
		int a = (int) ((progress * maxVolume) / 100);
		return a;
	}

	private void setbrightnessValue(float progress) {
		// 设置屏幕亮度
		if (progress == 0) {
			// 亮度为0程序会自动锁屏黑掉，所以设置最小为1
			progress = 1;
		}
		Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) progress);// 修改
		Window localWindow = getWindow();
		// 保存修改
		WindowManager.LayoutParams params = localWindow.getAttributes();
		params.screenBrightness = (float) (progress / 255.0f);
		localWindow.setAttributes(params);
	}

	private void setNewVoice(int voice) {
		// 设置程序音量
		audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, voice, 0);

	}

	public void set(int progress, int max) {
		// 设置视频时间显示
		tv_time.setText(toTime(progress) + "/" + toTime(max));
	}

	private void ShowView() {
		// 显示控件
		if (isShow == false) {
			isShow = true;
			ll_sound.startAnimation(mShowLeft);
			ll_sound.setVisibility(View.VISIBLE);
			ll_light.startAnimation(mShowRight);
			ll_light.setVisibility(View.VISIBLE);
			ll_seek.startAnimation(mShowBottom);
			ll_seek.setVisibility(View.VISIBLE);
			ll_back.startAnimation(mShowTop);
			ll_back.setVisibility(View.VISIBLE);

		}
	}

	private void HidView() {
		// 隐藏控件
		if (isShow == true) {
			ll_sound.startAnimation(mHiddenLeft);
			ll_sound.setVisibility(View.INVISIBLE);
			ll_light.startAnimation(mHiddenRight);
			ll_light.setVisibility(View.INVISIBLE);
			ll_seek.startAnimation(mHiddenBottom);
			ll_seek.setVisibility(View.INVISIBLE);
			ll_back.startAnimation(mHiddenTop);
			ll_back.setVisibility(View.INVISIBLE);
			isShow = false;
		}
	}

	public String toTime(int progress) {
		// 将int型转为为时间String型
		StringBuffer sb = new StringBuffer();
		int h = (progress / 3600000);// 小时位
		int s = (progress / 1000) % 60;// 秒位
		int m = (progress / 60000) % 60;// 分钟位
		if (h != 0) {// 若不满1小时则不显示小时位
			sb.append(h).append(":");
			if (m < 10) {// 若超过1小时且分钟位小于10，自动补0
				sb.append("0");
			}
		}
		sb.append(m).append(":");
		if (s < 10) {
			sb.append(0);
		}
		sb.append((progress / 1000) % 60);
		return sb.toString();
	}

	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error", e.toString());
		}
		return false;
	}

	private void loadindmediaplayer() {
		// 初始化mediaplayer并prepare
		if (mediaplayer == null && mSurfaceHolder != null) {
			mediaplayer = new MediaPlayer();
			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDisplay(mSurfaceHolder);
			try {
				mediaplayer.setDataSource(VedioOnline.this, uri);
				mediaplayer.prepareAsync();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				// System.out.println("网络视频无法打开");
				e.printStackTrace();
			}

			mediaplayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// 监听mediaplayer是否prepare
					try {
						max = mediaplayer.getDuration();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					// 加载完后自动启动，屏蔽了这行代码
					// 2015 04 16
			 	   //	DialogUtil.showToast(VedioOnline.this, "亲，视频已经加载好了，双击屏幕开始学习!");
					isPrepared = true;
					tv_connnect.setVisibility(View.GONE);
					sb_progress.setMax(max);
					set(currentPosition, max);
					
					// @LMX
					Message message =handler.obtainMessage();
					message.what = VedioOnline.AUTO_PLAY;
					handler.sendMessage(message);
					// @END
				}
			});
			mediaplayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

				@Override
				public void onBufferingUpdate(MediaPlayer mp, int bufferingProgress) {
					// 监听mediaplayer缓冲条
					if (sb_progress.getMax() == 100) {
						sb_progress.setSecondaryProgress(bufferingProgress);
					} else {
						bufferPosition = bufferingProgress * max / 100;
						sb_progress.setSecondaryProgress(bufferingProgress * max / 100);
					}
				}
			});
			mediaplayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO 播放完毕
					DialogUtil.showToast(VedioOnline.this, "播放完毕！");
					mediaplayer.seekTo(0);
					currentPosition = 0;
					bt_play.setVisibility(View.VISIBLE);
					bt_pause.setVisibility(View.INVISIBLE);
					bttype = true;
				}
			});
			mediaplayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// 播放错误
					DialogUtil.showToast(VedioOnline.this, "亲，视频出错啦！请重新播放");
					isPrepared = false;
					if (mediaplayer != null) {
						mediaplayer.release();
						mediaplayer = null;
						if (mSurfaceHolder != null) {
							mediaplayer = new MediaPlayer();
							mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							mediaplayer.setDisplay(mSurfaceHolder);
							try {
								mediaplayer.setDataSource(VedioOnline.this, uri);
								mediaplayer.prepareAsync();
							} catch (IllegalArgumentException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							} catch (IllegalStateException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
					}
					return false;
				}
			});
			mediaplayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete(MediaPlayer arg0) {
					// TODO 自动生成的方法存根
					// isSeek = true;
					// tv_connnect.setVisibility(View.GONE);
					// System.out.println("mzyseekto执行完毕");
				}
			});
			mediaplayer.setOnInfoListener(new OnInfoListener() {

				@Override
				public boolean onInfo(MediaPlayer arg0, int what, int arg2) {
					// 缓冲监听
					switch (what) {
					case MediaPlayer.MEDIA_INFO_BUFFERING_START:
						if (loadingshow == false) {
							// System.out.println("缓冲提示开始");
							tv_connnect.setText("\n   视频正在缓冲中请稍后...  \n   ");
							tv_connnect.setVisibility(View.VISIBLE);
							loadingshow = true;
						}
						break;
					case MediaPlayer.MEDIA_INFO_BUFFERING_END:
						if (loadingshow == true) {
							// System.out.println("缓冲提示结束");
							tv_connnect.setVisibility(View.GONE);
							loadingshow = false;
						}
						break;
					}
					return true;
				}
			});
		}
	}

	private void initwidget() {
		// 初始化控件
		bt_play = (ImageButton) findViewById(R.id.bt_play);
		bt_pause = (ImageButton) findViewById(R.id.bt_pause);
		bt_stop = (ImageView) findViewById(R.id.bt_stop);
		bt_play.setOnClickListener(this);
		bt_pause.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_light = (TextView) findViewById(R.id.tv_light);
		tv_voice = (TextView) findViewById(R.id.tv_voice);
		tv_vedioname = (TextView) findViewById(R.id.tv_vedioname);
		tv_connnect = (TextView) findViewById(R.id.tv_connnect);
		sb_progress = (SeekBar) findViewById(R.id.sb_progress);
		sb_light = (VerticalSeekBar) findViewById(R.id.sb_light);
		sb_voice = (VerticalSeekBar) findViewById(R.id.sb_voice);
		sv_vedio = (SurfaceView) findViewById(R.id.sv_video);
		sv_image = (SurfaceView) findViewById(R.id.sv_image);
		iv_sound = (ImageView) findViewById(R.id.iv_sound);
		iv_light = (ImageView) findViewById(R.id.iv_lignt);
		iv_seek = (ImageView) findViewById(R.id.iv_seek);
		iv_playmessage = (ImageView) findViewById(R.id.iv_playmessage);
		iv_sound.setVisibility(View.VISIBLE);
		iv_light.setVisibility(View.VISIBLE);
		iv_seek.setVisibility(View.VISIBLE);
		ll_sound = (LinearLayout) findViewById(R.id.ll_sound);
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_light = (LinearLayout) findViewById(R.id.ll_light);
		ll_seek = (LinearLayout) findViewById(R.id.ll_seek);
		ll_sound.setVisibility(View.INVISIBLE);
		ll_back.setVisibility(View.INVISIBLE);
		ll_light.setVisibility(View.INVISIBLE);
		ll_seek.setVisibility(View.INVISIBLE);

		mHiddenLeft = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mHiddenLeft.setDuration(500);
		mShowLeft = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowLeft.setDuration(500);
		mHiddenRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mHiddenRight.setDuration(500);
		mShowRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowRight.setDuration(500);
		mHiddenBottom = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		mHiddenBottom.setDuration(500);
		mShowBottom = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowBottom.setDuration(500);
		mHiddenTop = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		mHiddenTop.setDuration(500);
		mShowTop = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowTop.setDuration(500);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO 自动生成的方法存根
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO 自动生成的方法存根
				iv_sound.startAnimation(mHiddenLeft);
				iv_sound.setVisibility(View.GONE);
				iv_light.startAnimation(mHiddenRight);
				iv_light.setVisibility(View.GONE);
				iv_seek.startAnimation(mHiddenBottom);
				iv_seek.setVisibility(View.GONE);

			}
		}.execute(new Void[] {});
	}

	@SuppressWarnings("deprecation")
	private void initstate() {
		// 初始化控件状态
		tv_connnect.setText("\n   正在加载视频请稍后...  \n  ");
		tv_connnect.setVisibility(View.VISIBLE);
		// 初始化亮度
		Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);// 设置亮度调节的模式，0为手动，1为自动。
		currentLight = getbrightnessValue();// 获取屏幕当前亮度,最小1，最大255
		// System.out.println("屏幕当前亮度为：" + currentLight);
		sb_light.setMax(255);
		sb_light.setProgress(currentLight);
		tv_light.setText((currentLight * 100) / 255 + "%");
		// 初始化音量
		audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // STREAM_MUSIC该类型为多媒体声音，STREAM_SYSTEM系统音，STREAM_VOICE_CALL通话音，STREAM_RING铃音，STREAM_ALARM闹铃音
		currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获取当前音量，最小0，最大15
		// System.out.println("当前音量为：" + currentVolume);
		sb_voice.setMax(100);
		sb_voice.setProgress((currentVolume * 100) / maxVolume);
		tv_voice.setText((currentVolume * 100) / maxVolume + "%");
		// 初始化图片
		sv_image.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO 自动生成的方法存根
				// System.out.println("mzy图像销毁了");

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO 对SurfaceView进行画图
				// System.out.println("mzy图像创建了");
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vedio);

				Canvas canvas = sv_image.getHolder().lockCanvas();
				int w = sv_image.getWidth();
				int h = sv_image.getHeight();

				RectF rectF = new RectF(0, 0, w, h);
				canvas.drawBitmap(bitmap, null, rectF, null);
				sv_image.getHolder().unlockCanvasAndPost(canvas);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				// TODO 自动生成的方法存根
				// System.out.println("mzy图像大小改变了");
			}
		});
		// 初始化视频
		sv_vedio.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		sv_vedio.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder arg0) {
				// TODO 自动生成的方法存根
				mSurfaceHolder = null;
				if (null != mediaplayer) {
					if (sb_progress.getMax() > 100) {
						currentPosition = mediaplayer.getCurrentPosition();
						if (bttype == false) {
							try {
								mediaplayer.pause();
							} catch (IllegalStateException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							// System.out.println("mzy执行了暂停方法");
						}
					}
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO 自动生成的方法存根
				// DialogUtil.showToast(VedioOnline.this, "视频创建了");
				mSurfaceHolder = holder;
				if (mediaplayer != null && isPrepared == true) {
					// 此条件判断为用户跳出程序后又返回
					DialogUtil.showToast(VedioOnline.this, "亲，欢迎回来继续学习");
					sv_image.setVisibility(View.INVISIBLE);
					mediaplayer.setDisplay(mSurfaceHolder);
					if (bttype == false) {
						// 跳出前为播放状态，继续播放
						try {
							mediaplayer.start();
						} catch (IllegalStateException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					} else {
						// 跳出前为暂停状态，显示跳出时的画面
						mediaplayer.seekTo(currentPosition);
					}
				}
				// 加载mediaplayer并prepare
				loadindmediaplayer();

			}

			@Override
			public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
				// TODO 自动生成的方法存根

			}
		});

		sv_vedio.setOnTouchListener(new OnTouchListener() {
			float mPosX, mPosY, mCurrentPosX, mCurrentPosY, ePosX, ePosY;
			int changtype, count = 0, firsttime = 0, secondtime = 0;
			int time[] = { 0, 0 };

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下时获取xy坐标
					changtype = 0;// 每次按下会重新采集变更类型
					touch = true;
					mPosX = event.getX();
					mPosY = event.getY();
					ePosX = event.getX();
					ePosY = event.getY();
					clicktime = (int) System.currentTimeMillis();
					count++;
					if (count == 1) {
						firsttime = clicktime;
					}
					if (count == 2) {
						secondtime = clicktime;
						if (secondtime - firsttime < 400) {
							// 触发双击事件
							if (bttype == false) {
								pause();
							} else if (bttype == true) {
								play(currentPosition);
							}
							count = 0;
							firsttime = 0;
							secondtime = 0;
						} else {
							// 未触发则把本次点击状态设置为第一次，以便进行下一次判断
							count = 1;
							firsttime = secondtime;
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					mCurrentPosX = event.getX();
					mCurrentPosY = event.getY();
					if (Math.abs(mCurrentPosX - mPosX) > 10 || Math.abs(mCurrentPosY - mPosY) > 10) {
						// 为保证精准度，限制移动像素达到5才执行变更操作
						if ((Math.abs(mCurrentPosX - mPosX) < Math.abs(mCurrentPosY - mPosY)) && changtype != 3) {
							// 判断上下滑动事件
							if (isLeft(mPosX) && isLeft(mCurrentPosX) && changtype != 2) {
								// 左侧滑动，判断为修改音量
								if (isShow == false) {
									ShowView();
								}
								changeVoice(mCurrentPosY - mPosY, sv_vedio.getHeight());
								if (changtype == 0) {
									// 若是第一次判断，修改变更类型为1，变更音量，此时不再接受其他类型判断
									changtype = 1;
								}
							} else if (!isLeft(mPosX) && !isLeft(mCurrentPosX) && changtype != 1) {
								// 右侧滑动，判断为修改亮度
								if (isShow == false) {
									ShowView();
								}
								changeLight(mCurrentPosY - mPosY, sv_vedio.getHeight());
								if (changtype == 0) {
									// 若是第一次判断，修改变更类型为2，变更亮度，此时不再接受其他类型判断
									changtype = 2;
								}
							}
						} else if ((Math.abs(mCurrentPosX - mPosX) > Math.abs(mCurrentPosY - mPosY)) && changtype != 1 && changtype != 2 && isSeek == true) {
							// 左右滑动，修改视频进度条
							if (isShow == false) {
								ShowView();
							}
							changeSeekbar(mCurrentPosX - mPosX, sv_vedio.getWidth());
							// System.out.println("mzy执行changeSeekbar"+(Math.abs(mCurrentPosX
							// - mPosX)));
							if (changtype == 0) {
								// 若是第一次判断，修改变更类型为3，变更视频，此时不再接受其他类型判断
								changtype = 3;
							}
						}
						mPosX = mCurrentPosX;
						mPosY = mCurrentPosY;
					}
					break;
				case MotionEvent.ACTION_UP:// 获取移动结束后的坐标点
					leavetime = (int) System.currentTimeMillis();
					touch = false;
					if (time[0] == 0) {
						time[0] = leavetime;
						time[1] = leavetime;
					} else {
						time[0] = time[1];
						time[1] = leavetime;
					}
					if (Math.abs(ePosX - event.getX()) < 10 && Math.abs(ePosY - event.getY()) < 10 && (time[1] - time[0] > 400)) {
						new AsyncTask<Void, Void, Void>() {
							boolean doubleclick;

							@Override
							protected Void doInBackground(Void... params) {
								// TODO 自动生成的方法存根
								try {
									Thread.sleep(400);
									if ((int) System.currentTimeMillis() - leavetime < 400) {
										// 双击事件
										doubleclick = true;
									} else {
										// 单机事件
										doubleclick = false;
									}
								} catch (InterruptedException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								// TODO 自动生成的方法存根
								if (doubleclick == false) {
									if (isShow == false) {
										ShowView();
									} else if (isShow == true) {
										HidView();
									}
								}
							}
						}.execute(new Void[] {});
					}
					break;
				}
				return true;
			}
		});
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:// 更新视频进度条和时间显示
					int a = msg.getData().getInt("1");
					int b = msg.getData().getInt("2");
					set(a, b);
					sb_progress.setProgress(a);
					tv_connnect.setVisibility(View.GONE);
					// System.out.println("mzymedia改变滚动条" + "--->" + a);
					break;
				case 1:// 更新亮度百分比
					int progresslight = msg.getData().getInt("1");
					tv_light.setText(progresslight + "%");
					break;
				case 2:// 更新声音百分比
					int progressvoice = msg.getData().getInt("1");
					tv_voice.setText(progressvoice + "%");

					break;
				case 3:// 更新屏幕亮度
					int light = msg.getData().getInt("1");
					setbrightnessValue((float) light);
					break;
				case 4:// 更新多媒体音量
					int voice = msg.getData().getInt("1");
					setNewVoice(voice);
					break;
				case 5:// 执行购买弹窗事件
					
					break;
				case 6:// 根据进度条更新时间的显示
					final int progress = msg.getData().getInt("1");
					set(progress, sb_progress.getMax());
					if (isSeek == true&&!isfinish) {
						isSeek = false;
						// System.out.println("mzy滚动条改变mediaplayer " + "-->" +
						// progress);
						tv_connnect.setText("\n   正在加载请稍后...  \n  ");
						tv_connnect.setVisibility(View.VISIBLE);
						currentPosition = progress;
						new AsyncTask<Void, Integer, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								// TODO 自动生成的方法存根
								try {
									if (bttype == false) {
										mediaplayer.pause();
									}
									mediaplayer.seekTo(progress);
									if (bttype == false && (bufferPosition - progress) > 10000) {
										// 判断缓冲已超过1%开始播放
										mediaplayer.start();
									}
								} catch (IllegalStateException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
								catch(Exception e){
									e.printStackTrace();
								}
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								// TODO 自动生成的方法存根
								super.onPostExecute(result);
								isSeek = true;
							}

						}.execute(new Void[] {});

					}
					break;
				case 7:// 缓冲完成去掉缓冲提示
					tv_connnect.setVisibility(View.GONE);
					break;
				case 8:// 超过10秒未点击屏幕自动影藏控件
					HidView();
					break;
				case VedioOnline.AUTO_PLAY:
					play(currentPosition);
					break;
				}
			}
		};
		// 开启一个子线程，监控控件参数状态，若需要更新UI则发消息给主线程
		Runnable mRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				while (flag == true) {
					int progresslight = sb_light.getProgress();
					int progressvoice = sb_voice.getProgress();
					int progress = sb_progress.getProgress();
					if (progress > paycount + 500) {
						if (isbuy == false && max != 0) {
							// 如果播放时间超过10分钟，进行收费提醒
							Message message = new Message();
							Bundle bundle = new Bundle();
							message.setData(bundle);
							bundle.putInt("1", paycount);
							bundle.putInt("2", max);
							message.what = 5;
							handler.sendMessage(message);
						}
					}
					if ((progresslight * 100) / 255 != Integer.parseInt(tv_light.getText().toString().replace("%", "").trim())) {
						// 如果亮度进度条变化，发消息更新亮度百分比内容
						Message message = new Message();
						Bundle bundle = new Bundle();
						message.setData(bundle);
						bundle.putInt("1", (progresslight * 100) / 255);
						message.what = 1;
						handler.sendMessage(message);
					}
					if (progressvoice != Integer.parseInt(tv_voice.getText().toString().replace("%", "").trim())) {
						// 如果声音进度条变化，发消息更新声音百分比内容
						Message message = new Message();
						Bundle bundle = new Bundle();
						message.setData(bundle);
						bundle.putInt("1", progressvoice);
						message.what = 2;
						handler.sendMessage(message);
					}
					if (!(progresslight == 0 && currentLight == 1)) {
						// 如果亮度进度条变化，发消息更新屏幕亮度，当亮度进度条为0时，最低亮度设置为1，防止黑屏
						if (progresslight != currentLight) {
							// System.out.println("更改亮度:" + currentLight + "--》"
							// + progresslight);
							currentLight = progresslight;
							Message message = new Message();
							Bundle bundle = new Bundle();
							message.setData(bundle);
							bundle.putInt("1", currentLight);
							message.what = 3;
							handler.sendMessage(message);
						}
					}
					if (getVoice(progressvoice) != currentVolume) {
						// 如果音量进度条变化，发消息更新多媒体音量
						// System.out.println("更改声音" + currentVolume + "-->" +
						// getVoice(progressvoice));
						currentVolume = getVoice(progressvoice);
						Message message = new Message();
						Bundle bundle = new Bundle();
						message.setData(bundle);
						bundle.putInt("1", currentVolume);
						message.what = 4;
						handler.sendMessage(message);
					}
					if (mediaplayer != null) {
						// 如果视频进度条发生变化，发消息更新时间显示栏
						if (sb_progress.getMax() > 100) {
							if (isSeek == true && Math.abs(progress - currentPosition) > 1000) {
								// System.out.println("mzy滚动条改变时间显示 "+"-->" +
								// progress);
								Message message = new Message();
								Bundle bundle = new Bundle();
								message.setData(bundle);
								bundle.putInt("1", progress);
								message.what = 6;
								handler.sendMessage(message);
								// if(isSeek==true){
								// isSeek=false;
								// mediaplayer.pause();
								// System.out.println("滚动条改变mediaplayer "+"-->"
								// + progress);
								// currentPosition=progress;
								// mediaplayer.seekTo(progress);
								// }
							}
						}
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(mRunnable).start();
		// 新建一个计时器当播放视频时用来同步更新进度条
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if (isShow == true && clicktime != 0) {
					// System.out.println("mzy "+((int)System.currentTimeMillis()-clicktime));
					if ((int) System.currentTimeMillis() - leavetime > 10000 && touch == false) {
						Message message = new Message();
						message.what = 8;
						handler.sendMessage(message);
					}
				}
				if (mediaplayer != null) {
					if (mediaplayer.isPlaying() && max != 0) {
						int progress = mediaplayer.getCurrentPosition();
						// System.out.println("mzy"+progress);
						if (Math.abs(progress - currentPosition) > 0) {
							currentPosition = progress;
							Message message = new Message();
							Bundle bundle = new Bundle();
							message.setData(bundle);
							bundle.putInt("1", progress);
							bundle.putInt("2", max);
							message.what = 0;
							handler.sendMessage(message);
						}
					}
					if (!mediaplayer.isPlaying() && max != 0 && (bufferPosition - currentPosition) > 10000 && mSurfaceHolder != null && isSeek == true) {
						// 缓冲完成
						if (bttype == false) {
							// 继续播放
							try {
								mediaplayer.start();
							} catch (IllegalStateException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
						if (bttype == true) {
							// 去掉加载显示
							Message message = new Message();
							message.what = 7;
							handler.sendMessage(message);
						}
					}
				}
			}
		};
		timer.schedule(task, 500, 500);

	}

	private void initdate() {
		// 初始化数据
		// 获取视频信息
		 isbuy = true;
		String path = "http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4";
		try {
			uri = Uri.parse(URLDecoder.decode(path, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.bt_play:
			play(currentPosition);
			break;
		case R.id.bt_pause:
			pause();
			break;
		case R.id.bt_stop:
			isfinish=true;
			finish();
		}
	}

	private void pause() {
		// TODO 自动生成的方法存根
		if (mediaplayer != null && mediaplayer.isPlaying()) {
			currentPosition = mediaplayer.getCurrentPosition();
			mediaplayer.pause();
			changebtn();
		}
	}

	private void play(int currentPosition2) {
		if (isConnect(VedioOnline.this)) {
			if (mediaplayer != null && isPrepared == true) {
				// DialogUtil.showToast(VedioOnline.this, "开始播放视频");
				sv_image.setVisibility(View.INVISIBLE);
				mediaplayer.start();
				changebtn();
			} else {
				DialogUtil.showToast(VedioOnline.this, "亲，视频还未加载完毕，请稍等...");
			}
		} else {
			if (bufferPosition - sb_progress.getProgress() > 60000) {
				// 未联网并且缓冲进度不够1分钟时提醒联网
				if (mediaplayer != null && isPrepared == true) {
					DialogUtil.showToast(VedioOnline.this, "开始播放视频");
					mediaplayer.start();
					changebtn();
				} else {
					DialogUtil.showToast(VedioOnline.this, "亲，视频还未加载完毕，请稍等...");
				}
			} else {
				DialogUtil.showToast(VedioOnline.this, "本视频需要联网才能观看,请先联网!");
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		// System.out.println("mzy执行了onDestroy方法");
		flag = false;
		task.cancel();
		timer.purge();
		timer.cancel();
		if (mediaplayer != null) {
			// if(sb_progress.getMax()>100){
			// mediaplayer.stop();
			// }
			mediaplayer.release();
			mediaplayer = null;
		}
		if(bitmap!=null&&!bitmap.isRecycled()){
			bitmap.recycle();
			bitmap=null;
		}
	}
}
