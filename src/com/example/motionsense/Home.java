package com.example.motionsense;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.parse.Parse;


public class Home extends Activity implements OnClickListener {
	Intent camera;
	Intent search;
    ViewPager mPager;
    int Number = 0;
    ImageButton cameraB;
    ImageButton searchB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Parse.initialize(this, "W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr",
				"zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        cameraB = (ImageButton) findViewById(R.id.camerab);
        searchB = (ImageButton) findViewById(R.id.searchb);
        cameraB.setOnClickListener(this);
        searchB.setOnClickListener(this);
    }
 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.camerab){
			camera = new Intent(this,CameraActivity.class);
			startActivity(camera);
		}
		
		if (v.getId() == R.id.searchb){
			search = new Intent(this,SearchClass.class);
			startActivity(search);
		}
	}
 
        
    
}
