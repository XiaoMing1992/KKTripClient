package com.konka.kktripclient.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.konka.kktripclient.R;
import com.konka.kktripclient.bean.MyCollectBean;
import com.konka.kktripclient.database.CollectTableDao;
import com.konka.kktripclient.detail.DetailActivity;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.net.info.TicketsBean;
import com.konka.kktripclient.net.info.TourRoutesBean;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.ScaleRelativeLayout;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.konka.kktripclient.utils.CommonUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017-5-17.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.AdapterView> {
    private final String TAG = "CollectAdapter";

    private RecyclerView recyclerView;
    private RelativeLayout layout;
    private LinearLayout layout_menu;
    private LinearLayout layout_clear_old;
    private GridLayoutManager mLayoutMgr;

    private Handler handler = new Handler();
    private List<MyCollectBean> myCollects;
    private List<MyCollectBean> temp;

    private Context mContext;

    public CollectAdapter(Context context) {
        mContext = context;
    }

    public CollectAdapter(Context context, List<MyCollectBean> list, RecyclerView recyclerView) {
        mContext = context;
        myCollects = list;
        this.recyclerView = recyclerView;
    }

    private List<TourRoutesBean> mTourRoutesBeanList;
    private List<TicketsBean> mTicketsBeanList;

    public CollectAdapter(Context context, List<MyCollectBean> list, List<TourRoutesBean> tourRoutesBeanList, List<TicketsBean> ticketsBeanList, RecyclerView recyclerView) {
        mContext = context;
        myCollects = list;
        mTourRoutesBeanList = tourRoutesBeanList;
        mTicketsBeanList = ticketsBeanList;
        this.recyclerView = recyclerView;
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_page_collect_item, viewGroup, false);
        return new AdapterView(view);
    }

    @Override
    public void onBindViewHolder(final AdapterView adapterView, int position) {
        int state = -1;
        String original = null;
        String name = null;
        String discount = null;
        String thumbnail = null;

        LogUtils.d(TAG, "------------------- onBindViewHolder -------------------------------------- " + position + " start");
        LogUtils.d(TAG, "GoodsType = " + myCollects.get(position).getGoodsType());

        if (myCollects.get(position).getGoodsType() == DetailConstant.GOOD_TICKET) {
            for (int i = 0; i < mTicketsBeanList.size(); i++) {
                if (myCollects.get(position).getTicketId() == mTicketsBeanList.get(i).getId()) {
                    LogUtils.d(TAG, "GoodId = " + myCollects.get(position).getTicketId());

                    state = mTicketsBeanList.get(i).getState();
                    name = mTicketsBeanList.get(i).getName();
                    original = "" + mTicketsBeanList.get(i).getOriginalPrice();
                    discount = "" + mTicketsBeanList.get(i).getDiscountPrice();

                    if (state == 0) {
                        adapterView.old_layout.setVisibility(View.VISIBLE);
                    } else {
                        thumbnail = NetworkUtils.toURL(mTicketsBeanList.get(i).getThumbnail());
                        CommonUtils.downloadPicture(mContext, thumbnail, R.drawable.default_image, R.drawable.default_image, adapterView.img);
                    }

                    break;
                }
            }

        } else if (myCollects.get(position).getGoodsType() == DetailConstant.GOOD_ROUTE) {
            for (int i = 0; i < mTourRoutesBeanList.size(); i++) {
                if (myCollects.get(position).getTourRouteId() == mTourRoutesBeanList.get(i).getId()) {
                    LogUtils.d(TAG, "GoodId = " + myCollects.get(position).getTourRouteId());

                    state = mTourRoutesBeanList.get(i).getState();
                    name = mTourRoutesBeanList.get(i).getName();
                    original = "" + mTourRoutesBeanList.get(i).getOriginalPrice();
                    discount = "" + mTourRoutesBeanList.get(i).getDiscountPrice();

                    thumbnail = NetworkUtils.toURL(mTourRoutesBeanList.get(i).getThumbnail());

                    if (state == 0) {
                        adapterView.old_layout.setVisibility(View.VISIBLE);
                    } else {
                        //根据缩略图的路径来展示图片
                        CommonUtils.downloadPicture(mContext, thumbnail, R.drawable.default_image, R.drawable.default_image, adapterView.img);
                    }
                    break;
                }
            }
        }

        LogUtils.d(TAG, "state = " + state);
        LogUtils.d(TAG, "original = " + original);
        LogUtils.d(TAG, "name = " + name);
        LogUtils.d(TAG, "discount = " + discount);
        LogUtils.d(TAG, "thumbnail = " + thumbnail);
        LogUtils.d(TAG, "------------------- onBindViewHolder -------------------------------------- " + position + " end");

        adapterView.collect_item_layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                LogUtils.d(TAG, "--------> onFocusChange = " + b);
                adapterView.name.setSelected(b);
                //adapterView.name.setFocusable(b);
            }
        });

        //名称
        adapterView.name.setText(name);
        //原价
        adapterView.original.setText("¥" + CommonUtils.formatPrice(original));
        //折价
        adapterView.discount.setText("¥" + CommonUtils.formatPrice(discount));

        adapterView.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_MENU && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (adapterView.old_layout.getVisibility() != View.VISIBLE) {
                        adapterView.cancel_layout.setVisibility(View.VISIBLE);
                        return true;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && adapterView.cancel_layout.getVisibility() == View.VISIBLE) {
                    adapterView.cancel_layout.setVisibility(View.GONE);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                        || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (adapterView.cancel_layout.getVisibility() == View.VISIBLE) {
                        return true;
                    }
                }
                return false;
            }
        });

        adapterView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterView.old_layout.getVisibility() == View.VISIBLE) {
                    return;
                }

                if (adapterView.cancel_layout.getVisibility() == View.VISIBLE) { //点击删除
                    final int clickPosition = adapterView.getAdapterPosition();
                    LogUtils.d(TAG, "clickPosition = " + clickPosition);
                    if(clickPosition < 0) return;

                    if (!CollectTableDao.getInstance(mContext).delete(UserHelper.getInstance(mContext).getOpenId(), myCollects.get(clickPosition).getId())) {
                        Log.d(TAG, "delete " + myCollects.get(clickPosition).getId() + " fail");
                        return;
                    }
                    myCollects.remove(clickPosition);
                    notifyItemRemoved(clickPosition);


                    if (myCollects.isEmpty()) {
                        layout_menu.setVisibility(View.GONE);
                        layout_clear_old.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        layout.requestFocus();
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);

                        if (clickPosition > 0) {
                            recyclerView.getChildAt(clickPosition - 1 - mLayoutMgr.findFirstVisibleItemPosition()).requestFocus();
                        } else if (clickPosition == 0) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.getChildAt(clickPosition).requestFocus();
                                }
                            }, 200);
                        }
                    }
                } else if (adapterView.cancel_layout.getVisibility() == View.GONE) {
                    final int clickPosition = adapterView.getAdapterPosition();
                    LogUtils.d(TAG, "clickPosition = " + clickPosition);
                    if(clickPosition < 0) return;

                    JSONObject source = new JSONObject();
                    source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_SC);
                    source.put(Constant.BIG_DATA_VALUE_KEY_NAME, "-1");
                    source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, "-1");
                    if (myCollects.get(clickPosition).getGoodsType() == DetailConstant.GOOD_TICKET) {
                        LaunchHelper.startDetailActivity(mContext, myCollects.get(clickPosition).getGoodsType(), myCollects.get(clickPosition).getTicketId(), source.toJSONString());
                    } else if (myCollects.get(clickPosition).getGoodsType() == DetailConstant.GOOD_ROUTE) {
                        LaunchHelper.startDetailActivity(mContext, myCollects.get(clickPosition).getGoodsType(), myCollects.get(clickPosition).getTourRouteId(), source.toJSONString());
                    }
                    LogUtils.d(TAG, "type = " + myCollects.get(clickPosition).getGoodsType());
                    LogUtils.d(TAG, "ticketId = " + myCollects.get(clickPosition).getTicketId() + " tourRouteId = " + myCollects.get(clickPosition).getTourRouteId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCollects == null ? 0 : myCollects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class AdapterView extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView delete;
        //TextView old;
        TextView name;
        TextView original;
        TextView discount;
        RelativeLayout cancel_layout;
        RelativeLayout old_layout;
        ScaleRelativeLayout collect_item_layout;

        public AdapterView(View itemView) {
            super(itemView);
            img = (RoundedImageView) itemView.findViewById(R.id.img);
            delete = (TextView) itemView.findViewById(R.id.delete);
            //old = (TextView) itemView.findViewById(R.id.old);
            old_layout = (RelativeLayout) itemView.findViewById(R.id.old_layout);
            name = (TextView) itemView.findViewById(R.id.name);
            original = (TextView) itemView.findViewById(R.id.original);
            discount = (TextView) itemView.findViewById(R.id.discount);
            cancel_layout = (RelativeLayout) itemView.findViewById(R.id.cancel_layout);
            collect_item_layout = (ScaleRelativeLayout) itemView.findViewById(R.id.collect_item_layout);
        }
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public void setLayoutMenu(LinearLayout layout) {
        this.layout_menu = layout;
    }

    public void setLayoutClearOld(LinearLayout layout) {
        this.layout_clear_old = layout;
    }

    public void setmLayoutMgr(GridLayoutManager mLayoutMgr) {
        this.mLayoutMgr = mLayoutMgr;
    }


}

