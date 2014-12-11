package com.lopezgabriel.switchhub;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ScheduleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		final String user = ParseUser.getCurrentUser().getObjectId();
		// Get list of appliances from Parse.com and place them in ApplianceModel objects
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel").whereEqualTo("user", user);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> applianceList, ParseException e) {
				if (e == null) {
					List<String> spinnerArray =  new ArrayList<String>();

					for(ParseObject appliance : applianceList)
						spinnerArray.add(appliance.getString("name"));
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					ScheduleActivity.this, android.R.layout.simple_spinner_item, spinnerArray);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Spinner sItems = (Spinner) findViewById(R.id.spinner);
					sItems.setAdapter(adapter);
				} else {
				System.out.println("Error: " + e.getMessage());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String title = ParseUser.getCurrentUser().getString("name");

		setTitle(title + "'s Hub");
		return true;
	}


}
