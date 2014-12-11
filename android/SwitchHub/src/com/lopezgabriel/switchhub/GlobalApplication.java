package com.lopezgabriel.switchhub;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class GlobalApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu", "XT15aZdUPjk92tIkMTJkOHQdkrwznhmndXB5Gp5W");
		
		ParsePush.subscribeInBackground("", new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e == null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }
			  }
			});

	    Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
	}
	  
}
