package com.konka.kktripclient.detail.interfaces;

import com.konka.kktripclient.net.info.IEvent;

/**
 * Created by Zhou Weilin on 2017-6-10.
 */

public interface IDetailView extends IBaseView {
    void onError(String msg);
    void onUpdateRoute(IEvent event);
    void onUpdateTicket(IEvent event);
}
