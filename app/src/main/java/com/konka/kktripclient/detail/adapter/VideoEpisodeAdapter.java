package com.konka.kktripclient.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.presenter.WindowPlayer;
import com.konka.kktripclient.layout.view.GlideRoundTransform;
import com.konka.kktripclient.net.info.AllVideosEvent;
import com.konka.kktripclient.net.info.AllVideosEvent.DataBean.VideosBean;
import com.konka.kktripclient.ui.ScaleRelativeLayout;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhou Weilin on 2017-6-25.
 */

public class VideoEpisodeAdapter extends RecyclerView.Adapter<VideoEpisodeAdapter.VideoPlayerHolder>{
    private final String TAG = VideoEpisodeAdapter.class.getSimpleName();
    private Context mContext;
    private List<AllVideosEvent.DataBean.VideosBean> videoItemInfos = new ArrayList<>();
    private ItemListener mItemListener;
    private View mFocusedView;
    private GlideRoundTransform mRadiusTransform;

    public VideoEpisodeAdapter(Context context){
        this.mContext = context;
    }

    public void setVideoItemInfos(List<VideosBean> videoItemInfos) {
        this.videoItemInfos = videoItemInfos;
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->setVideoItemInfos-->videoItemInfos:"+videoItemInfos.size());
    }



    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @Override
    public VideoPlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoPlayerHolder holder = new VideoPlayerHolder(LayoutInflater.from(mContext).inflate(R.layout.video_episode_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoPlayerHolder holder, final int position) {
        if(videoItemInfos == null || videoItemInfos.size() == 0){
            return;
        }

        if(position >= videoItemInfos.size()){
            return;
        }
        holder.itemView.setTag(position);
        holder.itemName.setText(videoItemInfos.get(position).getName());
        loadImage(holder.itemIcon,videoItemInfos.get(position));
        holder.itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    holder.itemBottomBg.setSelected(true);
                    holder.itemName.setSelected(true);
                    holder.curIcon.setSelected(true);
                }else{
                    holder.itemBottomBg.setSelected(false);
                    holder.itemName.setSelected(false);
                    holder.curIcon.setSelected(false);
                }
            }
        });
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemListener != null){
                    if (videoItemInfos.size() > position) {
                        mItemListener.itemClick(videoItemInfos.get(position),position);
                    }

                }
            }
        });
        holder.itemLayout.setFocusable(true);
        setCurIcon(position,holder.curIcon);

    }

    @Override
    public int getItemCount() {
        if(videoItemInfos.size() != 0){
            return videoItemInfos.size();
        }
        return 0;
    }


    public class VideoPlayerHolder extends RecyclerView.ViewHolder {

        public ScaleRelativeLayout itemLayout;
        private RelativeLayout itemBottomBg;
        private RoundedImageView itemIcon;
        private TextView itemName;
        private ImageView curIcon;

        public VideoPlayerHolder(View itemView) {
            super(itemView);

            itemLayout = (ScaleRelativeLayout) itemView.findViewById(R.id.video_episode_relative_item);
            itemBottomBg = (RelativeLayout) itemView.findViewById(R.id.video_episode_relative_icon);
            itemIcon = (RoundedImageView) itemView.findViewById(R.id.video_episode_img_icon);
            itemName = (TextView) itemView.findViewById(R.id.video_episode_txt_item);
            curIcon = (ImageView) itemView.findViewById(R.id.video_episode_cur_icon);
        }
    }

    public interface ItemListener{
        void itemClick(VideosBean info, int pos);
    }

    private void loadImage(RoundedImageView imageView, VideosBean v) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        String url = v.getThumbnail();
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->loadImage-->url:"+url);
        if(mRadiusTransform==null){
            mRadiusTransform = new GlideRoundTransform(mContext, CommonUtils.dip2px(mContext,4));
        }
        Glide.with(mContext)
                .load(NetworkUtils.toURL(url))
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .transform(mRadiusTransform)
                .skipMemoryCache(true)
                .dontAnimate()
                .override(CommonUtils.dip2px(mContext,260), CommonUtils.dip2px(mContext,146))
                .into(imageView);
    }

    //设置需要获取焦点view，并设置该view获取焦点
    public void viewRequestFocus(View focusedView){
        if(focusedView != null){
            mFocusedView = focusedView;
        }
        if(mFocusedView != null){
            mFocusedView.requestFocus();
        }
    }

    //设置当前播放的标示
    private void setCurIcon(int position,ImageView imageView){
        if(position== WindowPlayer.getInstance().getCurPlayPos()){
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
