package com.parse.starter;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceObject");
	    query.findInBackground(new FindCallback<ParseObject>() {
	        public void done(final List<ParseObject> applianceList, ParseException e) {
	            if (e == null) {
	               LinearLayout ll = (LinearLayout) findViewById(R.id.main);

	            	for (final ParseObject appliance : applianceList) {
	            	    // create a new textview
	            		
	            		LinearLayout l2 = new LinearLayout(ParseStarterProjectActivity.this);
	            		
	            	    final TextView rowTextView = new TextView(ParseStarterProjectActivity.this);
	            	    // set some properties of rowTextView or something
	            	    rowTextView.setText("Appliance:  " + appliance.getString("ApplianceName"));
	            	    rowTextView.setLayoutParams(new LinearLayout.LayoutParams(
	            	    		0,
	            	            ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
	            	    // add the textview to the linearlayout
	            	    l2.addView(rowTextView);
	            	    	            	    
	            	    final ToggleButton button = new ToggleButton(ParseStarterProjectActivity.this);
	            	    button.setLayoutParams(new LinearLayout.LayoutParams(
	            	        ViewGroup.LayoutParams.WRAP_CONTENT,
	            	        ViewGroup.LayoutParams.WRAP_CONTENT));
	            	    button.setChecked(appliance.getBoolean("Status"));
	            	  
	            	    button.setOnClickListener(new View.OnClickListener() {
	                        public void onClick(final View view) {         
	                        	ParseQuery<ParseObject> query = ParseQuery.getQuery("LampObject");
	                        	query.getInBackground(appliance.getObjectId(), new GetCallback<ParseObject>() {
	                        	  public void done(ParseObject object, ParseException e) {
	                        	    if (e == null) {
	                        	    	boolean on = ((ToggleButton) view).isChecked();
	                        	    	if (on) {
	    	                        		// Enable
	    	                        		object.put("Status", true);
	    	                        		object.saveInBackground();
	    	                        	} else {
	    	                        		// Disable
	    	                        		object.put("Status", false);
	    	                        		object.saveInBackground();
	    	                        	}                    	    	
	                        	    } else {
	                        	      // something went wrong
	                        	    }
	                        	  }
	                        	});
	                        }
	            	    });
	            	    
	            	    l2.addView(button);
	            	    ll.addView(l2);
	            	    
	            	}           	
	            } else {
	        		System.out.println("ERROR");
	            }
	       }
	    });
	}
}
