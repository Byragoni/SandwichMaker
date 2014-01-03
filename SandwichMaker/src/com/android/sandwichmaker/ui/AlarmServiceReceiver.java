package com.android.sandwichmaker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.sandwichmaker.Utils;

public class AlarmServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.startNotification(context);
    }
}
