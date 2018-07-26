package com.konka.kktripclient.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.konka.android.tv.KKFactoryManager;
import com.konka.kktripclient.net.info.AllOrderInfoEvent;
import com.konka.kktripclient.net.info.AllRouteSortEvent;
import com.konka.kktripclient.net.info.AllTicketsSortEvent;
import com.konka.kktripclient.net.info.AllVideosEvent;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.OrderDeleteEvent;
import com.konka.kktripclient.net.info.OrderInfoEvent;
import com.konka.kktripclient.net.info.QueryRouteAndTicketEvent;
import com.konka.kktripclient.net.info.RouteDetailsByID;
import com.konka.kktripclient.net.info.RouteDetailsEvent;
import com.konka.kktripclient.net.info.StartADEvent;
import com.konka.kktripclient.net.info.TicketsDetailsByID;
import com.konka.kktripclient.net.info.TicketsDetailsEvent;
import com.konka.kktripclient.net.info.ToastAdverEvent;
import com.konka.kktripclient.net.info.TripTabsEvent;
import com.konka.kktripclient.net.info.VideoDetailsEvent;
import com.konka.kktripclient.net.json.GsonRequest;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by smith on 2017/05/26.
 */
public class HttpHelper {
    private static final String TAG = HttpHelper.class.getSimpleName();
    private static HttpHelper httpHelper;
    public String sn;
    private Context mContext;
    EventBus eventBus;
    public static final String TEST_BASE_URL= "http://10.120.2.193:8084/kktripserver/"; //接口头部
    public static final String BASE_URL = Constant.BASE_URL+"kktripserver/";
    public static final String ROUR_TYPE_URL="tour_routes/"; //路线分类字段
    public static final String TICKETS_TYPE_URL = "tickets/";
    public static final String ORDERS_URL = "orders/";
    public static final String COLLECT_URL = "/favorites/resources";
    public static final String TOAST_ADVER_URL =  "toast_advers/";
    public static final String TRIP_TABS = "tabs/";
    public static final String QUERY_ROUTEANDTICKETS = "favorites/resources";
    public static final String START_AD = "startup_advers/";
    public static final String VIDEO_TYPE_URL = "videos/";


    RequestQueue requestQueue;

    private HttpHelper(Context context){

        mContext=context.getApplicationContext();

    }

    /**
     * 单例模式获取
     *
     * @return
     */
    public static HttpHelper getInstance(Context context){
        if(httpHelper==null){
            httpHelper=new HttpHelper(context);
            httpHelper.init();
        }
        return httpHelper;
    }

    /**
     * 初始化,激活device

     */
    public void init(){
        initKonKaIF();
    }

    //初始化Konka接口
    private void initKonKaIF() {

        if(requestQueue==null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }

        if(sn!=null)
            return;
        sn= CommonUtils.getSerialNumber(mContext);
        if(sn==null || sn.equals("")){
            sn="unknow";
        }

        if(eventBus==null) {
            eventBus = EventBus.getDefault();
        }
    }

    /**
     * 获取路线的所有分类信息
     * @return
     */

    public boolean getAllRouteInfo(){
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + ROUR_TYPE_URL + "types?deviceid="+ sn  + getTimeStamp();

        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<AllRouteSortEvent>() {
            @Override
            public void onResponse(AllRouteSortEvent allRouteSortEvent) {
                LogUtils.d(TAG,"allRouteSortInfo == "+allRouteSortEvent);
                int length = allRouteSortEvent.getData().size();
                for(int i = 0;i<length;i++) {
                    LogUtils.d(TAG, "allRouteSortInfo.getId = " + allRouteSortEvent.getData().get(i).getId() + " allRouteSortInfo.getNAME = " + allRouteSortEvent.getData().get(i).getName());
                }

                eventBus.post(allRouteSortEvent);

            }
        };

        Type tkType=new TypeToken<AllRouteSortEvent>(){}.getType();
        request(listener,tkType,url,null,"AllRouteSortEvent",Request.Method.GET);
        return true;
    }

    /**
     * 根据分类分页获取旅游路线的信息
     * @param sortId
     * @param page
     * @param pageSize
     * @return
     */

    public boolean getRouteDetailsInfo(String sortId,int page,int pageSize) {
        if(sn == null || sortId == null || sortId.equals("")) {
            return false;
        }
        String url="";
        url=BASE_URL + ROUR_TYPE_URL + "?deviceid="+ sn  + getTimeStamp()+"&typeid="+sortId+"&page="+page +"&pageSize="+pageSize;
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<RouteDetailsEvent>() {
            @Override
            public void onResponse(RouteDetailsEvent routeDetailsEvent) {
                eventBus.post(routeDetailsEvent);
            }
        };

        Type tkType=new TypeToken<RouteDetailsEvent>(){}.getType();
        request(listener,tkType,url,null,"RouteDetailsEvent",Request.Method.GET);
        return true;
    }

