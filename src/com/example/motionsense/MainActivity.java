package com.example.motionsense;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MainActivity extends Activity implements OnClickListener {
	EditText username;
	EditText password;
	ImageButton sign;
	ParseObject user;
	ArrayList<String> value = new ArrayList();
	Intent home;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.start);
        Parse.initialize(this,"W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr","zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
        
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        
        sign = (ImageButton) findViewById(R.id.signin);
        sign.setOnClickListener(this);
        
        home = new Intent(this,Home.class);
        user = new ParseObject("User");
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.signin){
			System.out.println("GO");
			startActivity(home);
		}
	}
	
	




}
