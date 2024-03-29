package com.fongmi.android.tv.utils;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.BuildConfig;
import com.fongmi.android.tv.model.Channel;
import com.fongmi.android.tv.network.AsyncCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Utils {

	private static DisplayMetrics getDisplayMetrics() {
		return App.getInstance().getResources().getDisplayMetrics();
	}

	static String getString(int resId) {
		return App.getInstance().getString(resId);
	}

	public static String getString(int resId, Object... formatArgs) {
		return App.getInstance().getString(resId, formatArgs);
	}

	public static int dp2px(int dpValue) {
		return Math.round(dpValue * getDisplayMetrics().density);
	}

	public static boolean isDigitKey(int keyCode) {
		return isNumber(keyCode) || isNumberPad(keyCode);
	}

	public static boolean hasEvent(KeyEvent event) {
		return event.getAction() == KeyEvent.ACTION_DOWN && (isArrowKey(event) || isBackKey(event));
	}

	private static boolean isArrowKey(KeyEvent event) {
		return isEnterKey(event) || isUpKey(event) || isDownKey(event) || isLeftKey(event) || isRightKey(event);
	}

	private static boolean isNumber(int keyCode) {
		return keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9;
	}

	static boolean isNumberPad(int keyCode) {
		return keyCode >= KeyEvent.KEYCODE_NUMPAD_0 && keyCode <= KeyEvent.KEYCODE_NUMPAD_9;
	}

	static boolean isBackKey(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.KEYCODE_BACK;
	}

	static boolean isEnterKey(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_SPACE || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER;
	}

	static boolean isUpKey(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_UP || event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP;
	}

	static boolean isDownKey(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN;
	}

	static boolean isLeftKey(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT;
	}

	static boolean isRightKey(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT;
	}

	public static void setImmersiveMode(Activity activity) {
		int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		activity.getWindow().getDecorView().setSystemUiVisibility(flags);
	}

	public static void getList(AsyncCallback callback) {
		FirebaseDatabase.getInstance().getReference().child("channel").addValueEventListener(new AsyncCallback() {
			@Override
			public void onDataChange(@NonNull DataSnapshot data) {
				List<Channel> items = new ArrayList<>();
				for (DataSnapshot item : data.getChildren()) items.add(Channel.create(item));
				callback.onResponse(items);
			}
		});
	}

	public static void getVersion(Activity activity) {
		FirebaseDatabase.getInstance().getReference().child(BuildConfig.FLAVOR).addValueEventListener(new AsyncCallback() {
			@Override
			public void onDataChange(@NonNull DataSnapshot data) {
				FileUtil.checkUpdate(activity, (long) data.getValue());
			}
		});
	}
}
