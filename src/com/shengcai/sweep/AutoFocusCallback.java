package com.shengcai.sweep;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class AutoFocusCallback implements Camera.AutoFocusCallback {
	private static final String TAG = AutoFocusCallback.class.getSimpleName();
	private static final long AUTOFOCUS_INTERVAL_MS = 1500L;
	private Handler autoFocusHandler;
	private int autoFocusMessage;
	void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
	    this.autoFocusHandler = autoFocusHandler;
	    this.autoFocusMessage = autoFocusMessage;
	  }
	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO 自动生成的方法存根
		if (autoFocusHandler != null){
			Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
			autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
			autoFocusHandler = null;
		}
		else{
			Log.d(TAG, "Got auto-focus callback, but no handler for it");
		}
	}

}
