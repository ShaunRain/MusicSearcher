package com.Rain.musicsearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.Rain.musicsearch.dao.AlbumDao;
import com.Rain.musicsearch.dao.ArtistDao;
import com.Rain.musicsearch.dao.CompanyDao;
import com.Rain.musicsearch.dao.SongDao;
import com.Rain.musicsearch.domain.Album;
import com.Rain.musicsearch.domain.Artist;
import com.Rain.musicsearch.domain.Company;
import com.Rain.musicsearch.domain.Song;
import com.Rain.musicsearch.extra.Titanic;
import com.Rain.musicsearch.extra.TitanicTextView;
import com.Rain.musicsearch.grallery3d.Gallery3DActivity;

import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnTouchListener {
	// ������ʾ������menuʱ����ָ������Ҫ�ﵽ���ٶȡ� 
	public static final int SNAP_VELOCITY = 200;

	// ��Ļ���ֵ��
	private int screenWidth;

	// menu�����Ի����������Ե��ֵ��menu���ֵĿ��������marginLeft�����ֵ֮�󣬲����ټ��١�
	private int leftEdge;

	// menu�����Ի��������ұ�Ե��ֵ��Ϊ0����marginLeft����0֮�󣬲������ӡ�
	private int rightEdge = 0;

	//menu��ȫ��ʾʱ������content�Ŀ��ֵ��
	private int menuPadding = 80;

	// �����ݵĲ��֡�
	private View content;

	// menu�Ĳ��֡�
	private View menu;

	// menu���ֵĲ�����ͨ���˲���������leftMargin��ֵ��
	private LinearLayout.LayoutParams menuParams;

	// ��¼��ָ����ʱ�ĺ����ꡣ
	private float xDown;

	// ��¼��ָ�ƶ�ʱ�ĺ����ꡣ
	private float xMove;

	// ��¼�ֻ�̧��ʱ�ĺ����ꡣ
	private float xUp;

	// menu��ǰ����ʾ�������ء�ֻ����ȫ��ʾ������menuʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
	private boolean isMenuVisible;

	// ���ڼ�����ָ�������ٶȡ�
	private VelocityTracker mVelocityTracker;

	private TitanicTextView myTitanicTextView;
	private Titanic titanic;
	private boolean isAct = true;
	public static final int chooseSong = 1, chooseAlbum = 2, chooseArtist = 3,
			chooseCompany = 4, chooseCoverFlow = 5;
	private String PATH = "/data/data/com.Rain.musicsearch/databases/ipod.db";
	private AutoCompleteTextView auto_search;
	private Button bt_search;
	private RadioGroup choose;
	private RadioButton rb_track, rb_artist, rb_album;
	private ImageButton ib_about;
	private TextView tv_alltracks, tv_allalbums, tv_allartists,
			tv_allcompanies, tv_coverflow;
	private SongDao songdao;
	private AlbumDao albumdao;
	private ArtistDao artistdao;
	private CompanyDao companydao;
	private boolean f_track = false;
	private boolean f_artist = false;
	private boolean f_album = false;
	private boolean flag = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag", false);

		initValues();
		content.setOnTouchListener(this);

		songdao = new SongDao(this);
		albumdao = new AlbumDao(this);
		artistdao = new ArtistDao(this);
		companydao = new CompanyDao(this);

		initViews();
		ib_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAct) {
					titanic.cancel();
					isAct = false;
				}else {
					titanic.start(myTitanicTextView);
				}
			}

		});

		choose.setOnCheckedChangeListener(radiogpchange);
		tv_alltracks.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setAll(chooseSong);
			}
		});

		tv_allalbums.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setAll(chooseAlbum);
			}
		});

		tv_allartists.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setAll(chooseArtist);
			}
		});
		
		tv_allcompanies.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setAll(chooseCompany);
			}
		});

		tv_coverflow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setAll(chooseCoverFlow);
			}
		});

		File file = new File(PATH);
		if (!file.exists()) {
			createDB();

		}
		// result = readSD();
		// createBySD(result);

	}

	private void initViews() {
		myTitanicTextView = (TitanicTextView) findViewById(R.id.titanic_tv);
		titanic = new Titanic();
		titanic.start(myTitanicTextView);
		auto_search = (AutoCompleteTextView) findViewById(R.id.auto_search);
		bt_search = (Button) findViewById(R.id.bt_search);
		choose = (RadioGroup) findViewById(R.id.choose);
		rb_track = (RadioButton) findViewById(R.id.rb_track);
		rb_artist = (RadioButton) findViewById(R.id.rb_artist);
		rb_album = (RadioButton) findViewById(R.id.rb_album);
		tv_alltracks = (TextView) findViewById(R.id.tv_alltracks);
		tv_allalbums = (TextView) findViewById(R.id.tv_allalbums);
		tv_allartists = (TextView) findViewById(R.id.tv_allartists);
		tv_allcompanies = (TextView) findViewById(R.id.tv_allcompanies);
		tv_coverflow = (TextView) findViewById(R.id.tv_coverflow);
		ib_about = (ImageButton) findViewById(R.id.ib_about);
	}

	private RadioGroup.OnCheckedChangeListener radiogpchange = new RadioGroup.OnCheckedChangeListener() {

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == rb_track.getId()) {
				f_track = true;
				f_artist = false;
				f_album = false;
			} else if (checkedId == rb_artist.getId()) {
				f_track = false;
				f_artist = true;
				f_album = false;
			} else if (checkedId == rb_album.getId()) {
				f_track = false;
				f_artist = false;
				f_album = true;
			}
		}
	};

	public void scanSD(View view) {
		ArrayList<String> find = getFiles();
		int count = 0;
		for (String title : find) {
			String[] result = null;
			result = readSD(title);
			boolean flag = createBySD(result);
			if (flag) {
				count++;
			}
		}
		Toast.makeText(this, "��ȡ�����ļ����,����" + count + "����Ŀ", Toast.LENGTH_SHORT)
				.show();
	}

	public void createDB() {
		/*
		 * Songs Track Artist Album Time Cover 1.Stratus Uyama Hiroto A Song Of
		 * The Song 4:37 no1 2.22 Taylor Swift Red 3:52 no2 3.Take My Hand
		 * Simple Plan Simple Plan 3:51 no3 4. 5. 6. 7. 8. 9.
		 */
		songdao.create("Stratus", "Uyama Hiroto", "A Song Of The Song", "4:37",
				0);
		songdao.create("22", "Taylor Swift", "Red", "3:52", 1);
		songdao.create("stay stay stay", "Taylor Swift", "Red", "3:26", 1);
		songdao.create("Take My Hand", "Simple Plan", "Simple Plan", "3:51", 2);
		songdao.create("�������", "���Ө", "LaLa", "4:18", 3);
		songdao.create("Coffins", "Pegboard Nerds", "Coffins", "2:24", 4);
		songdao.create("Mine", "Taylor Swift", "Speak Now", "3:51", 5);
		songdao.create("My Way", "Chara", "MONTAGE", "5:34", 6);
		songdao.create("Sunday Park", "Chara", "MONTAGE", "4:17", 6);
		songdao.create("�Ϻ��٥���", "Chara", "MONTAGE", "4:59", 6);

		albumdao.create("Red", "Taylor Swift", "2013", "Big Machine Records",
				"Pop/Country", "11618242", 1);
		albumdao.create("Speak Now", "Taylor Swift", "2010",
				"Big Machine Records", "Pop/Country", "4918610", 5);
		albumdao.create("LaLa", "���Ө", "2009", "��������", "Mandarin Pop", "3746394", 3);
		albumdao.create("MONTAGE", "Chara", "1996", "Sony Music", "Pop,OST", "1437211", 6);

		artistdao.create("Taylor Swift", "United States",
				"Big Machine Records", "1989.12.13", 0);
		artistdao.create("���Ө", "TaiWan", "��������", "1984.12.20", 1);
		artistdao.create("Chara", "Japan", "Sony Music", "1968.1.13", 2);
		
		companydao.create("Big Machine Records", "USA", "2005", 0);
		companydao.create("��������", "TAIWAN", "2006", 1);
		companydao.create("Sony Music", "Japan", "1929(as ARC)", 2);
		
		
		
	}

	public void setAll(int all) {
		switch (all) {
		case chooseSong:
			ArrayList<Song> songs = songdao.findall();
			Intent intent1 = new Intent(MainActivity.this,
					AllTracksActivity.class);
			intent1.putExtra("songs", songs);
			intent1.putExtra("flag", flag);
			startActivity(intent1);
			break;

		case chooseAlbum:
			ArrayList<Album> albums = albumdao.findall();
			Intent intent2 = new Intent(MainActivity.this,
					AllAlbumsActivity.class);
			intent2.putExtra("albums", albums);
			intent2.putExtra("flag", flag);
			startActivityForResult(intent2, 20);
			break;

		case chooseArtist:
			ArrayList<Artist> artists = artistdao.findall();
			Intent intent3 = new Intent(MainActivity.this,
					AllArtistsActivity.class);
			intent3.putExtra("artists", artists);
			intent3.putExtra("flag", flag);
			startActivityForResult(intent3, 20);
			break;
			
		case chooseCompany:
			ArrayList<Company> companies = companydao.findall();
			Intent intent4 = new Intent(MainActivity.this,
					AllCompaniesActivity.class);
			intent4.putExtra("companies", companies);
			intent4.putExtra("flag", flag);
			startActivityForResult(intent4, 20);
			break;

		case chooseCoverFlow:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			ComponentName componentName = new ComponentName(
					"com.android.testflow", "com.android.tank.Main");
			intent.setComponent(componentName);
			startActivity(intent);
			break;

		}
	}

	public boolean createBySD(String[] result) {
		boolean flag = false;
		String trackSD = result[0];
		String artistSD = result[1];
		String albumSD = result[2];
		String timeSD = result[3];
		Song newsong;
		newsong = songdao.findBytrack(trackSD);
		if (newsong.getTrack() == null) {
			songdao.create(trackSD, artistSD, albumSD, timeSD, -1);
			flag = true;
		}
		return flag;
	}

	public String[] readSD(String title) {
		String[] result = new String[] { "0", "0", "0", "0" };
		String track = null, artist = null, album = null, time = null;
		try {
			File song = new File(Environment.getExternalStorageDirectory(),
					title);

			Intent scanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			scanIntent.setData(Uri.fromFile(song));
			getApplicationContext().sendBroadcast(scanIntent);

			FileInputStream file = new FileInputStream(song);
			int size = (int) song.length();
			file.skip(size - 128);
			byte[] last128 = new byte[128];
			file.read(last128);
			String id3 = new String(last128);
			String tag = id3.substring(0, 3);
			if (tag.equals("TAG")) {
				System.out.println("Title:" + id3.substring(3, 32).trim());
				System.out.println("Artist:" + id3.substring(33, 62).trim());
				System.out.println("Album:" + id3.substring(63, 91).trim());
				System.out.println("Year:" + id3.substring(93, 97).trim());

				track = id3.substring(3, 32).toString().trim();
				artist = id3.substring(33, 62).toString().trim();
				album = id3.substring(63, 91).toString().trim();
				time = id3.substring(93, 97).toString().trim();

			} else {
				System.out.println("No ID3 Info");
				file.close();
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR1");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR2");
			e.printStackTrace();
		}
		result[0] = track;
		result[1] = artist;
		result[2] = album;
		result[3] = time;
		return result;
	}

	private ArrayList<String> getFiles() {
		ArrayList<String> songs = new ArrayList<>();
		File[] allFiles = Environment.getExternalStorageDirectory().listFiles();
		for (int i = 0; i < allFiles.length; i++) {
			File file = allFiles[i];
			if (file.isFile() && file.getAbsolutePath().contains(".mp3")) {
				songs.add(file.getName().toString().trim());
			}
		}
		return songs;
	}

	public void searchIt(View view) {
		String search = auto_search.getText().toString().trim();
		if (f_track == true) {
			Song aim = songdao.findBytrack(search);
			Intent intent = new Intent(MainActivity.this, ResultActivity.class);

			intent.putExtra("track", aim.getTrack());
			intent.putExtra("artist", aim.getArtist());
			intent.putExtra("album", aim.getAlbum());
			intent.putExtra("time", aim.getTime());
			intent.putExtra("cover", aim.getCover());
			intent.putExtra("flag", flag);

			startActivityForResult(intent, 10);
		} else if (f_album == true) {
			Album aim = albumdao.findByalbum(search);
			Intent intent = new Intent(MainActivity.this, ResultActivity2.class);

			intent.putExtra("album", aim.getAlbum());
			intent.putExtra("artist", aim.getArtist());
			intent.putExtra("date", aim.getDate());
			intent.putExtra("company", aim.getCompany());
			intent.putExtra("style", aim.getStyle());
			intent.putExtra("id", aim.getId());
			intent.putExtra("cover", aim.getCover());
			intent.putExtra("flag", flag);

			String[] tracks = null;
			if (aim.getAlbum() != null) {
				tracks = albumdao.andSong(aim.getAlbum());
			}
			intent.putExtra("tracks", tracks);

			startActivityForResult(intent, 20);
		} else if (f_artist == true) {
			Artist aim = artistdao.findByartist(search);
			Intent intent = new Intent(MainActivity.this, ResultActivity3.class);

			intent.putExtra("name", aim.getName());
			intent.putExtra("area", aim.getArea());
			intent.putExtra("company", aim.getCompany());
			intent.putExtra("birth", aim.getBirth());
			intent.putExtra("photo", aim.getPhoto());
			intent.putExtra("flag", flag);

			String[] tracks = null;
			String[] albums = null;

			if (aim.getName() != null) {
				tracks = artistdao.andSong(aim.getName());
				albums = artistdao.andAlbum(aim.getName());
			}
			intent.putExtra("tracks", tracks);
			intent.putExtra("albums", albums);

			startActivityForResult(intent, 20);
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String track = bundle.getString("track");
					String newartist = bundle.getString("newartist");
					String newalbum = bundle.getString("newalbum");
					String newtime = bundle.getString("newtime");
					songdao.update(track, newartist, newalbum, newtime);
				}
			}
			break;
		case 2:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String track = bundle.getString("track");
					songdao.delete(track);
				}
			}
			break;

		case 3:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String album = bundle.getString("album");
					String newartist = bundle.getString("newartist");
					String newdate = bundle.getString("newdate");
					String newcompany = bundle.getString("newcompany");
					String newstyle = bundle.getString("newstyle");
					albumdao.update(album, newartist, newdate, newcompany,
							newstyle);
				}
			}
			break;

		case 4:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String album = bundle.getString("album");
					albumdao.delete(album);
				}
			}
			break;
		case 5:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String album = bundle.getString("album");
					auto_search.setText(album);
					rb_album.setChecked(true);
					bt_search.setPressed(true);
					searchIt(getCurrentFocus());
				}
			}
			break;

		case 6:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String name = bundle.getString("name");
					String newarea = bundle.getString("newarea");
					String newcompany = bundle.getString("newcompany");
					String newbirth = bundle.getString("newbirth");
					artistdao.update(name, newarea, newcompany, newbirth);
				}
			}
			break;

		case 7:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String artist = bundle.getString("name");
					artistdao.delete(artist);
				}
			}
			break;

		case 8:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String name = bundle.getString("name");
					auto_search.setText(name);
					rb_artist.setChecked(true);
					bt_search.setPressed(true);
					searchIt(getCurrentFocus());
				}
			}
			break;
			
		case 9:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String company = bundle.getString("name");
					companydao.delete(company);
				}
			}
			break;
			
		case 10:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String company = bundle.getString("name");
					Company aim = companydao.findBycompany(company);
					String name = aim.getName();
					String area = aim.getArea();
					String found = aim.getFound();
					int brand = aim.getBrand();
					Intent intent = new Intent(MainActivity.this, ResultActivity4.class);
					intent.putExtra("name", name);
					intent.putExtra("area", area);
					intent.putExtra("found", found);
					intent.putExtra("brand", brand);
					intent.putExtra("flag", flag);
					
					String[] artists = null;
					String[] albums = null;

					if (aim.getName() != null) {
						artists = companydao.andArtist(aim.getName());
						albums = companydao.andAlbum(aim.getName());
					}
					intent.putExtra("artists", artists);
					intent.putExtra("albums", albums);

					startActivityForResult(intent, 20);
					
				}
			}
			break;
			
		case 11:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String name = bundle.getString("name");
					String newarea = bundle.getString("newarea");
					String newfound = bundle.getString("newfound");
					companydao.update(name, newarea, newfound);
				}
			}
			break;

		}
	}

	 /**********************************/
	private void initValues() {
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		content = findViewById(R.id.content_main);
		menu = findViewById(R.id.menu);
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// ��menu�Ŀ������Ϊ��Ļ��ȼ�ȥmenuPadding
		menuParams.width = screenWidth - menuPadding;
		// ���Ե��ֵ��ֵΪmenu��ȵĸ���
		leftEdge = -menuParams.width;
		// menu��leftMargin����Ϊ���Ե��ֵ��������ʼ��ʱmenu�ͱ�Ϊ���ɼ�
		menuParams.leftMargin = leftEdge;
		// ��content�Ŀ������Ϊ��Ļ���
		content.getLayoutParams().width = screenWidth;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��ָ����ʱ����¼����ʱ�ĺ�����
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// ��ָ�ƶ�ʱ���ԱȰ���ʱ�ĺ����꣬������ƶ��ľ��룬������menu��leftMarginֵ���Ӷ���ʾ������menu
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isMenuVisible) {
				menuParams.leftMargin = distanceX;
			} else {
				menuParams.leftMargin = leftEdge + distanceX;
			}
			if (menuParams.leftMargin < leftEdge) {
				menuParams.leftMargin = leftEdge;
			} else if (menuParams.leftMargin > rightEdge) {
				menuParams.leftMargin = rightEdge;
			}
			menu.setLayoutParams(menuParams);
			break;
		case MotionEvent.ACTION_UP:
			// ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�����menu���棬���ǹ�����content����
			xUp = event.getRawX();
			if (wantToShowMenu()) {
				if (shouldScrollToMenu()) {
					scrollToMenu();
				} else {
					scrollToContent();
				}
			} else if (wantToShowContent()) {
				if (shouldScrollToContent()) {
					scrollToContent();
				} else {
					scrollToMenu();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return true;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾcontent�������ָ�ƶ��ľ����Ǹ������ҵ�ǰmenu�ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾcontent��
	 * 
	 * @return ��ǰ��������ʾcontent����true�����򷵻�false��
	 */
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾmenu�������ָ�ƶ��ľ������������ҵ�ǰmenu�ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾmenu��
	 * 
	 * @return ��ǰ��������ʾmenu����true�����򷵻�false��
	 */
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�����menuչʾ�����������ָ�ƶ����������Ļ��1/2��������ָ�ƶ��ٶȴ���SNAP_VELOCITY��
	 * ����ΪӦ�ù�����menuչʾ������
	 * 
	 * @return ���Ӧ�ù�����menuչʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�����contentչʾ�����������ָ�ƶ��������menuPadding������Ļ��1/2��
	 * ������ָ�ƶ��ٶȴ���SNAP_VELOCITY�� ����ΪӦ�ù�����contentչʾ������
	 * 
	 * @return ���Ӧ�ù�����contentչʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * ����Ļ������menu���棬�����ٶ��趨Ϊ30.
	 */
	private void scrollToMenu() {
		new ScrollTask().execute(30);
	}

	/**
	 * ����Ļ������content���棬�����ٶ��趨Ϊ-30.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	/**
	 * ����VelocityTracker���󣬲�������content����Ļ����¼����뵽VelocityTracker���С�
	 * 
	 * @param event
	 *            content����Ļ����¼�
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * ��ȡ��ָ��content���滬�����ٶȡ�
	 * 
	 * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ��
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * ����VelocityTracker����
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			// ���ݴ�����ٶ����������棬������������߽���ұ߽�ʱ������ѭ����
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				// Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��20���룬�������۲��ܹ���������������
				sleep(20);
			}
			if (speed[0] > 0) {
				isMenuVisible = true;
			} else {
				isMenuVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			menuParams.leftMargin = leftMargin[0];
			menu.setLayoutParams(menuParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			menuParams.leftMargin = leftMargin;
			menu.setLayoutParams(menuParams);
		}
	}

	/**
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * 
	 * @param millis
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
