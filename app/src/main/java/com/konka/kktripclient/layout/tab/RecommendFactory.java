package com.konka.kktripclient.layout.tab;

import android.view.View;

import com.konka.kktripclient.layout.data.KKBaseDataInfo;
import com.konka.kktripclient.layout.data.KKServerDataListener;
import com.konka.kktripclient.layout.data.KKServerDataManager;
import com.konka.kktripclient.layout.data.KKTabButtonDataInfo;
import com.konka.kktripclient.layout.data.KKTabDataInfo;
import com.konka.kktripclient.layout.data.KKTabItemDataInfo;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.layout.util.CommonFunction;
import com.konka.kktripclient.layout.track.TrackBaseActivity;
import com.konka.kktripclient.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecommendFactory {
    private static TrackBaseActivity mActivity;
    private static RecommendFactory instance;

    //页面缓存
    private List<RecommendTabButton> recommendTabs = new ArrayList<>();
    private List<RecommendPage> recommendPages = new ArrayList<>();

    private KKTabButtonDataInfo tabButtonDataInfo = new KKTabButtonDataInfo();
    private List<KKTabDataInfo> allTabsDataList = new ArrayList<>();
    private List<KKTabItemDataInfo> allTabsItemDataList = new ArrayList<>();

    private List<String> addTabsTitle = new ArrayList<>();

    private RecommendFactory(final TrackBaseActivity activity) {
        mActivity = activity;
    }

    public static synchronized RecommendFactory getInstance() {
        if (instance != null)
            return instance;
        else {
            mActivity = ActivityHandler.getInstance();
            instance = new RecommendFactory(mActivity);
            return instance;
        }
    }

//	public List<KKTabDataInfo> getAllTabsDatas() {
//		return allTabsDataList;
//	}

    private void createRecommend(KKTabDataInfo data, boolean first) {
        recommendTabs.add(new RecommendTabButton(data.menuItemName, data.tabBackground, first, false));
        recommendPages.add(new RecommendPage(data));
    }

    private void DrawRecommendPage(final String title) {
        final RecommendPage mRecommendPage = getRecommendPageByTitle(title);
        mRecommendPage.setTabItemDataInfoList(getTabItemByCache(title));
    }

    private List<KKTabItemDataInfo> getTabItemByCache(String tabName) {
        List<KKTabItemDataInfo> lists = new ArrayList<>();
        for (KKTabItemDataInfo data : allTabsItemDataList) {
            if (tabName.equals(data.tabName))
                lists.add(data);
        }
        return lists;
    }

//	public KKTabItemDataInfo getTabItemByTabData(KKTabDataInfo info) {
//		for (KKTabItemDataInfo allTabsItemData : allTabsItemDataList) {
//			allTabsItemData.title.equals(info.title);
//			return allTabsItemData;
//		}
//		return null;
//	}

    public KKTabDataInfo getTabByCache(String title) {
        for (KKTabDataInfo data : allTabsDataList) {
            if (data.menuItemName.equals(title))
                return data;
        }
        return null;
    }

    public RecommendPage getRecommendPageByTitle(String title) {
        for (RecommendPage data : recommendPages) {
            if (data.getTitle().equals(title))
                return data;
        }
        return null;
    }

    public RecommendTabButton getRecommendTabByTitle(String title) {
        for (RecommendTabButton data : recommendTabs) {
            if (data.getTitle().equals(title))
                return data;
        }
        return null;
    }

//	public boolean isTabInList(KKTabDataInfo data, ArrayList<KKBaseDataInfo> newtablist) {
//		for (KKBaseDataInfo info : newtablist) {
//			if (((KKTabDataInfo) info).title.equals(data.title))
//				return true;
//		}
//		return false;
//	}

    private boolean isTabInList(KKTabDataInfo data, List<KKTabDataInfo> tabList) {
        for (KKTabDataInfo info : tabList) {
            if (info.menuItemName.equals(data.menuItemName))
                return true;
        }
        return false;
    }

//	public boolean isTabInList(String title, List<KKTabDataInfo> tablist) {
//		for (KKTabDataInfo info : tablist) {
//			if (info.title.equals(title))
//				return true;
//		}
//		return false;
//	}

//	public boolean isTabInList(String title, ArrayList<Tab> tablist) {
//		for (Tab info : tablist) {
//			if (info.getTabName().equals(title))
//				return true;
//		}
//		return false;
//	}

//	public void removeTabsItem(String title) {
//		for (int i = 0; i < allTabsItemDataList.size(); i++) {
//			KKTabItemDataInfo tab = allTabsItemDataList.get(i);
//			if (tab.tabName.equals(title)) {
//				allTabsItemDataList.remove(i);
//				i--;
//			}
//		}
//	}

//	public void removeRecommendPage(String title) {
//		for (int i = 0; i < recommendPages.size(); i++) {
//			RecommendPage tab = recommendPages.get(i);
//			if (tab.getTitle().equals(title)) {
//				recommendPages.remove(i);
//				i--;
//			}
//		}
//	}

    //	public void removeRecommendTab(String title) {
//		for (int i = 0; i < recommendTabs.size(); i++) {
//			RecommendTabButton tab = recommendTabs.get(i);
//			if (tab.getTitle().equals(title)) {
//				recommendTabs.remove(i);
//				i--;
//			}
//		}
//	}

    /**
     * 通过RecommendFactory调用TabGroup小窗口onResume
     */
    public void onResumeVideoWidget() {
        TabGroup.sInstance.onResumeVideoWidget();
    }

    /**
     * 通过RecommendFactory调用TabGroup小窗口onPause
     */
    public void onPauseVideoWidget() {
        TabGroup.sInstance.onPauseVideoWidget();
    }

    private static final Comparator<KKTabDataInfo> TAB_POSITION = new Comparator<KKTabDataInfo>() {
        @Override
        public int compare(KKTabDataInfo a, KKTabDataInfo b) {
            if (a.orderID < b.orderID)
                return 1;
            if (a.orderID > b.orderID)
                return -1;
            return 0;
        }
    };

    public void addByNetwork() {
        mActivity.setTrackVisibility(false);

        recommendTabs.clear();
        recommendPages.clear();
        TabGroup.sInstance.removeAllTab();

        addLoadPage();

        KKServerDataManager.getInstance(mActivity).getTabListDataCache(new KKServerDataListener<KKBaseDataInfo>() {
                    @Override
                    public void onLoadSuccess(final ArrayList<KKBaseDataInfo> data, KKBaseDataInfo kkBaseDataInfo) {
                        recommendTabs.clear();
                        recommendPages.clear();

                        tabButtonDataInfo = (KKTabButtonDataInfo) kkBaseDataInfo;
                        for (int i = 0; i < data.size(); i++) {
                            KKBaseDataInfo da = data.get(i);

                            // 预防后台推送相同tab
                            if (addTabsTitle.contains(((KKTabDataInfo) da).menuItemName.trim()))
                                continue;

                            addTabsTitle.add(((KKTabDataInfo) da).menuItemName);

                            allTabsDataList.add((KKTabDataInfo) da);

                            createRecommend(((KKTabDataInfo) da), i == 0);

                            getRecommendPageByTitle(((KKTabDataInfo) da).menuItemName).resetLayout((KKTabDataInfo) da);
                        }
                        modifyDesktopTabByNetwork();
                    }

                    @Override
                    public void onLoadStart() {
                        allTabsDataList.clear();
                        addTabsTitle.clear();
                        allTabsItemDataList.clear();
                    }

                    @Override
                    public void onLoadFail() {
                        addErrorPage();
                    }

                    @Override
                    public void onDataNotChange() {

                    }
                });
    }

    private void modifyDesktopTabByNetwork() {
        // 后台推荐到首页
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                TabGroup.sInstance.removeAllTab();

                if (allTabsDataList.size() <= 0) return;
                Collections.sort(allTabsDataList, TAB_POSITION);
                boolean drawVideoWidget = false;
                for (int i = 0; i < allTabsDataList.size(); i++) {
                    KKTabDataInfo tab = allTabsDataList.get(i);
                    TabGroup.sInstance.addTab(tab.menuItemName, getRecommendPageByTitle(tab.menuItemName), getRecommendTabByTitle(tab.menuItemName));
                    for (int j = 0; !drawVideoWidget && j < tab.adverList.size(); j++) {
                        if (Constant.LAUNCH_VIDEO_WIDGET.equals(((KKTabItemDataInfo) tab.adverList.get(j)).type)) {
                            TabGroup.sInstance.DrawVideoWidget((KKTabItemDataInfo) tab.adverList.get(j));
                            drawVideoWidget = true;
                            break;
                        }
                    }
                }

                MyFactory.getInstance().initView();
                MyFactory.getInstance().addTab();

                TabGroup.sInstance.setCurrentTab(allTabsDataList.get(0).menuItemName, false);
                TabGroup.sInstance.DrawTabButton(tabButtonDataInfo);

                final View focusView = TabGroup.sInstance.getTabButton(allTabsDataList.get(0).menuItemName).getView();
                if (focusView != null) {
                    focusView.post(new Runnable() {
                        @Override
                        public void run() {
                            focusView.setFocusableInTouchMode(true);
                            focusView.requestFocus();

                            mActivity.setTrackVisibility(true);
                        }
                    });
                }

                allTabsItemDataList.clear();
                for (KKTabDataInfo da : allTabsDataList)
                    getTabItemByCache(da);
            }
        });
    }

    private List<KKTabItemDataInfo> getTabItemByCache(final KKTabDataInfo tabData) {
        final ArrayList<KKBaseDataInfo> data = tabData.adverList;

        CommonFunction.getCommonSubHandler().post(new Runnable() {
            @Override
            public void run() {
                for (KKBaseDataInfo da : data) {
                    allTabsItemDataList.add((KKTabItemDataInfo) da);
                }
                if (isTabInList(tabData, allTabsDataList))
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DrawRecommendPage(tabData.menuItemName);
                        }
                    });
            }
        });

        return allTabsItemDataList;
    }

    private void addErrorPage() {
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            public void run() {
                recommendTabs.clear();
                recommendPages.clear();
                TabGroup.sInstance.removeAllTab();

                ErrorFactory.getInstance().initView();
                ErrorFactory.getInstance().addTab();
            }
        });
    }

    private void addLoadPage() {
        LoadFactory.getInstance().initView();
        LoadFactory.getInstance().addTab();

        TabGroup.sInstance.setCurrentTab(LoadFactory.getInstance().getTabName(), false);
    }

}
