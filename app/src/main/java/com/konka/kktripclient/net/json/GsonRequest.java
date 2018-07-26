package com.konka.kktripclient.net.json;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.konka.kktripclient.net.info.HttpErrorEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;


/**
 * Created by smith on 2017/05/27.
 */
public class GsonRequest<T> extends Request<T> {

    /*
接口类型
 */
    public final static int TEA_INTERFACE=0;
    public final static int KONKA_INTERFACE=1;
    private static final String TAG ="GsonRequest";
    /*
    状态
     */
    public static String TEA_STATUS="status";
    public static String KONKA_RET="ret";
    public static String KONKA_STATUS="ret_code";
    public static String KONKA_SERVERADDR="ServerAddr";

    public static String DATA="data";

    private Response.Listener<T> listener;
    private Class<T> clazz;
    private Gson mGson;
    private Type type;

    private int requestStyle=TEA_INTERFACE;

    //添加额外的获取操作
    private OtherInfoListener<T> otherInfoListener;

    private Response.ErrorListener errorListener;
    EventBus eventBus;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener , OtherInfoListener otherInfoListener) {
        super(method, url, errorListener);
        this.listener=listener;
        this.clazz=clazz;
        this.errorListener=errorListener;
        this.otherInfoListener=otherInfoListener;
        GsonBuilder gb = new GsonBuilder();
        mGson=gb.registerTypeAdapter(String.class, new StringConverter())
                .disableHtmlEscaping()
                .create();
        eventBus= EventBus.getDefault();
        // TODO Auto-generated constructor stub
    }
    public GsonRequest(int method, String url, Type type, Response.Listener<T> listener,
                       Response.ErrorListener errorListener , OtherInfoListener otherInfoListener) {
        super(method, url, errorListener);
        this.listener=listener;
        this.type=type;
        this.otherInfoListener=otherInfoListener;
        mGson=new Gson();
        eventBus=new EventBus();
        // TODO Auto-generated constructor stub
    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        JSONObject object;
        String jsonString = "";
        try {
            jsonString = new String(response.data, "utf-8");
            if(!(jsonString==null || jsonString.equals(""))) {
                object = new JSONObject(jsonString);
                String retCode = object.getJSONObject("ret").getString("ret_code");
                String retMsg = object.getJSONObject("ret").getString("ret_msg");
                if(retCode.equals("0")) {
                    T rs = mGson.fromJson(jsonString, type);
                    return Response.success(rs, HttpHeaderParser.parseCacheHeaders(response));
                } else{

                    HttpErrorEvent httpErrorEvent = new HttpErrorEvent();
                    httpErrorEvent.setRetMsg(retMsg);
                    httpErrorEvent.setRetCode(retCode);
                    if(errorListener!=null) {
                        Log.d(TAG,"errorListener!=null");
                        errorListener.onErrorResponse(httpErrorEvent);
                    } else {
                        Log.d(TAG,"errorListener==null");
                        return Response.error(httpErrorEvent);
                    }
                    return null;
                }
            }
            Log.d(TAG,"json string = "+jsonString);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
        T rs = mGson.fromJson(jsonString, type);
        return Response.success(rs, HttpHeaderParser.parseCacheHeaders(response));

    }


    @Override
    protected void deliverResponse(T response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

    //用于返回额外数据
    public interface OtherInfoListener<M>{

        public M getOtherInfo(JSONObject object);

    }
}
