package com.example.motionsense;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class SensorActivity extends Activity implements SensorEventListener,
		Callback, OnTouchListener, OnClickListener {
	private SensorManager mSensorManager;
	double[] gravity = new double[3];
	double[] linear_acceleration = new double[3];
	int time = 0;
	boolean pressed = false;
	ImageButton press;
	boolean done = false;
	int counterGravity;
	int counterAccelerometer;
	int counterGyroscope;
	int counterRotation;
	double sumXGravity;
	double sumYGravity;
	double sumZGravity;
	ArrayList<Double> xGravity = new ArrayList();
	ArrayList<Double> yGravity = new ArrayList();
	ArrayList<Double> zGravity = new ArrayList();
	double sumXAcceleration;
	double sumYAcceleration;
	double sumZAcceleration;
	ArrayList<Double> xAcceleration = new ArrayList();
	ArrayList<Double> yAcceleration = new ArrayList();
	ArrayList<Double> zAcceleration = new ArrayList();
	Handler handle = new Handler(this);
	View progress;
	double sumXGyroscope;
	double sumYGyroscope;
	double sumZGyroscope;
	ArrayList<Double> xGyroscope = new ArrayList();
	ArrayList<Double> yGyroscope = new ArrayList();
	ArrayList<Double> zGyroscope = new ArrayList();
	double sumXRotation;
	double sumYRotation;
	double sumZRotation;
	ArrayList<Double> xRotation = new ArrayList();
	ArrayList<Double> yRotation = new ArrayList();
	ArrayList<Double> zRotation = new ArrayList();
	final double alpha = 0.8;
	long duration;
	byte[] fileByte;
	Sensor mSensor;
	Thread thread;
	Load l;
	int width;
	ImageButton cancel;
	ImageButton next;
	Dialog dialog;
	ParseObject object;
	EditText title;
	String completeTitle;
	String difficulty;
	ParseFile file;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr",
				"zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		width = metrics.widthPixels;
		setActionBar();
		cancel = (ImageButton) findViewById(R.id.cancel);
		next = (ImageButton) findViewById(R.id.next);
		press = (ImageButton) findViewById(R.id.startmotion);
		press.setOnTouchListener(this);
		cancel.setOnTouchListener(this);
		next.setOnTouchListener(this);
		progress = (View) findViewById(R.id.progressbar);

		Intent i = getIntent();
		duration = i.getLongExtra("seconds", 0);
		fileByte = i.getBundleExtra("outputFile").getByteArray("output");

		thread = new Thread(new Runnable() {
			long start = 0;
			long time = duration / 40;
			public void run() {
				System.out.println("IT HAS STARTED " + duration);

				start = 0;
				try {
					while (start < time) {
						System.out.println("IT HAS STARTED");
						Thread.sleep(40);
						start++;
						Bundle bundle = new Bundle();
						bundle.putLong("status", start);
						Message message = new Message();
						message.setData(bundle);
						handle.sendMessage(message);
					}
				} catch (InterruptedException e) {
					progress.getLayoutParams().width = 0;
					progress.requestLayout();
					thread.stop();
				}
			}
		});
	}

	private void setActionBar() {
		ActionBar actionBar = getActionBar();
		// displaying custom ActionBar
		View mActionBarView = getLayoutInflater().inflate(R.layout.cameratop,
				null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	protected void onStop() {
		super.onStop();

	}

	// SensorEventListener
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			counterAccelerometer++;
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			// Remove the gravity contribution with the high-pass filter.
			linear_acceleration[0] = event.values[0] - gravity[0];
			linear_acceleration[1] = event.values[1] - gravity[1];
			linear_acceleration[2] = event.values[2] - gravity[2];

			sumXAcceleration += linear_acceleration[0];
			sumYAcceleration += linear_acceleration[1];
			sumZAcceleration += linear_acceleration[2];

			if (counterAccelerometer % 3 == 0) {
				xAcceleration.add(sumXAcceleration / 3);
				yAcceleration.add(sumYAcceleration / 3);
				zAcceleration.add(sumZAcceleration / 3);
				sumXAcceleration = 0;
				sumYAcceleration = 0;
				sumZAcceleration = 0;
			}
		}

		else if (sensor.getType() == Sensor.TYPE_GRAVITY) {

			counterGravity++;
			sumXGravity += event.values[0];
			sumYGravity += event.values[1];
			sumZGravity += event.values[2];

			if (counterGravity % 3 == 0) {
				xGravity.add(sumXGravity / 3);
				yGravity.add(sumYGravity / 3);
				zGravity.add(sumZGravity / 3);
				sumXGravity = 0;
				sumYGravity = 0;
				sumZGravity = 0;
			}
		}

		else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			counterGyroscope++;
			sumXGyroscope += event.values[0];
			sumYGyroscope += event.values[1];
			sumZGyroscope += event.values[2];

			if (counterGyroscope % 3 == 0) {
				xGyroscope.add(sumXGyroscope / 3);
				yGyroscope.add(sumYGyroscope / 3);
				zGyroscope.add(sumZGyroscope / 3);
				sumXGyroscope = 0;
				sumYGyroscope = 0;
				sumZGyroscope = 0;
			}

		}

		else {

			counterRotation++;
			sumXRotation += event.values[0];
			sumYRotation += event.values[1];
			sumZRotation += event.values[2];

			if (counterRotation % 3 == 0) {
				xRotation.add(sumXRotation / 3);
				yRotation.add(sumYRotation / 3);
				zRotation.add(sumZRotation / 3);
				sumXRotation = 0;
				sumYRotation = 0;
				sumZRotation = 0;
			}
		}

	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		long currentWidth = arg0.getData().getLong("status");
		progress.getLayoutParams().width = (int) (currentWidth * width / (duration / 40));
		progress.requestLayout();

		return true;
	}

	public void cancelButton(View v) {
		if (pressed == true) {
			thread.interrupt();

			pressed = false;
			done = false;
			xGravity = new ArrayList();
			yGravity = new ArrayList();
			zGravity = new ArrayList();
			sumXAcceleration = 0;
			sumYAcceleration = 0;
			sumZAcceleration = 0;
			xAcceleration = new ArrayList();
			yAcceleration = new ArrayList();
			zAcceleration = new ArrayList();
			sumXGyroscope = 0;
			sumYGyroscope = 0;
			sumZGyroscope = 0;
			xGyroscope = new ArrayList();
			yGyroscope = new ArrayList();
			zGyroscope = new ArrayList();
			sumXRotation = 0;
			sumYRotation = 0;
			sumZRotation = 0;
			xRotation = new ArrayList();
			yRotation = new ArrayList();
			zRotation = new ArrayList();
			thread.interrupt();
			progress.getLayoutParams().width = 0;
			progress.requestLayout();
			l.cancel(true);
			l = new Load();
			l.execute();
			press.setBackgroundResource(R.drawable.bluecircle);
		}

	}

	public void nextButton(View v) {
		if (done) {
			dialog = new Dialog(SensorActivity.this, R.style.myBackgroundStyle);
			dialog.setContentView(R.layout.dialog);
			dialog.show();
			title = (EditText) dialog.findViewById(R.id.editText1);
			RadioButton one = (RadioButton) dialog
					.findViewById(R.id.radioButton1);
			RadioButton two = (RadioButton) dialog
					.findViewById(R.id.radioButton2);
			RadioButton three = (RadioButton) dialog
					.findViewById(R.id.radioButton3);

			one.setOnClickListener(this);
			two.setOnClickListener(this);
			three.setOnClickListener(this);

			Button complete = (Button) dialog.findViewById(R.id.confirm);
			complete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					completeTitle = title.getText().toString();
					dialog.hide();
					new Save().execute();
				}
			});
		}
	}

	class Load extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void seconds) {
			mSensorManager.unregisterListener(SensorActivity.this);
			done = true;
		}
	}

	class Save extends AsyncTask<Void, Void, Void> {
		ProgressDialog d;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
			d = new ProgressDialog(SensorActivity.this);
			d.setTitle("Loading");
			d.show();
		}



		@Override
		protected Void doInBackground(Void... params) {

			object = new ParseObject("Media");
			System.out.println("FILEEEE " + fileByte);
			System.out.println("FILEEEE2 " + fileByte.toString());

			file = new ParseFile("video.mp4", fileByte);
			file.saveInBackground(new SaveCallback() {

				public void done(ParseException e) {
					if (e != null) {
						Toast.makeText(getApplicationContext(),
								"Error saving: " + e.getMessage(),
								Toast.LENGTH_LONG).show();
					} else {
						JSONArray gravityX = new JSONArray();
						JSONArray gravityY = new JSONArray();
						JSONArray gravityZ = new JSONArray();

						JSONArray accelerometerX = new JSONArray();
						JSONArray accelerometerY = new JSONArray();
						JSONArray accelerometerZ = new JSONArray();

						JSONArray gyroscopeX = new JSONArray();
						JSONArray gyroscopeY = new JSONArray();
						JSONArray gyroscopeZ = new JSONArray();

						JSONArray rotationX = new JSONArray();
						JSONArray rotationY = new JSONArray();
						JSONArray rotationZ = new JSONArray();

						for (int i = 0; i < xGravity.size(); i++) {
							gravityX.put(xGravity.get(i));
							gravityY.put(yGravity.get(i));
							gravityZ.put(zGravity.get(i));
						}
						for (int i = 0; i < xAcceleration.size(); i++) {
							accelerometerX.put(xAcceleration.get(i));
							accelerometerY.put(yAcceleration.get(i));
							accelerometerZ.put(zAcceleration.get(i));
						}
						for (int i = 0; i < xGyroscope.size(); i++) {
							gyroscopeX.put(xGyroscope.get(i));
							gyroscopeY.put(yGyroscope.get(i));
							gyroscopeZ.put(zGyroscope.get(i));
						}
						for (int i = 0; i < xRotation.size(); i++) {
							rotationX.put(xRotation.get(i));
							rotationY.put(yRotation.get(i));
							rotationZ.put(zRotation.get(i));
						}
						object.put("title", completeTitle);
						object.put("difficulty", difficulty);
						object.put("gravityX", gravityX);
						object.put("gravityY", gravityY);
						object.put("gravityZ", gravityZ);
						object.put("accelerometerX", accelerometerX);
						object.put("accelerometerY", accelerometerY);
						object.put("accelerometerZ", accelerometerZ);
						object.put("gyroscopeX", gyroscopeX);
						object.put("gyroscopeY", gyroscopeY);
						object.put("gyroscopeZ", gyroscopeZ);
						object.put("rotationX", rotationX);
						object.put("rotationY", rotationY);
						object.put("rotationZ", rotationZ);
						object.put("video", file);
						object.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								System.out.println("DONE");
								if (e == null) {
									d.dismiss();

								} else {
									Toast.makeText(
											getApplicationContext()
													.getApplicationContext(),
											"Error saving: " + e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							}

						});
					}
				}
			});

			return null;
		}

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case (R.id.next):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				next.setBackgroundColor(Color.parseColor("#1f8991"));
				break;
			case (MotionEvent.ACTION_UP):
				next.setBackgroundColor(Color.parseColor("#32c2d6"));
				break;
			}
			break;

		case (R.id.cancel):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				cancel.setBackgroundColor(Color.parseColor("#1f8991"));
				break;
			case (MotionEvent.ACTION_UP):
				cancel.setBackgroundColor(Color.parseColor("#32c2d6"));
				break;
			}
			break;

		case (R.id.startmotion):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				System.out.println(pressed +"PRESSED");
				if (pressed == false) {
					press.setBackgroundResource(R.drawable.fullblue);
					mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
					mSensorManager.registerListener(this, mSensorManager
							.getDefaultSensor(Sensor.TYPE_GRAVITY),
							SensorManager.SENSOR_DELAY_UI);
					mSensorManager.registerListener(this, mSensorManager
							.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_UI);
					mSensorManager.registerListener(this, mSensorManager
							.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
							SensorManager.SENSOR_DELAY_UI);
					mSensorManager.registerListener(this, mSensorManager
							.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
							SensorManager.SENSOR_DELAY_UI);
					System.out.println("WHERE MOTHER FUCKER");
					thread.start();
					System.out.println("AM MOTHER FUCKER");

					l = new Load();
					
					l.execute();
					System.out.println("I AT MOTHER FUCKER");

					pressed = true;
					break;
				}
			}
			break;
		}

		return super.onTouchEvent(arg1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case (R.id.radioButton1):
			difficulty = "Easy";
			break;
		case (R.id.radioButton2):
			difficulty = "Intermediate";

			break;
		case (R.id.radioButton3):
			difficulty = "Hard";

			break;
		}
	}

}
