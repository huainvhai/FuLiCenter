package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.IModelBoutique;
import cn.ucai.fulicenter.model.net.ModelBoutique;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {
    public static final String TAG = BoutiqueFragment.class.getSimpleName();
    public static final int ACTION_DOWNLOAD = 0;
    public static final int ACTION_PULL_DOWN = 2;

    ArrayList<BoutiqueBean> mBoutiqueList;

    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.rvBoutique)
    RecyclerView rvBoutique;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    LinearLayoutManager mLayoutManager;
    BoutiqueAdapter mAdapter;
    IModelBoutique mModel;
    @BindView(R.id.loadMore)
    TextView loadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        initView();
        mModel = new ModelBoutique();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvHint.setVisibility(View.VISIBLE);
                downloadBoutiqueList(ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        downloadBoutiqueList(ACTION_DOWNLOAD);
    }

    private void downloadBoutiqueList(final int action) {
        mModel.downloadData(getActivity(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                Log.e(TAG, Arrays.toString(result));
                srl.setVisibility(View.VISIBLE);
                loadMore.setVisibility(View.GONE);
                if (result.length > 0 && result != null) {
                    ArrayList<BoutiqueBean> boutiqueBean = ConvertUtils.array2List(result);
                    switch (action) {
                        case ACTION_DOWNLOAD:
                            mAdapter.initBoutiqueList(boutiqueBean);
                            break;
                        case ACTION_PULL_DOWN:
                            srl.setRefreshing(false);
                            tvHint.setVisibility(View.GONE);
                            mAdapter.initBoutiqueList(boutiqueBean);
                            break;
                    }
                } else {
                    srl.setVisibility(View.GONE);
                    loadMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                srl.setVisibility(View.GONE);
                loadMore.setVisibility(View.VISIBLE);
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvBoutique.setLayoutManager(mLayoutManager);
        rvBoutique.addItemDecoration(new SpaceItemDecoration(15));
        mBoutiqueList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(getActivity(), mBoutiqueList);
        rvBoutique.setAdapter(mAdapter);

        srl.setVisibility(View.GONE);
        loadMore.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.loadMore)
    public void onClick() {
        downloadBoutiqueList(ACTION_DOWNLOAD);
    }
}
