package com.konka.kktripclient.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.haizhi.SDK.SDKClient;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class BigDataHelper {
    private final String TAG = getClass().getSimpleName();
    private static BigDataHelper mInstance;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.FIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

    /**
     * 队列类型，FIFO--先进先出，LIFO--后进先出
     */
    private enum Type {
        FIFO, LIFO
    }

    /**
     * 单例模式
     */
    private BigDataHelper(int threadCount, Type type) {
        init(threadCount, type);
    }

    /**
     * 获得单实例
     */
    public static BigDataHelper getInstance() {
        if (mInstance == null) {
            synchronized (BigDataHelper.class) {
                if (mInstance == null) {
                    mInstance = new BigDataHelper(DEFAULT_THREAD_COUNT, Type.FIFO);
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化操作，包括后台轮询和创建线程池
     */
    private void init(int threadCount, Type type) {
        initBackThread();
        // 创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 初始化后台轮询线程
     */
    private void initBackThread() {
        // 后台轮询线程
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池去取出一个任务进行执行
                        Runnable command = getTask();
                        if (null != command) {
                            mThreadPool.execute(command);
                        }
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        // 开启线程
        mPoolThread.start();
    }

    /**
     * 从任务队列取出一个方法
     */
    private Runnable getTask() {
        try {
            if (mType == Type.FIFO) {
                return mTaskQueue.pollFirst();
            } else if (mType == Type.LIFO) {
                return mTaskQueue.pollLast();
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加一个任务
     */
    private synchronized void addTask(Runnable runnable) {
        try {
            mTaskQueue.add(runnable);
            if (mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mPoolThreadHandler != null)
            mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 上传路线订单数量
     * order_time(订单时间:2017-04-28 14:59:24), order_id(订单号),
     * route_id(路线ID), route_name(路线名称), route_type(类型:路线/门票), route_price(价格),
     * start_place(出发地), order_success(是否支付成功:0/1), order_source(订单来源:收藏页/列表页/海报位/订单页); 订单确认页操作
     *
     * @param order_time    订单时间 2017-04-28 14:59:24
     * @param order_id      订单号
     * @param route_id      路线ID
     * @param route_name    路线名称
     * @param route_type    类型:路线/门票
     * @param route_price   价格
     * @param start_place   出发地
     * @param order_success 是否支付成功:0,1
     * @param order_source  订单来源:收藏页/列表页/海报位/订单页
     */
    public synchronized void sendRouteUserOrder(final String order_time, final String order_id, final String route_id,
                                                final String route_name, final String route_type, final String route_price,
                                                final String start_place, final String order_success, final String order_source) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (order_time == null || order_id == null || route_id == null
                        || route_name == null || route_type == null || route_price == null
                        || start_place == null || order_success == null || order_source == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_ROUTE_USER_ORDER + ": order_time=" + order_time + " order_id=" + order_id + " route_id=" + route_id
                            + " route_name=" + route_name + " route_type=" + route_type + " route_price=" + route_price
                            + " start_place=" + start_place + " order_success=" + order_success + " order_source=" + order_source);
                    Map<String, String> map = new HashMap<>();
                    map.put("order_time", order_time);
                    map.put("order_id", order_id);
                    map.put("route_id", route_id);
                    map.put("route_name", route_name);
                    map.put("route_type", route_type);
                    map.put("route_price", route_price);
                    map.put("start_place", start_place);
                    map.put("order_success", order_success);
                    map.put("order_source", order_source);
                    SDKClient.sentLog(Constant.BIG_DATA_ROUTE_USER_ORDER, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传广告位置点击量
     * enter_time(点击时间), duration(停留时间), source(启动页/推荐位/弹窗), title(广告名称)
     *
     * @param enter_time 点击时间
     * @param duration   停留时间
     * @param source     启动页/推荐位/弹窗
     * @param title      广告名称
     */
    public synchronized void sendRecommendClick(final String enter_time, final String duration, final String source, final String title) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || duration == null || source == null || title == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_RECOMMEND_CLICK + ": enter_time=" + enter_time
                            + " duration=" + duration + " source=" + source + " title=" + title);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("duration", duration);
                    map.put("source", source);
                    map.put("title", title);
                    SDKClient.sentLog(Constant.BIG_DATA_RECOMMEND_CLICK, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传APP累计用户量(启动apk时批量上传方式上报)
     * enter_time(2017-04-28 14:59:24), mac(1F:7E:89), duration(使用时长); 启动apk时批量上传方式上报
     *
     * @param enter_time 启动时间 2017-04-28 14:59:24
     * @param mac        MAC地址
     * @param duration   使用时长
     */
    public synchronized void sendRouteUserInfo(final String enter_time, final String mac, final String duration) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || mac == null || duration == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_ROUTE_USER_INFO + ": enter_time=" + enter_time + " mac=" + mac + " duration=" + duration);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("mac", mac);
                    map.put("duration", duration);
                    SDKClient.sentLog(Constant.BIG_DATA_ROUTE_USER_INFO, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传商品关注热度(进入详情页时触发)
     * enter_time(点击时间), goods_name(路线/门票名称), type(类型:路线/门票),
     * duration(停留时间), price(价格), source(来源); 进入详情页时触发
     *
     * @param enter_time 点击时间
     * @param goods_name 路线/门票名称
     * @param type       类型:路线/门票
     * @param duration   停留时间
     * @param price      价格
     * @param source     来源
     */
    public synchronized void sendEnterGoodsDetail(final String enter_time, final String goods_name, final String type,
                                                  final String duration, final String price, final String source) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || goods_name == null || type == null || duration == null || price == null || source == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_ENTER_GOODS_DETAIL + ": enter_time=" + enter_time + " goods_name=" + goods_name
                            + " type=" + type + " duration=" + duration + " price=" + price + " source=" + source);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("goods_name", goods_name);
                    map.put("type", type);
                    map.put("duration", duration);
                    map.put("price", price);
                    map.put("source", source);
                    SDKClient.sentLog(Constant.BIG_DATA_ENTER_GOODS_DETAIL, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传视频内容播放次数、播放人次
     * enter_time(点击时间), duration(播放时长), video_name(视频名称), route_name(路线名称)
     *
     * @param enter_time 点击时间
     * @param duration   播放时长
     * @param video_name 视频名称
     * @param route_name 路线名称
     */
    public synchronized void sendPlayVideo(final String enter_time, final String duration, final String video_name, final String route_name) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || duration == null || video_name == null || route_name == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_PLAY_VIDEO + ": enter_time=" + enter_time
                            + " duration=" + duration + " video_name=" + video_name + " route_name=" + route_name);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("duration", duration);
                    map.put("video_name", video_name);
                    map.put("route_name", route_name);
                    SDKClient.sentLog(Constant.BIG_DATA_PLAY_VIDEO, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传视频查看详情点击量、点击人次
     * enter_time(点击时间), video_name(视频名称), source(来源渠道:首页配置/查看列表/弹窗推荐)
     *
     * @param enter_time 点击时间
     * @param video_name 视频名称
     * @param source     来源渠道:首页配置/查看列表/弹窗推荐
     */
    public synchronized void sendVideoMenu(final String enter_time, final String video_name, final String source) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || video_name == null || source == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_VIDEO_MENU + ": enter_time=" + enter_time + " video_name=" + video_name + " source=" + source);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("video_name", video_name);
                    map.put("source", source);
                    SDKClient.sentLog(Constant.BIG_DATA_VIDEO_MENU, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传路线/门票详情页面马上浪点击量、点击人次(商品详情页的操作)
     * enter_time(点击时间), goods_name(海报名称), route_type(类型:路线/门票), button_name(点击按钮:马上浪); 商品详情页的操作
     *
     * @param enter_time  点击时间
     * @param goods_name  海报名称
     * @param route_type  类型:路线/门票
     * @param button_name 点击按钮:马上浪
     */
    public synchronized void sendDetailButtonClick(final String enter_time, final String goods_name, final String route_type, final String button_name) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || goods_name == null || button_name == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_DETAIL_BUTTON_CLICK + ": enter_time=" + enter_time
                            + " goods_name=" + goods_name + " route_type=" + route_type + " button_name=" + button_name);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("goods_name", goods_name);
                    map.put("route_type", route_type);
                    map.put("button_name", button_name);
                    SDKClient.sentLog(Constant.BIG_DATA_DETAIL_BUTTON_CLICK, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传订单信息填写页面点击量、点击人次、提交次数、取消次数
     * enter_time(点击时间), duration(停留时间), button_name(操作结果:确认提交/取消),
     * goods_name(商品名称), price(价格), start_place(出发地)
     *
     * @param enter_time  触发时间 2017-04-28 14:59:24
     * @param duration    停留时间
     * @param button_name 操作结果:确认提交/取消
     * @param goods_name  商品名称
     * @param price       价格
     * @param start_place 出发地
     */
    public synchronized void sendFillInOrderInfo(final String enter_time, final String duration, final String button_name,
                                                 final String goods_name, final String price, final String start_place) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (enter_time == null || duration == null || button_name == null || goods_name == null || price == null || start_place == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_FILL_IN_ORDER_INFO + ": enter_time=" + enter_time + " duration=" + duration
                            + " button_name=" + button_name + " goods_name=" + goods_name + " price=" + price + " start_place=" + start_place);
                    Map<String, String> map = new HashMap<>();
                    map.put("enter_time", enter_time);
                    map.put("duration", duration);
                    map.put("button_name", button_name);
                    map.put("goods_name", goods_name);
                    map.put("price", price);
                    map.put("start_place", start_place);
                    SDKClient.sentLog(Constant.BIG_DATA_FILL_IN_ORDER_INFO, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传付款前用户登录次数、人次(请求登录用户中心时触发)
     * login_time(登录时间), source(从哪个页面跳转到登录页的:启动页、付款页等), result(是否登录成功); 请求登录用户中心时触发
     *
     * @param login_time 登录时间 2017-04-28 14:59:24
     * @param source     从哪个页面跳转到登录页的:启动页、付款页等
     * @param result     是否登录成功
     */
    public synchronized void sendLoginUserCenter(final String login_time, final String source, final String result) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (login_time == null || source == null || result == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_LOGIN_USER_CENTER + ": login_time=" + login_time + " source=" + source + " result=" + result);
                    Map<String, String> map = new HashMap<>();
                    map.put("login_time", login_time);
                    map.put("source", source);
                    map.put("result", result);
                    SDKClient.sentLog(Constant.BIG_DATA_LOGIN_USER_CENTER, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传报错的次数和报错内容
     * error_time(发生时间), error_code(错误码), error_detail(错误信息)
     *
     * @param error_time   发生时间 2017-04-28 14:59:24
     * @param error_code   错误码
     * @param error_detail 错误信息
     */
    public synchronized void sendErrorEvent(final String error_time, final String error_code, final String error_detail) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (error_time == null || error_code == null || error_detail == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_ERROR_EVENT + ": error_time=" + error_time + " error_code=" + error_code + " error_detail=" + error_detail);
                    Map<String, String> map = new HashMap<>();
                    map.put("error_time", error_time);
                    map.put("error_code", error_code);
                    map.put("error_detail", error_detail);
                    SDKClient.sentLog(Constant.BIG_DATA_ERROR_EVENT, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

    /**
     * 上传收藏页面的收藏内容
     * collect_time(收藏时间), goods_name(商品名称), price(商品价格), route_type(商品类型)
     *
     * @param collect_time 收藏时间 2017-04-28 14:59:24
     * @param goods_name   商品名称
     * @param price        商品价格
     * @param route_type   商品类型
     */
    public synchronized void sendCollectGoods(final String collect_time, final String goods_name, final String price, final String route_type) {
        if (Constant.MONKEY)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (collect_time == null || goods_name == null || price == null || route_type == null) {
                    LogUtils.i(TAG, "data is null, stop send");
                } else {
                    LogUtils.i(TAG, Constant.BIG_DATA_COLLECT_GOODS + ": collect_time=" + collect_time + " goods_name=" + goods_name + " price=" + price + " route_type=" + route_type);
                    Map<String, String> map = new HashMap<>();
                    map.put("collect_time", collect_time);
                    map.put("goods_name", goods_name);
                    map.put("price", price);
                    map.put("route_type", route_type);
                    SDKClient.sentLog(Constant.BIG_DATA_COLLECT_GOODS, map);
                }
                mSemaphoreThreadPool.release();
            }
        };
        addTask(runnable);
    }

}
