package com.Rain.musicsearch.extra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class DoubanMusic {

	String isbnUrl = "http://api.douban.com/music/subject/";
	JSONObject jsonobj;
	public static String[] api_result = new String[11];

	/*
	 * 利用豆瓣API获取json字符串
	 */
	public DoubanMusic(String id) {
		DoubanMusic isbn = new DoubanMusic();
		try {
			String musicjson = isbn.fetchMusicInfoByXML(id);
			JSONObject jsonobj = isbn.stringToJson(musicjson);
			isbn.setBookData();

		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}

	public DoubanMusic() {
		// TODO Auto-generated constructor stub
	}

	public String fetchMusicInfoByXML(String isbnNo) throws IOException {
		String requestUrl = isbnUrl + isbnNo + "?alt=json";
		URL url = new URL(requestUrl);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		br.close();
		return sb.toString();
	}

	// 把json字符串转换为json
	public JSONObject stringToJson(String str) throws JSONException {
		jsonobj = new JSONObject(str);
		return jsonobj;
	}

	// 设置获取的音乐信息并返回对象
	public void setBookData() throws JSONException {
		// 歌手
		/*
		 * JSONObject mesge = jsonobj.getJSONObject("author");
		 * System.out.println("author:" + mesge.getString("$t"));
		 */

		JSONArray links = jsonobj.getJSONArray("link");
		JSONObject cover;
		for(int i=0;i<4;i++) {
			cover = links.getJSONObject(i);
			if(cover.getString("@rel").equals("image")) {
				String temp = cover.getString("@href");
				char[] spic = temp.toCharArray();
				spic[23] = 'l';
				String lpic = new String(spic);
				//System.out.println("lpic: " + lpic);
				api_result[10] = lpic;
			}
		}

		JSONArray bookMessage = jsonobj.getJSONArray("db:attribute");
		JSONObject info;

		for (int i = 0; i < 10; i++) {
			info = bookMessage.getJSONObject(i);
			String tag = info.getString("@name");

			switch (tag) {
			case "singer":
				api_result[0] = info.getString("$t");
				break;
			case "version":
				if (api_result[1] != null) {
					api_result[1] = api_result[1] + "/" + info.getString("$t");
				} else {
					api_result[1] = info.getString("$t");
				}
				break;
			case "media":
				api_result[2] = info.getString("$t");
				break;
			case "pubdate":
				api_result[3] = info.getString("$t");
				break;
			case "publisher":
				api_result[4] = info.getString("$t");
				break;
			case "discs":
				api_result[5] = info.getString("$t");
				break;
			case "ean":
				api_result[6] = info.getString("$t");
				break;
			case "tracks":
				api_result[7] = info.getString("$t");
				break;

			}
		}

		/*
		 * info = bookMessage.getJSONObject(0); //System.out.println("discs: " +
		 * info.getString("$t")); api_result[5] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(1); //System.out.println("EAN: " +
		 * info.getString("$t")); api_result[6] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(2); //System.out.println("tracks: "
		 * + info.getString("$t")); api_result[7] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(3); //System.out.println("pubdate: "
		 * + info.getString("$t")); api_result[3] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(4); System.out.println("title: " +
		 * info.getString("$t"));
		 * 
		 * info = bookMessage.getJSONObject(5); //System.out.println("singer: "
		 * + info.getString("$t")); api_result[0] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(6); //System.out.println("version: "
		 * + info.getString("$t")); api_result[1] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(7); //System.out.println("version: "
		 * + info.getString("$t")); api_result[1] = api_result[1] +"/"+
		 * info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(8);
		 * //System.out.println("publisher: " + info.getString("$t"));
		 * api_result[4] = info.getString("$t");
		 * 
		 * info = bookMessage.getJSONObject(9); //System.out.println("media: " +
		 * info.getString("$t")); api_result[2] = info.getString("$t");
		 */

		JSONObject rate = jsonobj.getJSONObject("gd:rating");
		String result = rate.toString();
		// System.out.println("Rate: " + rate.getDouble("@average"));
		api_result[8] = rate.getString("@average");
		// System.out.println("RateNum: " + rate.getInt("@numRaters"));
		api_result[9] = rate.getString("@numRaters");
		
		

	}

}
