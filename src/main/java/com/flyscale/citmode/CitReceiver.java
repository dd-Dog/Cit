package com.flyscale.citmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by bian on 2018/3/6.
 */

public class CitReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("CitReceiver", "收到广播");
        String action = intent.getAction();
        if (TextUtils.equals(action, "android.provider.Telephony.SECRET_CODE")&&
                TextUtils.equals(intent.getData().getHost(), "9125")){
            Intent target = new Intent();
            target.setClassName("com.flyscale.citmode", "com.flyscale.citmode.MainActivity");
            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        }else if (TextUtils.equals(action, "com.flyscale.send.action.KEYCODE")){
            //中止广播
            //进入cit模式
            SharedPreferences sp = context.getSharedPreferences("cit", Context.MODE_PRIVATE);
            boolean citmode = sp.getBoolean("citmode", false);
            if (citmode){
                Log.i("CitReceiver", "当前是CIT模式，拦截广播");
                abortBroadcast();
            }
        }
    }
}