    /**
     * 获取门票的所有分类信息
     * @return
     */
    public boolean getAllTicketsInfo(){
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + TICKETS_TYPE_URL + "types?deviceid="+ sn  + getTimeStamp();

        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<AllTicketsSortEvent>() {
            @Override
            public void onResponse(AllTicketsSortEvent allTicketsSortEvent) {
                eventBus.post(allTicketsSortEvent);
            }
        };

        Type tkType=new TypeToken<AllTicketsSortEvent>(){}.getType();
        request(listener,tkType,url,null,"AllTicketsSortEvent",Request.Method.GET);
        return true;
    }

    /**
     * 根据分类分页获取门票的信息
     * @param sortId
     * @param page
     * @param pageSize
     * @return
     */
    public boolean getTicketsDetailsInfo(String sortId,int page,int pageSize) {
        if(sn == null || sortId == null || sortId.equals("")) {
            return false;
        }
        String url="";
        url=BASE_URL + TICKETS_TYPE_URL + "?deviceid="+ sn  + getTimeStamp()+"&typeid="+sortId+"&page="+page +"&pageSize="+pageSize;
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<TicketsDetailsEvent>() {
            @Override
            public void onResponse(TicketsDetailsEvent ticketsDetailsEvent) {
                eventBus.post(ticketsDetailsEvent);
            }
        };

        Type tkType=new TypeToken<TicketsDetailsEvent>(){}.getType();
        request(listener,tkType,url,null,"TicketsDetailsEvent",Request.Method.GET);
        return true;
    }

    public boolean getRouteDetailsByID(int sortId) {
        if(sn == null||sortId<0) {
            return false;
        }
        String url="";
        url=BASE_URL + ROUR_TYPE_URL + sortId+"?deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<RouteDetailsByID>() {
            @Override
            public void onResponse(RouteDetailsByID routeDetailsByID) {
                eventBus.post(routeDetailsByID);
            }
        };

