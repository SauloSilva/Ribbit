package com.saulo.ribbit;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		Uri imageUri = getIntent().getData();
		String senderName = getIntent().getExtras().getString(ParseConstants.KEY_SENDER_NAME);
		
		setTitle(getString(R.string.image_sender_to) + " " + senderName);
		Picasso.with(this).load(imageUri.toString()).into(imageView);
		
		Timer time = new Timer();
		time.schedule(new TimerTask() {
			
			@Override
			public void run() {
				finish();
			}
		}, 10*1000);
	}
}