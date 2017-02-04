package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/1/18.
 */

public interface IModelUser {
    void login(Context context, String username, String password, OnCompleteListener<String> listener);

    void register(Context context, String username, String usernick, String password, OnCompleteListener<String> listener);

    void updateNick(Context context, String username, String usernick, OnCompleteListener<String> listener);

    void updateAvatar(Context context, String nameOrhxid, File file, OnCompleteListener<String> listener);

    void getCollectCount(Context context, String username, OnCompleteListener<MessageBean> listener);

    void findCollects(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);
}
