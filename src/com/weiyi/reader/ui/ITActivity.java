package com.weiyi.reader.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.weiyi.reader.adpater.DownLoadThread;
import com.weiyi.reader.adpater.DownloadListAdapter;
import com.weiyi.reader.adpater.ITAdapter;
import com.weiyi.reader.adpater.NiceEyeGridAdapter;
import com.weiyi.reader.anim.ExitAnimation;
import com.weiyi.reader.common.Constant;
import com.weiyi.reader.common.PhotoBlogListAsyncTask;
import com.weiyi.reader.entity.ITBlog;
import com.weiyi.reader.entity.NicePhoto;
import com.weiyi.reader.util.FileUtil;
import com.weiyi.reader.util.ITBlogUtil;
import com.weiyi.reader.util.MyNetImageCacheManager;
import com.weiyi.reader.view.MyGridView;
import com.weiyi.reader.view.MyNetImageView;
import com.weiyi.reader.view.MyScrollView;

/**
 * IT�Ķ�ģ��
 * 
 * @author κ����
 * */
public class ITActivity extends BaseActivity implements OnTouchListener,
		OnClickListener {
	private final int POINT_COUNT = 3;
	Bundle savedInstanceState;
	// ������3D
	MyScrollView myScrollView;
	ViewFlipper viewFlipper;
	GestureDetector gestureDetector;
	int currentPoint;
	ImageView point1, point2, point3;
	RelativeLayout mainTitle;
	RelativeLayout disgrace, seductive, story, people, military, car;
	TextView disgraceTime, seductiveTime, storyTime, peopleTime, militaryTime,
			carTime;
	// ListView�����ݲ���
	ListView listView;
	// ListViewҳ�Ų���
	View listViewFooter;// ListView�ײ�View
	// Button askMoreButton;// �ײ����ظ��ఴť
	LinearLayout footerLoadProcess;// �ײ���ʾ���ڼ���
	// ������������
	List<ITBlog> data;// sumData������ data��ǰ�б���ʾ������
	List<ITBlog> sumData;
	ITAdapter itAdapter;
	String currentCategoryUrl = Constant.DISGRACE_CATEGORY_URL;// ��ǰ�������µ�URL
	// ����һ��������������
	int pageRowNum = 0;
	// ����ͷ���ȱ�־
	ImageView mProgressBarImage;// ������Բ�ν�����ͼƬ��־
	public ProgressBar toolbarProgress;// �������Բ�ν�����������ʾ
	public ImageView toolbarBack;// ������棬Բ�ν�����������Ϻ���ʾ����ImageView,
	public TextView currentCategoryTitle;// ��ǰ�������
	// �ײ������˵�
	// LinearLayout footerMenu;// �ײ������˵�
	TextView footerMain, footerHelp, footerItBuiness, footerItInfo;
	// �������ݻ�ȡ��ϸ��½���
	Handler handler;
	ITBroadcastReceiver broadcastReceiver;
	// �첽���������б�
	BlogListAsyncTask blogListAsyncTask;
	int lastItem = 0;// �����б����һ������
	boolean isLoadmoreOver = true;// ���ظ��ද���Ƿ����
	// ��������еı���
	public List<NicePhoto> photos;
	public NiceEyeGridAdapter adapter;
	public GridView gridView;
	public PhotoBlogListAsyncTask niceEyeTask;
	// �������������Ҫ�ı�������
	public MyGridView myGridView;
	LinearLayout point_images_layout;// car_title_layout.xml��point_images�����Բ���
	ImageView[] images_point;// ��Ų����е�����image��
	TextView showDate;
	int[] tempImage;
	int mCurImage = 1;// ��ǰ��image
	MyNetImageView title_image;// ������image
	public PhotoBlogListAsyncTask cartoonTask;
	// �������
	ListView downloadList;
	DownloadListAdapter dListAdapter;
	Handler downloadHandler;
	File[] files;
	// �������
	Handler cacheHandler;

	// final String url = "http://tool.httpcn.com/hl.shtml";
	// Document doc;
	// WebView webView;
	// Handler handlers;
	// String c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.it_layout);
		data = new ArrayList<ITBlog>();
		sumData = new ArrayList<ITBlog>();
		initBroadCast();// ��ʼ���㲥������
		initMainView();
		addClickListener();
		new DownLoadThread().start();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initBroadCast() {
		broadcastReceiver = new ITBroadcastReceiver(this);
		if (MyNetImageCacheManager.getInstance().isExistCachePath() == null) {
			Toast.makeText(this, R.string.not_sdcard, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void initMainView() {
		initMainTitle();
		disgrace = (RelativeLayout) findViewById(R.id.disgrace);
		seductive = (RelativeLayout) findViewById(R.id.seductive);
		story = (RelativeLayout) findViewById(R.id.story);
		people = (RelativeLayout) findViewById(R.id.people);
		military = (RelativeLayout) findViewById(R.id.military);
		car = (RelativeLayout) findViewById(R.id.car);
		// ���շ��ʴ���
		Random random = new Random();
		String day = "����";
		String time = "��";
		disgraceTime = (TextView) disgrace
				.findViewById(R.id.mainlist_item_datatime);
		seductiveTime = (TextView) seductive
				.findViewById(R.id.mainlist_item_datatime);
		storyTime = (TextView) story.findViewById(R.id.mainlist_item_datatime);
		peopleTime = (TextView) people
				.findViewById(R.id.mainlist_item_datatime);
		militaryTime = (TextView) military
				.findViewById(R.id.mainlist_item_datatime);
		carTime = (TextView) car.findViewById(R.id.mainlist_item_datatime);
		disgraceTime.setText(day + String.valueOf(random.nextInt(1500)) + time);
		seductiveTime
				.setText(day + String.valueOf(random.nextInt(1500)) + time);
		storyTime.setText(day + String.valueOf(random.nextInt(1500)) + time);
		peopleTime.setText(day + String.valueOf(random.nextInt(1500)) + time);
		militaryTime.setText(day + String.valueOf(random.nextInt(1500)) + time);
		carTime.setText(day + String.valueOf(random.nextInt(1500)) + time);
		initFooterMenuView(Constant.IT_MAIN_PAGE);
		mProgressBarImage = (ImageView) findViewById(R.id.toolbar_progress_image);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.LOAD_MORE_FINISH:
					hiddenProgress();
					// listView.smoothScrollToPosition(lastItem + 1);
					itAdapter.notifyDataSetChanged();
					footerLoadProcess.setVisibility(LinearLayout.GONE);
					break;
				case Constant.LOAD_BLOG_AFTER_CLICK:
					Bundle bundle = msg.getData();
					initAfterClickView(bundle.get("typeUrl").toString(), bundle
							.get("typeTitle").toString());
					new DownLoadThread().start();
					break;
				}
			}
		};
	}

	private void initMainTitle() {
		myScrollView = (MyScrollView) findViewById(R.id.myScrollView);
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		mainTitle = (RelativeLayout) findViewById(R.id.main_title);
		point1 = (ImageView) findViewById(R.id.view_bg1_selector);
		point2 = (ImageView) findViewById(R.id.view_bg2_selector);
		point3 = (ImageView) findViewById(R.id.view_bg3_selector);
		point1.setEnabled(true);
		viewFlipper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DownLoadThread().start();
				Toast.makeText(getApplicationContext(),
						"�鿴�����'��ͼ����'ר��...", Toast.LENGTH_SHORT).show();
			}
		});
		mainTitle.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
		gestureDetector = new GestureDetector(new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (e1.getX() - e2.getX() > 10) {

					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							ITActivity.this, R.anim.push_left_in));

					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							ITActivity.this, R.anim.push_left_out));

					if (currentPoint < POINT_COUNT - 1) {

						viewFlipper.showNext();
						currentPoint++;
						setPointSelector();
					}
					return true;

				} else if (e1.getX() - e2.getX() < -10) {

					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							ITActivity.this, R.anim.push_right_in));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							ITActivity.this, R.anim.push_right_out));

					if (currentPoint > 0) {

						viewFlipper.showPrevious();
						currentPoint--;
						setPointSelector();
					}
					return true;
				}
				return true;
			}

			private void setPointSelector() {
				// TODO Auto-generated method stub
				point1.setEnabled(false);
				point2.setEnabled(false);
				point3.setEnabled(false);
				switch (currentPoint) {
				case 0:
					point1.setEnabled(true);
					break;
				case 1:
					point2.setEnabled(true);
					break;
				case 2:
					point3.setEnabled(true);
					break;
				}
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		myScrollView.setGestureDetector(gestureDetector);
	}

	private void initFooterMenuView(String selectedPage) {
		footerMain = (TextView) findViewById(R.id.footer_main);
		footerHelp = (TextView) findViewById(R.id.footer_help);
		footerItInfo = (TextView) findViewById(R.id.footer_it_info);
		footerItBuiness = (TextView) findViewById(R.id.footer_it_business);
		if (selectedPage.equals(Constant.IT_MAIN_PAGE)) {
			footerMain.setTextColor(Color.BLACK);
		} else if (selectedPage.equals(Constant.IT_HELP_PAGE)) {
			footerHelp.setTextColor(Color.BLACK);
		} else if (Constant.COLLECT_CATEGORY_TITLE.equals(selectedPage)) {
			footerItInfo.setTextColor(Color.BLACK);
		} else if (Constant.CARTOON_CATEGORY_TITLE.equals(selectedPage)) {
			footerItBuiness.setTextColor(Color.BLACK);

		}
		footerHelp.setOnTouchListener(this);
		footerItBuiness.setOnTouchListener(this);
		footerItInfo.setOnTouchListener(this);
		footerMain.setOnTouchListener(this);
	}

	private void changeFooterViewBg() {
		footerMain.setTextColor(Color.GRAY);
		footerHelp.setTextColor(Color.GRAY);
		footerItInfo.setTextColor(Color.GRAY);
		footerItBuiness.setTextColor(Color.GRAY);
	}

	/**
	 * ���ĳһ�������º�������ʾ��ʼ���ؽ�����
	 * */
	public void showProgress() {
		if (toolbarProgress != null)
			toolbarProgress.setVisibility(ProgressBar.VISIBLE);
		if (mProgressBarImage != null)
			mProgressBarImage.setVisibility(ImageView.GONE);
		if (toolbarBack != null)
			toolbarBack.setVisibility(ImageView.GONE);
		if (footerLoadProcess != null)
			footerLoadProcess.setVisibility(TextView.VISIBLE);

	}

	public void hiddenProgress() {
		if (toolbarProgress != null)
			toolbarProgress.setVisibility(ProgressBar.GONE);
		if (toolbarBack != null)
			toolbarBack.setVisibility(ImageView.VISIBLE);
		if (footerLoadProcess != null)
			footerLoadProcess.setVisibility(LinearLayout.GONE);

	}

	/**
	 * ע���������¼�
	 * */
	public void addClickListener() {
		disgrace.setOnClickListener(this);
		seductive.setOnClickListener(this);
		story.setOnClickListener(this);
		people.setOnClickListener(this);
		military.setOnClickListener(this);
		car.setOnClickListener(this);

		footerHelp.setOnTouchListener(this);
		footerItBuiness.setOnTouchListener(this);
		footerItInfo.setOnTouchListener(this);
		footerMain.setOnTouchListener(this);
		// footerHelp.setOnClickListener(this);
		// footerItBuiness.setOnClickListener(this);
		// footerItInfo.setOnClickListener(this);
		// footerMain.setOnClickListener(this);
	}

	/**
	 * �����������Ķ���ĸ��ݴ���Ĳ����ж������ַ�����Ķ�,������Ӧ������װ��
	 * 
	 * @param typeUrl
	 *            ���������URL
	 * @param typeTitle
	 *            �������ͱ���
	 * */
	public void initAfterClickView(String typeUrl, String typeTitle) {
		// ������������
		if (typeUrl.equals(Constant.SEDUCTIVE_CATEGORY_URL)) {
			initNiceEyeView();
			return;
		}
		setContentView(R.layout.it_blog);
		initFooterMenuView(typeTitle);
		// ���ԭ������
		clear();
		// ��ȡͷ�����ȱ���
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		currentCategoryTitle = (TextView) findViewById(R.id.it_category_title);
		// ��ȡ�б�ListView
		listView = (ListView) findViewById(R.id.list_view);
		listView.setDivider(null);
		// ��ʼ���ײ�����View,Ҫ����������Ч֮ǰ
		listViewFooter = View.inflate(listView.getContext(),
				R.layout.listview_footer, null);
		footerLoadProcess = (LinearLayout) listViewFooter
				.findViewById(R.id.footer_load_process);
		listView.addFooterView(listViewFooter);
		// // ���ͷ��View
		// View header = View.inflate(listView.getContext(), R.layout.ad, null);
		// listView.addHeaderView(header);
		itAdapter = new ITAdapter(ITActivity.this, data);
		listView.setAdapter(itAdapter);
		addListener();
		// ��ʼ��ListView��Data����
		currentCategoryUrl = typeUrl;// ���õ�ǰ�������µ�URL
		currentCategoryTitle.setText(typeTitle);// ���õ�ǰ�������µı�������
		blogListAsyncTask = new BlogListAsyncTask();
		blogListAsyncTask.execute(typeUrl);// �첽���������б�
	}

	private void clear() {
		if (blogListAsyncTask != null) {
			blogListAsyncTask.setStop(true);
			blogListAsyncTask.cancel(true);
			blogListAsyncTask = null;
		}
		if (niceEyeTask != null) {
			niceEyeTask.setRunning(false);
			niceEyeTask.cancel(true);
			niceEyeTask = null;
		}
		if (cartoonTask != null) {
			cartoonTask.setRunning(false);
			cartoonTask.cancel(true);
			cartoonTask = null;
		}
		sumData.removeAll(sumData);
		data.removeAll(data);
		if (photos != null) {
			photos.removeAll(photos);
		}
		if (itAdapter != null)
			itAdapter.notifyDataSetChanged();
		if (blogListAsyncTask != null) {
			blogListAsyncTask.cancel(true);
			Log.v("clear.....", blogListAsyncTask.isCancelled() + "");
		}
	}

	/**
	 * ע���Ҫ�ļ�����,ListView��toolbarBack������
	 * */
	public void addListener() {
		// ΪtoolbarBackע�������
		toolbarBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCreate(savedInstanceState);
			}
		});
		// ΪListViewע�������
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Log.v("onItemClick", "onItemClickListener" + id + ".."
						+ position);
				if (position >= data.size()) {
					Toast.makeText(ITActivity.this, R.string.wait_load,
							Toast.LENGTH_SHORT).show();
					return;
				}
				new DownLoadThread().start();
				Toast.makeText(ITActivity.this, "׼������...",
						Toast.LENGTH_SHORT).show();
				if (data.get(position) != null) {
					ITBlog itBlog = data.get(position);
					Intent intent = new Intent(ITActivity.this,
							WebReadActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ITBlog", itBlog);
					bundle.putInt("index", position);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		// ����ListView��������
		listView.setOnScrollListener(new OnScrollListener() {
			int sumItemCount = 0;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastItem == itAdapter.getCount()) {
					Log.v("onScrollStateChanged", scrollState + "adapter"
							+ itAdapter.getCount());
					if ((data.size() == pageRowNum
							|| data.size() == sumData.size() || sumItemCount == sumData
							.size() + 1)
							&& (blogListAsyncTask.getStatus() != AsyncTask.Status.RUNNING)
							&& isLoadmoreOver) {// ��ǰ�첽�̲߳�������
						listView.removeFooterView(listViewFooter);
						Toast.makeText(ITActivity.this, R.string.load_over,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (blogListAsyncTask.getStatus() != AsyncTask.Status.RUNNING
							&& isLoadmoreOver) {
						footerLoadProcess.setVisibility(LinearLayout.VISIBLE);
						// listView.smoothScrollToPosition(lastItem + 2);
						itAdapter.notifyDataSetChanged();
						new Thread() {
							public void run() {
								isLoadmoreOver = false;
								// ģ����ظ���ȴ�����
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								loadMoreData();
							}
						}.start();
					}
					if (blogListAsyncTask.getStatus() == AsyncTask.Status.RUNNING
							|| !isLoadmoreOver) {
						Toast.makeText(ITActivity.this,
								R.string.loading_can_not_page,
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.v("onScroll", firstVisibleItem + ".." + lastItem);
				lastItem = firstVisibleItem + visibleItemCount - 1;
				sumItemCount = totalItemCount;
				if (!isLoadmoreOver)
					footerLoadProcess.setVisibility(LinearLayout.VISIBLE);
			}
		});
	}

	public void loadMoreData() {
		new DownLoadThread().start();
		Log.v("loadMoreData1", itAdapter.getCount() + "");
		int count = itAdapter.getCount();
		if (count + Constant.LOAD_ROW_TIME < pageRowNum) {
			for (int i = count; i < count + Constant.LOAD_ROW_TIME; i++) {
				if (sumData.size() > i)
					data.add(sumData.get(i));
				Log.v("loadMoreData2", itAdapter.getCount() + "");
			}
		} else {
			for (int i = count; i < pageRowNum; i++) {
				if (sumData.size() > i)
					data.add(sumData.get(i));
				Log.v("loadMoreData3", itAdapter.getCount() + "");
			}
		}
		isLoadmoreOver = true;
		handler.sendEmptyMessage(Constant.LOAD_MORE_FINISH);
	}

	/**
	 * ����������������Ӧ�¼�
	 * */
	class MyClickListener implements OnClickListener {
		private String typeUrl;// ���������URL���������ͣ�
		private String typeTitle;// �������±��⣨�������ͣ�

		public MyClickListener(String typeUrl, String typeTitle) {
			this.typeUrl = typeUrl;
			this.typeTitle = typeTitle;
		}

		@Override
		public void onClick(View v) {
			initAfterClickView(typeUrl, typeTitle);
		}
	}

	// �ٰ�һ�η��ؼ��˳����򡱵�ʵ��
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (downloadList != null) {
			downloadList = null;
			if (downloadHandler != null) {
				downloadHandler = null;
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			onCreate(this.savedInstanceState);
			hiddenProgress();
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(this, R.string.exit_hint, Toast.LENGTH_SHORT)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				Animation exitAnimation = new ExitAnimation();
				ImageView im = new ImageView(this);
				BitmapFactory.Options options = new Options();
				options.inSampleSize = 2;
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.welcome, options);
				im.setScaleType(ScaleType.FIT_XY);
				im.setImageBitmap(bmp);
				setContentView(im);
				im.startAnimation(exitAnimation);
				exitAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				});
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * �첽���أɣԣ£��������б�
	 * */
	class BlogListAsyncTask extends AsyncTask<String, Integer, List<ITBlog>> {
		boolean isStop = false;

		public void setStop(boolean isStop) {
			this.isStop = isStop;
		}

		@Override
		protected void onPreExecute() {
			showProgress();
			super.onPreExecute();
		}

		// <span class="link_title">
		// <a
		// href="/up1up2up3/article/details/8191923">
		// 师傅的智慧</a>
		// </span>

		@Override
		protected List<ITBlog> doInBackground(String... params) {
			List<ITBlog> itBlogs = new ArrayList<ITBlog>();
			try {
				Document doc = Jsoup.connect(params[0]).get();
				Elements titles = doc.getElementsByClass(
						Constant.ITBLOG_TITLE_CLASS).tagName("a");// 获取所有class=link_title的标签元素
				Elements dates = doc
						.getElementsByClass(Constant.ITBlOG_DATE_CLASS);
				for (int i = 0; !isStop && i < titles.size(); ++i) {
					if (!isCancelled()) {
						String blogUrl = Constant.ITBLOG_URL
								+ titles.get(i).child(0).attributes()
										.get("href");// 每篇文章的URL
						String iconUrl = ITBlogUtil
								.getIconUrlByBlogUrl(blogUrl);
						ITBlog itBlog = new ITBlog();
						if (iconUrl != null) {
							itBlog.setIconUrl(iconUrl);// 设置每篇文章的头图标URL
						}
						itBlog.setTilte(titles.get(i).text());// 获取a标签内的文本，即文章标题
						itBlog.setDate(dates.get(i).text());// 获取文章发表日期
						itBlog.setUrl(blogUrl);// 获取超链接属性href的值
						if (data.size() < Constant.FIRST_ROW_NUM)
							data.add(itBlog);
						publishProgress((int) ((float) i / titles.size()) * 100);
						itBlogs.add(itBlog);
						++pageRowNum;
					} else {
						return null;
					}
				}
			} catch (Exception e) {
				sendBroadcast(new Intent(Constant.SOCKETTIMEOUT));
				e.printStackTrace();
			}
			return itBlogs;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.v("onProgressUpdate....", isCancelled() + "");
			if (isCancelled()) {// ����߳���ȡ���������ݲ�����
				sumData.removeAll(sumData);
				data.removeAll(data);
				if (itAdapter != null)
					itAdapter.notifyDataSetChanged();
				return;
			}
			if (data.size() <= Constant.FIRST_ROW_NUM) {// ��һ�μ��ؼ���
				if (itAdapter != null) {
					itAdapter.notifyDataSetChanged();
				}
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<ITBlog> result) {
			Log.v("onPostExecute....", isCancelled() + "");
			if (isCancelled() || result == null) {
				if (itAdapter != null) {
					itAdapter.notifyDataSetChanged();
				}
				return;
			}
			if (result != null && result.size() == 0) {
				if (itAdapter != null) {
					itAdapter.notifyDataSetChanged();
				}
				Toast.makeText(ITActivity.this, R.string.not_data,
						Toast.LENGTH_SHORT).show();
			}
			initSumData(result);
			if (itAdapter != null)
				itAdapter.notifyDataSetChanged();
			hiddenProgress();
			super.onPostExecute(result);
		}

		/**
		 * ��ʼ��ListView���б�����
		 * 
		 * @param lists
		 *            ��ʼ����һ������µ�����
		 * */
		private void initSumData(List<ITBlog> lists) {
			Log.v("initDataList......", lists.size() + "");
			// ���ԭ�������б�
			sumData.removeAll(sumData);
			sumData = lists;
			pageRowNum = sumData.size();// �ܼ�¼��
		}

		@Override
		protected void onCancelled() {
			sumData.removeAll(sumData);
			data.removeAll(data);
			itAdapter.notifyDataSetChanged();
			Log.v("onCancelled......", data.size() + "");
			super.onCancelled();
		}
	}

	private void initCartoonView() {
		setContentView(R.layout.cartoon_layout);
		myGridView = (MyGridView) findViewById(R.id.cartoon_grid_view);
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		currentCategoryTitle = (TextView) findViewById(R.id.it_category_title);
		clear();
		currentCategoryTitle.setText(Constant.CARTOON_CATEGORY_TITLE);
		photos = new ArrayList<NicePhoto>();
		adapter = new NiceEyeGridAdapter(this, photos);
		myGridView.setAdapter(adapter);
		initTitleLayout();
		cartoonTask = new PhotoBlogListAsyncTask(this);
		cartoonTask.execute(Constant.CARTOON_CATEGORY_URL);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if (position >= photos.size()) {
					Toast.makeText(ITActivity.this, R.string.wait_load,
							Toast.LENGTH_SHORT).show();
					return;
				}
				new DownLoadThread().start();
				Toast.makeText(ITActivity.this, "���ڼ���...",
						Toast.LENGTH_SHORT).show();
				NicePhoto cartoon = photos.get(position);
				if (cartoon != null) {
					Intent intent = new Intent(ITActivity.this,
							ReadCartoonActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("cartoon", cartoon);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

	}

	private void initTitleLayout() {
		showDate = (TextView) findViewById(R.id.page2_show_date);
		showDate.setText(new SimpleDateFormat("yyyy��MM��dd��")
				.format(new Date()));

		point_images_layout = (LinearLayout) findViewById(R.id.point_images_layout);
		images_point = new ImageView[point_images_layout.getChildCount()];
		title_image = (MyNetImageView) findViewById(R.id.title_image);
		tempImage = new int[] { R.drawable.loadimg_bg, R.drawable.loadimg_bg,
				R.drawable.loadimg_bg };
		setScrollerImagePoint();
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x123) {
					if (mCurImage > tempImage.length - 1) {
						mCurImage = 0;
					}
					setScrollerImagePoint();
					title_image.setAnimation(AnimationUtils.loadAnimation(
							ITActivity.this, android.R.anim.fade_out));
					title_image.setImageResource(tempImage[mCurImage]);
					if (photos != null && photos.size() > mCurImage) {
						String iconUrl = photos.get(mCurImage).getIconUrl();
						if (iconUrl != null) {
							title_image.setImgUrl(iconUrl, true);
						}
						title_image.setPhoto(photos.get(mCurImage));
						title_image.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								MyNetImageView myNetImageView = (MyNetImageView) v;
								Toast.makeText(ITActivity.this,
										R.string.loading, Toast.LENGTH_SHORT)
										.show();
								startActivity(getIntentForCartoon(myNetImageView
										.getPhoto()));

							}
						});
					}
					title_image.setAnimation(AnimationUtils.loadAnimation(
							ITActivity.this, android.R.anim.fade_in));
					++mCurImage;
				}
			}

		};
		// ��ʱ����ͼƬ���л���ʾ
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0x123);
				}
			}
		}, 0, 800);

	}

	/**
	 * ͼ��ͼ�����pointImage�ĵ�ǰָʾ״��
	 * */
	private void setScrollerImagePoint() {
		// TODO Auto-generated method stub
		for (int i = 0; i < point_images_layout.getChildCount(); ++i) {
			images_point[i] = (ImageView) point_images_layout.getChildAt(i);
			images_point[i].setEnabled(false);
		}
		if (mCurImage < images_point.length) {
			images_point[mCurImage].setEnabled(true);
		}
	}

	private void initNiceEyeView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.nice_eye_gridview);
		gridView = (GridView) findViewById(R.id.nice_eye_gridview);
		// ��ȡͷ�����ȱ���
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		currentCategoryTitle = (TextView) findViewById(R.id.it_category_title);
		initFooterMenuView(Constant.IT_MAIN_PAGE);

		currentCategoryTitle.setText(Constant.SEDUCTIVE_CATEGORY_TITLE);
		photos = new ArrayList<NicePhoto>();

		photos.addAll(getLocalPhoto());// ��ӱ�����Դ�ļ�

		adapter = new NiceEyeGridAdapter(this, photos);
		gridView.setAdapter(adapter);

		init();
		addGridViewListener();
	}

	/**
	 * ��ȡ������Դͼ��
	 * */
	private List<NicePhoto> getLocalPhoto() {
		// TODO Auto-generated method stub
		List<NicePhoto> photos = new ArrayList<NicePhoto>();
		NicePhoto photo1 = new NicePhoto();
		NicePhoto photo2 = new NicePhoto();

		ArrayList<String> resIds1 = getResIdList(19, "r");// ���ǳ�������ЩMM
		ArrayList<String> resIds2 = getResIdList(18, "s");// ����������Ů����������������˽����

		photo1.setTilte("���Ǹճ���(1)");
		photo2.setTilte("����������Ů����������������˽����");

		photo1.setPhotoUrls(resIds1);
		photo2.setPhotoUrls(resIds2);
		photos.add(photo1);
		photos.add(photo2);

		return photos;
	}

	/**
	 * ��ȡ��ԴID�б�
	 * 
	 * @param sum
	 *            ����Ŀ
	 * @param prefix
	 *            ��Դ��ǰ׺
	 * */
	private ArrayList<String> getResIdList(int sum, String prefix) {
		ArrayList<String> resIds = new ArrayList<String>();
		for (int i = 0; i < sum; ++i) {
			int resId = getResources().getIdentifier(prefix + i, "drawable",
					getPackageName());
			resIds.add(String.valueOf(resId));
		}
		return resIds;
	}

	private void init() {
		niceEyeTask = new PhotoBlogListAsyncTask(this);
		niceEyeTask.execute(Constant.SEDUCTIVE_CATEGORY_URL);
	}

	private void addGridViewListener() {
		// TODO Auto-generated method stub
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position >= photos.size()) {
					Toast.makeText(ITActivity.this, R.string.wait_load,
							Toast.LENGTH_SHORT).show();
					return;
				}
				new DownLoadThread().start();
				Toast.makeText(ITActivity.this, "���ڼ���...",
						Toast.LENGTH_SHORT).show();
				NicePhoto photo = photos.get(position);
				if (photo != null) {
					startActivity(getIntentForPhoto(photo));
				}
			}
		});
	}

	/**
	 * ��ȡ��ת��Intent
	 * */
	private Intent getIntentForPhoto(NicePhoto photo) {
		Intent intent = null;
		intent = new Intent(ITActivity.this, ReadPhotoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("photo", photo);
		intent.putExtras(bundle);
		return intent;
	}

	/**
	 * ��ȡ��ת��Intent
	 * */
	private Intent getIntentForCartoon(NicePhoto photo) {
		Intent intent = null;
		intent = new Intent(ITActivity.this, ReadCartoonActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("cartoon", photo);
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// sendBroadcast(new Intent(Constant.UPDATE_ITREADER_NOTIFY));//
		// ���͹㲥֪ͨ��ʾ
	}

	public void initHelp() {
		setContentView(R.layout.setting_activity);
		ListView areaRadioListView = null;
		RadioOnClick click = new RadioOnClick(Integer.parseInt(pre.getValue(
				Constant.FONTKEY, "7")));
		HelpClickListener listerClick = new HelpClickListener(click,
				areaRadioListView);
		RelativeLayout font = (RelativeLayout) findViewById(R.id.font);
		RelativeLayout pf = (RelativeLayout) findViewById(R.id.pf);
		RelativeLayout about = (RelativeLayout) findViewById(R.id.about);
		RelativeLayout share = (RelativeLayout) findViewById(R.id.share);
		font.setOnClickListener(listerClick);
		pf.setOnClickListener(listerClick);
		about.setOnClickListener(listerClick);
		share.setOnClickListener(listerClick);
	}

	/**
	 * ѡ�������С�ĵ�ѡ��ť������
	 * */
	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			ITActivity.this.pre.setValue(Constant.FONTKEY, index + "");
			dialog.dismiss();
		}
	}

	class HelpClickListener implements OnClickListener {
		RadioOnClick click;
		ListView areaRadioListView;

		public HelpClickListener(RadioOnClick click, ListView areaRadioListView) {
			this.click = click;
			this.areaRadioListView = areaRadioListView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.font:
				AlertDialog ad = new AlertDialog.Builder(ITActivity.this)
						.setTitle("ѡ�������С")
						.setSingleChoiceItems(Constant.fontList,
								click.getIndex(), click).create();
				areaRadioListView = ad.getListView();
				ad.show();
				break;
			case R.id.pf:// �������
				delCache();
				break;
			case R.id.about:
				Intent it = new Intent(ITActivity.this, AboutActivity.class);
				startActivity(it);
				break;
			case R.id.share:
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "��ӥ���ķ���");
				intent.putExtra(
						Intent.EXTRA_TEXT,
						"��ӥ���ģ�����һ�������Ķ����������Ϊ�ߴ���������ռ���������ͼ���������ͣ������ȵ㣬�������������£������ܿ��ȣ���������������Ȥ��Ϊ�����Щ���ĵ�ʱ�䣬�ȹ������ò͵�ʱ�䣬�����ڵ�ʲô�أ��Ͽ��ж��ɣ���׿�г��������г��ȸ����г�������������");
				startActivity(Intent.createChooser(intent, getTitle()));
				break;
			}
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			return true;
		}
		switch (v.getId()) {
		case R.id.footer_main:
			changeFooterViewBg();
			footerMain.setTextColor(Color.RED);
			onCreate(savedInstanceState);
			break;
		case R.id.footer_help:
			changeFooterViewBg();
			// startActivity(new Intent(this, HelpActivity.class));
			initHelp();
			break;
		case R.id.footer_it_info:// ����
			changeFooterViewBg();
			footerItInfo.setTextColor(Color.RED);
			initDownloadList();
			break;
		case R.id.footer_it_business:// ����
			changeFooterViewBg();
			footerItBuiness.setTextColor(Color.RED);
			initCartoonView();
			break;
		}
		return true;
	}

	public void delCache() {
		// TODO Auto-generated method stub
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("������ջ���...");
		cacheHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Toast.makeText(getApplicationContext(), "�����������",
						Toast.LENGTH_SHORT).show();
				progressDialog.cancel();
			}

		};
		progressDialog.show();
		new Thread() {
			public void run() {
				FileUtil.delCache();
				cacheHandler.sendEmptyMessage(0x123);
			}
		}.start();
		new DownLoadThread().start();
	}

	private void initDownloadList() {
		setContentView(R.layout.download_list);
		ImageView toolBack = (ImageView) findViewById(R.id.toolbar_back);
		TextView textView = (TextView) findViewById(R.id.it_category_title);
		toolBack.setVisibility(ImageView.GONE);
		textView.setText("�ҵ�����");
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("���ڼ����ļ��б�...");
		downloadList = (ListView) findViewById(R.id.download_list);
		// View view = View.inflate(downloadList.getContext(), R.layout.ad,
		// null);
		// downloadList.addFooterView(view);
		downloadHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					dListAdapter = new DownloadListAdapter(
							getApplicationContext(), files);
					downloadList.setAdapter(dListAdapter);
					progressDialog.cancel();
				}
			}

		};
		progressDialog.show();
		new Thread() {
			public void run() {
				files = null;
				files = FileUtil.getDownloadFiles();
				downloadHandler.sendEmptyMessage(0x123);
			}
		}.start();
		new DownLoadThread().start();
		downloadList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				new DownLoadThread().start();
				Intent intent = new Intent(ITActivity.this,
						ImageZoomActivity.class);
				intent.putExtra("file", files[position].getAbsolutePath());
				intent.putExtra("COMMAND", "NOT_DOWNLOAD");
				startActivity(intent);
			}

		});
	}

	@Override
	public void onClick(View v) {
		String typeUrl = null, typeTitle = null;
		switch (v.getId()) {
		case R.id.disgrace:
			typeTitle = Constant.DISGRACE_CATEGORY_TITLE;
			typeUrl = Constant.DISGRACE_CATEGORY_URL;
			break;
		case R.id.seductive:
			typeTitle = Constant.SEDUCTIVE_CATEGORY_TITLE;
			typeUrl = Constant.SEDUCTIVE_CATEGORY_URL;
			break;
		case R.id.story:
			typeTitle = Constant.STORY_CATEGORY_TITLE;
			typeUrl = Constant.STORY_CATEGORY_URL;
			break;
		case R.id.people:
			typeTitle = Constant.PEOPLE_CATEGORY_TITLE;
			typeUrl = Constant.PEOPLE_CATEGORY_URL;
			break;
		case R.id.military:
			typeTitle = Constant.HOT_NEWS_CATEGORY_TITLE;
			typeUrl = Constant.HOT_NEWS_CATEGORY_URL;
			break;
		case R.id.car:
			typeTitle = Constant.CAR_CATEGORY_TITLE;
			typeUrl = Constant.CAR_CATEGORY_URL;
			break;
		}
		new DownLoadThread().start();
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putCharSequence("typeUrl", typeUrl);
		bundle.putCharSequence("typeTitle", typeTitle);
		msg.setData(bundle);
		msg.what = Constant.LOAD_BLOG_AFTER_CLICK;
		handler.sendMessage(msg);// ���Ϳ�ʼ������ʾ
	}
}
