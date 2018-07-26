package com.konka.kktripclient.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.adapter.ManageOrderAdapter;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.AllOrderInfoEvent;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.OrderDeleteEvent;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.AppRecyclerView;
import com.konka.kktripclient.ui.RecycleViewDivider;
import com.konka.kktripclient.ui.ToastView;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ManageOrderActivity extends AppCompatActivity {
    private final String TAG = ManageOrderActivity.class.getSimpleName();
    private Context mContext = ManageOrderActivity.this;
    private AppRecyclerView orderView;
    private List<AllOrderInfoEvent.DataBean.OrdersBean> orderList = null;

    private ManageOrderAdapter adapter = null;
    private final int ITEM_SPAN_COUNT = 1;

    private EventBus eventBus;//数据处理

    private int curPage = 1;    //当前页
    private int pageSize = 10;   //每页的item的大小
    private int totalCount = 1; //总共item

    private int curPosition = 0;
    private int deleteNum = 0;
    //private boolean isLastOne = false;
    private GridLayoutManager mLayoutMgr;
    private LinearLayout layout_loading;
    private LinearLayout layout_load_fail;
    private RelativeLayout order_title;
    private RelativeLayout menu_layout;
    private ProgressBar load_more;
    private final int LOAD_OK = 0x00;
    private final int LOAD_FAIL = 0x01;
    private final int NO_NETWORK = 0x02;
    private final int SCROLL_TO_LAST_ITEM = 0x03;

    private final int SUCCESS = 0x04;
    private final int ORDER_NOT_EXIST = 0x05;
    private final int ORDER_UNDONE = 0x06;
    private final int OTHER = 0x07;

    private final int KEYCODE_DPAD_UP = 1;
    private final int KEYCODE_DPAD_DOWN = 2;
    private final int Y_OFFSET = 80;

    private ToastView toastView;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_OK:
                    LogUtils.d(TAG, "========= LOAD_OK =========");
                    order_title.setVisibility(View.VISIBLE); //显示标题
                    if (curPage == 1) {
                        LogUtils.d(TAG, "========= LOAD_OK ========= curPage == 1");
                        load_more.setVisibility(View.GONE); //关闭进度条
                        layout_loading.setVisibility(View.GONE); //关闭进度条
                        layout_load_fail.setVisibility(View.GONE); //关闭加载失败进度条
                        adapter.notifyDataSetChanged();
                    } else {
                        LogUtils.d(TAG, "========= LOAD_OK ========= curPage != 1" + " curPosition = "
                                + curPosition);
                        try {
                            adapter.notifyDataSetChanged();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    load_more.setVisibility(View.GONE); //关闭进度条
                                    LogUtils.d(TAG, "curPosition -1- mLayoutMgr.findFirstVisibleItemPosition() = "
                                            + (curPosition - 1 - mLayoutMgr.findFirstVisibleItemPosition()));
                                    if (curPosition - 1 - mLayoutMgr.findFirstVisibleItemPosition() < 0)
                                        return;
                                    orderView.getChildAt(curPosition - 1 - mLayoutMgr.findFirstVisibleItemPosition()).requestFocus();
                                }
                            }, 1000);
                            layout_load_fail.setVisibility(View.GONE); //关闭加载失败进度条
                        } catch (Exception e) {
                            e.printStackTrace();
                            //layout_load_fail.setVisibility(View.VISIBLE); //显示加载失败进度条
                            //order_title.setVisibility(View.GONE); //不显示标题
                            //menu_layout.setVisibility(View.GONE);//不显示菜单
                        }
                    }
                    break;

                case LOAD_FAIL:
                    LogUtils.d(TAG, "========= LOAD_FAIL =========");
                    layout_loading.setVisibility(View.GONE); //关闭进度条
                    load_more.setVisibility(View.GONE); //关闭进度条
                    if (curPage == 1 && orderList.isEmpty()) {
                        layout_load_fail.setVisibility(View.VISIBLE);
                        menu_layout.setVisibility(View.GONE);
                    }
                    break;

                case NO_NETWORK:
                    LogUtils.d(TAG, "========= NO_NETWORK =========");
                    load_more.setVisibility(View.GONE); //关闭进度条
                    layout_loading.setVisibility(View.GONE); //关闭进度条
                    layout_load_fail.setVisibility(View.VISIBLE);
                    menu_layout.setVisibility(View.GONE);
                    break;

                case SCROLL_TO_LAST_ITEM:
                    load_more.setVisibility(View.GONE);
                    break;

                case SUCCESS:
                    LogUtils.d(TAG, "SUCCESS curPosition = " + curPosition);
                    if (orderList.isEmpty()) {
                        return;
                    }
                    orderList.remove(curPosition);
                    adapter.notifyItemRemoved(curPosition);

                    deleteNum += 1;
                    curPosition -= 1;

                    adapter.notifyDataSetChanged();
                    if (orderList.isEmpty()) {
                        Log.d(TAG, "orderList is empty.");
                        //getNextPage();
                    } else {
                        LogUtils.d(TAG, "curPosition -1- mLayoutMgr.findFirstVisibleItemPosition() = "
                                + (curPosition - mLayoutMgr.findFirstVisibleItemPosition()));
                        Log.d(TAG, "curPosition "+curPosition);

                        if (curPosition > 0) {
                            if (curPosition - mLayoutMgr.findFirstVisibleItemPosition() < 0)
                                return;

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    orderView.getChildAt(curPosition - mLayoutMgr.findFirstVisibleItemPosition()).requestFocus();
                                }
                            }, 1000);

                        } else if (curPosition == 0) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    orderView.getChildAt(curPosition).requestFocus();
                                }
                            }, 1000);
                        }
                    }

                    LogUtils.d(TAG, "ManageOrderActivity --> OrderConstant.OPERATE_TYPE = " + OrderConstant.OPERATE_TYPE);
                    if (toastView == null)
                        toastView = new ToastView(ManageOrderActivity.this);
                    if (OrderConstant.OPERATE_TYPE == OrderConstant.CANCEL_ORDER)
                        toastView.setText("取消订单成功");
                    else if (OrderConstant.OPERATE_TYPE == OrderConstant.DELETE_ORDER)
                        toastView.setText("删除订单成功");
                    else if (OrderConstant.OPERATE_TYPE == -1)
                        return;
                    else
                        toastView.setText("操作成功");
                    toastView.show();

                    //if (deleteNum)
                    //if(orderList.size()== pageSize-2){
                        getNextPage();
                    //}
                    break;

                case ORDER_NOT_EXIST:
                    if (toastView == null)
                        toastView = new ToastView(ManageOrderActivity.this);
                    toastView.setText("订单不存在");
                    toastView.show();
                    break;

                case ORDER_UNDONE:
                    if (toastView == null)
                        toastView = new ToastView(ManageOrderActivity.this);
                    toastView.setText("订单未完成");
                    toastView.show();
                    break;

                case OTHER:
                    if (toastView == null)
                        toastView = new ToastView(ManageOrderActivity.this);
                    toastView.setText("网络异常，取消订单出错");
                    toastView.show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_manage_order);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //initData();
        register();

        Intent intent = getIntent();
        LogUtils.d(TAG, "intent = "+intent);
        if (intent != null){
                LogUtils.d(TAG, "order IS_REFRESH = "+OrderConstant.IS_REFRESH);
                if (OrderConstant.IS_REFRESH) {
                    if (OrderConstant.OEFRESH_TYPE == 0){
                        refresh(Constant.ORDER_STATE_OUT);
                        OrderConstant.OEFRESH_TYPE = -1;
                    }else {
                        refresh(Constant.ORDER_STATE_PAY);
                        OrderConstant.OEFRESH_TYPE = -1;
                    }
                    OrderConstant.IS_REFRESH = false;
                }
        }
    }

    private void refresh(final int new_state){
        LogUtils.d(TAG, "refresh curPosition = "+curPosition);
        if (orderList.isEmpty())
            return;
        orderList.get(curPosition).setState(new_state);
        adapter.notifyDataSetChanged();
        if (curPosition > 0) {
            if (curPosition - mLayoutMgr.findFirstVisibleItemPosition() < 0)
                return;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orderView.getChildAt(curPosition - mLayoutMgr.findFirstVisibleItemPosition()).requestFocus();
                }
            }, 1000);

        } else if (curPosition == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orderView.getChildAt(curPosition).requestFocus();
                }
            }, 1000);
        }
    }

    private void initView() {
        toastView = new ToastView(ManageOrderActivity.this);
        orderView = (AppRecyclerView) findViewById(R.id.order_list);
        load_more = (ProgressBar) findViewById(R.id.load_more);
        layout_loading = (LinearLayout) findViewById(R.id.layout_loading);
        order_title = (RelativeLayout) findViewById(R.id.order_title);
        layout_load_fail = (LinearLayout) findViewById(R.id.layout_load_fail);
        menu_layout = (RelativeLayout) findViewById(R.id.menu_layout);
    }

    private void initData() {
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            handler.sendEmptyMessage(NO_NETWORK);
            return;
        }

        orderList = new ArrayList<>();

        //设置layoutManager
        mLayoutMgr = new GridLayoutManager(ManageOrderActivity.this, ITEM_SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        orderView.setLayoutManager(mLayoutMgr);
        orderView.addItemDecoration(new RecycleViewDivider(ManageOrderActivity.this, false,
                (int) this.getResources().getDimension(R.dimen.manage_order_item_dividerHeight),
                (int) this.getResources().getDimension(R.dimen.manage_order_item_dividerWidth)));

        layout_loading.setVisibility(View.VISIBLE); //显示进度条

        if (UserHelper.getInstance(this).getOpenId() != null) {
            LogUtils.d(TAG, "curPage=" + curPage + "  pageSize=" + pageSize + "  userId=" + Integer.valueOf(UserHelper.getInstance(this).getOpenId()) + "  userOrgin=" + UserHelper.getInstance(this).getsUserOrgin());
            getData(curPage, pageSize, Integer.valueOf(UserHelper.getInstance(this).getOpenId()), UserHelper.getInstance(this).getsUserOrgin()); //获取订单信息
        }

        adapter = new ManageOrderAdapter(ManageOrderActivity.this, orderList, orderView);
        adapter.setmLayoutMgr(mLayoutMgr);
        adapter.setProgressBar(load_more);
        orderView.setAdapter(adapter);
        adapter.setOnFocusItemListener(new ManageOrderAdapter.OnFocusItemListener() {
            @Override
            public void onFocusChange(View view, boolean b, int position) {
                if (b) {
                    LogUtils.d(TAG, "scroll view position is " + position);
                    LogUtils.d(TAG, "(pageSize*curPage) = " + (pageSize * curPage));

                    curPosition = position;
                    LogUtils.d(TAG, "curPosition = " + curPosition);
                    LogUtils.d(TAG, "--> deleteNum = " + deleteNum);

                    if (position + 1 >= totalCount) {
                        LogUtils.d(TAG, "curPosition1 = " + curPosition + "    totalCount1 = " + totalCount);
                        return;
                    }

                    LogUtils.d(TAG, "curPosition2 = " + curPosition + "    totalCount2 = " + totalCount);

                    LogUtils.d(TAG, "continue (pageSize * curPage - deleteNum) = " + (pageSize * curPage - deleteNum));
                    LogUtils.d(TAG, "orderList.size() = " + orderList.size());
                    /*pageSize * curPage*/

                    if ((position + 1 + deleteNum) % (pageSize * curPage) == 0) {
                        LogUtils.d(TAG, " --> position = " + position);
                        load_more.setVisibility(View.VISIBLE); //显示进度条
                        curPage += 1;
                        LogUtils.d(TAG, "curPage is " + curPage);
                        if (UserHelper.getInstance(ManageOrderActivity.this).getOpenId() != null) {
                            getData(curPage, pageSize, Integer.valueOf(UserHelper.getInstance(ManageOrderActivity.this).getOpenId()), 0); //获取下一页的数据
                        }
                    }
                }
            }
        });
    }

    private void getNextPage(){
        load_more.setVisibility(View.VISIBLE); //显示进度条
        //curPage = 1;
        LogUtils.d(TAG, "getNextPage curPage is " + curPage+" curPosition="+curPosition);
        if (UserHelper.getInstance(ManageOrderActivity.this).getOpenId() != null) {
            getData(curPage, pageSize, Integer.valueOf(UserHelper.getInstance(ManageOrderActivity.this).getOpenId()), 0); //获取下一页的数据
        }
    }

    private void getData(int current_page, int page_size, int user_id, int user_orgin) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            //register();
            HttpHelper.getInstance(this).getOrderInfo(current_page, page_size, user_id, user_orgin);
        }
        else
            handler.sendEmptyMessage(LOAD_FAIL);
    }

    private synchronized void loadData(List<AllOrderInfoEvent.DataBean.OrdersBean> orders) {
        int len = orderList.size();
        LogUtils.d(TAG, "loadData previous orderList len"+len);
        for (int i = 0; i < orders.size(); i++) {
            boolean addFlag = false;
            for (int j=0;j<len;j++) {
                if ( !orderList.get(j).getId().equals(orders.get(i).getId())) {
                    LogUtils.d(TAG, "loadData Id = "+orderList.get(j).getId()+"  j="+j);
                    if (j == len-1) {
                        LogUtils.d(TAG, "loadData add order, now j="+j);
                        orderList.add(orders.get(i));
                        addFlag = true;
                    }
                }else{
                    addFlag = true;
                    break;
                }
            }
            if ( !addFlag )
                orderList.add(orders.get(i));
        }
        LogUtils.d(TAG, "loadData later orderList size"+orderList.size());
        LogUtils.d(TAG, orderList.toString());
    }

    //接收消息
    //TODO eventBus事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        if (event instanceof AllOrderInfoEvent) {
            if (((AllOrderInfoEvent) event).getData().getOrders() != null /*&& ((AllOrderInfoEvent) event).getData().getOrders().size() != 0*/) {
                if (((AllOrderInfoEvent) event).getData().getOrders().size() != 0)
                    LogUtils.d(TAG, "order infos thumbnail = " + ((AllOrderInfoEvent) event).getData().getOrders().get(0).getThumbnail() + " goodname=" + ((AllOrderInfoEvent) event).getData().getOrders().get(0).getGoodsName());
                loadData(((AllOrderInfoEvent) event).getData().getOrders()); //装载数据
            }
            if (((AllOrderInfoEvent) event).getData().getOrders() != null) {
                int page = ((AllOrderInfoEvent) event).getData().getPagination().getPage();
                int pageSize = ((AllOrderInfoEvent) event).getData().getPagination().getPageSize();
                totalCount = ((AllOrderInfoEvent) event).getData().getPagination().getCount();
                LogUtils.d(TAG, "AllOrderInfoEvent --> page =" + page + " pageSize =" + pageSize + " totalCount =" + totalCount);
            }
            handler.sendEmptyMessage(LOAD_OK);
        } else if (event instanceof OrderDeleteEvent) {
            LogUtils.d(TAG, "OrderDeleteEvent = " + ((OrderDeleteEvent) event).getRet().getRet_msg());
            if (((OrderDeleteEvent) event).getRet().getRet_code() != null && ((OrderDeleteEvent) event).getRet().getRet_code().equals("IE457003")) {
                handler.sendEmptyMessage(ORDER_NOT_EXIST);
            } else if (((OrderDeleteEvent) event).getRet().getRet_code() != null && ((OrderDeleteEvent) event).getRet().getRet_code().equals("IE457004")) {
                handler.sendEmptyMessage(ORDER_UNDONE);
            } else if (((OrderDeleteEvent) event).getRet().getRet_code() != null && ((OrderDeleteEvent) event).getRet().getRet_code().equals("0")) {
                handler.sendEmptyMessage(SUCCESS);
            }
        } else if (event instanceof HttpErrorEvent) {
            String type = ((HttpErrorEvent) event).getReq_type();
            LogUtils.d(TAG, "HttpErrorEvent type = " + type);
            if("AllOrderInfoEvent".equals(type) || "OrderDeleteEvent".equals(type)){
                LogUtils.d(TAG, "HttpErrorEvent = " + ((HttpErrorEvent) event).getRetCode() + " " + ((HttpErrorEvent) event).getRetMsg());
                if (((HttpErrorEvent) event).getRetCode() != null && ((HttpErrorEvent) event).getRetCode().equals("IE457003")) {
                    handler.sendEmptyMessage(ORDER_NOT_EXIST);
                } else if (((HttpErrorEvent) event).getRetCode() != null && ((HttpErrorEvent) event).getRetCode().equals("IE457004")) {
                    handler.sendEmptyMessage(ORDER_UNDONE);
                } else{
                    //handler.sendEmptyMessage(OTHER);
                    handler.sendEmptyMessage(LOAD_FAIL);
                }
            }


        }
    }

    @Override
    protected void onDestroy() {
        unregister();
        super.onDestroy();
    }

    private long mCurrentFlyTime;
    private long mLastFlyTime;

    private void doudong(View view, int type) {
        mCurrentFlyTime = System.currentTimeMillis();
        if (mCurrentFlyTime - mLastFlyTime <= 200) {
            return;
        }
        mLastFlyTime = mCurrentFlyTime;

        AnimatorSet tAnimatorSet = new AnimatorSet();
        ObjectAnimator tAnimator1 = ObjectAnimator.ofFloat(view, "translationY", type == KEYCODE_DPAD_UP ? Y_OFFSET : -Y_OFFSET);
        tAnimator1.setDuration(100);
        ObjectAnimator tAnimator2 = ObjectAnimator.ofFloat(view, "translationY", 0);
        tAnimator2.setDuration(100);
        tAnimatorSet.play(tAnimator1).before(tAnimator2);
        tAnimatorSet.setInterpolator(new DecelerateInterpolator());
        tAnimatorSet.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
            LogUtils.d(TAG, "onKeyDown curPosition = " + curPosition+"  totalCount = " + totalCount);
            if ((curPosition + 1 + deleteNum) >= totalCount) {
                if (curPosition == orderList.size()-1) {
                    doudong(orderView, KEYCODE_DPAD_DOWN);
                    return true;
                }
            }

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (curPosition == 0) {
                doudong(orderView, KEYCODE_DPAD_UP);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onPause(this);
    }


    private void register(){
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
        }
        if (!eventBus.isRegistered(this)) {
            LogUtils.d(TAG, "register eventBus");
            eventBus.register(this);
        }
    }

    private void unregister(){
        if (eventBus != null) {
            if (eventBus.isRegistered(this)) {
                eventBus.unregister(this);
            }
        }
    }
}
