package com.lopezgabriel.switchhub;

import android.app.Application;

import com.parse.Parse;

public class GlobalApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu", "XT15aZdUPjk92tIkMTJkOHQdkrwznhmndXB5Gp5W");

	    Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
	}
	  
}
