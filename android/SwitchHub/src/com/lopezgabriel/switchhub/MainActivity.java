package com.lopezgabriel.switchhub;

import java.util.ArrayList;
import java.util.List;

import com.parse.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

		// Get list of appliances from Parse.com and place them in ApplianceModel objects
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> applianceList, ParseException e) {
				if (e == null) {
					for(int i = 0; i < applianceList.size(); i++) {
						ApplianceModel appliance = new ApplianceModel();
						appliance.setObjectId(applianceList.get(i).getObjectId());
						appliance.setApplianceId(applianceList.get(i).getInt("applianceId"));
						appliance.setPower(applianceList.get(i).getBoolean("power"));
						appliance.setSynced(applianceList.get(i).getBoolean("synced"));

						appliances.add(appliance);

						// Create views for the fetched appliances
						LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
						LinearLayout layout_row = new LinearLayout(getBaseContext());

						TextView text = new TextView(MainActivity.this);
						text.setText("Appliance " + appliance.getApplianceId() + " ");
						ToggleButton toggle = new ToggleButton(MainActivity.this);
						toggle.setChecked(appliance.isPower());

						final String objectId = appliance.getObjectId();
						// add listener to toggle
						toggle.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								onToggleClicked(v, objectId);
							}
						} );

						layout_row.addView(text);
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
		boolean toOn = ((ToggleButton) view).isChecked();

		if (toOn) {
			// turn on appliance
			ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");

			// retrieve the object by id
			query.getInBackground(objectId, new GetCallback<ParseObject>() {
				public void done(ParseObject applianceModel, ParseException e) {
					if (e == null) {
						applianceModel.put("power", true);
						applianceModel.put("synced", false);
						applianceModel.saveInBackground();
					}
				}
			});
		} else {
			// turn off appliance
			ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");

			// retrieve the object by id
			query.getInBackground(objectId, new GetCallback<ParseObject>() {
				public void done(ParseObject applianceModel, ParseException e) {
					if (e == null) {
						// update fields
						applianceModel.put("power", false);
						applianceModel.put("synced", false);
						applianceModel.saveInBackground();
					}
				}
			});
		}
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
