package com.konka.kktripclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.konka.kktripclient.bean.MyCollectBean;
import com.konka.kktripclient.detail.DetailConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017-5-19.
 */

public class CollectTableDao {

    private static volatile CollectTableDao instance;
    private Context mContext;

    private CollectTableDao(Context context){
        mContext = context;
    }

    public static CollectTableDao getInstance(Context context){
        if (instance == null){
            synchronized (CollectTableDao.class){
                if (instance == null){
                    instance = new CollectTableDao(context);
                }
            }
        }
        return instance;
    }


    public synchronized boolean insert(String user_id, int type, int id){
        if (user_id == null || user_id.isEmpty())
            return false;
        ContentValues values = new ContentValues();
        if (type == DetailConstant.GOOD_ROUTE) {
            values.put(MyCollectTable.ROURROUTEID, id);
        }
        else if (type == DetailConstant.GOOD_TICKET) {
            values.put(MyCollectTable.TICKETID, id);
        }
        else {
            return false;
        }

        values.put(MyCollectTable.IDTYPE, type);
        values.put(MyCollectTable.USERID, user_id);
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getWritableDatabase();
            database.insert(MyCollectTable.TABLENAME, null, values);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean deleteByGoodId(String user_id, int type, int id){
        if (user_id == null || user_id.isEmpty())
            return false;
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getWritableDatabase();
            String where = null;
            if (type == DetailConstant.GOOD_ROUTE) {
                where = MyCollectTable.ROURROUTEID + " =? "+ " and "+MyCollectTable.USERID + " =? ";
            }else if (type == DetailConstant.GOOD_TICKET){
                where = MyCollectTable.TICKETID + " =? "+ " and "+MyCollectTable.USERID + " =? ";
            }else {
                return false;
            }
            database.delete(MyCollectTable.TABLENAME, where, new String[]{String.valueOf(id), user_id});
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<MyCollectBean> query(){
        Cursor cursor = null;
        List<MyCollectBean> myCollects = new ArrayList<>();

        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getReadableDatabase();
            cursor = database.query(MyCollectTable.TABLENAME, null, null, null, null, null, null);
            while (cursor.moveToNext()){
                MyCollectBean myCollect = new MyCollectBean();
                myCollect.setId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TABLEID)));
                myCollect.setTicketId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TICKETID)));
                myCollect.setTourRouteId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.ROURROUTEID)));
                myCollect.setGoodsType(cursor.getInt(cursor.getColumnIndex(MyCollectTable.IDTYPE)));
                myCollects.add(myCollect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor !=  null)
                cursor.close();
        }
        return myCollects;
    }

    public List<MyCollectBean> query(String user_id){
        Cursor cursor = null;
        List<MyCollectBean> myCollects = new ArrayList<>();
        if (user_id == null || user_id.isEmpty())
            return myCollects;

        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getReadableDatabase();
            String where = MyCollectTable.USERID + "=?";
            cursor = database.query(MyCollectTable.TABLENAME, null, where, new String[]{user_id}, null, null, null);
            while (cursor.moveToNext()){
                MyCollectBean myCollect = new MyCollectBean();
                myCollect.setId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TABLEID)));
                myCollect.setTicketId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TICKETID)));
                myCollect.setTourRouteId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.ROURROUTEID)));
                myCollect.setGoodsType(cursor.getInt(cursor.getColumnIndex(MyCollectTable.IDTYPE)));
                myCollect.setUser_id(cursor.getString(cursor.getColumnIndex(MyCollectTable.USERID)));
                myCollects.add(myCollect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor !=  null)
                cursor.close();
        }
        return myCollects;
    }

    public List<MyCollectBean> query(int id){
        Cursor cursor = null;
        List<MyCollectBean> myCollects = new ArrayList<>();
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getReadableDatabase();
            String where = MyCollectTable.TABLEID + "=?";
            cursor = database.query(MyCollectTable.TABLENAME, null, where, new String[]{String.valueOf(id)}, null, null, null);
            while (cursor.moveToNext()){
                MyCollectBean myCollect = new MyCollectBean();
                myCollect.setId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TABLEID)));
                myCollect.setTicketId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TICKETID)));
                myCollect.setTourRouteId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.ROURROUTEID)));
                myCollect.setGoodsType(cursor.getInt(cursor.getColumnIndex(MyCollectTable.IDTYPE)));
                myCollects.add(myCollect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor !=  null)
                cursor.close();
        }
        return myCollects;
    }

    public boolean queryByGoodId(String user_id, int type, int id){
        if (user_id == null || user_id.isEmpty())
            return false;
        String where = null;
        if (type == DetailConstant.GOOD_ROUTE) {
            where = MyCollectTable.ROURROUTEID + "=?" + " and " + MyCollectTable.IDTYPE + "=?"+ " and "+MyCollectTable.USERID + " =? ";
        }else if (type == DetailConstant.GOOD_TICKET){
            where = MyCollectTable.TICKETID + "=?" + " and " + MyCollectTable.IDTYPE + "=?"+ " and "+MyCollectTable.USERID + " =? ";
        }else {
            return false;
        }

        Cursor cursor = null;
        List<MyCollectBean> myCollects = new ArrayList<>();
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getReadableDatabase();
            cursor = database.query(MyCollectTable.TABLENAME, null, where, new String[]{String.valueOf(id), String.valueOf(type), user_id}, null, null, null);
            while (cursor.moveToNext()){
                MyCollectBean myCollect = new MyCollectBean();
                myCollect.setId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TABLEID)));
                myCollect.setTicketId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.TICKETID)));
                myCollect.setTourRouteId(cursor.getInt(cursor.getColumnIndex(MyCollectTable.ROURROUTEID)));
                myCollect.setUser_id(cursor.getString(cursor.getColumnIndex(MyCollectTable.USERID)));
                myCollects.add(myCollect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor !=  null)
                cursor.close();
        }
        if(myCollects==null||myCollects.size()==0){
            return false;
        }
        return true;
    }

    public synchronized boolean deleteAll(){
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getWritableDatabase();
            database.delete(MyCollectTable.TABLENAME, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized boolean delete(String user_id, int id){
        if (user_id == null || user_id.isEmpty())
            return false;
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getWritableDatabase();
            String where = MyCollectTable.TABLEID + " =? " + " and "+MyCollectTable.USERID + " =? ";
            database.delete(MyCollectTable.TABLENAME, where, new String[]{String.valueOf(id), user_id});
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void update(MyCollectBean myCollect){
        ContentValues values = new ContentValues();
        values.put(MyCollectTable.GOODID, myCollect.getGoods_id());
        values.put(MyCollectTable.GOODNAME, myCollect.getGoods_name());
        try {
            SQLiteDatabase database = KKTripSQLHelper.getInstance(mContext).getWritableDatabase();
            String where = MyCollectTable.GOODID + " =? ";
            database.update(MyCollectTable.TABLENAME, values, where, new  String[]{myCollect.getGoods_id()});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
