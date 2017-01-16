package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

/**
 * Created by Administrator on 2017/1/12.
 */

public interface IModelCategory {
    void downloadData(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
    void downloadData(Context context, int parentId, OnCompleteListener<CategoryChildBean[]> listener);
}
