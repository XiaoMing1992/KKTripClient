package com.konka.kktripclient.layout.tab.base;

import android.view.View;

/**
 * Created by YangDeZun on 2016/5/3.
 * <p>
 * Tab的Content基类
 */
public abstract class TabContent implements TabStateCallback {
    public abstract View getView();
}
