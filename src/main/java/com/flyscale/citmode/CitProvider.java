package com.flyscale.citmode;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by bian on 2018/3/14.
 */

public class CitProvider extends ContentProvider{
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.flyscale.cit.provider", "citmode", 1);
    }
    private MyCursor cursor;
    private HashMap<String, ArrayList<String>> allDatas = new HashMap<String, ArrayList<String>>();

    @Override
    public boolean onCreate() {
        return false;
    }
    /**
     * 加载我们的数据信息
     */
    public void loadAllData() {
        allDatas.clear();
        for (int pos = 0; pos < 1; pos++) {
            ArrayList<String> dataList = new ArrayList<String>();
            SharedPreferences sp = getContext().getSharedPreferences("cit", Context.MODE_PRIVATE);
            boolean enabled = sp.getBoolean("citmode", false);
            dataList.add((enabled ? 1 : 0) + "");
            allDatas.put(pos + "", dataList);
        }
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String
            selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == 1) {
            cursor = new MyCursor();
            loadAllData();
            cursor.updateAllData(allDatas.values());
            return cursor;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[]
            selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String
            selection, String[] selectionArgs) {
        return 0;
    }
    public class MyCursor extends AbstractCursor {
        private static final String TAG = "MyCursor";

        private String[] columnNames = null;//构建cursor时必须先传入列数组以规定列数

        /**
         * 数据区域
         */
        //所有的数据
        private ArrayList<ArrayList<String>> allDatas = new ArrayList<ArrayList<String>>();//在构造的时候填充数据，里层数据的size=columnNames.leng
        //当前一项的数据
        private ArrayList<String> oneLineData = null;//onMove时填充

        public MyCursor() {
            //必须构建完整列信息
//            columnNames = new String[]{"id", "name"};
            columnNames = new String[]{"ipshortcut"};
        }

        /**
         * 加载所有的数据信息
         */
        public void updateAllData(Collection<ArrayList<String>> data) {
            mPos = -1;
            allDatas.clear();
            allDatas.addAll(data);
        }

        /**
         * 加载单个的数据信息
         */
        public void updateUserData(ArrayList<String> data) {
            mPos = -1;
            allDatas.clear();
            allDatas.add(data);
        }

        /**
         * 获取当前行对象，为一个oneLineDatastring[]
         */
        @Override
        public boolean onMove(int oldPosition, int newPosition) {
            if (newPosition < 0 || newPosition >= getCount()) {
                oneLineData = null;
                return false;
            }

            int index = newPosition;
            if (index < 0 || index >= allDatas.size()) {
                return false;
            }
            oneLineData = allDatas.get(index);
            return super.onMove(oldPosition, newPosition);
        }

        /**
         * 获取游标行数
         */
        @Override
        public int getCount() {
            return allDatas.size();
        }

        /**
         * 获取列名称
         */
        @Override
        public String[] getColumnNames() {
            return columnNames;
        }


        @Override
        public String getString(int column) {
            if (oneLineData == null) {
                return null;
            }
            return oneLineData.get(column);
        }

        @Override
        public int getInt(int column) {
            Log.i("MyCursor", "getInt");
            Object value = getString(column);
            try {
                return value != null ? ((Number) value).intValue() : null;
            } catch (ClassCastException e) {
                if (value instanceof CharSequence) {
                    try {
                        return Integer.valueOf(value.toString());
                    } catch (NumberFormatException e2) {
                        Log.e(TAG, "Cannotparse int value for " + value + "at key " + column);
                        return 0;
                    }
                } else {
                    Log.e(TAG, "Cannotcast value for " + column + "to a int: " + value, e);
                    return 0;
                }
            }
        }

        /**
         * 以下参考getInt(int column)
         */
        @Override
        public short getShort(int column) {
            return 0;
        }

        @Override
        public long getLong(int column) {
            return 0;
        }

        @Override
        public float getFloat(int column) {
            return 0;
        }

        @Override
        public double getDouble(int column) {
            return 0;
        }

        @Override
        public boolean isNull(int column) {
            return false;
        }
    }
}
