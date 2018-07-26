package com.konka.kktripclient.layout.data;

import android.content.Intent;

/**
 * 某个tab具体内容的数据类型
 *
 * @author The_one
 */
public class KKTabItemDataInfo extends KKBaseDataInfo {
    /**
     * 归属的tab
     */
    public String tabName;

    /**
     * 广告位圆角,单位：度数 默认为 0.会覆盖全局的圆角度数
     */
    public int filletDegree;
    /**
     * theme_tab_adver_layout主键
     */
    public int layoutId;
    /**
     * 广告位高
     */
    public int height;
    /**
     * 广告位宽
     */
    public int width;
    /**
     * 广告位X坐标 单位：像素
     */
    public int x;
    /**
     * 广告位Y坐标 单位：像素
     */
    public int y;

    /**
     * theme_adver_content主键
     */
    public int contentId;
    /**
     * 标题格式--- 0:图内左对其 1:图内居中 2:图内右对齐 3:图下方左对齐 4:图下方居中 5:图下方右对齐 默认:0
     */
    public int align;
    /**
     * 选中放大比例 0.01 两位小数表示百分比
     */
    public double enlargeScale;
    /**
     * 推荐位名称
     */
    public String firstTitle;
    public String secondTitle;
    /**
     * 是否有焦点效果 0：有焦点 1：无焦点 默认 0
     */
    public int isFocus;
    /**
     * 是否显示名字，0:不显示   1:显示
     */
    public int isShowTitle;
    /**
     * 推荐类型，00：APK拉起，41：收藏，42：路线列表, 43：路线详情，44：门票列表，45：门票详情，46：视频，47：视频小窗口
     */
    public String type;
    /**
     * 海报图层一(底图)
     */
    public String posterBottom;
    /**
     * 海报图层一(底图)MD5
     */
    public String posterBottomMd5;
    /**
     * 海报图层二
     */
    public String posterMiddle;
    /**
     * 海报图层二MD5
     */
    public String posterMiddleMd5;
    /**
     * 海报图层三(顶图)
     */
    public String posterTop;
    /**
     * 海报图层三(顶图)MD5
     */
    public String posterTopMd5;
    /**
     * 状态 0：停用 1：启用 默认 1
     */
    public int state;

    /**
     * 启动方式，0-activity，1-broadcast
     */
    public String startType;
    /**
     * 启动的intent
     */
    public Intent startIntent;

    public int goodsID;
    public String goodsName;

    public String typeID;
    public String typeName;
}
