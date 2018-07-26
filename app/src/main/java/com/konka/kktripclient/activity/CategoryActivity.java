package com.konka.kktripclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.adapter.CategoryItemAdapter;
import com.konka.kktripclient.activity.adapter.CategoryListAdapter;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.AllRouteSortEvent;
import com.konka.kktripclient.net.info.AllTicketsSortEvent;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.RouteDetailsEvent;
import com.konka.kktripclient.net.info.TicketsDetailsEvent;
import com.konka.kktripclient.ui.AppRecyclerView;
import com.konka.kktripclient.ui.RecycleViewDivider;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * 分类Activity
 */

public class CategoryActivity extends Activity {
    private final String TAG = this.getClass().getSimpleName();

    private TextView categoryTitle;
    private RelativeLayout loadingLayout;
    private RelativeLayout errorLayout;

    private AppRecyclerView categoryList;
    private AppRecyclerView categoryItem;
    private CategoryListAdapter categoryListAdapter;
    private CategoryItemAdapter categoryItemAdapter;
    private GridLayoutManager gridLayoutManager;

    private EventBus eventBus;

    private int listPosition;// 记录分类list中的位置
    private boolean addMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        initData(getIntent());
        setOnListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
        initData(intent);
    }

    @Override
    protected void onStart() {
        LogUtils.d(TAG, "onStart");
        super.onStart();
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        super.onDestroy();
        categoryItem.clearOnScrollListeners();
        EventBus.getDefault().removeAllStickyEvents();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        Glide.get(this).clearMemory();
    }

    private void initView() {
        categoryTitle = (TextView) findViewById(R.id.category_list_title);
        loadingLayout = (RelativeLayout) findViewById(R.id.category_loading_layout);
        errorLayout = (RelativeLayout) findViewById(R.id.category_error_layout);

        categoryList = (AppRecyclerView) findViewById(R.id.category_list);
        categoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        categoryList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, (int) getResources().getDimension(R.dimen.category_list_divider_height), getResources().getColor(R.color.transparent)));
        categoryListAdapter = new CategoryListAdapter(this);
        categoryList.setAdapter(categoryListAdapter);

        gridLayoutManager = new GridLayoutManager(this, 3);
        categoryItem = (AppRecyclerView) findViewById(R.id.category_item);
        categoryItem.setLayoutManager(gridLayoutManager);
        categoryItem.addItemDecoration(new RecycleViewDivider(this, true, (int) getResources().getDimension(R.dimen.category_item_divider_height), (int) getResources().getDimension(R.dimen.category_item_divider_width)));
        categoryItemAdapter = new CategoryItemAdapter(this);
        categoryItem.setAdapter(categoryItemAdapter);
        categoryItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    categoryItemAdapter.setGlideResumeRequests();
                } else {
                    categoryItemAdapter.setGlidePauseRequests();
                }
            }

        });
        categoryListAdapter.setCategoryItemAdapter(categoryItemAdapter);

        showLoading();

    }

    private void initData(Intent intent) {
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }
        if (intent.getStringExtra(Constant.KEY_TYPE).equals(Constant.LAUNCH_ROUTE_LIST)) {
            categoryTitle.setText(getString(R.string.category_list_name_route));
            if (HttpHelper.getInstance(this).getAllRouteInfo()) {
                LogUtils.d(TAG, "getAllRouteInfo");
            }
        } else if (intent.getStringExtra(Constant.KEY_TYPE).equals(Constant.LAUNCH_TICKET_LIST)) {
            categoryTitle.setText(getString(R.string.category_list_name_ticket));
            if (HttpHelper.getInstance(this).getAllTicketsInfo()) {
                LogUtils.d(TAG, "getAllTicketsInfo");
            }
        }
    }

    private void setOnListener() {
        categoryListAdapter.setViewFocus(new CategoryListAdapter.GetViewFocus() {
            @Override
            public void getPosition(int position) {
                listPosition = position;
                categoryItemAdapter.setListTitle(categoryListAdapter.getListName(listPosition));
            }

            @Override
            public void getFocusChange() {
                showLoading();
                categoryItem.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            gridLayoutManager.scrollToPositionWithOffset(0, 0);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }, 200);
            }
        });

        categoryItemAdapter.setViewFocus(new CategoryItemAdapter.GetViewFocus() {
            @Override
            public void getFocus(int keycode) {
                switch (keycode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        try {
                            RecyclerView.ViewHolder viewHolder = categoryList.findViewHolderForAdapterPosition(listPosition);
                            ((CategoryListAdapter.ItemHolder) viewHolder).itemLayout.requestFocus();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void getUpdate(int page, int pageSize) {
                showLoading();
                addMore = true;
                categoryListAdapter.getAllSortList(listPosition, page, pageSize);
            }

            @Override
            public void getComplete() {
                hideLoading();
            }
        });
    }

    private void showLoading() {
        categoryListAdapter.isLoading = true;
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        categoryListAdapter.isLoading = false;
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    //TODO eventBus事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        hideLoading();
        if (event instanceof AllRouteSortEvent) {// 路线的所有分类信息
            LogUtils.d(TAG, "onMessageEvent AllRouteSortEvent");
            if (((AllRouteSortEvent) event).getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                categoryListAdapter.setAllRouteSortList(((AllRouteSortEvent) event).getData());
            } else {
                LogUtils.d(TAG, "no data");
            }
        } else if (event instanceof AllTicketsSortEvent) {// 门票的所有分类信息
            LogUtils.d(TAG, "onMessageEvent AllTicketsSortEvent");
            if (((AllTicketsSortEvent) event).getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                categoryListAdapter.setAllTicketsSortList(((AllTicketsSortEvent) event).getData());
            } else {
                LogUtils.d(TAG, "no data");
            }
        } else if (event instanceof RouteDetailsEvent) {// 分类分页获取旅游路线的信息
            LogUtils.d(TAG, "onMessageEvent RouteDetailsEvent");
            if (((RouteDetailsEvent) event).getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                if (addMore) {
                    categoryItemAdapter.addRouteDetails(((RouteDetailsEvent) event).getData());
                    addMore = false;
                } else {
                    categoryItemAdapter.setRouteDetails(((RouteDetailsEvent) event).getData());
                }
            } else {
                LogUtils.d(TAG, "no data");
            }
        } else if (event instanceof TicketsDetailsEvent) {// 分类分页获取门票的信息
            LogUtils.d(TAG, "onMessageEvent TicketsDetailsEvent");
            if (((TicketsDetailsEvent) event).getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                if (addMore) {
                    categoryItemAdapter.addTicketsDetails(((TicketsDetailsEvent) event).getData());
                    addMore = false;
                } else {
                    categoryItemAdapter.setTicketsDetails(((TicketsDetailsEvent) event).getData());
                }
            } else {
                LogUtils.d(TAG, "no data");
            }
        } else if (event instanceof HttpErrorEvent) {
            LogUtils.d(TAG, "HttpErrorEvent type:" + ((HttpErrorEvent) event).getReq_type());
            if ("AllRouteSortEvent".equals(((HttpErrorEvent) event).getReq_type()) || "AllTicketsSortEvent".equals(((HttpErrorEvent) event).getReq_type())
                    || "RouteDetailsEvent".equals(((HttpErrorEvent) event).getReq_type()) || "TicketsDetailsEvent".equals(((HttpErrorEvent) event).getReq_type())) {
                errorLayout.setVisibility(View.VISIBLE);
            }
        }
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
}
