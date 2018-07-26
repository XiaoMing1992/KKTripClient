package com.konka.kktripclient.detail.interfaces;

/**
 * Created by Zhou Weilin on 2017-6-10.
 */

public interface IProcessData extends IBasePresenter{
    void detachView();
    void getDataById(int type,int id);
}