        Type tkType=new TypeToken<RouteDetailsByID>(){}.getType();
        request(listener,tkType,url,null,"RouteDetailsByID",Request.Method.GET);
        return true;
    }

    public boolean getTicketsDetailsByID(int sortId) {
        if(sn == null||sortId<0) {
            return false;
        }
        String url="";
        url=BASE_URL + TICKETS_TYPE_URL + sortId+"?deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<TicketsDetailsByID>() {
            @Override
            public void onResponse(TicketsDetailsByID ticketsDetailsByID) {
                eventBus.post(ticketsDetailsByID);
            }
        };

        Type tkType=new TypeToken<TicketsDetailsByID>(){}.getType();
        request(listener,tkType,url,null,"TicketsDetailsByID",Request.Method.GET);
        return true;
    }

    public boolean getVideosDetailsByID(int sortId) {
        if(sn == null||sortId<0) {
            return false;
        }
        String url="";
        url=BASE_URL + VIDEO_TYPE_URL + sortId+"?deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<VideoDetailsEvent>() {
            @Override
            public void onResponse(VideoDetailsEvent videoDetailsEvent) {
                eventBus.post(videoDetailsEvent);
            }
        };

        Type tkType=new TypeToken<VideoDetailsEvent>(){}.getType();
        request(listener,tkType,url,null,"VideoDetailsEvent",Request.Method.GET);
        return true;
    }


    public boolean getToastAdverDetails() {
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + TOAST_ADVER_URL + "?deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<ToastAdverEvent>() {
            @Override
            public void onResponse(ToastAdverEvent toastAdverEvent) {
                eventBus.post(toastAdverEvent);
            }
        };

        Type tkType=new TypeToken<ToastAdverEvent>(){}.getType();
        request(listener,tkType,url,null,"ToastAdverEvent",Request.Method.GET);
        return true;
    }

    public boolean getQueryTourAndTicket(String touteList,String ticketList) {
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + QUERY_ROUTEANDTICKETS + "?tourRouteIds="+touteList+"&ticketIds="+ticketList+"&deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<QueryRouteAndTicketEvent>() {
            @Override
            public void onResponse(QueryRouteAndTicketEvent queryRouteAndTicketEvent) {
                eventBus.post(queryRouteAndTicketEvent);
            }
        };

        Type tkType=new TypeToken<QueryRouteAndTicketEvent>(){}.getType();
        request(listener,tkType,url,null,"QueryRouteAndTicketEvent",Request.Method.GET);
        return true;
    }

    public boolean getOrderInfo(int page,int pageSize,int userID, int userOrgin) {
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + ORDERS_URL + "?page="+page+"&pageSize="+pageSize+"&userId="+userID+"&userOrgin="+userOrgin+"&deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<AllOrderInfoEvent>() {
            @Override
            public void onResponse(AllOrderInfoEvent allOrderInfoEvent) {
                //LogUtils.d(TAG,"response == "+allOrderInfoEvent.getData().getOrders().get(0).getThumbnail());
                eventBus.post(allOrderInfoEvent);
            }
        };

        Type tkType=new TypeToken<AllOrderInfoEvent>(){}.getType();
        request(listener,tkType,url,null,"AllOrderInfoEvent",Request.Method.GET);
        return true;
    }

    public boolean getOrderInfoByID(String sortId) {
        if(sn == null) {
            return false;
        }
        String url="";
        url= BASE_URL + ORDERS_URL +sortId+ "?&deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<OrderInfoEvent>() {
            @Override
            public void onResponse(OrderInfoEvent orderInfoEvent) {
                eventBus.post(orderInfoEvent);
            }
        };

        Type tkType=new TypeToken<OrderInfoEvent>(){}.getType();
        request(listener,tkType,url,null,"OrderInfoEvent",Request.Method.GET);
        return true;
    }

    public boolean getStartAd() {
        if(sn == null) {
            return false;
        }
        String url="";
     //   http://test.kkapp.com/kktripserver/startup_advers/?deviceid=MBW1506V00000063F4I1&timestamp=1497865260361
        url=BASE_URL + START_AD + "?deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<StartADEvent>() {
            @Override
            public void onResponse(StartADEvent startADEvent) {
                eventBus.post(startADEvent);
            }
        };

        Type tkType=new TypeToken<StartADEvent>(){}.getType();
        request(listener,tkType,url,null,"StartADEvent",Request.Method.GET);
        return true;
    }

    public boolean getAllVideos(int page,int pageSize) {
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + VIDEO_TYPE_URL + "?deviceid="+ sn  + getTimeStamp()+"&page="+page +"&pageSize="+pageSize;
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<AllVideosEvent>() {
            @Override
            public void onResponse(AllVideosEvent allVideosEvent) {
                eventBus.post(allVideosEvent);
            }
        };

        Type tkType=new TypeToken<AllVideosEvent>(){}.getType();
        request(listener,tkType,url,null,"AllVideosEvent",Request.Method.GET);
        return true;
    }

    public boolean getTripTabs() {
        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + TRIP_TABS + "?deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<TripTabsEvent>() {
            @Override
            public void onResponse(TripTabsEvent tripTabsEvent) {
                eventBus.post(tripTabsEvent);
            }
        };

        Type tkType=new TypeToken<TripTabsEvent>(){}.getType();
        request(listener,tkType,url,null,"TripTabsEvent",Request.Method.GET);
        return true;
    }

    public String getAdUrl() {
        return BASE_URL + START_AD + "?deviceid=" + sn + getTimeStamp();
    }

    public String getTripTabsUrl() {
        return BASE_URL + TRIP_TABS + "?deviceid="+ sn  + getTimeStamp();
    }

    private<T> void request(Response.Listener<T> listener, Type tktype, String url,
                            GsonRequest.OtherInfoListener infoListener, final String req_type, int method){
        Response.ErrorListener errorListener=new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d(TAG,"onErrorResponse!!!");
                HttpErrorEvent event = new HttpErrorEvent();
                event.setReq_type(req_type);
                if (error instanceof HttpErrorEvent) {
                    event.setRetCode(((HttpErrorEvent) error).getRetCode());
                    event.setRetMsg(((HttpErrorEvent) error).getRetMsg());
                } else if (error instanceof com.android.volley.TimeoutError) {
                    event.setRetCode("000001");
                    event.setRetMsg("网络请求超时！");
                } else if (error instanceof com.android.volley.NoConnectionError) {
                    event.setRetCode("000002");
                    event.setRetMsg("无网络连接！");
                } else if (error instanceof com.android.volley.NetworkError) {
                    event.setRetCode("000003");
                    event.setRetMsg("网络出错！");
                } else {
                    event.setRetCode("100001");
                    event.setRetMsg("未知错误！");
                }
                eventBus.post(event);
                BigDataHelper.getInstance().sendErrorEvent(CommonUtils.getCurrentDateString(), event.getRetCode(), event.getRetMsg());
            }
        };
        Request request=new GsonRequest<T>(method, url,tktype, listener, errorListener,infoListener);
        //缓存
        request.setShouldCache(true);

        requestQueue.add(request);
    }

    /**
     * 支付加密之后post请求
     * @param key
     * @return
     */
    public String getPayVerifyResult(String key){
        String url = BASE_URL + "orders/";

        StringBuilder builder = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/json");//这个参数要对应好
            conn.connect();

            DataOutputStream out = new DataOutputStream(conn
                    .getOutputStream());

            byte[] content = key.getBytes("utf-8");

            out.write(content, 0, content.length);
            out.flush();
            out.close(); // flush and close

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK){
                return null;
            }

            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
        return builder.toString();
    }

    /**
     * 通过订单号获取可以重新支付的信息
     * @param orderId
     * @return
     */
    public String getPayInfoById(String orderId){
        return NetworkUtils.httpGet(BASE_URL + "orders/repay?id="+orderId);
    }


    public boolean doDelete(String sortId) {

        if(sn == null) {
            return false;
        }
        String url="";
        url=BASE_URL + ORDERS_URL +sortId+ "?&deviceid="+ sn  + getTimeStamp();
        LogUtils.d(TAG,"url == "+url);
        Response.Listener listener = new Response.Listener<OrderDeleteEvent>() {
            @Override
            public void onResponse(OrderDeleteEvent orderDeleteEvent) {
                eventBus.post(orderDeleteEvent);
            }
        };

        Type tkType=new TypeToken<OrderDeleteEvent>(){}.getType();
        request(listener,tkType,url,null,"OrderDeleteEvent",Request.Method.DELETE);
        return true;

/*        StringRequest sr = new StringRequest(Request.Method.DELETE,url,new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                LogUtils.d(TAG,""+s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.d(TAG,"error");
            }
        });
        requestQueue.add(sr);*/
    }

