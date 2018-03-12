package com.flyscale.citmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by bian on 2018/3/6.
 */

public class CitReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, "android.provider.Telephony.SECRET_CODE")&&
                TextUtils.equals(intent.getData().getHost(), "9125")){
            Intent target = new Intent();
            target.setClassName("com.flyscale.citmode", "com.flyscale.citmode.MainActivity");
            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        }
    }
}
