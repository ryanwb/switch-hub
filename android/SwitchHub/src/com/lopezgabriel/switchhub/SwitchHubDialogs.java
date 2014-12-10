package com.lopezgabriel.switchhub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SwitchHubDialogs {

	static boolean resetToggle = false;
	
	public static void launchSyncingDialog(final Activity activity, final View view, final String objectId, final boolean toOn, final String name) {
		final String power = toOn ? "on" : "off";
		final ProgressDialog progressDialog = ProgressDialog.show(activity, "", "Turning " + power + " " + name + "...", true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
				try {
					for (int i = 0; i < 20; i++) {
						ParseObject applianceModel = query.get(objectId);
						if (applianceModel.getBoolean("synced") == true) {
							// toggle has been synced with the hub
							break;
						}
						// sleep for .5 seconds
						Thread.sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				progressDialog.dismiss();

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
									applianceModel.save();
								}
								else {
									applianceModel.put("power", true);
									applianceModel.put("synced", true);
									applianceModel.save();
								}
								resetToggle = true;
								((Switch) view).toggle();
								resetToggle = false;

								launchSyncFailureAlert(activity, power, name);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	public static void launchSyncFailureAlert(Activity activity, String power, String name) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

		// set dialog message
		alertDialogBuilder
		.setMessage("Failed to turn " + power + " " + name + ".")
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

	public static void launchValidateDeviceDialog(final Activity activity, final String objectId, final String name) {
		final ProgressDialog progressDialog = ProgressDialog.show(activity, "", "Adding " + name + "...", true);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
				try {
					for (int i = 0; i < 20; i++) {
						ParseObject applianceModel = query.get(objectId);
						if (applianceModel.getBoolean("new") == false) {
							// toggle has been synced with the hub
							break;
						}
						// sleep for .5 seconds
						Thread.sleep(500);
					}
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
								// if failed to sync
								ParseObject applianceModel = query.get(objectId);
								if(applianceModel.getBoolean("new")) {				
									launchInvalidDeviceAlert(activity, name);
									applianceModel.deleteInBackground();									
								}
								activity.recreate();

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
				progressDialog.dismiss();
			}			
		});
		thread.start();
	}

	public static void launchInvalidDeviceAlert(Activity activity, String name) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

		// set dialog message
		alertDialogBuilder
		.setMessage("Failed to add " + name + ".")
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
	
	public static void launchTryToConnect(final Activity activity, final String objectId, final String name) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
		try {
			ParseObject applianceModel = query.get(objectId);
			applianceModel.put("new", true);
			applianceModel.save();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		final ProgressDialog progressDialog = ProgressDialog.show(activity, "", "Connecting " + name + "...", true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
				try {
					for (int i = 0; i < 20; i++) {
						ParseObject applianceModel = query.get(objectId);
						if (applianceModel.getBoolean("connected") == true) {
							break;
						}
						// sleep for .5 seconds
						Thread.sleep(500);
					}
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplianceModel");
								// if failed to sync
								ParseObject applianceModel = query.get(objectId);
								if(!applianceModel.getBoolean("connected")) {
									launchFailedToConnectAlert(activity, name);
								} else {
									activity.recreate();
								}
								applianceModel.put("new", false); // needed?
								applianceModel.save();
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
				progressDialog.dismiss();
			}
		}).start();
	}
	
	public static void launchFailedToConnectAlert(Activity activity, String name) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

		// set dialog message
		alertDialogBuilder
		.setMessage("Failed to connect " + name + ".")
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

	public static void launchNotConnectedAlert(MainActivity activity, String name) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

		// set dialog message
		alertDialogBuilder
		.setMessage(name + " is no longer connected. Please make sure it's connected to the hub.")
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