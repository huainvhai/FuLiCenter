package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;

/**
 * Created by Administrator on 2017/1/12.
 */

public interface IModelBoutique {
    void downloadData(Context context, OnCompleteListener<BoutiqueBean[]> listener);
}
