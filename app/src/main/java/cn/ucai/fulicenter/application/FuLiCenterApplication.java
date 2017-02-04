package cn.ucai.fulicenter.application;

import android.app.Application;

import java.util.HashMap;

import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/1/10.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    //在内存中保存数据
    private static User user;
    private static HashMap<Integer,CartBean> cartList = new HashMap<>();

    public static HashMap<Integer, CartBean> getCartList() {
        return cartList;
    }

    public static void setCartList(HashMap<Integer, CartBean> cartList) {
        FuLiCenterApplication.cartList = cartList;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }
}
