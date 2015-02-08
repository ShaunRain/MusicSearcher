package com.Rain.musicsearch.grallery3d;
import java.util.ArrayList;

import com.Rain.musicsearch.R;
import com.Rain.musicsearch.domain.Album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class Gallery3DActivity extends Activity {

	private TextView tvTitle; 	
	private GalleryView gallery; 	
	private ImageAdapter adapter;
	
	private ArrayList<Album> albums;
	private int[] album_cover;
	private String[] album_name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grallery_layout);
		
		/*Intent intent = getIntent();
		albums =  (ArrayList<Album>) intent.getSerializableExtra("albums");
		album_cover = new int[albums.size()];
		album_name = new String[albums.size()];
		int i = 0;
		for(Album album : albums ) {
			album_cover[i] = album.getCover();
			album_name[i] = album.getAlbum();	
			i++;
		}
*/
		initRes();
	}
	
	private void initRes(){
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		gallery = (GalleryView) findViewById(R.id.mygallery);

		adapter = new ImageAdapter(this);
		adapter.createReflectedImages();
		gallery.setAdapter(adapter);
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tvTitle.setText(adapter.titles[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		gallery.setOnItemClickListener(new OnItemClickListener() {			// 设置点击事件监听
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(Gallery3DActivity.this, "img " + (position+1) + " selected", Toast.LENGTH_SHORT).show();
			}
		});
	}
}