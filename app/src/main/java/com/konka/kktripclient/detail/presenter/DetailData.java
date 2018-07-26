package com.konka.kktripclient.detail.presenter;

import android.content.Context;

import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.interfaces.IDetailView;
import com.konka.kktripclient.detail.interfaces.IProcessData;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.RouteDetailsByID;
import com.konka.kktripclient.net.info.TicketsBean;
import com.konka.kktripclient.net.info.TicketsDetailsByID;
import com.konka.kktripclient.net.info.TourRoutesBean;
import com.konka.kktripclient.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

/**
 * Created by Zhou Weilin on 2017-6-10.
 */

public class DetailData implements IProcessData {
    private final String TAG = DetailData.class.getSimpleName();
    private WeakReference<IDetailView> mIDetailView;
    private Context mContext;


    public DetailData(Context context, IDetailView view){
        mContext = context.getApplicationContext();//得获取application的context,不然HttpHelper会导致内存泄漏
        mIDetailView = new WeakReference<IDetailView>(view);//界面创建，与界面取得联系
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    /**
     * 界面销毁，Presenter与界面断开联系
     */
    @Override
    public void detachView() {
        if (mIDetailView != null) {
            mIDetailView.clear();
            mIDetailView = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(IEvent event) {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->isViewAttached():"+isViewAttached()+"===event:"+event.getClass().getSimpleName());
        if(isViewAttached()){
            if (event instanceof HttpErrorEvent) {
                String type = ((HttpErrorEvent) event).getReq_type();
                String msg = ((HttpErrorEvent) event).getRetMsg();
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->HttpErrorEvent-->type:"+type+" msg:"+msg);
                if("RouteDetailsByID".equals(type) || "TicketsDetailsByID".equals(type)
                        || "TourRoutesBean".equals(type) || "TicketsBean".equals(type)){//解决其他地方出错，这里也会接收到出错包显示出错问题
                    mIDetailView.get().onError(msg);
                }


            }
            if (event instanceof RouteDetailsByID) {
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->RouteDetailsByID:"+((RouteDetailsByID) event).toString());
                TourRoutesBean tourRoutesBean = ((RouteDetailsByID) event).getData();
                mIDetailView.get().onUpdateRoute(tourRoutesBean);

            }
            if (event instanceof TicketsDetailsByID) {
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->TicketsDetailsByID:"+((TicketsDetailsByID) event).toString());
                TicketsBean ticketBean = ((TicketsDetailsByID) event).getData();
                mIDetailView.get().onUpdateTicket(ticketBean);

            }
            if (event instanceof TourRoutesBean) {
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->TourRoutesBean:"+((TourRoutesBean) event).toString());
                mIDetailView.get().onUpdateRoute(event);

            }
            if (event instanceof TicketsBean) {
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->TicketsBean:"+((TicketsBean) event).toString());
                mIDetailView.get().onUpdateTicket(event);

            }
        }

    }

    //判断界面是否销毁
    private boolean isViewAttached() {
        return mIDetailView != null && mIDetailView.get() != null;
    }

    @Override
    public void getDataById(int type,int id) {
        if(isViewAttached()&&type!=-1&&id!=-1){
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->getDataById-->type:"+type+"===id:"+id);
            if(type== DetailConstant.GOOD_ROUTE){
                HttpHelper.getInstance(mContext).getRouteDetailsByID(id);
            }else if(type==DetailConstant.GOOD_TICKET){
                HttpHelper.getInstance(mContext).getTicketsDetailsByID(id);
            }

        }else {
            mIDetailView.get().onError(mContext.getString(R.string.detail_data_error));
        }

    }
}
