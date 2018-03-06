package com.flyscale.citmode.fragment.items;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by bian on 2018/3/6.
 */

public class BatteryFragment extends BaseFragment {

    private View mView;
    private IntentFilter mIntentFilter;
    private ListView mList;
    private ArrayList<String> mData;
    private BatteryInfoAdapter mBatteryInfoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mActivity.registerReceiver(mBatteryReceiver, mIntentFilter);
    }

    public View initView() {
        mView = mActivity.getLayoutInflater().inflate(R.layout.activity_list, null);
        TextView title = (TextView) mView.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[3]);
        mList = (ListView) mView.findViewById(R.id.main);
        mList.setDivider(null);
        mData = new ArrayList<String>();
        mBatteryInfoAdapter = new BatteryInfoAdapter();
        mList.setAdapter(mBatteryInfoAdapter);
        return mView;
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            mActivity.remove(this);
        }
    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                boolean present = intent.getBooleanExtra("present", false);
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 0);
                //mV
                int voltage = intent.getIntExtra("voltage", 0);
                //温度，0.1度单位
                int temperature = intent.getIntExtra("temperature", 0);
                String technology = intent.getStringExtra("technology");

                //电池状态，返回是一个数字
                // BatteryManager.BATTERY_STATUS_CHARGING 表示是充电状态
                // BatteryManager.BATTERY_STATUS_DISCHARGING 放电中
                // BatteryManager.BATTERY_STATUS_NOT_CHARGING 未充电
                // BatteryManager.BATTERY_STATUS_FULL 电池满
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                String chargeStatus = "";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        chargeStatus = getResources().getString(R.string.unknown);
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        chargeStatus = getResources().getString(R.string.charging);
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        chargeStatus = getResources().getString(R.string.discharging);
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        chargeStatus = getResources().getString(R.string.not_charging);
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        chargeStatus = getResources().getString(R.string.full);
                        break;
                }

                //充电类型 BatteryManager.BATTERY_PLUGGED_AC 表示是充电器，不是这个值，表示是 USB
                int plugged = intent.getIntExtra("plugged", 0);
                String pluggedType = "";
                switch (plugged) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        pluggedType = getResources().getString(R.string.plugged_ac);
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        pluggedType = getResources().getString(R.string.plugged_usb);
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        pluggedType = getResources().getString(R.string.plugged_wireless);
                        break;
                    default:
                        pluggedType = getResources().getString(R.string.not_charging);
                }

                //电池健康情况，返回也是一个数字
                //BatteryManager.BATTERY_HEALTH_GOOD 良好
                //BatteryManager.BATTERY_HEALTH_OVERHEAT 过热
                //BatteryManager.BATTERY_HEALTH_DEAD 没电
                //BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE 过电压
                //BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE 未知错误
                int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
                String healthStr = "";
                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        healthStr = getResources().getString(R.string.health_good);
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        healthStr = getResources().getString(R.string.health_overheat);
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        healthStr = getResources().getString(R.string.health_dead);
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        healthStr = getResources().getString(R.string.health_over_voltage);
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        healthStr = getResources().getString(R.string.health_unspecified_failure);
                        break;

                }
                Log.d("Battery", "present=" + present + ",level：" + level + ",scale=" + scale + ",voltage=" + voltage
                        + ",temperature=" + temperature + ",chargeStatus=" + chargeStatus + ",pluggedType=" +
                        pluggedType + ",health=" + healthStr + ",technology=" + technology);
                mData.add(getResources().getString(R.string.battery_present) + (present ?
                        getResources().getString(R.string.yes) : getResources().getString(R.string.no) + "\n"));
                mData.add(getResources().getString(R.string.battery_level) + (level + "\n"));
                mData.add(getResources().getString(R.string.battery_scale) + (scale + "\n"));
                mData.add(getResources().getString(R.string.battery_voltage) + (voltage + "mV\n"));
                mData.add(getResources().getString(R.string.battery_temperature) + ((float) temperature / 10f + "\n"));
                mData.add(getResources().getString(R.string.chargeStatus) + (chargeStatus + "\n"));
                mData.add(getResources().getString(R.string.pluggedType) + (pluggedType + "\n"));
                mData.add(getResources().getString(R.string.tecnology) + (technology + "\n"));
                mData.add(getResources().getString(R.string.health) + (healthStr));
                mBatteryInfoAdapter.notifyDataSetChanged();
            }
        }
    };

    class BatteryInfoAdapter extends BaseAdapter {

        public BatteryInfoAdapter() {
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView item = (TextView) getLayoutInflater().inflate(R.layout.item, parent, false);
            item.setText(mData.get(position));
            return item;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mBatteryReceiver);
    }
}
