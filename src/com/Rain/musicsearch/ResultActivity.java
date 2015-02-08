package com.Rain.musicsearch;

import java.util.ArrayList;

import com.Rain.musicsearch.dao.SongDao;
import com.Rain.musicsearch.domain.Song;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ResultActivity extends Activity {
	private static final int RESULT_CHANGE = 1;
	private static final int RESULT_DELETE = 2;
	protected static final int RESULT_OPEN = 5;
	private EditText ed_track;
	private EditText ed_album;
	private EditText ed_artist;
	private EditText ed_time;
	private ImageView iv_cover;
	private ImageView bg_cover;
	private double width;
	private double height;
	
	private boolean flag = false;

	private int[] covers = { R.drawable.albumnull, R.drawable.cover1,
			R.drawable.cover2, R.drawable.cover3, R.drawable.cover4,
			R.drawable.cover5, R.drawable.cover6, R.drawable.cover7};
	private Bitmap[] bitmaps;
	String track;
	String album;
	String artist;
	String time;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_song);
		
		getScreen();
		setBitmap(covers);
		
		ed_track = (EditText) findViewById(R.id.ed_track);
		ed_album = (EditText) findViewById(R.id.ed_album);
		ed_artist = (EditText) findViewById(R.id.ed_artist);
		ed_time = (EditText) findViewById(R.id.ed_time);

		iv_cover = (ImageView) findViewById(R.id.iv_result);
		iv_cover.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String openAlbum = ed_album.getText().toString().trim();
				Intent intent = new Intent(ResultActivity.this,
						MainActivity.class);
				intent.putExtra("album", openAlbum);
				setResult(RESULT_OPEN, intent);
				// Toast.makeText(ResultActivity.this, "删除成功",
				// Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		Intent intent = getIntent();
		String track = intent.getStringExtra("track");
		String artist = intent.getStringExtra("artist");
		String album = intent.getStringExtra("album");
		String time = intent.getStringExtra("time");
		int cover = intent.getIntExtra("cover", -1);
		flag = intent.getBooleanExtra("flag", false);
		Song result = new Song(track, artist, album, time, cover);
		setText(result);

	}

	public void getScreen() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	public void setBitmap(int[] covers) {
		Resources res = getResources();
		bitmaps = new Bitmap[covers.length];
		for(int i=0;i<covers.length;i++) {
			bitmaps[i] = BitmapFactory.decodeResource(res, covers[i]);
		}
	}

	public void setText(Song song) {

		ed_track.setText(song.getTrack());
		ed_album.setText(song.getAlbum());
		ed_artist.setText(song.getArtist());
		ed_time.setText(song.getTime());
		int coverResult = song.getCover();
		iv_cover.setImageResource(covers[coverResult + 1]);
		
		//Drawable cover = iv_cover.getDrawable().mutate();  
		
		setBG(bitmaps[coverResult + 1]);
		
	}

	public void changeInfo(View view) {
		

		// dao.update(track, newartist, newalbum, newtime);

		if (flag) {
			String track = ed_track.getText().toString().trim();
			String newalbum = ed_album.getText().toString().trim();
			String newartist = ed_artist.getText().toString().trim();
			String newtime = ed_time.getText().toString().trim();
			Intent intent = new Intent(ResultActivity.this, MainActivity.class);
			intent.putExtra("track", track);
			intent.putExtra("newartist", newartist);
			intent.putExtra("newalbum", newalbum);
			intent.putExtra("newtime", newtime);
			setResult(RESULT_CHANGE, intent);
			// getParent().startActivityForResult(intent, 3);
			Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
			finish();
		}else {
			Toast.makeText(this, "无操作权限！", Toast.LENGTH_SHORT).show();
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
									String track = ed_track.getText()
											.toString().trim();
									Intent intent = new Intent(
											ResultActivity.this,
											MainActivity.class);
									intent.putExtra("track", track);
									setResult(RESULT_DELETE, intent);

									Toast.makeText(ResultActivity.this, "删除成功",
											Toast.LENGTH_SHORT).show();
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
			Toast.makeText(this, "无操作权限！", Toast.LENGTH_SHORT).show();
		}

	}

	public void setBG(Bitmap bmp) {
		Bitmap result = blurBitmap(bmp);
		double result2_height = result.getHeight();
		double result2_width = (width/height)*result2_height;
		int x = (int) ((result.getWidth() - result2_width)/2);
		int y = 0;
		Bitmap result2 = Bitmap.createBitmap(result, x, y, (int)result2_width, (int)result2_height, null, false);
		Drawable background = new BitmapDrawable(result2);
		
		ResultActivity.this.getWindow().setBackgroundDrawable(background);
	}

	public Bitmap blurBitmap(Bitmap bitmap) {

		// Let's create an empty bitmap with the same size of the bitmap we want
		// to blur
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		// Instantiate a new Renderscript
		RenderScript rs = RenderScript.create(getApplicationContext());

		// Create an Intrinsic Blur Script using the Renderscript
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs,
				Element.U8_4(rs));

		// Create the Allocations (in/out) with the Renderscript and the in/out
		// bitmaps
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

		// Set the radius of the blur
		blurScript.setRadius(25.f);

		// Perform the Renderscript
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);

		// Copy the final bitmap created by the out Allocation to the outBitmap
		allOut.copyTo(outBitmap);

		// recycle the original bitmap
		bitmap.recycle();

		// After finishing everything, we destroy the Renderscript.
		rs.destroy();

		return outBitmap;
	}

}
