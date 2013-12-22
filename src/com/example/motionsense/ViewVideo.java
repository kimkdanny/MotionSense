package com.example.motionsense;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.parse.Parse;

public class ViewVideo extends Activity implements OnClickListener,
		OnTouchListener {
	ImageButton back;
	ImageButton forward;
	Intent demo;
	Intent urlVid;
	VideoView vid;
	long duration;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr",
				"zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
		urlVid = getIntent();
		String url = urlVid.getStringExtra("url");
		id = urlVid.getStringExtra("id");
		setContentView(R.layout.camera);
		setActionBar();
		vid = (VideoView) findViewById(R.id.videoView1);

		MediaController mc = new MediaController(this);
		mc.setAnchorView(vid);
		Uri uri = Uri.parse(url);
		vid.setMediaController(mc);
		mc.setMediaPlayer(vid);
		vid.setVideoURI(uri);
		vid.start();
		
		vid.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer mp) {
				duration = vid.getDuration();
				vid.start();

			}
		});

		back = (ImageButton) findViewById(R.id.back);
		forward = (ImageButton) findViewById(R.id.forward);
		back.setOnClickListener(this);
		forward.setOnClickListener(this);
		back.setOnTouchListener(this);
		forward.setOnTouchListener(this);
	}

	private void setActionBar() {
		ActionBar actionBar = getActionBar();
		// displaying custom ActionBar
		View mActionBarView = getLayoutInflater().inflate(
				R.layout.videoviewtop, null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back) {
			finish();
		}

		else if (v.getId() == R.id.forward) {
			demo = new Intent(this,Demo.class);
			demo.putExtra("id", id);
			demo.putExtra("duration",duration);
			startActivity(demo);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case (R.id.forward):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				forward.setBackgroundColor(Color.parseColor("#1f8991"));
				break;
			case (MotionEvent.ACTION_UP):
				forward.setBackgroundColor(Color.parseColor("#32c2d6"));
				break;
			}
			break;

		case (R.id.back):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				back.setBackgroundColor(Color.parseColor("#1f8991"));
				break;
			case (MotionEvent.ACTION_UP):
				back.setBackgroundColor(Color.parseColor("#32c2d6"));
				break;
			}
			break;
		}
		return super.onTouchEvent(arg1);
	}

}
