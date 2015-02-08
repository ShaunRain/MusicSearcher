package com.Rain.musicsearch;

import java.util.ArrayList;

import com.Rain.musicsearch.AllAlbumsActivity.itemAdapter;
import com.Rain.musicsearch.AllAlbumsActivity.itemAdapter.ViewHolder;
import com.Rain.musicsearch.domain.Album;
import com.Rain.musicsearch.domain.Artist;
import com.Rain.musicsearch.swipemenulistview.SwipeMenu;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuCreator;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuItem;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuListView;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.Rain.musicsearch.swipemenulistview.SwipeMenuListView.OnSwipeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class AllArtistsActivity extends Activity{
	
	private int[] photos = { R.drawable.artistnull, R.drawable.photo1,
			R.drawable.photo2, R.drawable.photo3 };
	
	protected static final int RESULT_DELETE = 7;
	private static final int RESULT_OPEN = 8;
	private boolean flag = false;
	private ArrayList<Artist> mitemList;
	private itemAdapter mAdapter;
	private SwipeMenuListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allartists);
		
		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag", false);
		ArrayList<Artist> artists = (ArrayList<Artist>) intent.getSerializableExtra("artists");
		
		mitemList = artists;

		mListView = (SwipeMenuListView) findViewById(R.id.list_allartists);
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
				Artist item = mitemList.get(position);
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

	private void delete(final Artist item) {
		if (flag) {
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("Sure to delete？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									String name = item.getName()
											.toString().trim();
									Intent intent = new Intent(
											AllArtistsActivity.this,
											MainActivity.class);
									intent.putExtra("name", name);
									setResult(RESULT_DELETE, intent);

									Toast.makeText(AllArtistsActivity.this,
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
			Toast.makeText(this, "无操作权限!", Toast.LENGTH_SHORT).show();
		}
	}

	private void open(Artist item) {
		String openArtist = item.getName().toString().trim();
		Intent intent = new Intent(AllArtistsActivity.this,
				MainActivity.class);
		intent.putExtra("name", openArtist);
		setResult(RESULT_OPEN, intent);
		finish();
	}

	class itemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mitemList.size();
		}

		@Override
		public Artist getItem(int position) {
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
			Artist item = getItem(position);
			Resources res = getResources();
			holder.iv_icon.setImageDrawable(res.getDrawable(photos[item.getPhoto()+1]));
			holder.tv_name.setText(item.getName());
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
