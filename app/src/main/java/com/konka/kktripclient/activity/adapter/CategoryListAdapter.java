package com.konka.kktripclient.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.AllRouteSortEvent;
import com.konka.kktripclient.net.info.AllTicketsSortEvent;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_one on 2017-5-25.
 * <p>
 * 分类的List的Adapter
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ItemHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private List<AllRouteSortEvent.DataBean> mAllRouteSortList = new ArrayList<>();
    private List<AllTicketsSortEvent.DataBean> mAllTicketsSortList = new ArrayList<>();
    private CategoryItemAdapter categoryItemAdapter;// 用于判断是否含有数据
    private int mType = Constant.CATEGORY_DEFAULT;
    private int mPage = 1;// 代表加载第几页
    private int mPageSize = 12;// 代表一页加载的大小
    public boolean isLoading = false;// 是否正在加载

    private GetViewFocus viewFocus;
    private TextView textView;// 保存上一次获取到焦点的TextView

    public CategoryListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewConstant) {
        ItemHolder itemHolder = new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.category_list, parent, false));
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        switch (mType) {
            case Constant.CATEGORY_ROUTE:
                holder.itemName.setText(mAllRouteSortList.get(position).getName());
                break;
            case Constant.CATEGORY_TICKET:
                holder.itemName.setText(mAllTicketsSortList.get(position).getName());
                break;
        }

        holder.itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LogUtils.d(TAG, "position hasFocus:" + position);
                    viewFocus.getPosition(position);
                    if (textView != null) {
                        textView.setTextColor(mContext.getResources().getColor(R.color.main_primary));
                    }
                    if (holder.itemName != textView) {
                        viewFocus.getFocusChange();
                        getAllSortList(position, mPage, mPageSize);
                    }
                    textView = holder.itemName;
                    textView.setTextColor(mContext.getResources().getColor(R.color.white));
                    holder.itemName.setAlpha(1f);
                    holder.itemName.setSelected(true);
                } else {
                    LogUtils.d(TAG, "position noFocus:" + position);
                    textView.setTextColor(mContext.getResources().getColor(R.color.main_accent));
                    holder.itemName.setAlpha(0.7f);
                    holder.itemName.setSelected(false);
                }
            }
        });

        holder.itemLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            if (categoryItemAdapter.getItemCount() == 0 || isLoading) {
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (getItemCount() == position + 1) {
                                return true;
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
                return mAllRouteSortList.size();
            case Constant.CATEGORY_TICKET:
                return mAllTicketsSortList.size();
            case Constant.CATEGORY_DEFAULT:
            default:
                return 0;
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public RelativeLayout itemLayout;
        private TextView itemName;

        public ItemHolder(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.category_list_layout);
            itemName = (TextView) itemView.findViewById(R.id.category_list_name);
        }
    }

    public interface GetViewFocus {
        void getPosition(int position);

        void getFocusChange();
    }

    public void setCategoryItemAdapter(CategoryItemAdapter categoryItemAdapter) {
        this.categoryItemAdapter = categoryItemAdapter;
    }

    public void setViewFocus(GetViewFocus viewFocus) {
        this.viewFocus = viewFocus;
    }

    public void setAllRouteSortList(List<AllRouteSortEvent.DataBean> dataBean) {
        mType = Constant.CATEGORY_ROUTE;
        mAllRouteSortList = dataBean;
        notifyDataSetChanged();
    }

    public void setAllTicketsSortList(List<AllTicketsSortEvent.DataBean> dataBean) {
        mType = Constant.CATEGORY_TICKET;
        mAllTicketsSortList = dataBean;
        notifyDataSetChanged();
    }

    public String getListName(int position) {
        switch (mType) {
            case Constant.CATEGORY_ROUTE:
                return mAllRouteSortList.get(position).getName();
            case Constant.CATEGORY_TICKET:
                return mAllTicketsSortList.get(position).getName();
            case Constant.CATEGORY_DEFAULT:
            default:
                return "";
        }
    }

    public void getAllSortList(int position, int page, int pageSize) {
        switch (mType) {
            case Constant.CATEGORY_ROUTE:
                HttpHelper.getInstance(mContext).getRouteDetailsInfo(String.valueOf(mAllRouteSortList.get(position).getId()), page, pageSize);
                break;
            case Constant.CATEGORY_TICKET:
                HttpHelper.getInstance(mContext).getTicketsDetailsInfo(String.valueOf(mAllTicketsSortList.get(position).getId()), page, pageSize);
                break;
        }
    }
}
