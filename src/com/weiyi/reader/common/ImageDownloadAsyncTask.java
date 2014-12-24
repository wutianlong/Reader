package com.weiyi.reader.common;

import com.weiyi.reader.util.ImageUtil;
import com.weiyi.reader.util.MyNetImageCacheManager;
import com.weiyi.reader.view.GestureImageView;
import com.weiyi.reader.view.MyNetImageView;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private String picUrl;
	private MyNetImageView myNetImageView;
	private GestureImageView imageView;

	public ImageDownloadAsyncTask(MyNetImageView myNetImageView) {
		this.myNetImageView = myNetImageView;
	}

	public ImageDownloadAsyncTask(GestureImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		picUrl = params[0];
		if (imageView != null) {
			return ImageUtil.getHttpBitmap(picUrl, imageView);
		} else {
			return ImageUtil.getHttpBitmap(picUrl, myNetImageView);
		}

	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			if (myNetImageView != null) {
				MyNetImageCacheManager.getInstance().putLocal(picUrl, result);
				if (!myNetImageView.isCartoon) {
					myNetImageView.setImageBitmap(result);
				} else {
					myNetImageView.setImageBitmap(Bitmap.createBitmap(result,
							0, 0, result.getWidth(), result.getHeight() / 4));
				}
			}
			if (imageView != null) {
				MyNetImageCacheManager.getInstance().putLocal(picUrl, result);
				imageView.setImageBitmap(result);
				imageView.init();
			}
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

}
