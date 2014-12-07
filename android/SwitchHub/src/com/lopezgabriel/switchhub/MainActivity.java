package com.lopezgabriel.switchhub;

import java.util.ArrayList;
import java.util.List;

import com.parse.*;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.app.ProgressDialog;

public class MainActivity extends Activity {

	List<ApplianceModel> appliances = new ArrayList<ApplianceModel>(); 
	boolean resetToggle = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
	
	    Typeface tf = Typeface.createFromAsset(this.getAssets(),
	            "fonts/raleway.ttf");
	    TextView tv = (TextView) findViewById(R.id.logo);
	    tv.setTypeface(tf);
	    
		ParseUser user = ParseUser.getCurrentUser();

		// Get list of appliances from Parse.com and place them in ApplianceModel objects
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel").whereEqualTo("user", user);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> applianceList, ParseException e) {
				if (e == null) {
					LinearLayout layout = (LinearLayout) findViewById(R.id.main);
					
					for(ParseObject appliance : applianceList) {
						ApplianceModel appl = new ApplianceModel();
						appl.setObjectId(appliance.getObjectId());
						appl.setApplianceName(appliance.getString("name"));
						appl.setPower(appliance.getBoolean("power"));
						appl.setSynced(appliance.getBoolean("synced"));

						appliances.add(appl);

						// Create views for the fetched appliances
						LinearLayout layout_row = new LinearLayout(MainActivity.this);

						TextView text_row = new TextView(MainActivity.this);
						text_row.setText(appl.getApplianceName() + " ");
						text_row.setLayoutParams(new LinearLayout.LayoutParams(
	            	    		0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
						
						layout_row.addView(text_row);
						final Switch toggle = new Switch(MainActivity.this);
						toggle.setLayoutParams(new LinearLayout.LayoutParams(
		            	        ViewGroup.LayoutParams.WRAP_CONTENT,
		            	        ViewGroup.LayoutParams.WRAP_CONTENT));
						toggle.setChecked(appl.isPower());

						final String objectId = appliance.getObjectId();
						
						// add listener to toggle
						toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (!resetToggle) {
									onToggleClicked(toggle, objectId);		
								}
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

		
		findViewById(R.id.logout_button).setOnClickListener(new OnClickListener() {
		      @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		      @Override
		      public void onClick(View v) {
		        ParseUser.logOut();

		        // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
		        // logs out on older devices, we'll just exit.
		        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		          Intent intent = new Intent(MainActivity.this,
		              DispatchActivity.class);
		          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
		              | Intent.FLAG_ACTIVITY_NEW_TASK);
		          startActivity(intent);
		        } else {
		          finish();
		        }
		      }
		    });
	}

	public void onToggleClicked(View view, String objectId) {
		ParseUser user = ParseUser.getCurrentUser();
		final boolean toOn = ((Switch) view).isChecked();

		try {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel").whereEqualTo("user", user);
			// retrieve the object by id
			ParseObject applianceModel = query.get(objectId);
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

			// wait for the hub to sync the toggle
			launchSyncingDialog(view, objectId, toOn);	

		} catch (ParseException e) {
			e.printStackTrace();
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
		case R.id.action_add_device:		
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			
			final EditText name = new EditText(this);
			name.setHint("Appliance Name");
			layout.addView(name);
			
			final EditText address = new EditText(this);
			address.setHint("Bluetooth Address");
			layout.addView(address);
			
			AlertDialog.Builder alert = new AlertDialog.Builder(this)
			.setTitle("Add Your New Appliance")
			.setView(layout)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					
					 ParseObject new_appliance = new ParseObject("ApplianceModel");
					 new_appliance.put("power", true);
					 new_appliance.put("synced", false);
					 new_appliance.put("valid", false);
					 new_appliance.put("name", name.getText().toString());
					 new_appliance.put("bluetooth", address.getText().toString());
					 new_appliance.put("user", ParseUser.getCurrentUser());
					 new_appliance.saveInBackground();
					 
					 // Call loading screen after and await for valid field
					 
				  }
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
			
			// 3. Get the AlertDialog from create()
			AlertDialog dialog = alert.create();
			dialog.show();
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

	public void launchSyncingDialog(final View view, final String objectId, final boolean toOn) {
		final String power = toOn ? "on" : "off";
		final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Turning " + power + " device...", true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
						try {
							for (int i = 0; i < 10; i++) {
								ParseObject applianceModel = query.get(objectId);
								if (applianceModel.getBoolean("synced") == true) {
									// toggle has been synced with the hub
									break;
								}
								// sleep for .25 seconds
								Thread.sleep(500);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						progressDialog.dismiss();
					}			
				});
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
							// if failed to sync
							ParseObject applianceModel = query.get(objectId);
							if(!applianceModel.getBoolean("synced")) {				
								applianceModel = query.get(objectId);
								// reset toggle
								if (toOn) {
									// turn on appliance		
									applianceModel.put("power", false);
									applianceModel.put("synced", true);
									applianceModel.saveInBackground();
								}
								else {
									applianceModel.put("power", true);
									applianceModel.put("synced", true);
									applianceModel.saveInBackground();
								}
								resetToggle = true;
								((Switch) view).toggle();
								resetToggle = false;

								launchSyncFailureAlert(power);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	public void launchSyncFailureAlert(String power) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

		// set dialog message
		alertDialogBuilder
		.setMessage("Failed to turn " + power + " device.")
		.setCancelable(false)
		.setNegativeButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.dismiss();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
}
