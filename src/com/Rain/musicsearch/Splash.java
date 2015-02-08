package com.Rain.musicsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity{
	private final int SPLASH_DISPLY_LENGTH = 4000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(Splash.this,LoginActivity.class);
				Splash.this.startActivity(mainIntent);
				Splash.this.finish();
			}
		},SPLASH_DISPLY_LENGTH);
		
	}

}
