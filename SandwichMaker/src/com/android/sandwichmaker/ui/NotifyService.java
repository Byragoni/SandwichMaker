/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.sandwichmaker.ui;

import com.android.sandwichmaker.MainActivity;
import com.android.sandwichmaker.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class NotifyService extends IntentService {

	private NotificationManager mNotificationManager;
	private String mMessage;
	private long mMillis;
	NotificationCompat.Builder builder;

	public NotifyService() {

		super("com.android.sandwichmaker.ui");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mMessage = intent.getStringExtra(CommonConstants.EXTRA_MESSAGE);
		mMillis = intent.getLongExtra(CommonConstants.EXTRA_TIMER,
				CommonConstants.DEFAULT_TIMER_DURATION);
		NotificationManager nm = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);

	//	nm.cancel(CommonConstants.NOTIFICATION_ID);
		String action = intent.getAction();
		if(action.equals(CommonConstants.ACTION_PING)) {
			issueNotification(intent, mMessage);
		} 

	}

	private void issueNotification(Intent intent, String msg) {
		mNotificationManager = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);

		Bitmap bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher);

		// Constructs the Builder object.
		builder = new NotificationCompat.Builder(this)
		.setLargeIcon(bmp)
		.setSmallIcon(R.drawable.ic_launcher)
		.setTicker(getString(R.string.hint))
		.setContentTitle(getString(R.string.app_name))
		.setContentText(getString(R.string.notify_message))
		.setAutoCancel(true)
		.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtra(CommonConstants.EXTRA_MESSAGE, msg);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent =
				PendingIntent.getActivity(
						this,
						0,
						resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT
						);

		builder.setContentIntent(resultPendingIntent);
		startTimer(mMillis);
	}

	private void issueNotification(NotificationCompat.Builder builder) {
		mNotificationManager = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.notify(CommonConstants.NOTIFICATION_ID, builder.build());
	}

	private void startTimer(long millis) {
		try {
			Thread.sleep(millis);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		issueNotification(builder);
	}
}
