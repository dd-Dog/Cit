package com.flyscale.citmode.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.flyscale.citmode.R;

/**
 * Created by bian on 2018/3/5.
 */

public class ItemTestListFragment extends BaseFragment {
    private static final String TAG = "ItemTestListFragment";
    private static final int MANUAL_TEST = 1;
    private ListView mMainTree;
    private String[] mMainData;
    private ManualTreeAdapter mManualTreeAdapter;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mMainData = getResources().getStringArray(R.array.test_list);
    }

    public View initView() {
        mView = mActivity.getLayoutInflater().inflate(R.layout.activity_list, null);
        mMainTree = (ListView) mView.findViewById(R.id.main);
        mManualTreeAdapter = new ManualTreeAdapter();
        mMainTree.setAdapter(mManualTreeAdapter);
        TextView title = (TextView) mView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.app_name));

        mMainTree.setDivider(null);
        mMainTree.setSelection(0);
        mMainTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.testItem(mMainTree.getSelectedItemPosition());
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setCurrentFragment(this);
    }

    @Override
    public void onKeyUp(int keyCode) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_MENU:
                mActivity.testItem(mMainTree.getSelectedItemPosition());
                break;

        }
    }


    class ManualTreeAdapter extends BaseAdapter {

        public ManualTreeAdapter() {
        }

        @Override
        public int getCount() {
            return mMainData.length;
        }

        @Override
        public String getItem(int position) {
            return mMainData[position % mMainData.length];
        }

        @Override
        public long getItemId(int position) {
            return position % mMainData.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView item = (TextView) mActivity.getLayoutInflater().inflate(R.layout.item, parent, false);
            item.setText(mMainData[position % mMainData.length]);
            return item;
        }
    }
}
