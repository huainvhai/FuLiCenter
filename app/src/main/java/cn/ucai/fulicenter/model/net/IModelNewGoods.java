package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/1/11.
 */

public interface IModelNewGoods {
    void downloadData(Context context, int catId, int pageId, OnCompleteListener<NewGoodsBean[]> listener);
}
