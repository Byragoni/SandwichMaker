package com.android.sandwichmaker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.sandwichmaker.location.GPSTracker;
import com.android.sandwichmaker.ui.AlarmServiceReceiver;
import com.android.sandwichmaker.ui.CommonConstants;
import com.android.sandwichmaker.ui.NotifyService;

import java.io.File;
import java.io.IOException;

public class Utils {

	private static final int NOTIFICATION_ID = 1;

   public static String APP_FOLDER_PATH = Environment.getExternalStorageDirectory().getPath()+"/PHI2/";


    public static  String DEBUG_TAG = "PHI debugging";
    public static Double HOME_LATITUDE = 12.99303;
    public static Double HOME_LONGITUDE = 77.72797;
    public static Double DISTANCE_ERROR_MARGIN = 2.0;


    public static String getLocation() {
		GPSTracker gps;
		gps = new GPSTracker(MainActivity.getInstance());

		if(gps.canGetLocation()) {


			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			return "" + longitude + "," + latitude;

		} else {
			gps.showSettingsAlert();
		}
		return "0,0";

	}

	public static void generateNotification(Context context) {

		int icon = R.drawable.ic_launcher;
		String appname = context.getResources().getString(R.string.app_name);
		String message = context.getResources().getString(R.string.notify_message);
		String ticker = context.getResources().getString(R.string.hint);
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification;

		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		notification = builder.setContentIntent(contentIntent)
				.setLargeIcon(bmp)
				.setSmallIcon(icon)
				.setTicker(ticker)
				.setWhen(0)
				.setAutoCancel(true)
				.setContentTitle(appname)
				.setContentText(message)
				.build();

		notificationManager.notify(NOTIFICATION_ID, notification);

	}
    public static File getAppFolder()  {
       File file = new File(Utils.APP_FOLDER_PATH);

        if(file==null) {
            try {
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.mkdir()) {

            Log.v(Utils.DEBUG_TAG, "Directory created");
        } else {
            Log.v(Utils.DEBUG_TAG, "Something really went wrong");

        }


        return file;
    }
	public static void startNotification(Context context) {
	
		long milliseconds = CommonConstants.NOTIFICATION_DELAY;
		Intent mServiceIntent = new Intent(context, NotifyService.class);
		mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, "What's on your mind?");
		mServiceIntent.setAction(CommonConstants.ACTION_PING);
		mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, milliseconds);
		context.startService(mServiceIntent);
	}

	public static void startAlarm(Context context) {
	
		long delay = CommonConstants.NOTIFICATION_DELAY;
		Intent intent = new Intent(context, AlarmServiceReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, 0, delay, pendingIntent);
	}


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
