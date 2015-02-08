package com.Rain.musicsearch;

import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
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
import android.widget.TextView;
import android.widget.Toast;
import com.Rain.musicsearch.domain.Album;
import com.Rain.musicsearch.domain.Artist;

public class ResultActivity3 extends Activity{
	private boolean flag = false;
	private static final int RESULT_CHANGE = 6;
	private static final int RESULT_DELETE = 7;

	private EditText ed_artist_name;
	private EditText ed_artist_area;
	private EditText ed_artist_company;
	private EditText ed_artist_birth;
	private ImageView iv_photo;

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

	private int[] photos = { R.drawable.artistnull, R.drawable.photo1,
			R.drawable.photo2, R.drawable.photo3 };

	String name;
	String area;
	String birth;
	String company;
	String[] tracks;
	String[] ablums;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager2);
		/*------------------------*/
		InitImageView();
		InitTextView();
		InitViewPager();
		/*-------------------------*/

		ed_artist_name = (EditText) view1.findViewById(R.id.ed_artist_name);
		ed_artist_area = (EditText) view1.findViewById(R.id.ed_artist_area);
		ed_artist_company = (EditText) view1.findViewById(R.id.ed_artist_company);
		ed_artist_birth= (EditText) view1.findViewById(R.id.ed_artist_birth);
		iv_photo = (ImageView) view1.findViewById(R.id.iv_photo);

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String area = intent.getStringExtra("area");
		String company = intent.getStringExtra("company");
		String birth = intent.getStringExtra("birth");
		int photo = intent.getIntExtra("photo", -1);
		flag = intent.getBooleanExtra("flag", false);

		String[] tracks = intent.getStringArrayExtra("tracks");
		setTracks(tracks);
		String[] albums = intent.getStringArrayExtra("albums");
		setAlbums(albums);

		Artist result = new Artist(name, area, company, birth, photo);
		setText(result);

	}

	public void setText(Artist artist) {

		ed_artist_name.setText(artist.getName());
		ed_artist_area.setText(artist.getArea());
		ed_artist_company.setText(artist.getCompany());
		ed_artist_birth.setText(artist.getBirth());
		int photoResult = artist.getPhoto();
		iv_photo.setImageResource(photos[photoResult + 1]);

	}
	 

	public void setTracks(String[] tracks) {
		LinearLayout artist_song_list = (LinearLayout) view2
				.findViewById(R.id.artist_song_list);
		if (tracks == null) {// null对象不能使用 equal来判断，也不能使用toString函数.
			TextView tv = new TextView(this);
			tv.setTextSize(20);
			tv.setTextColor(Color.WHITE);
			tv.setText("");
			artist_song_list.addView(tv);
		} else {
			for (String track : tracks) {
				TextView tv = new TextView(this);
				tv.setTextSize(20);
				tv.setTextColor(Color.WHITE);
				tv.setText(track);
				artist_song_list.addView(tv);
			}
		}
	}
	
	public void setAlbums(String[] albums) {
		LinearLayout artist_album_list = (LinearLayout) view3
				.findViewById(R.id.artist_album_list);
		if (albums == null) {// null对象不能使用 equal来判断，也不能使用toString函数.
			TextView tv = new TextView(this);
			tv.setTextSize(20);
			tv.setTextColor(Color.WHITE);
			tv.setText("");
			artist_album_list.addView(tv);
		} else {
			for (String album : albums) {
				TextView tv = new TextView(this);
				tv.setTextSize(20);
				tv.setTextColor(Color.WHITE);
				tv.setText(album);
				artist_album_list.addView(tv);
			}
		}
	}

	
	
	public void changeInfo(View view) {
		if (flag) {
			String name = ed_artist_name.getText().toString().trim();
			String newarea = ed_artist_area.getText().toString().trim();
			String newcompany = ed_artist_company.getText().toString().trim();
			String newbirth = ed_artist_birth.getText().toString().trim();
			Intent intent = new Intent(ResultActivity3.this, MainActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("newarea", newarea);
			intent.putExtra("newcompany", newcompany);
			intent.putExtra("newbirth", newbirth);
			setResult(RESULT_CHANGE, intent);
			Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
			finish();
		}else {
			Toast.makeText(this, "无操作权限", Toast.LENGTH_SHORT).show();
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
									String artist = ed_artist_name.getText()
											.toString().trim();
									Intent intent = new Intent(
											ResultActivity3.this,
											MainActivity.class);
									intent.putExtra("name", artist);
									setResult(RESULT_DELETE, intent);

									Toast.makeText(ResultActivity3.this,
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
		}else {
			Toast.makeText(this, "无操作权限", Toast.LENGTH_SHORT).show();
		}

	}

	/*****************************************/
	private void InitViewPager() {
		viewPager = (ViewPager) ResultActivity3.this
				.findViewById(R.id.viewpager2);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.activity_result_artist1, null);
		view2 = inflater.inflate(R.layout.activity_result_artist2, null);
		view3 = inflater.inflate(R.layout.activity_result_artist3, null);
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
		textView1 = (TextView) ResultActivity3.this.findViewById(R.id.text4);
		textView2 = (TextView) ResultActivity3.this.findViewById(R.id.text5);
		textView3 = (TextView) ResultActivity3.this.findViewById(R.id.text6);

		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
		textView3.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 2 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据 3
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor2);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.b)
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
