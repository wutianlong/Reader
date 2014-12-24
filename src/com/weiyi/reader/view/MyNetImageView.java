package com.weiyi.reader.view;

import com.weiyi.reader.common.ImageDownloadAsyncTask;
import com.weiyi.reader.entity.Category;
import com.weiyi.reader.entity.NicePhoto;
import com.weiyi.reader.ui.R;
import com.weiyi.reader.util.MyNetImageCacheManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class MyNetImageView extends ImageView {
	private String imgUrl;
	private Category category;
	private NicePhoto photo;
	public boolean isCartoon;

	public MyNetImageView(Context context) {
		super(context);
	}

	public MyNetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyNetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setImgUrl(String url, boolean isCartoon) {
		this.imgUrl = url;
		this.isCartoon = isCartoon;
		loadImg(url);
	}

	private void loadImg(String url) {
		if (MyNetImageCacheManager.getInstance().isBitmapExistInLocal(url)) {
			Log.v("loadImg in MyNetImageView form cache", url);
			Bitmap bitmap = MyNetImageCacheManager.getInstance()
					.getBitmapFromLocal(url);
			if (!isCartoon) {
				this.setImageBitmap(bitmap);
			} else {
				if (bitmap != null) {
					setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
							bitmap.getWidth(), bitmap.getHeight() / 3));
				} else {
					Bitmap fail = BitmapFactory.decodeResource(getResources(),
							R.drawable.download_fail_bg);
					setImageBitmap(Bitmap.createBitmap(fail, 0, 0,
							fail.getWidth(), fail.getHeight() / 3));
					if (fail != null && !fail.isRecycled()) {
						fail.recycle();
						fail = null;
					}
				}
			}
		} else {
			Log.v("loadImg in MyNetImageView form net", url);
			new ImageDownloadAsyncTask(this).execute(url);
		}
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public NicePhoto getPhoto() {
		return photo;
	}

	public void setPhoto(NicePhoto photo) {
		this.photo = photo;
	}

}
