package cn.ucai.fulicenter.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CollectAdapter;
import cn.ucai.fulicenter.controller.adapter.GoodsAdapter;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;
import cn.ucai.fulicenter.view.DisplayUtils;

public class CollectsActivity extends AppCompatActivity {
    private static final String TAG = CollectsActivity.class.getSimpleName();
    public static final int ACTION_DOWNLOAD = 0;
    public static final int ACTION_PULL_UP = 1;
    public static final int ACTION_PULL_DOWN = 2;

    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.rvCollect)
    RecyclerView rvCollect;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    User user;
    IModelUser model;
    GridLayoutManager mLayoutManager;
    CollectAdapter mAdapter;
    int pageId = 1;
    UpdateReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        DisplayUtils.initBactWithTitle(this, "收藏的宝贝");
        user = FuLiCenterApplication.getUser();
        mReceiver = new UpdateReceiver();
        if (user == null) {
            finish();
        } else {
            initView();
            initData(ACTION_DOWNLOAD, pageId);
            setPullDownListener();
            setPullUpListener();
            setReceiverListener();
        }
    }

    private void setReceiverListener() {
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_COLLECT);
        registerReceiver(mReceiver, filter);
    }

    private void initData(final int action, int pageId) {
        model = new ModelUser();
        model.findCollects(this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                Log.e(TAG, Arrays.toString(result));
                mAdapter.setMore(result.length > 0 && result != null);
                if (!mAdapter.isMore()) {
                    if (action == ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据加载");
                    }
                    return;
                }
                mAdapter.setFooter("加载更多");
                ArrayList<CollectBean> goodsList = ConvertUtils.array2List(result);
                switch (action) {
                    case ACTION_DOWNLOAD:
                        mAdapter.initGoodsList(goodsList);
                        break;
                    case ACTION_PULL_DOWN:
                        srl.setRefreshing(false);
                        tvHint.setVisibility(View.GONE);
                        mAdapter.initGoodsList(goodsList);
                        break;
                    case ACTION_PULL_UP:
                        mAdapter.addGoodsList(goodsList);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "error=" + error);
            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red)
        );
        mLayoutManager = new GridLayoutManager(this, I.COLUM_NUM);
        rvCollect.setLayoutManager(mLayoutManager);
        rvCollect.setHasFixedSize(true);
        mAdapter = new CollectAdapter(this, new ArrayList<CollectBean>());
        rvCollect.addItemDecoration(new SpaceItemDecoration(15));
        rvCollect.setAdapter(mAdapter);
    }

    private void setPullUpListener() {
        rvCollect.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mAdapter.setDragging(newState == RecyclerView.SCROLL_STATE_DRAGGING);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore()
                        && lastPosition == mAdapter.getItemCount() - 1) {
                    pageId++;
                    initData(ACTION_PULL_UP, pageId);
                }
            }
        });
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvHint.setVisibility(View.VISIBLE);
                pageId = 1;
                initData(ACTION_PULL_DOWN, pageId);
            }
        });
    }

    class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int goodsId = intent.getIntExtra(I.Collect.GOODS_ID, 0);
            Log.e(TAG, "onReceive goodsId" + goodsId);
            mAdapter.removeItem(goodsId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}
