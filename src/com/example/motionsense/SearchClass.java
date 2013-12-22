package com.example.motionsense;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SearchClass extends Activity implements OnQueryTextListener {
	SearchView sv;
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<String> difficulties = new ArrayList<String>();
	TextView titleTV;
	TextView difficultyTV;
	ArrayList<ParseFile> vid = new ArrayList<ParseFile>();
	ListView lv;
	AdapterClass ac;
	ArrayList<String> id = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Parse.initialize(this, "W5vtHZQ3gyzSNOxNIhJSbzpUA9mB2Z5UjpbrTfDr",
				"zMC1dkN3PTIsI1FPMrdD3rbI51p6vtsA75hsBbLQ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		sv = (SearchView) findViewById(R.id.searchView1);
		lv = (ListView) findViewById(R.id.listsearch);
		sv.setOnQueryTextListener(this);
	}
	
	private void setActionBar() {
		ActionBar actionBar =getActionBar();
		// displaying custom ActionBar
		View mActionBarView = getLayoutInflater().inflate(
				R.layout.top, null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

	}

	public class AdapterClass extends ArrayAdapter<String> {
		Context context;
		ArrayList<String> ts;
		ArrayList<String> ds;
		ArrayList<String> d;

		ArrayList<ParseFile> parsefiles;

		public AdapterClass(Context c, ArrayList<String> t,
				ArrayList<String> d, ArrayList<ParseFile> f, ArrayList<String> i) {
			super(c, R.layout.row, R.id.titleText, t);
			this.context = c;
			this.ts = t;
			this.ds = d;
			this.d = i;
			this.parsefiles = f;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			// TODO Auto-generated method stub
			if (row == null) {
				System.out.println("HERE");
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.row, parent, false);
			}

			titleTV = (TextView) row.findViewById(R.id.titleText);
			difficultyTV = (TextView) row.findViewById(R.id.difficultText);
			titleTV.setText(ts.get(position));
			difficultyTV.setText(ds.get(position));
			final int x = position;
			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Close dialog
					Intent intent = new Intent(getApplicationContext(),
							ViewVideo.class);
					intent.putExtra("id", d.get(x));
					intent.putExtra("url", parsefiles.get(x).getUrl());
					startActivity(intent);
				}
			});

			return row;
		}

	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		System.out.println(arg0);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Media");
		query.whereContains("title", arg0);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				System.out.println("SIZE " + scoreList.size());
				if (scoreList.size() != 0) {
					for (ParseObject obj : scoreList) {
						titles.add(obj.getString("title"));
						difficulties.add(obj.getString("difficulty"));
						vid.add(obj.getParseFile("video"));
						id.add(obj.getObjectId());
					}
				}
				ac = new AdapterClass(getApplicationContext(), titles,
						difficulties, vid,id);
				lv.setAdapter(ac);

			}

		});
		return true;
	}

}
