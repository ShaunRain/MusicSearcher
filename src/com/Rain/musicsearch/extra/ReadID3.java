package com.Rain.musicsearch.extra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*import javax.sound.sampled.AudioFileFormat;
 import javax.sound.sampled.AudioSystem;
 import javax.sound.sampled.UnsupportedAudioFileException;*/

import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

public class ReadID3 {
	// 传入参数:
	/*
	 * 
	 */
	private void getFiles(ArrayList<File> fileList) {  
	    File[] allFiles = Environment.getExternalStorageDirectory().listFiles();  
	    for (int i = 0; i < allFiles.length; i++) {  
	        File file = allFiles[i];  
	        if (file.isFile()&&file.getAbsolutePath().contains(".mp3")) {  
	            fileList.add(file); 
	        } 
	    }  
	}  
	public ReadID3() {
		
	}
	public String[] readSD() {	
		String[] result = new String[]{"0","0","0","0"};
		String track = null,artist =null,album = null,time = null;
		try {
			File song = new File(Environment.getExternalStorageDirectory(),
					"Damien Rice - I Don't Want To Change You.mp3");
			/*
			 * AudioFormat aff = AudioSystem.getAudioFileFormat(song); Map props
			 * = aff.properties();
			 */
			// Long total = (long)Math.round( ( ((Long)
			// props.get("duration")).longValue() )/1000);

			FileInputStream file = new FileInputStream(song);
			int size = (int) song.length();
			file.skip(size - 128);
			byte[] last128 = new byte[128];
			file.read(last128);
			String id3 = new String(last128);
			String tag = id3.substring(0, 3);
			if (tag.equals("TAG")) {
				
				// System.out.println("Duration:" + total);
				
				/*xmString = new String(sb.toString().getBytes("UTF-8"));  
			    xmlUTF8 = URLEncoder.encode(xmString, "UTF-8"); */
			    
				String trackcode = id3.substring(3, 32).toString().trim();
				String artistcode = id3.substring(33, 62).toString().trim();
				String albumcode = id3.substring(63, 91).toString().trim();
				String timecode = id3.substring(93, 97).toString().trim();
				
				track = URLEncoder.encode(trackcode,"UTF-8");
				artist = URLEncoder.encode(artistcode,"UTF-8");
				album = URLEncoder.encode(albumcode,"UTF-8");
				time = URLEncoder.encode(timecode,"UTF-8");
				
				System.out.println("Title:" + track);
				System.out.println("Artist:" + artist);
				System.out.println("Album:" + album);
				System.out.println("Year:" + time);
				// cover = -1;
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
		return result;
		
	}

}
