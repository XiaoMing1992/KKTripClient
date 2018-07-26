package com.konka.kktripclient.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.ManageOrderItemActivity;
import com.konka.kktripclient.activity.OrderConstant;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.AllOrderInfoEvent;
import com.konka.kktripclient.ui.ToastView;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by HP on 2017-5-23.
 */

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.AdapterView> {
    private final String TAG = "ManageOrderAdapter";

    private List<AllOrderInfoEvent.DataBean.OrdersBean> myOrders;
    private Context mContext;
    private RecyclerView recyclerView;
    private List<RelativeLayout> layout;
    private GridLayoutManager mLayoutMgr;
    private ProgressBar load_more;

    private OnFocusItemListener onFocusItemListener;
    public void setOnFocusItemListener(OnFocusItemListener listener){
        this.onFocusItemListener = listener;
    }

    public ManageOrderAdapter(Context context) {
        mContext = context;
    }

    public ManageOrderAdapter(Context context, List<AllOrderInfoEvent.DataBean.OrdersBean> list, RecyclerView recyclerView) {
        mContext = context;
        myOrders = list;
        this.recyclerView = recyclerView;
    }

    public void setProgressBar(ProgressBar load_more){
        this.load_more = load_more;
    }

    @Override
    public ManageOrderAdapter.AdapterView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_manage_order_item, viewGroup, false);
        return new ManageOrderAdapter.AdapterView(view);
    }

    long lastTime = 0; //记录删除订单的上次时间
    @Override
    public void onBindViewHolder(final ManageOrderAdapter.AdapterView adapterView, final int position) {
        LogUtils.d(TAG, "order_number = "+myOrders.get(position).getId()+"  State = "+myOrders.get(position).getState());

        //订单编号
        adapterView.order_number.setText(""+myOrders.get(position).getId());

        //根据缩略图的路径来展示图片
        CommonUtils.downloadPicture(mContext, NetworkUtils.toURL(myOrders.get(position).getThumbnail()), R.drawable.default_image, R.drawable.default_image, adapterView.img);

        //名称
        adapterView.goods_name.setText(myOrders.get(position).getGoodsName());
        //购买数量
        adapterView.amount.setText("" + myOrders.get(position).getAmount());
        //订单金额
        adapterView.price.setText(myOrders.get(position).getPrice());

        //订单有四种状态（0:订单生成、1:用户已支付、2:商家确认、3:出行完成）
        adapterView.state.setText(getState(myOrders.get(position).getState()));
        //LogUtils.d(TAG, "State = "+myOrders.get(position).getState());


        adapterView.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_MENU && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (System.currentTimeMillis() - lastTime > 3000) { //控制删除订单的时间间隔为3秒
                        //lastTime = System.currentTimeMillis();
                        if (load_more != null && load_more.getVisibility() != View.VISIBLE)
                            showPopwindow(mContext, adapterView.itemView, adapterView.getAdapterPosition());
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
/*                    adapterView.delete.setVisibility(View.GONE);
                    return true;*/
                }
                return false;
            }
        });

        adapterView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int clickPosition = adapterView.getAdapterPosition();
                LogUtils.d(TAG, "clickPosition = " + clickPosition);
                if(clickPosition < 0) return;

                Intent intent = new Intent();
                intent.setClass(mContext, ManageOrderItemActivity.class);
                //intent.putExtra("item", myOrders.get(clickPosition));
                LogUtils.d(TAG, "item_id = " + myOrders.get(clickPosition).getId());
                intent.putExtra("item_id", myOrders.get(clickPosition).getId());
                mContext.startActivity(intent);
            }
        });

        if (onFocusItemListener != null) {
            adapterView.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    LogUtils.d(TAG, "=== scroll view position is " + position);
                    onFocusItemListener.onFocusChange(adapterView.itemView, b, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return myOrders == null ? 0 : myOrders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class AdapterView extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView goods_name;
        TextView order_number;
        TextView amount;
        TextView price;
        TextView state;

        public AdapterView(View itemView) {
            super(itemView);
            img = (RoundedImageView) itemView.findViewById(R.id.img);
            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            order_number = (TextView) itemView.findViewById(R.id.order_number);
            amount = (TextView) itemView.findViewById(R.id.amount);
            price = (TextView) itemView.findViewById(R.id.price);
            state = (TextView) itemView.findViewById(R.id.state);
        }
    }

    private String getState(int stateCode) {
        String state = null;
        switch (stateCode) {
            case Constant.ORDER_STATE_MAKE:
                state = mContext.getResources().getString(R.string.order_state_make);
                break;
            case Constant.ORDER_STATE_PAY:
                state = mContext.getResources().getString(R.string.order_state_pay);
                break;
            case Constant.ORDER_STATE_CONFIRM:
                state = mContext.getResources().getString(R.string.order_state_confirm);
                break;
            case Constant.ORDER_STATE_DONE:
                state = mContext.getResources().getString(R.string.order_state_done);
                break;
            case Constant.ORDER_STATE_OUT:
                state = mContext.getResources().getString(R.string.order_state_out);
                break;
        }
        return state;
    }

    public void setLayout(List<RelativeLayout> layout) {
        this.layout = layout;
    }

    public void setmLayoutMgr(GridLayoutManager mLayoutMgr) {
        this.mLayoutMgr = mLayoutMgr;
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow(final Context context, View view2, final int position) {
        LogUtils.d(TAG, "position = " + position);
        if(position < 0) return;

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.delete_order, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
/*        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);*/

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.AnimationRightFade);
        // 在底部显示
        window.showAtLocation(view2, Gravity.RIGHT, 0, 0);

        final TextView delete = (TextView) view.findViewById(R.id.delete);
        final TextView cancel = (TextView) view.findViewById(R.id.cancel);

        cancel.requestFocus();
        cancel.setTextColor(context.getResources().getColor(R.color.order_color3));
        cancel.setTextSize(24);
        cancel.setAlpha(1.0f);
        cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    cancel.setTextColor(context.getResources().getColor(R.color.order_color3));
                    cancel.setTextSize(24);
                    cancel.setAlpha(1.0f);
                } else {
                    cancel.setTextColor(context.getResources().getColor(R.color.order_color2));
                    cancel.setTextSize(20);
                    cancel.setAlpha(0.3f);
                }
            }
        });

        delete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    delete.setTextColor(context.getResources().getColor(R.color.order_color3));
                    delete.setTextSize(24);
                    delete.setAlpha(1.0f);
                } else {
                    delete.setTextColor(context.getResources().getColor(R.color.order_color2));
                    delete.setTextSize(20);
                    delete.setAlpha(0.3f);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(TAG, "click delete " + position);

                if (window != null && window.isShowing()) {
                    window.dismiss();
                }

                if (myOrders.get(position).getState() == Constant.ORDER_STATE_PAY ||
                        myOrders.get(position).getState() == Constant.ORDER_STATE_CONFIRM) {
                    ToastView toastView = new ToastView(mContext);
                    toastView.setText(R.string.order_can_not_delete);
                    toastView.show();
                    //Toast.makeText(mContext, R.string.order_can_not_delete, Toast.LENGTH_SHORT).show();
                    return;
                }
                //请求服务器删除id为myOrders.get(clickPosition).getId()的订单
                OrderConstant.OPERATE_TYPE = OrderConstant.DELETE_ORDER;
                LogUtils.d(TAG, "position = " +position+"  order_id = " + myOrders.get(position).getId());
                boolean is_ok = HttpHelper.getInstance(mContext).doDelete(""+myOrders.get(position).getId());
                if (!is_ok) {
                    ToastView toastView = new ToastView(mContext);
                    toastView.setText( R.string.order_delete_fail);
                    toastView.show();
                    return;
                }
                lastTime = System.currentTimeMillis(); //设置上次删除的时间

/*                myOrders.remove(position);
                notifyItemRemoved(position);*/

/*                if (myOrders.isEmpty()) {

                } else {
                    if (position > 0) {
                        Log.d(TAG, String.valueOf(mLayoutMgr.findFirstVisibleItemPosition()));
                        recyclerView.getChildAt(position - 1 - mLayoutMgr.findFirstVisibleItemPosition()).requestFocus();
                    } else if (position == 0) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.getChildAt(position).requestFocus();
                            }
                        }, 200);
                    }
                }*/
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(TAG, "click cancel");

                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
            }
        });

        cancel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (window != null && window.isShowing()) {
                        window.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

        delete.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (window != null && window.isShowing()) {
                        window.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow dismiss");
            }
        });


    }

    public interface OnFocusItemListener {
        void onFocusChange(View view, boolean b, int position);
    }
}

