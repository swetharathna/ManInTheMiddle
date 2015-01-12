package com.cmpe277.maninthemiddle;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends ActionBarActivity {

	public static String url_value;
	public static ProgressBar pb;
	public static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		
		pb = (ProgressBar)findViewById(R.id.progressBar1);
		assert(null != pb);
		if (null != MainActivity.pb) {
			MainActivity.pb.setVisibility(ProgressBar.INVISIBLE);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onFetchSecretClick(View v) {
		EditText url = (EditText) findViewById(R.id.editText1);
		url_value=url.getText().toString();
		 new SecretKeyTask().execute();
	}
}
