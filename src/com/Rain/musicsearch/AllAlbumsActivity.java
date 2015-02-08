package com.Rain.musicsearch;

import java.util.ArrayList;
import java.util.List;

import com.Rain.musicsearch.domain.Album;
import com.Rain.musicsearch.domain.Song;
import com.Rain.musicsearch.swipemenulistview.SwipeMenu;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuCreator;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuItem;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuListView;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuListView.OnSwipeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class AllAlbumsActivity extends Activity {
	protected static final int RESULT_DELETE = 4;
	private static final int RESULT_OPEN = 5;
	private boolean flag = false;
	private ArrayList<Album> mitemList;
	private itemAdapter mAdapter;
	private SwipeMenuListView mListView;

	private int[] covers = { R.drawable.albumnull, R.drawable.cover1,
			R.drawable.cover2, R.drawable.cover3, R.drawable.cover4,
			R.drawable.cover5, R.drawable.cover6, R.drawable.cover7 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allalbums);

		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag", false);
		ArrayList<Album> albums = (ArrayList<Album>) intent
				.getSerializableExtra("albums");

		mitemList = albums;

		mListView = (SwipeMenuListView) findViewById(R.id.list_allalbums);
		mAdapter = new itemAdapter();
		mListView.setAdapter(mAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				Album item = mitemList.get(position);
				switch (index) {
				case 0:
					// open
					open(item);
					break;
				case 1:
					// delete
					// delete(item);
					mitemList.remove(position);
					mAdapter.notifyDataSetChanged();
					delete(item);
					break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						position + " long click", 0).show();
				return false;
			}
		});

	}

	private void delete(final Album item) {
		if (flag) {
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("Sure to delete？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									String album = item.getAlbum().toString()
											.trim();
									Intent intent = new Intent(
											AllAlbumsActivity.this,
											MainActivity.class);
									intent.putExtra("album", album);
									setResult(RESULT_DELETE, intent);

									Toast.makeText(AllAlbumsActivity.this,
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

	private void open(Album item) {
		String openAlbum = item.getAlbum().toString().trim();
		Intent intent = new Intent(AllAlbumsActivity.this, MainActivity.class);
		intent.putExtra("album", openAlbum);
		setResult(RESULT_OPEN, intent);
		finish();
	}

	class itemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mitemList.size();
		}

		@Override
		public Album getItem(int position) {
			return mitemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			Album item = getItem(position);
			Resources res = getResources();
			holder.iv_icon.setImageDrawable(res.getDrawable(covers[item
					.getCover() + 1]));
			holder.tv_name.setText(item.getAlbum());
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
