package com.lopezgabriel.switchhub;

import java.util.ArrayList;
import java.util.List;

import com.parse.*;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	List<ApplianceModel> appliances = new ArrayList<ApplianceModel>(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu", "XT15aZdUPjk92tIkMTJkOHQdkrwznhmndXB5Gp5W");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    Typeface tf = Typeface.createFromAsset(this.getAssets(),
	            "fonts/raleway.ttf");
	    TextView tv = (TextView) findViewById(R.id.logo);
	    tv.setTypeface(tf);

		// Get list of appliances from Parse.com and place them in ApplianceModel objects
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> applianceList, ParseException e) {
				if (e == null) {
					LinearLayout layout = (LinearLayout) findViewById(R.id.main);
					
					for(ParseObject appliance : applianceList) {
						ApplianceModel appl = new ApplianceModel();
						appl.setObjectId(appliance.getObjectId());
						appl.setApplianceName(appliance.getString("ApplianceName"));
						appl.setApplianceId(appliance.getInt("applianceId"));
						appl.setPower(appliance.getBoolean("power"));
						appl.setSynced(appliance.getBoolean("synced"));

						appliances.add(appl);

						// Create views for the fetched appliances
						LinearLayout layout_row = new LinearLayout(MainActivity.this);

						TextView text_row = new TextView(MainActivity.this);
						text_row.setText("Appliance " + appl.getApplianceId() + " ");
						text_row.setLayoutParams(new LinearLayout.LayoutParams(
	            	    		0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
						
						layout_row.addView(text_row);
						ToggleButton toggle = new ToggleButton(MainActivity.this);
						toggle.setLayoutParams(new LinearLayout.LayoutParams(
		            	        ViewGroup.LayoutParams.WRAP_CONTENT,
		            	        ViewGroup.LayoutParams.WRAP_CONTENT));
						toggle.setChecked(appl.isPower());

						final String objectId = appliance.getObjectId();
						// add listener to toggle
						toggle.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								onToggleClicked(v, objectId);
							}
						});

						layout_row.addView(toggle);
						layout.addView(layout_row);                 	
					}
				} else {
					System.out.println("Error: " + e.getMessage());
				}
			}
		});

	}

	public void onToggleClicked(View view, String objectId) {
		final boolean toOn = ((ToggleButton) view).isChecked();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
		// retrieve the object by id
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject applianceModel, ParseException e) {
				if (e == null) {
					if (toOn) {
						// turn on appliance		
						applianceModel.put("power", true);
						applianceModel.put("synced", false);
						applianceModel.saveInBackground();
					}
					else {
						applianceModel.put("power", false);
						applianceModel.put("synced", false);
						applianceModel.saveInBackground();
					}
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			recreate();
			break;
		case R.id.action_settings:
			// do something?
			break;
		default:
			break;
		}

		return true;
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
