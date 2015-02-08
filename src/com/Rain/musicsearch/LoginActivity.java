package com.Rain.musicsearch;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/*
	 * private static final int RESULT_VIP = 8; private static final int
	 * RESULT_visitor = 9;
	 */
	private EditText et_username;
	private EditText et_password;
	private CheckBox cb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		et_password = (EditText) this.findViewById(R.id.et_password);
		et_username = (EditText) this.findViewById(R.id.et_username);
		cb = (CheckBox) this.findViewById(R.id.cb_remember);
		
		setBack();
		
		Map<String, String> map = com.Rain.musicsearch.extra.LoginService
				.getSavedUserInfo(this);

		if (map != null) {
			et_username.setText(map.get("username"));
			et_password.setText(map.get("password"));
		}
	}

	public void setBack() {
		Resources res = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.splash_cat4);
		Drawable background = new BitmapDrawable(blurBitmap(bmp));
		LoginActivity.this.getWindow().setBackgroundDrawable(background);
	}

	public void login(View view) {
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT);
		} else {
			if (cb.isChecked()) {
				// 保存密码
				boolean result = com.Rain.musicsearch.extra.LoginService
						.saveUserInfo(this, username, password);
			}
			// 登陆判断 密码和用户名是否正确
			if (username.equals(password)) {
				Toast.makeText(this, "进入管理员模式", 0).show();
				Intent mainIntent = new Intent(LoginActivity.this,
						MainActivity.class);
				mainIntent.putExtra("flag", true);
				LoginActivity.this.startActivity(mainIntent);
				LoginActivity.this.finish();

			} else {
				Toast.makeText(this, "进入游客模式", 0).show();
				Intent mainIntent = new Intent(LoginActivity.this,
						MainActivity.class);
				mainIntent.putExtra("flag", false);
				LoginActivity.this.startActivity(mainIntent);
				LoginActivity.this.finish();
			}
		}
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
