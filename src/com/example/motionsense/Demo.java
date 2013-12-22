package com.example.motionsense;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.ImageButton;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Demo extends Activity implements OnClickListener, OnTouchListener,
		Callback, SensorEventListener {
	ImageButton back;
	ImageButton forward;
	private SensorManager mSensorManager;
	double[] gravity = new double[3];
	double[] linear_acceleration = new double[3];
	int time = 0;
	boolean pressed = false;
	boolean done = false;
	int counterGravity;
	int counterAccelerometer;
	int counterGyroscope;
	int counterRotation;
	double sumXGravity;
	double sumYGravity;
	Load l;
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
	int width;
	Thread thread;
	ParseObject object;
	ImageButton press;
	String id;

	ArrayList<Double> TxGravity = new ArrayList();
	ArrayList<Double> TyGravity = new ArrayList();
	ArrayList<Double> TzGravity = new ArrayList();

	ArrayList<Double> TxAcceleration = new ArrayList();
	ArrayList<Double> TyAcceleration = new ArrayList();
	ArrayList<Double> TzAcceleration = new ArrayList();

	ArrayList<Double> TxGyroscope = new ArrayList();
	ArrayList<Double> TyGyroscope = new ArrayList();
	ArrayList<Double> TzGyroscope = new ArrayList();

	ArrayList<Double> TxRotation = new ArrayList();
	ArrayList<Double> TyRotation = new ArrayList();
	ArrayList<Double> TzRotation = new ArrayList();
	ArrayList<Double> trueanswer = new ArrayList();

	ArrayList<Double> answer = new ArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Parse.initialize(this, "W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr",
				"zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
		setContentView(R.layout.activity_main);

		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		width = metrics.widthPixels;
		setActionBar();
		progress = (View) findViewById(R.id.progressbar);
		back = (ImageButton) findViewById(R.id.back);
		forward = (ImageButton) findViewById(R.id.forward);
		press = (ImageButton) findViewById(R.id.startmotion);
		press.setOnTouchListener(this);
		back.setOnClickListener(this);
		forward.setOnClickListener(this);
		back.setOnTouchListener(this);
		forward.setOnTouchListener(this);

		Intent i = getIntent();
		duration = i.getLongExtra("duration", 0);
		id = i.getStringExtra("id");
		thread = new Thread(new Runnable() {
			long start = 0;
			long time = duration / 40;

			public void run() {

				start = 0;
				try {

					while (start < time) {
						Thread.sleep(40);
						start++;
						Bundle bundle = new Bundle();
						bundle.putLong("status", start);
						Message message = new Message();
						message.setData(bundle);
						handle.sendMessage(message);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block

				}

			}
		});
		object = new ParseObject("Media");

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

		case (R.id.startmotion):
			switch (arg1.getAction()) {
			case (MotionEvent.ACTION_DOWN):
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
					l = new Load();
					l.execute();
					thread = new Thread(new Runnable() {

						long start = 0;
						long time = duration / 40;

						public void run() {

							start = 0;
							try {

								while (start < time) {
									Thread.sleep(40);
									start++;
									Bundle bundle = new Bundle();
									bundle.putLong("status", start);
									Message message = new Message();
									message.setData(bundle);
									handle.sendMessage(message);
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
							}

						}
					});
					thread.start();
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
		if (v.getId() == R.id.back) {
			finish();
		} else if (v.getId() == R.id.forward) {
			new LoadValues().execute();
		}
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

	public void nextButton(View v) {
		if (done) {

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
			mSensorManager.unregisterListener(Demo.this);
			done = true;
		}
	}

	class LoadValues extends AsyncTask<String, Void, ArrayList<Double>> {

		@Override
		protected ArrayList<Double> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Media");
			query.getInBackground(id, new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (e == null) {
						JSONArray gravityX = object.getJSONArray("gravityX");
						JSONArray gravityY = object.getJSONArray("gravityY");
						JSONArray gravityZ = object.getJSONArray("gravityZ");
						for (int i = 0; i < gravityX.length(); i++) {
							try {
								TxGravity.add(gravityX.getDouble(i));
								TyGravity.add(gravityY.getDouble(i));
								TzGravity.add(gravityZ.getDouble(i));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						JSONArray accelerometerX = object
								.getJSONArray("accelerometerX");
						JSONArray accelerometerY = object
								.getJSONArray("accelerometerY");
						JSONArray accelerometerZ = object
								.getJSONArray("accelerometerZ");

						for (int i = 0; i < accelerometerX.length(); i++) {
							try {
								TxAcceleration.add(accelerometerX.getDouble(i));
								TyAcceleration.add(accelerometerY.getDouble(i));
								TzAcceleration.add(accelerometerZ.getDouble(i));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						JSONArray gyroscopeX = object
								.getJSONArray("gyroscopeX");
						JSONArray gyroscopeY = object
								.getJSONArray("gyroscopeY");
						JSONArray gyroscopeZ = object
								.getJSONArray("gyroscopeZ");

						for (int i = 0; i < gyroscopeX.length(); i++) {
							try {
								TxGyroscope.add(gyroscopeX.getDouble(i));
								TyGyroscope.add(gyroscopeY.getDouble(i));
								TzGyroscope.add(gyroscopeZ.getDouble(i));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						JSONArray rotationX = object.getJSONArray("rotationX");
						JSONArray rotationY = object.getJSONArray("rotationY");
						JSONArray rotationZ = object.getJSONArray("rotationZ");

						for (int i = 0; i < rotationX.length(); i++) {
							try {
								TxRotation.add(rotationX.getDouble(i));
								TyRotation.add(rotationY.getDouble(i));
								TzRotation.add(rotationZ.getDouble(i));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						answer = Calculate.percentErrorTime(TxGravity, TyGravity,
								TzGravity, TxRotation, TyRotation, TzRotation,
								TxGyroscope, TyGyroscope, TzGyroscope,
								TxAcceleration, TyAcceleration, TzAcceleration,
								xGravity, yGravity, zGravity, xRotation,
								yRotation, zRotation, xGyroscope, yGyroscope,
								zGyroscope, xAcceleration, yAcceleration,
								zAcceleration);
						trueanswer = new ArrayList();
						System.out.println("WHAT THE FUCK IS THE ANSWER " + answer);
						for (int i =0;i<answer.size();i++){
							double a = answer.get(i);
							if (a > 100){
								a = (a-100)/4 + 100;
							}
							if (a < 100){
								System.out.println("A " + a);
								a = 100 - (100-a)/4;
								System.out.println("B " + a);
							}
							
							trueanswer.add(a/100);
						}
					
						System.out.println("WHAT THE FUCK IS THE TRUE ANSWER " + trueanswer);
						
						String x = "";

						ArrayList<String> converted = new ArrayList();
						for (int i = 0;i<trueanswer.size();i++){
							converted.add("" + trueanswer.get(i));
						}
						System.out.println("HERE THE FUCKING HELL IS THE TRUE FUCKING ANSWER FUCK " + converted);

						mSensorManager.unregisterListener(Demo.this);
						done = true;
						Intent i = new Intent(getApplicationContext(),MyActivity.class);
						i.putExtra("answer", converted);
						startActivity(i);
					} else {
						// something went wrong
					}
				}
			});
			return trueanswer;
		}

		protected void onPostExecute(ArrayList<Double> a) {
			
		}
	}

}
