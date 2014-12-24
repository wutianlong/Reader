package com.weiyi.reader.common;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.weiyi.reader.entity.NicePhoto;
import com.weiyi.reader.ui.ITActivity;
import com.weiyi.reader.util.BlogUtil;

public class PhotoBlogListAsyncTask extends
		AsyncTask<String, Void, List<NicePhoto>> {
	ITActivity activity;
	boolean isRunning = true;

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public PhotoBlogListAsyncTask(ITActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		if (activity.toolbarBack != null) {
			activity.toolbarBack.setVisibility(ImageView.GONE);
		}
		if (activity.toolbarProgress != null) {
			activity.toolbarProgress.setVisibility(ProgressBar.VISIBLE);
		}
		super.onPreExecute();
	}

	@Override
	protected List<NicePhoto> doInBackground(String... params) {
		Document doc;
		try {
			doc = Jsoup.connect(params[0]).timeout(10 * 1000).get();
			Elements titles = doc.getElementsByClass(Constant.BLOG_TITLE_CLASS)
					.tagName("a");
			Elements dates = doc.getElementsByClass(Constant.BlOG_DATE_CLASS);
			Elements urls = titles.select(Constant.HREF_SELECT);
			for (int i = 0; isRunning && i < titles.size(); ++i) {
				String blogUrl = Constant.ITBLOG_URL
						+ urls.get(i).attributes().get("href");
				NicePhoto photo = new NicePhoto();
				photo.setTilte(titles.get(i).text());
				photo.setDate(dates.get(i).text());
				photo.setUrl(blogUrl);
				if (Constant.CARTOON_CATEGORY_URL.equals(params[0])) {
					photo.cartoonUrl = BlogUtil.getCartoonUrl(blogUrl);
					photo.setIconUrl(photo.cartoonUrl);
				} else {
					photo.setPhotoUrls(BlogUtil
							.getImageUrlListByBlogUrl(blogUrl));
					String iconUrl = photo.getPhotoUrls().get(0);
					if (iconUrl != null)
						photo.setIconUrl(iconUrl);
				}
				activity.photos.add(photo);
				publishProgress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activity.photos;
	}

	@Override
	protected void onPostExecute(List<NicePhoto> result) {
		if (activity.adapter != null) {
			activity.adapter.notifyDataSetChanged();
		}
		if (activity.toolbarBack != null) {
			activity.toolbarBack.setVisibility(ImageView.VISIBLE);
		}
		if (activity.toolbarProgress != null) {
			activity.toolbarProgress.setVisibility(ProgressBar.GONE);
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		if (activity.adapter != null) {
			activity.adapter.notifyDataSetChanged();
		}
		super.onProgressUpdate(values);
	}

}
