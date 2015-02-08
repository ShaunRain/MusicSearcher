package com.Rain.musicsearch;

import java.util.ArrayList;
import java.util.List;

import com.Rain.musicsearch.dao.AlbumDao;
import com.Rain.musicsearch.dao.SongDao;
import com.Rain.musicsearch.domain.Album;
import com.Rain.musicsearch.domain.Song;
import com.Rain.musicsearch.extra.DoubanMusic;
import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity2 extends Activity {
	private boolean flag = false;
	private static final int RESULT_CHANGE = 3;
	private static final int RESULT_DELETE = 4;

	private String id = null;
	private DoubanMusic douban;
	private String[] api_result = new String[11];

	private EditText ed_album2;
	private EditText ed_artist2;
	private EditText ed_date;
	private EditText ed_company;
	private EditText ed_style;
	private ImageView iv_cover;

	private TextView api_artist;
	private TextView api_version;
	private TextView api_media;
	private TextView api_pubdate;
	private TextView api_publisher;
	private TextView api_discs;
	private TextView api_ean;
	private TextView api_tracks;
	private TextView api_rate;
	private TextView api_number;
	private RatingBar api_ratebar;
	private SmartImageView api_cover;

	/*---------------------*/
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView textView1, textView2, textView3;
	private List<View> views;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View view1, view2, view3;// 各个页卡

	/*---------------------*/

	private int[] covers = { R.drawable.albumnull, R.drawable.cover1,
			R.drawable.cover2, R.drawable.cover3, R.drawable.cover4,
			R.drawable.cover5, R.drawable.cover6, R.drawable.cover7 };

	String album;
	String date;
	String aritist;
	String company;
	String style;
	String[] tracks;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				api_result = DoubanMusic.api_result;
				api_artist.setText(api_result[0]);
				api_version.setText(api_result[1]);
				api_media.setText(api_result[2]);
				api_pubdate.setText(api_result[3]);
				api_publisher.setText(api_result[4]);
				api_discs.setText(api_result[5]);
				api_ean.setText(api_result[6]);
				api_tracks.setText(api_result[7]);
				api_rate.setText(api_result[8]);
				api_number.setText(api_result[9]);
				api_cover.setImageUrl(api_result[10].toString().trim(),
						R.drawable.ic_launcher, R.drawable.ic_launcher);
				api_ratebar.setRating(Float.valueOf(api_result[8]));
				break;
			}

		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager1);
		/*------------------------*/
		InitImageView();
		InitTextView();
		InitViewPager();
		/*-------------------------*/

		intViews();

		Intent intent = getIntent();
		String album = intent.getStringExtra("album");
		String artist = intent.getStringExtra("artist");
		String date = intent.getStringExtra("date");
		String company = intent.getStringExtra("company");
		String style = intent.getStringExtra("style");
		id = intent.getStringExtra("id");
		int cover = intent.getIntExtra("cover", -1);
		// System.out.println("cover"+cover);
		flag = intent.getBooleanExtra("flag", false);

		String[] tracks = intent.getStringArrayExtra("tracks");
		setTracks(tracks);

		Album result = new Album(album, artist, date, company, style, id, cover);
		setText(result);

		if (id != null) {
			new Thread() {
				public void run() {
					super.run();
					douban = new DoubanMusic(id);
					handler.sendEmptyMessage(0);

				};
			}.start();
		}

		// setDetail();
	}

	public void intViews() {
		ed_album2 = (EditText) view1.findViewById(R.id.ed_album2);
		ed_artist2 = (EditText) view1.findViewById(R.id.ed_artist2);
		ed_date = (EditText) view1.findViewById(R.id.ed_date);
		ed_company = (EditText) view1.findViewById(R.id.ed_company);
		ed_style = (EditText) view1.findViewById(R.id.ed_style);
		iv_cover = (ImageView) view1.findViewById(R.id.iv_result);

		api_artist = (TextView) view3.findViewById(R.id.api_artist);
		api_version = (TextView) view3.findViewById(R.id.api_version);
		api_media = (TextView) view3.findViewById(R.id.api_media);
		api_pubdate = (TextView) view3.findViewById(R.id.api_pubdate);
		api_publisher = (TextView) view3.findViewById(R.id.api_publisher);
		api_discs = (TextView) view3.findViewById(R.id.api_discs);
		api_ean = (TextView) view3.findViewById(R.id.api_ean);
		api_tracks = (TextView) view3.findViewById(R.id.api_tracks);
		api_rate = (TextView) view3.findViewById(R.id.api_rate);
		api_number = (TextView) view3.findViewById(R.id.api_number);
		api_cover = (SmartImageView) view3.findViewById(R.id.api_cover);

		api_ratebar = (RatingBar) view3.findViewById(R.id.api_ratebar);
		api_ratebar.setMax(10);
	}

	public void setText(Album arubamu) {

		ed_album2.setText(arubamu.getAlbum());
		ed_artist2.setText(arubamu.getArtist());
		ed_date.setText(arubamu.getDate());
		ed_company.setText(arubamu.getCompany());
		ed_style.setText(arubamu.getStyle());
		int coverResult = arubamu.getCover();
		System.out.println("coverresult: " + coverResult);
		iv_cover.setImageResource(covers[coverResult + 1]);

	}

	public void setTracks(String[] tracks) {
		LinearLayout scroll_list = (LinearLayout) view2
				.findViewById(R.id.scroll_list);
		if (tracks == null) {// null对象不能使用 equal来判断，也不能使用toString函数.
			TextView tv = new TextView(this);
			tv.setTextSize(20);
			tv.setTextColor(Color.WHITE);
			tv.setText("");
			scroll_list.addView(tv);
		} else {
			for (String track : tracks) {
				TextView tv = new TextView(this);
				tv.setTextSize(20);
				tv.setTextColor(Color.WHITE);
				tv.setText(track);
				scroll_list.addView(tv);
			}
		}
	}

	public void changeInfo(View view) {
		if (flag) {
			String album = ed_album2.getText().toString().trim();
			String newartist = ed_artist2.getText().toString().trim();
			String newdate = ed_date.getText().toString().trim();
			String newcompany = ed_company.getText().toString().trim();
			String newstyle = ed_style.getText().toString().trim();
			Intent intent = new Intent(ResultActivity2.this, MainActivity.class);
			intent.putExtra("album", album);
			intent.putExtra("newartist", newartist);
			intent.putExtra("newdate", newdate);
			intent.putExtra("newcompany", newcompany);
			intent.putExtra("newstyle", newstyle);
			setResult(RESULT_CHANGE, intent);
			Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "无操作权限!", Toast.LENGTH_SHORT).show();
		}
	}

	public void deleteInfo(View view) {
		if (flag) {
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("Sure to delete？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									String album = ed_album2.getText()
											.toString().trim();
									Intent intent = new Intent(
											ResultActivity2.this,
											MainActivity.class);
									intent.putExtra("album", album);
									setResult(RESULT_DELETE, intent);

									Toast.makeText(ResultActivity2.this,
											"删除成功", Toast.LENGTH_SHORT).show();
									finish();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// 点击“返回”后的操作,这里不设置没有任何操作
									finish();
								}
							}).create();
			dialog.show();
		} else {
			Toast.makeText(this, "无操作权限!", Toast.LENGTH_SHORT).show();
		}

	}

	/*****************************************/
	private void InitViewPager() {
		viewPager = (ViewPager) ResultActivity2.this
				.findViewById(R.id.viewpager1);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.activity_result_album1, null);
		view2 = inflater.inflate(R.layout.activity_result_album2, null);
		/*
		 * 根据数据为view2 添加textview
		 */

		view3 = inflater.inflate(R.layout.activity_result_album3, null);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 */

	private void InitTextView() {
		textView1 = (TextView) ResultActivity2.this.findViewById(R.id.text1);
		textView2 = (TextView) ResultActivity2.this.findViewById(R.id.text2);
		textView3 = (TextView) ResultActivity2.this.findViewById(R.id.text3);

		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
		textView3.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 2 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据 3
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 
	 * 头标点击监听 3
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}

	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {

			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
		}

	}
}
