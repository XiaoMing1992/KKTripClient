package com.konka.kktripclient.detail.customview;

import android.content.Context;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class NetWorkSpeedText extends TextView {
	private Context mContext;
	private boolean mShowNetWorkSpeed = true;
	private boolean mTaskShow = false;
	
	private Handler mSpeedHandler;
	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private NetWorkSpeedTask mNetWorkSpeedTask;
	
	public NetWorkSpeedText(Context context) {
        this(context, null);
    }

	public NetWorkSpeedText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public NetWorkSpeedText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	
	protected void onFinishInflate() {
		 super.onFinishInflate();
		 if(!mTaskShow){
			 mHandler = new Handler(){
					public void handleMessage(Message msg) {
						switch(msg.what){
						case 1:
							float bytes = ((Float)msg.obj).floatValue();
							setSpeedText(bytes);
							break;
						}
					}
				};
		 }		 
    }
	
	public void setVisibility(int visibility){
		super.setVisibility(visibility);
		if(visibility == View.VISIBLE){
			if(mTaskShow){
				if(mNetWorkSpeedTask == null || mNetWorkSpeedTask.getStatus() == AsyncTask.Status.FINISHED){
					mNetWorkSpeedTask = new NetWorkSpeedTask();
					mNetWorkSpeedTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
				}
			}else{
				show();
			}
		}else{
			if (mTaskShow) {
				if (mNetWorkSpeedTask != null) {
					mNetWorkSpeedTask.cancel(true);
					mNetWorkSpeedTask = null;
				}
			}else{
				hide();
			}		
		}
	}
	
	private void start(NetSpeed parama){
		if(mShowNetWorkSpeed){
			if(mSpeedHandler == null){
				mHandlerThread = new HandlerThread("network_speed_thread");
				mHandlerThread.start();
				mSpeedHandler = new Handler(mHandlerThread.getLooper()){
						public void handleMessage(Message msg) {
							switch(msg.what){
							case 123:
								NetSpeed  netspeed = (NetSpeed)msg.obj;
//								long lg1 = TrafficStats.getTotalRxBytes();
								long lg1 = getTotalReceivedBytes();
								float f1 = (float)(System.currentTimeMillis() - netspeed.itime) / 1000.0F;
								float f2 = (float)(lg1 - netspeed.ibyte) / f1 / 1024.0F;
								publishMessage(f2);
								show();
								break;
							}
						}
					};
			}
		}
		mSpeedHandler.removeMessages(123);
	    Message localMessage = mSpeedHandler.obtainMessage(123, parama);
	    mSpeedHandler.sendMessageDelayed(localMessage, 1000L);
	}
	
	public void show(){
		if(mShowNetWorkSpeed){
//			start(new NetSpeed(TrafficStats.getTotalRxBytes(), System.currentTimeMillis()));
			start(new NetSpeed(getTotalReceivedBytes(), System.currentTimeMillis()));
		}
	}
	
	public void hide(){
		if(mSpeedHandler != null){
			mSpeedHandler.removeCallbacksAndMessages(null);
			mHandler.removeCallbacksAndMessages(null);
		}
	}
	
	public void quit(){
		if(mTaskShow){
			if (mTaskShow) {
				if (mNetWorkSpeedTask != null) {
					mNetWorkSpeedTask.cancel(true);
					mNetWorkSpeedTask = null;
				}
			}
			return;
		}
		try {
			if (mSpeedHandler != null) {
				mSpeedHandler.removeCallbacksAndMessages(null);
				mSpeedHandler = null;
			}
			if (mHandlerThread != null) {
				mHandlerThread.quit();
				mHandlerThread = null;
			}
			if (mHandler != null) {
				mHandler.removeCallbacksAndMessages(null);
				mHandler = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class NetSpeed{
		long ibyte;
		long itime;
		public NetSpeed(long lg1, long lg2){
			ibyte = lg1;
			itime = lg2;
		}
	}
	
	public void publishMessage(float bytes) {
		if(mHandler != null){
			mHandler.removeMessages(1);
			Message localMessage = Message.obtain(mHandler, 1, Float.valueOf(bytes));
			mHandler.sendMessage(localMessage);
		}		
	}
	
	class NetWorkSpeedTask extends AsyncTask<Void, Float, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			while(true){
				if ((isCancelled()) || (!NetWorkSpeedText.this.isShown()))
			          return null;
				long pretime = System.currentTimeMillis();
//				long prebytes = TrafficStats.getTotalRxBytes();
				long prebytes = getTotalReceivedBytes();
				try {
					Thread.sleep(1000L);
					float time = (float)(System.currentTimeMillis() - pretime) / 1000.0F;
					if (time <= 0.0F)
			            continue;
//					long newbytes = TrafficStats.getTotalRxBytes();
					long newbytes = getTotalReceivedBytes();
					float bytes = (float)(newbytes - prebytes) / time / 1024.0F;
					publishProgress(bytes);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		protected void onProgressUpdate(Float... values) {
			float net = values[0].floatValue();
			setSpeedText(net);
	    }
	}
	
	private void setSpeedText(float net){
		String speed = null;
		if(net > 1000){
			net = net/1000;
			DecimalFormat decimalFormat=new DecimalFormat("####.#");
			speed = decimalFormat.format(net) + " MB/s";
		}else{
			DecimalFormat decimalFormat=new DecimalFormat("####");
			speed = decimalFormat.format(net) + " KB/s";
		}
		NetWorkSpeedText.this.setText(speed);
	}
	
	private long getTotalReceivedBytes() {
		String line="";
		long received = 0;
		long tmp = 0;
		boolean isNum = false;
		received= TrafficStats.getTotalRxBytes();
		if(received==0){	//解决有些平台用上述方式获取不了流量值		
			try {
				FileReader fr = new FileReader("/proc/net/dev");
				BufferedReader in = new BufferedReader(fr, 500);
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("rmnet") || line.startsWith("eth")
							|| line.startsWith("wlan")) {
						String[] segs = line.split(":")[1].split(" ");
						for (int i = 0; i < segs.length; i++) {
							isNum = true;
							try {
								tmp = Long.parseLong(segs[i]);
							} catch (Exception e) {
								isNum = false;
							}
							if (isNum == true) {
								received = received + tmp;
								break;
							}
						}
					}
				}
			} catch (IOException e) {
				return -1;
			}
		}
		return received;
	}
	
}
