package com.konka.kktripclient.activity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.layout.view.GlideRoundTransform;
import com.konka.kktripclient.net.info.PaginationBean;
import com.konka.kktripclient.net.info.RouteDetailsEvent;
import com.konka.kktripclient.net.info.TicketsBean;
import com.konka.kktripclient.net.info.TicketsDetailsEvent;
import com.konka.kktripclient.net.info.TourRoutesBean;
import com.konka.kktripclient.ui.ScaleRelativeLayout;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_one on 2017-5-25.
 * <p>
 * 分类的List中Item的Adapter
 */

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ItemHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private List<TourRoutesBean> mRouteDetailsList = new ArrayList<>();
    private List<TicketsBean> mTicketsDetailsList = new ArrayList<>();
    private PaginationBean mRouteDetailsPaginationBean = new PaginationBean();
    private PaginationBean mTicketsDetailsPaginationBean = new PaginationBean();
    private int mType = Constant.CATEGORY_DEFAULT;
    private int spanCount = 3;

    private GlideRoundTransform roundTransform;

    private GetViewFocus viewFocus;
    private String listTitle;// 所在类别名称
    private int itemPosition;// 记录位置

    public CategoryItemAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewConstant) {
        ItemHolder itemHolder = new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false));
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        switch (mType) {
            case Constant.CATEGORY_ROUTE:
                holder.itemName.setText(mRouteDetailsList.get(position).getName());
                holder.itemDiscountPrice.setText(String.valueOf(mContext.getString(R.string.category_item_price) + CommonUtils.formatPrice(String.valueOf(mRouteDetailsList.get(position).getDiscountPrice()))));
                holder.itemOriginalPrice.setText(String.valueOf(mContext.getString(R.string.category_item_price) + CommonUtils.formatPrice(String.valueOf(mRouteDetailsList.get(position).getOriginalPrice()))));
                holder.itemOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 中划线
                loadImage(holder.itemIcon, NetworkUtils.toURL(mRouteDetailsList.get(position).getThumbnail()));
                break;
            case Constant.CATEGORY_TICKET:
                holder.itemName.setText(mTicketsDetailsList.get(position).getName());
                holder.itemDiscountPrice.setText(String.valueOf(mContext.getString(R.string.category_item_price) + CommonUtils.formatPrice(String.valueOf(mTicketsDetailsList.get(position).getDiscountPrice()))));
                holder.itemOriginalPrice.setText(String.valueOf(mContext.getString(R.string.category_item_price) + CommonUtils.formatPrice(String.valueOf(mTicketsDetailsList.get(position).getOriginalPrice()))));
                holder.itemOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 中划线
                loadImage(holder.itemIcon, NetworkUtils.toURL(mTicketsDetailsList.get(position).getThumbnail()));
                break;
        }

        holder.itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.itemName.setSelected(true);
                    holder.itemDiscountPrice.setSelected(true);
                    holder.itemOriginalPrice.setSelected(true);
                } else {
                    holder.itemName.setSelected(false);
                    holder.itemDiscountPrice.setSelected(false);
                    holder.itemOriginalPrice.setSelected(false);
                }
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject source = new JSONObject();
                source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_LB);
                source.put(Constant.BIG_DATA_VALUE_KEY_NAME, listTitle);
                source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, String.valueOf(position));
                switch (mType) {
                    case Constant.CATEGORY_ROUTE:
                        if (position < mRouteDetailsList.size()) {
                            LaunchHelper.startDetailActivity(mContext, -1, -1, source.toJSONString());
                            EventBus.getDefault().postSticky(mRouteDetailsList.get(position));
                        }
                        break;
                    case Constant.CATEGORY_TICKET:
                        if (position < mTicketsDetailsList.size()) {
                            LaunchHelper.startDetailActivity(mContext, -1, -1, source.toJSONString());
                            EventBus.getDefault().postSticky(mTicketsDetailsList.get(position));
                        }
                        break;
                }
            }
        });

        holder.itemLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            if (isFirstColumn(position)) {
                                if (viewFocus != null) {
                                    viewFocus.getFocus(keyCode);
                                }
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            switch (mType) {
                                case Constant.CATEGORY_ROUTE:
                                    if (isLastLine(position, mRouteDetailsList)) {
                                        if (mRouteDetailsPaginationBean.getPage() * mRouteDetailsPaginationBean.getPageSize() < mRouteDetailsPaginationBean.getCount()) {
                                            viewFocus.getUpdate(mRouteDetailsPaginationBean.getPage() + 1, mRouteDetailsPaginationBean.getPageSize());
                                            return true;
                                        } else {
                                            return true;
                                        }
                                    }
                                    break;
                                case Constant.CATEGORY_TICKET:
                                    if (isLastLine(position, mTicketsDetailsList)) {
                                        if (mTicketsDetailsPaginationBean.getPage() * mTicketsDetailsPaginationBean.getPageSize() < mTicketsDetailsPaginationBean.getCount()) {
                                            viewFocus.getUpdate(mTicketsDetailsPaginationBean.getPage() + 1, mTicketsDetailsPaginationBean.getPageSize());
                                            return true;
                                        } else {
                                            return true;
                                        }
                                    }
                                    break;
                            }
                            break;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        switch (mType) {
            case Constant.CATEGORY_ROUTE:
                return mRouteDetailsList.size();
            case Constant.CATEGORY_TICKET:
                return mTicketsDetailsList.size();
            case Constant.CATEGORY_DEFAULT:
            default:
                return 0;
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ScaleRelativeLayout itemLayout;
        private RoundedImageView itemIcon;
        private TextView itemName;
        private TextView itemDiscountPrice;
        private TextView itemOriginalPrice;

        public ItemHolder(View itemView) {
            super(itemView);
            itemLayout = (ScaleRelativeLayout) itemView.findViewById(R.id.category_item_layout);
            itemIcon = (RoundedImageView) itemView.findViewById(R.id.category_item_icon);
            itemName = (TextView) itemView.findViewById(R.id.category_item_name);
            itemDiscountPrice = (TextView) itemView.findViewById(R.id.category_item_discount_price);
            itemOriginalPrice = (TextView) itemView.findViewById(R.id.category_item_original_price);
        }
    }

    public interface GetViewFocus {
        void getFocus(int keycode);

        void getUpdate(int page, int pageSize);

        void getComplete();
    }

    public void setRouteDetails(RouteDetailsEvent.DataBean dataBean) {
        mType = Constant.CATEGORY_ROUTE;
        mRouteDetailsList = dataBean.getTourRoutes();
        mRouteDetailsPaginationBean = dataBean.getPagination();
        notifyDataSetChanged();
        LogUtils.d(TAG, "mRouteDetailsList.size(set):" + mRouteDetailsList.size());
    }

    public void setTicketsDetails(TicketsDetailsEvent.DataBean dataBean) {
        mType = Constant.CATEGORY_TICKET;
        mTicketsDetailsList = dataBean.getTickets();
        mTicketsDetailsPaginationBean = dataBean.getPagination();
        notifyDataSetChanged();
        LogUtils.d(TAG, "mTicketsDetailsList.size(set):" + mTicketsDetailsList.size());
    }

    public void addRouteDetails(RouteDetailsEvent.DataBean dataBean) {
        mType = Constant.CATEGORY_ROUTE;
        int start = mRouteDetailsList.size();
        mRouteDetailsList.addAll(dataBean.getTourRoutes());
        mRouteDetailsPaginationBean = dataBean.getPagination();
        notifyItemRangeInserted(start, dataBean.getTourRoutes().size());
        viewFocus.getComplete();
        LogUtils.d(TAG, "mRouteDetailsList.size(add):" + mRouteDetailsList.size());
    }

    public void addTicketsDetails(TicketsDetailsEvent.DataBean dataBean) {
        mType = Constant.CATEGORY_TICKET;
        int start = mTicketsDetailsList.size();
        mTicketsDetailsList.addAll(dataBean.getTickets());
        mTicketsDetailsPaginationBean = dataBean.getPagination();
        notifyItemRangeInserted(start, dataBean.getTickets().size());
        viewFocus.getComplete();
        LogUtils.d(TAG, "mTicketsDetailsList.size(add):" + mTicketsDetailsList.size());
    }

    // 用于设置Glide图片加载
    public void setGlidePauseRequests() {
        Glide.with(mContext).pauseRequests();
    }

    // 用于设置Glide图片加载
    public void setGlideResumeRequests() {
        Glide.with(mContext).resumeRequests();
    }

    public void setViewFocus(GetViewFocus viewFocus) {
        this.viewFocus = viewFocus;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    private boolean isLastLine(int position, List list) {
        if (list.size() % spanCount == 0) {
            return position >= list.size() - spanCount;
        }
        int term = list.size() - list.size() % spanCount;
        return position >= term;
    }

    private boolean isFirstLine(int position) {
        return position >= 0 && position < spanCount;
    }

    private boolean isFirstColumn(int position) {
        return position % spanCount == 0;
    }

    private void loadImage(RoundedImageView imageView, String url) {
        if (roundTransform == null) {
            roundTransform = new GlideRoundTransform(mContext, 4);
        }
        try {
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .override(mContext.getResources().getDimensionPixelOffset(R.dimen.category_item_image_width), mContext.getResources().getDimensionPixelOffset(R.dimen.category_item_image_height))
                    .transform(roundTransform)
                    .into(imageView);
        } catch (OutOfMemoryError e) {
            imageView.setImageResource(R.drawable.default_image);
        }
    }

}