/*    public String okPost(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(json)
                .build();
        return url;
    }*/


    public String login(String url, String json) {

        OkHttpClient client = new OkHttpClient();
        final MediaType JSON
                = MediaType.parse("application/json;charset=utf-8");
        try {
            //把请求的内容字符串转换为json
            RequestBody body = RequestBody.create(JSON, json);

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            okhttp3.Response response = null;

            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

   /*
    public static JSONObject post(String url, String data) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost post = new HttpPost(url);

        HttpEntity entity = new StringEntity(data);

        post.setEntity(entity);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
        post.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        String response = null;

        try {

            httpResponse = client.execute(post);
            response = handleResponse(httpResponse, response);

        } catch (IOException e) {

        } finally {
            post.releaseConnection();
            httpResponse.close();
            client.close();
        }

        return JSONObject.parseObject(response);
    }
*/


    private String getSerialNumber() {
        byte[] serial = KKFactoryManager.getInstance(mContext.getApplicationContext()).getSerialNumber();
        if (serial == null) {
            Log.i("getSerialNumber##serial","null");
            return "";
        }
        String serialNum = new String(serial).trim();
        if (serialNum == null || serialNum.equals("")) {
            return "";
        }
        return serialNum.substring(0, 20);
    }


    private String getTimeStamp(){
        return "&timestamp="+System.currentTimeMillis();
    }


    public boolean deleteOrderById(int id){
        String url="";
        url=BASE_URL + ORDERS_URL + "?deviceid="+ sn + getTimeStamp()+"&id="+id;
        LogUtils.d(TAG,"url == "+url);

        return false;
    }

    public String queryOrderById(int id){
        String response = null;
        String url="";
        url=BASE_URL + ORDERS_URL + "?deviceid="+ sn + getTimeStamp()+"&id="+id;
        LogUtils.d(TAG,"url == "+url);

        return response;
    }

    public boolean queryCollect(List<Integer> tourRouteIdsd, List<Integer>ticketIds){
        if (sn == null)
            return false;

        String response = null;
        String url="";
        url=BASE_URL + COLLECT_URL + "?deviceid="+ sn + getTimeStamp()+"&tourRouteIds="+tourRouteIdsd+"&ticketIds="+ticketIds;
        LogUtils.d(TAG,"url == "+url);

        Response.Listener listener = new Response.Listener<AllRouteSortEvent>() {
            @Override
            public void onResponse(AllRouteSortEvent allRouteSortEvent) {
                LogUtils.d(TAG,"allRouteSortInfo == "+allRouteSortEvent);
                int length = allRouteSortEvent.getData().size();
                for(int i = 0;i<length;i++) {
                    LogUtils.d(TAG, "allRouteSortInfo.getId = " + allRouteSortEvent.getData().get(i).getId() + " allRouteSortInfo.getNAME = " + allRouteSortEvent.getData().get(i).getName());
                }

                eventBus.post(allRouteSortEvent);

            }
        };

        Type tkType=new TypeToken<AllRouteSortEvent>(){}.getType();
        request(listener,tkType,url,null,"queryCollect",Request.Method.GET);
        return true;

        //return response;
    }


    @Override
    public String toString() {
        return super.toString();
    }

}
