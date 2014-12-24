package com.weiyi.reader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.WeakHashMap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class MyNetImageCacheManager extends WeakHashMap<String, Bitmap> {
	private static final String CACHE_FILE = "/Reader/Cache";
	private static MyNetImageCacheManager myNetImgCache = new MyNetImageCacheManager();

	public static MyNetImageCacheManager getInstance() {
		return myNetImgCache;
	}

	public boolean isBitmapExist(String url) {
		boolean isExist = containsKey(url);
		if (!isExist) {
			isExist = isBitmapExistLocal(url);
		}
		return isExist;
	}

	private boolean isBitmapExistLocal(String url) {
		boolean isExistLocal = true;
		String fileName = UrlToFileName(url);
		String path = isExistCachePath();
		File file = new File(path, fileName);
		if (!file.exists()) {
			isExistLocal = false;
		} else {
			isExistLocal = cacheBitmapToMemory(file, url);
		}
		return isExistLocal;
	}

	private boolean cacheBitmapToMemory(File file, String url) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		byte[] b = StreamUtil.getByteByStream(is);
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		if (bitmap != null) {
			this.put(url, bitmap, false);
			return true;
		} else {
			return false;
		}
	}

	// 缓存到本WeakHashMap中
	public Bitmap put(String key, Bitmap bitmap, boolean isCacheToLocal) {
		if (isCacheToLocal) {
			return this.put(key, bitmap);
		} else {
			return super.put(key, bitmap);
		}
	}

	@Override
	public Bitmap put(String key, Bitmap value) {
		OutputStream os = null;
		String fileName = UrlToFileName(key);
		String path = isExistCachePath();
		File file = new File(path, fileName);
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		value.compress(CompressFormat.JPEG, 100, os);

		try {
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		os = null;
		return super.put(key, value);
	}

	public String isExistCachePath() {
		String cachePath, rootPath = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			rootPath = Environment.getExternalStorageDirectory().toString();
		} else {
			return null;
		}
		cachePath = rootPath + CACHE_FILE;
		File file = new File(cachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		Log.v("cachePath", cachePath);
		return cachePath;
	}

	private String UrlToFileName(String url) {
		String replacement = "_";
		String fileName = url.replace("//", replacement);
		fileName = fileName.replace("/", replacement);
		fileName = fileName.replace(":", replacement);
		fileName = fileName.replace("=", replacement);
		fileName = fileName.replace("&", replacement);
		fileName = fileName.replace("?", replacement);
		return fileName;
	}

	// 存放bitmap 对象内容 到本地文件
	public void putLocal(String key, Bitmap value) {
		OutputStream os = null;
		String fileName = UrlToFileName(key);
		String path = isExistCachePath();
		File file = new File(path, fileName);
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		value.compress(CompressFormat.JPEG, 100, os);

		try {
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isBitmapExistInLocal(String url) {
		if (url == null) {
			return false;
		}
		String fileName = UrlToFileName(url);
		String path = isExistCachePath();
		File file = new File(path, fileName);
		if (!file.exists()) {
			return false;
		} else {
			return true;
		}
	}

	public Bitmap getBitmapFromLocal(String url) {
		FileInputStream fis = null;
		String fileName = UrlToFileName(url);
		String path = isExistCachePath();
		File file = new File(path, fileName);
		if (!file.exists()) {
			return null;
		} else {
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return BitmapFactory.decodeStream(fis);
		}
	}
}
