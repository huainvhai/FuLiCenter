package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModeGoods;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;
    IModelGoods mModel;
    IModelUser userModel;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    @BindView(R.id.ivBack2)
    ImageView ivBack2;

    boolean isCollect;
    @BindView(R.id.iv_good_collect)
    ImageView ivGoodCollect;
    GoodsDetailsBean goodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);

        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            MFGT.finish(this);
        } else {
            initData();
        }
    }


    private void initData() {
        mModel = new ModeGoods();
        mModel.downloadData(this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    goodsBean = result;
                    showGoodsDetail(result);
                } else {
                    MFGT.finish(GoodsDetailActivity.this);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showGoodsDetail(GoodsDetailsBean goods) {
        tvGoodName.setText(goods.getGoodsName());
        tvGoodNameEnglish.setText(goods.getGoodsEnglishName());
        tvGoodPriceCurrent.setText(goods.getCurrencyPrice());
        tvGoodPriceShop.setText(goods.getShopPrice());
        wvGoodBrief.loadDataWithBaseURL(null, goods.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
        salv.startPlayLoop(indicator, getAlbumUrl(goods), getAlbumCount(goods));
    }

    private int getAlbumCount(GoodsDetailsBean goods) {
        if (goods != null && goods.getProperties() != null && goods.getProperties().length > 0) {
            return goods.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumUrl(GoodsDetailsBean goods) {
        if (goods != null && goods.getProperties() != null && goods.getProperties().length > 0) {
            AlbumsBean[] albums = goods.getProperties()[0].getAlbums();
            if (albums.length > 0 && albums != null) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < urls.length; i++) {
                    urls[i] = albums[i].getImgUrl();
                }
                return urls;
            }
        }
        return new String[0];
    }

    @OnClick(R.id.ivBack2)
    public void onClickBack() {
        MFGT.finish(this);
    }

    @OnClick(R.id.iv_good_share)
    public void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(getString(R.string.ssdk_oks_share));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(goodsBean.getShareUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(goodsBean.getGoodsName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(goodsBean.getGoodsThumb());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(goodsBean.getShareUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(goodsBean.getGoodsName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(goodsBean.getShareUrl());

// 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCollectStatus();
    }

    private void setCollectStatus() {
        if (isCollect) {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.iv_good_collect)
    public void onClickCollect() {
        //首先判断是否登录
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            //点击图标改变收藏的状态
            setCollect(user);
        } else {
            MFGT.gotoLogin(this);
        }
    }

    private void setCollect(User user) {
        mModel.setCollect(this, goodsId, user.getMuserName(),
                isCollect ? I.ACTION_DELETE_COLLECT : I.ACTION_ADD_COLLECT,
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            //状态取反 然后重新设置
                            isCollect = !isCollect;
                            setCollectStatus();
                            CommonUtils.showShortToast(result.getMsg());
                            sendBroadcast(new Intent(I.BROADCAST_UPDATA_COLLECT)
                                    .putExtra(I.Collect.GOODS_ID, goodsId));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initCollectStatus() {
        //首先判断是否登录
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            //即登录成功
            mModel.isCollect(this, goodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollect = true;
                    } else {
                        isCollect = false;
                    }
                    setCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                    setCollectStatus();
                }
            });
        }
    }

    @OnClick(R.id.iv_good_cart)
    public void addCart() {
        userModel = new ModelUser();
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            userModel.updateCart(this, user.getMuserName(), I.ACTION_CART_ADD, goodsId,
                    1, 0, new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                CommonUtils.showShortToast(R.string.add_goods_success);
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        } else {
            MFGT.gotoLogin(this);
        }
    }

}
