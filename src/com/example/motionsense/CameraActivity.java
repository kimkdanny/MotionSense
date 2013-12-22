package com.example.motionsense;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.Parse;
import com.parse.ParseObject;

public class CameraActivity extends Activity implements SurfaceHolder.Callback,
		OnErrorListener, OnInfoListener, OnTouchListener, OnPreparedListener, MediaScannerConnectionClient {
	Camera camera;
	private MediaScannerConnection mMs;

	public static final int MEDIA_TYPE_IMAGE = 72;
	SurfaceHolder holder;
	boolean playing = false;
	private MediaRecorder recorder = null;
	private boolean recorded = false;
	boolean recording = true;
	Uri uriVideo = null;
	private String outputFileName;
	boolean front = true;
	boolean second = true;
	boolean first = true;
	VideoView vid;
	boolean recordState = true;
	ImageButton cancelB;
	ImageButton nextB;
	ParseObject upload;
	long duration;
	Intent sense;
	byte[] bFile;
	File outputFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		Parse.initialize(this, "W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr",
				"zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
		setActionBar();
		vid = (VideoView) findViewById(R.id.videoView1);
		cancelB = (ImageButton) findViewById(R.id.cancel);
		nextB = (ImageButton) findViewById(R.id.next);
		cancelB.setOnTouchListener(this);
		nextB.setOnTouchListener(this);
		vid.setOnTouchListener(this);
		vid.setOnPreparedListener(this);
		sense = new Intent(this, SensorActivity.class);
	}

	private void setActionBar() {
		ActionBar actionBar = getActionBar();
		// displaying custom ActionBar
		View mActionBarView = getLayoutInflater().inflate(R.layout.cameratop,
				null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

	}

	public void cancelButton(View v) {
		playing = false;

		if (recorder != null) {
			stopRecording();
			recorded = false;
			uriVideo = null;
			camera = null;
			recorder = null;
			outputFileName = null;
			camera = null;
			initializeCamera();
			try {
				camera.setPreviewDisplay(holder);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			camera.setDisplayOrientation(90);
			camera.startPreview();

			try {
				camera.setPreviewDisplay(holder);
				System.out.println("DONE?");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("CANCEL ERROR");
			}
			camera.startPreview();
			recorded = false;
			return;
		}

		if (recorded == true) {
			if (holder != null) {
				if (front == false)
					front = !front;
				stopPlayBack();
				uriVideo = null;
				recorder = null;
				outputFileName = null;
				camera = null;
				initializeCamera();
				try {
					camera.setPreviewDisplay(holder);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				camera.setDisplayOrientation(90);
				camera.startPreview();

				try {
					camera.setPreviewDisplay(holder);
					System.out.println("DONE?");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("CANCEL ERROR");
				}
				camera.startPreview();
				recorded = false;
			}
		}

	}

	public void nextButton(View v) {
		Intent next = new Intent(this, SensorActivity.class);
		if (!recording) {
			if (recorded) {
				stopPlayBack();
				System.out.println("WHATTTT");

				mMs = new MediaScannerConnection(getApplicationContext(), this);
			    mMs.connect();
			}
		} 
		else {
			System.out.println("HMMMMMMMMMMMMMMMMMMMM");
			stopRecording();
			recording = false;
			playRecording();
		}
	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub
		if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {

			stopRecording();
			recording = false;
			playRecording();
		}
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub
		stopRecording();
		Toast.makeText(getApplicationContext(),
				"Error Reached. Stopped Recording", Toast.LENGTH_LONG).show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("SURFACE CREATED ERROR");
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (Exception e) {
			System.out.println("SURFACE DESTROYED ERROR");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (camera != null)
			camera.release();
	}

	private void initializeRecording() {
		if (recorder != null)
			return;

		outputFileName = Environment.getExternalStorageDirectory()
				+ "/videooutput.mp4";
		outputFile = new File(outputFileName);

		if (outputFile.exists())
			outputFile.mkdirs();

		FileInputStream fileInputStream = null;

		bFile = new byte[(int) outputFile.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(outputFile);
			fileInputStream.read(bFile);
			fileInputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			camera.stopPreview();
			camera.unlock();
			recorder = new MediaRecorder();
			recorder.setCamera(camera);
			recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setVideoSize(720, 480);
			recorder.setVideoFrameRate(15);
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			recorder.setMaxDuration(10000);
			recorder.setPreviewDisplay(holder.getSurface());
			recorder.setOutputFile(outputFileName);
			if (front) {
				recorder.setOrientationHint(90);
				System.out.println("THIS IS " + front);
			} else {
				System.out.println("THIS IS " + front);

				System.out.println("HEILO");
				recorder.setOrientationHint(270);
			}
			recorder.prepare();

		}

		catch (Exception e) {
			System.out.println("INITIALIZE RECORDING ERROR");
		}

	}

	@SuppressWarnings("deprecation")
	private boolean initializeCamera() {
		try {
			System.out.println("NOT OPENED YET");

			camera = Camera.open();
			System.out.println("OPENED");

			@SuppressWarnings("unused")
			Camera.Parameters camParams = camera.getParameters();
			System.out.println("param");

			camera.setDisplayOrientation(90);
			System.out.println("orientation");

			// else{
			// System.out.println("BACKWARDSSS");
			//
			// camera.setDisplayOrientation(180);
			// }

			if (front == false) {
				camera.setDisplayOrientation(180);
			}
			camera.lock();
			System.out.println("locked");

			holder = vid.getHolder();
			System.out.println("holder");

			holder.addCallback(this);
			System.out.println("callback");

			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		} catch (Exception e) {
			System.out.println("INITIALIZE CAMERA ERROR");
			return false;
		}
		return true;
	}

	private void stopRecording() {
		recording = false;
		if (recorder != null) {
			recorder.setOnErrorListener(null);
			recorder.setOnInfoListener(null);
			try {
				recorder.stop();
			} catch (Exception e) {
				System.out.println("STOP RECORDING ERROR");
			}
			releaseCamera();
			releaseRecorder();
		}
	}

	private void releaseCamera() {
		// TODO Auto-generated method stub
		if (camera != null) {
			try {
				camera.reconnect();
			} catch (Exception e) {
				System.out.println("RELEASE CAMERA ERROR");
			}
			camera.release();
			camera = null;
		}
	}

	private void releaseRecorder() {
		if (recorder != null) {
			recorder.release();
			recorder = null;
		}
	}

	private void playRecording() {
		playing = true;
		MediaController mc = new MediaController(this);

		vid.setMediaController(mc);
		vid.setVideoPath(outputFileName);
		vid.start();
	}

	private void stopPlayBack() {
		vid.stopPlayback();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("RESUME");
		// recordState = false;
		// front = true;
		// first = true;
		// recorded = false;
		recording = false;
		// playing = false;
		if (!initializeCamera())
			finish();
	}

	private void beginRecording() {
		playing = true;
		recording = true;
		recorder.setOnErrorListener(this);
		recorder.setOnInfoListener(this);
		recorder.start();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case (R.id.videoView1):

			if (recordState && !recorded) {
				if (MotionEvent.ACTION_DOWN == arg1.getAction()) {

					recorded = true;
					System.out.println("RECORDING");
					initializeCamera();
					initializeRecording();
					beginRecording();
					return true;
				}
			}
			break;

		case (R.id.next):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				nextB.setBackgroundColor(Color.parseColor("#1f8991"));
				break;
			case (MotionEvent.ACTION_UP):
				nextB.setBackgroundColor(Color.parseColor("#32c2d6"));
				break;
			}
			break;

		case (R.id.cancel):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				cancelB.setBackgroundColor(Color.parseColor("#1f8991"));
				break;
			case (MotionEvent.ACTION_UP):
				cancelB.setBackgroundColor(Color.parseColor("#32c2d6"));
				break;
			}
			break;
		}
		return super.onTouchEvent(arg1);
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		duration = vid.getDuration();
	}

	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
	    mMs.scanFile(outputFile.getAbsolutePath(), null);

	}

	@Override
	public void onScanCompleted(String arg0, Uri arg1) {
		System.out.println("DONE SCAN");
		Intent next = new Intent(this, SensorActivity.class);

		vid.stopPlayback();
		System.out.println("FILE " + outputFile.getAbsolutePath());
		FileInputStream fileInputStream = null;
		bFile = new byte[(int) outputFile.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(outputFile);
			fileInputStream.read(bFile);
			fileInputStream.close();
			System.out.println("SCAN");

		} catch (Exception e) {
			System.out.println ("WHAT THE FUCK");
			e.printStackTrace();
		}

		System.out.println("BFILE " + bFile);
		
		Bundle b = new Bundle();
		b.putByteArray("output", bFile);
		next.putExtra("outputFile", b);
		next.putExtra("seconds", duration);
		System.out.println("BFILE " + b);

		startActivity(next);		
	}
}
