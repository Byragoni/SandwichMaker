package com.android.sandwichmaker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.sandwichmaker.Utils;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Utils.startAlarm(context);
        }
    }

}


