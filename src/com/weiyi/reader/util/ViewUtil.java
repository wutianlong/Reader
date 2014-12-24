package com.weiyi.reader.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class ViewUtil {
	public static class Screen {
		public int width;
		public int height;
	}

	public static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	public static Screen getScreenSize(Context context) {
		Screen screen = new Screen();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screen.width = dm.widthPixels;
		screen.height = dm.heightPixels;
		return screen;
	}
}
