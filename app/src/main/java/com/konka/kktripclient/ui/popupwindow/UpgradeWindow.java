package com.konka.kktripclient.ui.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.bean.ProgressBean;
import com.konka.kktripclient.bean.UpgradeDetailDataBean;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.CommonUtils;
/*import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;*/

import java.io.File;
import java.util.List;

/**
 * Created by HP on 2017-5-25.
 */

public class UpgradeWindow extends PopupWindow implements View.OnClickListener{
    private final String TAG = "UpgradeWindow";

    private Context mContext;
    private View mView;
    private Button upgrade_button;
    private TextView upgrade_content;
    private UpgradeDetailDataBean data;
    private List<UpgradeDetailDataBean> dataList;
    private ProgressBean progressBean;

    private ProgressBar progressBar;
    //private FileDownloadListener listener;

    public UpgradeWindow(Context context){
        initView(context);
        progressBean = new ProgressBean();
    }

    private void initView(Context context){
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.upgrade_window_view, null);
        setContentView(mView);
        upgrade_button = (Button)mView.findViewById(R.id.upgrade_button);
        upgrade_content = (TextView)mView.findViewById(R.id.upgrade_content);
        progressBar = (ProgressBar)mView.findViewById(R.id.progress);
        progressBar.setProgress(progressBar.getProgress() * 100);
        progressBar.setSecondaryProgress(progressBar.getSecondaryProgress() * 100);

        CommonUtils.computeScreenSize(context);
        LogUtils.d(TAG, "ScreenWidth = "+CommonUtils.getScreenWidth()+" ScreenHeight = "+CommonUtils.getScreenHeight());
        setWidth(CommonUtils.getScreenWidth());
        setHeight(CommonUtils.getScreenHeight());
        upgrade_button.setOnClickListener(this);
        setFocusable(true);
        initListener();



    }

    private void initListener(){
        mView.setFocusable(true);
        mView.setFocusableInTouchMode(true);
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if (UpgradeWindow.this.isShowing())
                        UpgradeWindow.this.dismiss();
                    return true;
                }else if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN){ //升级
/*                    if (UpgradeWindow.this.isShowing())
                        UpgradeWindow.this.dismiss();*/
                    download();
                    return true;
                }
                return false;
            }
        });
    }

    public void show() {
        try{
            showAtLocation(mView, Gravity.CENTER, 0, 0);
            //showLoading();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upgrade_button:
               // download();
/*                if (UpgradeWindow.this.isShowing())
                    UpgradeWindow.this.dismiss();*/
                break;


        }
    }

    private void download(){
        LogUtils.d(TAG, "download");
        //if (dataList != null && dataList.size()!=0){
            //data = dataList.get(0);
            //if (data.getVersion_code() > CommonUtils.getVersionCode(mContext)){
                String apkUrl = "http://ftp-apk.pconline.com.cn/a2318ae3f4ce2e39aac1bd605095dac8/pub/download/201010/pconline1493186630135.apk";
                final String path = CommonUtils.getApkDownloadPath(mContext);
                LogUtils.d(TAG, " path = "+path);

                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
        /*listener = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                LogUtil.d(TAG, " pending ");
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                int progress = soFarBytes * 100 / totalBytes;
                progressBean.setProgress(progress);
                //EventBus.getDefault().post(progressBean);
                progressBar.setProgress(progress);
                LogUtil.d(TAG, "download progress "+ progress);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                progressBean.setCompleted(true);
                // EventBus.getDefault().post(progressBean);
                //PreferencesHelper.saveInt(PreferencesHelper.DOWNLOADED_VERSION, mNewestVersion);
                CommonUtils.installApk(mContext, path);
                LogUtil.d(TAG, "download completed ");
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                LogUtil.d(TAG, "download paused ");
                int progress = soFarBytes * 100 / totalBytes;
                progressBar.setProgress(progress);
            }

            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                LogUtil.d(TAG, "retryingTimes = "+retryingTimes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                LogUtil.d(TAG, "FileDownloader error: " + e.toString());
                //Trace.Debug("FileDownloader error: " + e.toString());
                progressBean.setError(true);
                progressBean.setErrorType(ProgressBean.ERROR_DOWNLOAD);
                //EventBus.getDefault().post(progressBean);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                LogUtil.d(TAG, " warn ");
            }
        };

                FileDownloader.getImpl().create(apkUrl)
                        .setPath(path)
                        .setListener(listener).start();
                        */
            }
       // }
    //}
}
