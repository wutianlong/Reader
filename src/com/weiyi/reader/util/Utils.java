package com.weiyi.reader.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

public class Utils {

	/**
	 * View组件显示的内容可以通过cache机制保存为bitmap, 主要有以下方法:
	 * 
	 * void setDrawingCacheEnabled(boolean flag),
	 * 
	 * Bitmap getDrawingCache(boolean autoScale),
	 * 
	 * void buildDrawingCache(boolean autoScale),
	 * 
	 * void destroyDrawingCache()
	 */
	public static Bitmap getBitmap(Activity activity) {
		Bitmap bitmap;
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		bitmap = view.getDrawingCache();
		return bitmap;
	}

	public static void clearBitmapCache(Activity activity) {
		activity.getWindow().getDecorView().destroyDrawingCache();
	}
}
