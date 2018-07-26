package com.konka.kktripclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.konka.kktripclient.bean.MyCollectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_one on 2017-5-9.
 * <p>
 * 数据库
 */

public class KKTripSQLHelper extends SQLiteOpenHelper {
    private final String TAG = this.getClass().getSimpleName();
    private static final String DBNAME = "kktrip.db";
    private static final int VERSION = 1;
    private static KKTripSQLHelper instance;

    private final String create_collect_table = "create table if not exists" + " " + MyCollectTable.TABLENAME + "("

            + MyCollectTable.TABLEID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT"
            + ","
/*            + MyCollectTable.GOODID + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.GOODNAME + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.IMGURL + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.ORIGINALPRICE + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.DISCOUNTPRICE + " " + "TEXT NOT NULL"
            + ","*/
            + MyCollectTable.ROURROUTEID + " " + "INTEGER"
            + ","
            + MyCollectTable.TICKETID + " " + "INTEGER"
            + ","
            + MyCollectTable.IDTYPE + " " + "INTEGER"
            + ","
            +MyCollectTable.USERID + " " + "TEXT NOT NULL"
            + ");";

    private final String create_collect_table_temp = "create table if not exists" + " " + MyCollectTable.TABLENAMETEMP + "("

            + MyCollectTable.TABLEID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT"
            + ","
/*            + MyCollectTable.GOODID + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.GOODNAME + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.IMGURL + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.ORIGINALPRICE + " " + "TEXT NOT NULL"
            + ","
            + MyCollectTable.DISCOUNTPRICE + " " + "TEXT NOT NULL"
            + ","*/
            + MyCollectTable.ROURROUTEID + " " + "INTEGER"
            + ","
            + MyCollectTable.TICKETID + " " + "INTEGER"
            + ","
            + MyCollectTable.IDTYPE + " " + "INTEGER"
            + ","
            +MyCollectTable.USERID + " " + "TEXT NOT NULL"
            + ");";

    private final String query_table = "select * from" + " " + MyCollectTable.TABLENAME + ";";
    private final String query_table_temp = "select * from" + " " + MyCollectTable.TABLENAMETEMP + ";";

    private final String drop_table = "drop table if exists" + " " + MyCollectTable.TABLENAME + ";";
    private final String drop_table_temp = "drop table if exists" + " " + MyCollectTable.TABLENAMETEMP + ";";

    public KKTripSQLHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public static KKTripSQLHelper getInstance(Context context) {
        if (instance == null) {
            instance = new KKTripSQLHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(create_collect_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade:    oldVersion="+oldVersion+"    newVersion="+newVersion);

        if(oldVersion == 1) {
            Cursor cursor = null;
            List<MyCollectBean> myCollects = new ArrayList<>();
            try {
                cursor = db.rawQuery(query_table, null);
                while (cursor.moveToNext()) {
                    MyCollectBean myCollect = new MyCollectBean();
                    myCollect.setId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TABLEID)));
                    myCollect.setTourRouteId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.ROURROUTEID)));
                    myCollect.setTicketId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TICKETID)));
                    myCollect.setGoodsType(cursor.getInt(cursor.getColumnIndex(MyCollectTable.IDTYPE)));
                    myCollect.setUser_id(cursor.getString(cursor.getColumnIndex(MyCollectTable.USERID)));
                    myCollects.add(myCollect);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            }

            db.execSQL(create_collect_table_temp); //创建临时表，存放原来的数据
            for (int i = 0; i < myCollects.size(); i++) {
                ContentValues values = new ContentValues();

                values.put(MyCollectTable.TABLEID, myCollects.get(i).getId());
                values.put(MyCollectTable.ROURROUTEID, myCollects.get(i).getTourRouteId());
                values.put(MyCollectTable.TICKETID, myCollects.get(i).getTicketId());
                values.put(MyCollectTable.IDTYPE, myCollects.get(i).getGoodsType());
                values.put(MyCollectTable.USERID, myCollects.get(i).getUser_id());

                try {
                    db.insert(MyCollectTable.TABLENAMETEMP, null, values); //插入数据到临时表
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            db.execSQL(drop_table); //删除掉原来的表
            db.execSQL(create_collect_table); //重新创建表

            Cursor myCursor = null;
            List<MyCollectBean> collects = new ArrayList<>();
            try {
                myCursor = db.rawQuery(query_table_temp, null);
                while (myCursor.moveToNext()) {
                    MyCollectBean myCollect = new MyCollectBean();
                    myCollect.setId(myCursor.getInt(myCursor.getColumnIndex(MyCollectTable.TABLEID)));
                    myCollect.setTourRouteId(myCursor.getInt(myCursor.getColumnIndex(MyCollectTable.ROURROUTEID)));
                    myCollect.setTicketId(myCursor.getInt(myCursor.getColumnIndex(MyCollectTable.TICKETID)));
                    myCollect.setGoodsType(myCursor.getInt(myCursor.getColumnIndex(MyCollectTable.IDTYPE)));
                    myCollect.setUser_id(myCursor.getString(myCursor.getColumnIndex(MyCollectTable.USERID)));
                    collects.add(myCollect);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (myCursor != null && !myCursor.isClosed())
                    myCursor.close();
            }


            for (int i = 0; i < collects.size(); i++) {
                ContentValues values = new ContentValues();

                values.put(MyCollectTable.TABLEID, collects.get(i).getId());
                values.put(MyCollectTable.ROURROUTEID, collects.get(i).getTourRouteId());
                values.put(MyCollectTable.TICKETID, collects.get(i).getTicketId());
                values.put(MyCollectTable.IDTYPE, collects.get(i).getGoodsType());
                values.put(MyCollectTable.USERID, collects.get(i).getUser_id());

                try {
                    db.insert(MyCollectTable.TABLENAME, null, values); //插入数据到临时表
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            db.execSQL(drop_table_temp); //删除临时表
        }
    }

}
