package com.konka.kktripclient.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2017-12-13.
 */

public class UmengAnalytic {
    /**
     event id ：自定义事件id
     key ：自定义事件下的参数
     value ：自定义事件参数下的参数值
     */

    //=========计数事件, 使用计数事件需要在后台添加事件时选择“计数事件”=========

    /**
     * 统计发生次数
     * @param context，指当前的Activity
     * @param eventId， 为当前统计的事件ID
     */
    public static void count(Context context, String eventId){
        MobclickAgent.onEvent(context, eventId);
    }

    /**
     * 统计点击行为各属性被触发的次数
     * @param context
     * @param eventId
     * @param map， 为当前事件的属性和取值（Key-Value键值对）
     */
    public static void count(Context context, String eventId, Map<String,String> map){
        MobclickAgent.onEvent(context, eventId, map);
    }

    //=========计算事件，使用计算事件需要在后台添加事件时选择“计算事件”=========
    /**
     * 统计数值型变量的值的分布
     * @param context
     * @param id 为事件ID
     * @param map 为当前事件的属性和取值
     * @param du 当前事件的数值，取值范围是-2,147,483,648 到 +2,147,483,647 之间的有符号整数，
     *           即int 32类型，如果数据超出了该范围，会造成数据丢包，影响数据统计的准确性
     */
    public static void count(Context context, String id, Map<String,String> map, int du){
        MobclickAgent.onEventValue(context, id, map, du);
    }


}
