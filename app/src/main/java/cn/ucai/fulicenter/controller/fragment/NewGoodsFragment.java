package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.GoodsAdapter;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    public static final int ACTION_DOWNLOAD = 0;
    public static final int ACTION_PULL_UP = 1;
    public static final int ACTION_PULL_DOWN = 2;

    public static final String TAG = NewGoodsFragment.class.getSimpleName();

    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.rvBoutique)
    RecyclerView rvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    GoodsAdapter mAdapter;
    GridLayoutManager mLayoutManager;
    IModelNewGoods mModel;
    ArrayList<NewGoodsBean> mGoodsList;

    int mPageId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);
        initView();
        mModel = new ModelNewGoods();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mAdapter.setDragging(newState == RecyclerView.SCROLL_STATE_DRAGGING);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore()
                        && lastPosition == mAdapter.getItemCount() - 1) {
                    mPageId++;
                    downloadGoodsList(ACTION_PULL_UP, mPageId);
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
                mPageId = 1;
                downloadGoodsList(ACTION_PULL_DOWN, mPageId);
            }
        });
    }

    private void initData() {
        mPageId = 1;
        downloadGoodsList(ACTION_DOWNLOAD, mPageId);
    }

    private void downloadGoodsList(final int action, int pageId) {
        int catId = getActivity().getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID,0);
        mModel.downloadData(getContext(), catId, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                Log.e(TAG, Arrays.toString(result));
                mAdapter.setMore(result.length > 0 && result != null);
                if (!mAdapter.isMore()) {
                    if (action == ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据加载");
                    }
                    return;
                }
                mAdapter.setFooter("加载更多");
                ArrayList<NewGoodsBean> goodsList = ConvertUtils.array2List(result);
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
                Log.e(TAG, error);
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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
        mLayoutManager = new GridLayoutManager(getContext(), I.COLUM_NUM);
        rvGoods.setLayoutManager(mLayoutManager);
        rvGoods.setHasFixedSize(true);
        mGoodsList = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(), mGoodsList);
        rvGoods.addItemDecoration(new SpaceItemDecoration(15));
        rvGoods.setAdapter(mAdapter);

    }

    public void sortGoods(int sortBy){
        mAdapter.sortGoods(sortBy);
    }

}
