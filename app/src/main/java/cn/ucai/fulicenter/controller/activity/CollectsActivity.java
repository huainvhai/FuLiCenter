package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        DisplayUtils.initBactWithTitle(this, "收藏的宝贝");
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            initView();
            initData();
        }
    }

    private void initData() {
        model = new ModelUser();
        model.findCollects(this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                if (result == null) {

                } else {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    Log.e(TAG, "list=" + list.size() + Arrays.toString(result));
                    mAdapter.initGoodsList(list);
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
}
