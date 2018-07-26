package com.konka.kktripclient.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.adapter.CollectAdapter;
import com.konka.kktripclient.bean.MyCollectBean;
import com.konka.kktripclient.database.CollectTableDao;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.QueryRouteAndTicketEvent;
import com.konka.kktripclient.net.info.TicketsBean;
import com.konka.kktripclient.net.info.TourRoutesBean;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.AppRecyclerView;
import com.konka.kktripclient.ui.RecycleViewDivider;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private final String TAG = CollectActivity.class.getSimpleName();
    private Context mContext;

    private LinearLayout layout_clear_old;
    private LinearLayout layout_menu;
    private AppRecyclerView collectView;
    private GridLayoutManager mLayoutMgr;
    private RelativeLayout layout_empty;
    private Button bt_empty_collect;
    private ImageView img_clear;
    private TextView tv_clear;
    private ProgressBar progress;
    private LinearLayout layout_loading;
    private LinearLayout layout_load_fail;

    private List<MyCollectBean> temp;
    private List<MyCollectBean> collectList;
    private List<TourRoutesBean> tourRoutesBeanList;
    private List<TicketsBean> ticketsBeanList;
    private CollectAdapter adapter = null;

    private static final int START_CLEAR = 0x00;
    private static final int CLEAR_ERROR = 0x01;
    private static final int STOP_CLEAR = 0x02;
    private static final int UPDATE_CLEAR = 0x03;
    private static final int LOAD_OK = 0x04;
    private static final int LOAD_FAIL = 0x05;
    private static final int DISMISS_PROGRESS = 0x06;
    private static final int NO_NETWORK = 0x07;

    private final int ITEM_SPAN_COUNT = 4;
    private int row_child_num = 4; //每一行多少item
    private EventBus eventBus;//数据处理

    public static boolean fresh = false;

    private static class MyHandler extends Handler {
        private final WeakReference<CollectActivity> mActivity;

        public MyHandler(CollectActivity activity) {
            mActivity = new WeakReference<CollectActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CollectActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case START_CLEAR:
                        activity.progress.setVisibility(View.VISIBLE);
                        break;

                    case CLEAR_ERROR:
                        activity.progress.setVisibility(View.GONE);
                        break;

                    case STOP_CLEAR:
                        activity.progress.setVisibility(View.GONE);
                        if (activity.collectList.isEmpty()) {
                            activity.layout_menu.setVisibility(View.GONE);
                            activity.layout_clear_old.setVisibility(View.GONE);
                            activity.collectView.setVisibility(View.GONE);
                            activity.layout_empty.setVisibility(View.VISIBLE);
                            activity.layout_empty.requestFocus();
                        }
                        break;

                    case UPDATE_CLEAR:
                        activity.collectList.remove(msg.arg1);
                        activity.adapter.notifyItemRemoved(msg.arg1);
                        break;

                    case LOAD_OK:
                        LogUtils.d(activity.TAG, "size = " + activity.temp.size());
                        if (activity.temp.size() == 0) {
                            //progress.setVisibility(View.GONE);
                            activity.layout_loading.setVisibility(View.GONE);
                            activity.layout_menu.setVisibility(View.GONE);
                            activity.layout_clear_old.setVisibility(View.GONE);
                            activity.collectView.setVisibility(View.GONE);
                            activity.layout_empty.setVisibility(View.VISIBLE);
                            activity.layout_empty.requestFocus();
                        } else {
                            activity.getServerDatas();
                        }
                        break;


                    case LOAD_FAIL:
                        if (activity.adapter != null)
                            activity.adapter.notifyDataSetChanged();
                        //progress.setVisibility(View.GONE);
                        activity.layout_loading.setVisibility(View.GONE);
                        //layout_load_fail.setVisibility(View.VISIBLE);
                        break;

                    case DISMISS_PROGRESS:
                        activity.sort();
                        //设置adapter
                        activity.adapter = new CollectAdapter(activity, activity.collectList, activity.tourRoutesBeanList, activity.ticketsBeanList, activity.collectView);
                        activity.adapter.setmLayoutMgr(activity.mLayoutMgr);
                        activity.adapter.setLayout(activity.layout_empty);
                        activity.adapter.setLayoutMenu(activity.layout_menu);
                        activity.adapter.setLayoutClearOld(activity.layout_clear_old);
                        activity.collectView.setAdapter(activity.adapter);
                        activity.collectView.setVisibility(View.VISIBLE);
                        activity.collectView.requestFocus();
                        activity.collectView.setSelected(false);
                        //progress.setVisibility(View.GONE);
                        activity.layout_menu.setVisibility(View.VISIBLE);
                        activity.layout_clear_old.setVisibility(View.VISIBLE);
                        activity.layout_loading.setVisibility(View.GONE);
                        activity.layout_empty.setVisibility(View.GONE);
                        break;

                    case NO_NETWORK:
                        activity.layout_menu.setVisibility(View.GONE);
                        activity.layout_clear_old.setVisibility(View.GONE);
                        activity.collectView.setVisibility(View.GONE);
                        activity.layout_empty.setVisibility(View.GONE);
                        activity.layout_loading.setVisibility(View.GONE);
                        activity.layout_load_fail.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page_collect);
        mContext = CollectActivity.this;
        initView();
        //initData();
        listener();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        layout_clear_old = (LinearLayout) findViewById(R.id.layout_clear_old);
        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
        collectView = (AppRecyclerView) findViewById(R.id.collection_list);
        layout_empty = (RelativeLayout) findViewById(R.id.layout_empty);
        bt_empty_collect = (Button) findViewById(R.id.bt_empty_collect);
        progress = (ProgressBar) findViewById(R.id.progress);
        layout_loading = (LinearLayout) findViewById(R.id.layout_loading);
        layout_load_fail = (LinearLayout) findViewById(R.id.layout_load_fail);

        img_clear = (ImageView) findViewById(R.id.img_clear);
        tv_clear = (TextView) findViewById(R.id.tv_clear);

        mLayoutMgr = new GridLayoutManager(mContext, ITEM_SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        collectView.setLayoutManager(mLayoutMgr);
        collectView.addItemDecoration(new RecycleViewDivider(mContext, true,
                (int) this.getResources().getDimension(R.dimen.collect_item_dividerHeight),
                (int) this.getResources().getDimension(R.dimen.collect_item_dividerWidth), row_child_num));
    }

    private void initData() {
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }

        if (!NetworkUtils.isNetworkConnected(mContext)) {
            mHandler.sendEmptyMessage(NO_NETWORK);
            return;
        }

        collectView.requestFocus();
        collectView.setSelected(false);
        layout_clear_old.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    img_clear.setImageResource(R.drawable.clear_icon_focus);
                    tv_clear.setTextColor(getResources().getColor(R.color.collect_color1));
                } else {
                    img_clear.setImageResource(R.drawable.clear_icon_unfocus);
                    tv_clear.setTextColor(getResources().getColor(R.color.collect_color4));
                }
            }
        });
        layout_loading.setVisibility(View.VISIBLE);

        mMyThread = new MyThread(CollectActivity.this);
        mMyThread.start();
    }

    private MyThread mMyThread;

    private static class MyThread extends Thread {
        private WeakReference<CollectActivity> mWkdActivityRef;

        public MyThread(CollectActivity activity) {
            mWkdActivityRef = new WeakReference<CollectActivity>(activity);
        }

        @Override
        public void run() {
            super.run();
            if (mWkdActivityRef == null) {
                return;

            } else if (mWkdActivityRef.get() != null) {
                mWkdActivityRef.get().temp = CollectTableDao.getInstance(mWkdActivityRef.get()).query(UserHelper.getInstance(mWkdActivityRef.get()).getOpenId());
                if (mWkdActivityRef.get().temp != null) {
                    mWkdActivityRef.get().mHandler.sendEmptyMessageDelayed(LOAD_OK, 1000);
                } else {
                    mWkdActivityRef.get().mHandler.sendEmptyMessageDelayed(LOAD_FAIL, 1000);
                }
            }
        }
    }

    private void listener() {
        bt_empty_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMyThread != null){
                    mHandler.removeCallbacks(mMyThread);
                    mMyThread = null;
                }

                LaunchHelper.startCategoryActivity(mContext, Constant.LAUNCH_ROUTE_LIST);
            }
        });

        layout_clear_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    }

    private void getServerDatas() {
        String routeIds = "";
        String ticketIds = "";
        for (int i = 0; i <temp.size(); i++) {
            if (temp.get(i).getGoodsType() == DetailConstant.GOOD_ROUTE) {
                LogUtils.d(TAG, "routeIds i = " +i);
                if (routeIds.equals("")) {
                    routeIds = routeIds + temp.get(i).getTourRouteId();
                } else {
                    routeIds = routeIds + "," + temp.get(i).getTourRouteId();
                }
            } else if (temp.get(i).getGoodsType() == DetailConstant.GOOD_TICKET) {
                LogUtils.d(TAG, "ticketIds i = " +i);
                if (ticketIds.equals("")) {
                    ticketIds = ticketIds + temp.get(i).getTicketId();
                } else {
                    ticketIds = ticketIds + "," + temp.get(i).getTicketId();
                }
            }
        }
        LogUtils.d(TAG, "routeIds = " + routeIds + " <--------------  ticketIds= " + ticketIds);
        HttpHelper.getInstance(this).getQueryTourAndTicket(routeIds, ticketIds);
    }

    //TODO eventBus事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        if (event instanceof QueryRouteAndTicketEvent) {
            if (((QueryRouteAndTicketEvent) event).getData().getTourRoutes() != null && ((QueryRouteAndTicketEvent) event).getData().getTourRoutes().size() != 0)
                LogUtils.d(TAG, "收藏路线名 = " + ((QueryRouteAndTicketEvent) event).getData().getTourRoutes().get(0).getName());
            if (((QueryRouteAndTicketEvent) event).getData().getTickets() != null && ((QueryRouteAndTicketEvent) event).getData().getTickets().size() != 0)
                LogUtils.d(TAG, " 收藏门票名 = " + ((QueryRouteAndTicketEvent) event).getData().getTickets().get(0).getName()+" 收藏门票状态 = " +((QueryRouteAndTicketEvent) event).getData().getTickets().get(0).getState());

            if (((QueryRouteAndTicketEvent) event).getData().getTourRoutes() != null)
                tourRoutesBeanList = ((QueryRouteAndTicketEvent) event).getData().getTourRoutes();
            if (((QueryRouteAndTicketEvent) event).getData().getTickets() != null)
                ticketsBeanList = ((QueryRouteAndTicketEvent) event).getData().getTickets();

            mHandler.sendEmptyMessageDelayed(DISMISS_PROGRESS, 0); //隐藏掉加载条
        } else if (event instanceof HttpErrorEvent) {
            String type = ((HttpErrorEvent) event).getReq_type();
            LogUtils.d(TAG, "HttpErrorEvent type = " + type);
            if("QueryRouteAndTicketEvent".equals(type)){
                LogUtils.d(TAG, "HttpErrorEvent = " + ((HttpErrorEvent) event).getRetCode() + " " + ((HttpErrorEvent) event).getRetMsg());
                mHandler.sendEmptyMessageDelayed(LOAD_FAIL, 0); //隐藏掉加载条
            }

        }
    }

    private void clear() {
        mHandler.sendEmptyMessage(START_CLEAR);
        if (collectList != null) {
            for (int i = 0; i < collectList.size(); i++) {
                final int position = i;
                int state = -1;

                if (collectList.get(position).getGoodsType() == DetailConstant.GOOD_ROUTE) {
                    if (tourRoutesBeanList != null) {
                        for (int k = 0; k < tourRoutesBeanList.size(); k++) { //注意 k 值
                            if (collectList.get(position).getTourRouteId() == tourRoutesBeanList.get(k).getId()) {
                                state = tourRoutesBeanList.get(k).getState();
                                break;
                            }
                        }
                    }
                } else if (collectList.get(position).getGoodsType() == DetailConstant.GOOD_TICKET) {
                    if (ticketsBeanList != null) {
                        for (int k = 0; k < ticketsBeanList.size(); k++) { //注意 k 值
                            if (collectList.get(position).getTicketId() == ticketsBeanList.get(k).getId()) {
                                state = ticketsBeanList.get(k).getState();
                                break;
                            }
                        }
                    }
                }
                LogUtils.d(TAG, "i = " + i);
                LogUtils.d(TAG, "position = " + position);
                LogUtils.d(TAG, "state = " + state);
                if (state == 0) {
                    boolean delete = CollectTableDao.getInstance(mContext).delete(UserHelper.getInstance(mContext).getOpenId(), collectList.get(position).getId());
                    if (delete) {
                        LogUtils.d(TAG, "delete "+collectList.get(position).getId());
                        collectList.remove(position);
                        adapter.notifyItemRemoved(position);
                        i = -1; //重新又开始检测
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(STOP_CLEAR, 1000);
    }

    @Override
    protected void onDestroy() {
        if (eventBus != null) {
            if (eventBus.isRegistered(this)) {
                eventBus.unregister(this);
            }
        }

        if(mMyThread != null){
            mHandler.removeCallbacks(mMyThread);
            mMyThread = null;
        }

        if(mContext != null)
            mContext = null;

        super.onDestroy();
    }

    private void sort() {
        List<MyCollectBean> temp1 = new ArrayList<>();
        List<MyCollectBean> temp0 = new ArrayList<>();
        int state = -1;
        for (int p = 0; p < temp.size(); p++) {
            if (temp.get(p).getGoodsType() == DetailConstant.GOOD_TICKET) {
                for (int i = 0; i < ticketsBeanList.size(); i++) {
                    if (temp.get(p).getTicketId() == ticketsBeanList.get(i).getId()) {
                        LogUtils.d(TAG, "-->sort TicketId = " + temp.get(p).getTicketId());

                        state = ticketsBeanList.get(i).getState();
                        if (state == 1){
                            temp1.add(temp.get(p));
                        }else if (state == 0){
                            temp0.add(temp.get(p));
                        }
                    }
                }
            } else if (temp.get(p).getGoodsType() == DetailConstant.GOOD_ROUTE) {
                for (int i = 0; i < tourRoutesBeanList.size(); i++) {
                    if (temp.get(p).getTourRouteId() == tourRoutesBeanList.get(i).getId()) {
                        LogUtils.d(TAG, "-->sort RouteId = " + temp.get(p).getTourRouteId());

                        state = tourRoutesBeanList.get(i).getState();
                        if (state == 1){
                            temp1.add(temp.get(p));
                        }else if (state == 0){
                            temp0.add(temp.get(p));
                        }
                    }
                }
            }
        }

        collectList = new ArrayList<>();
        for (int i=temp1.size()-1;i>=0;i--){
            collectList.add(temp1.get(i));
        }

        for (int i=temp0.size()-1;i>=0;i--){
            collectList.add(temp0.get(i));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("fresh", "fresh = "+fresh);
        if (fresh){
            fresh = false;
            initData();
        }
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onPause(this);
    }
}
