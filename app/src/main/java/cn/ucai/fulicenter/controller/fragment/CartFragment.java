package cn.ucai.fulicenter.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CartAdapter;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;
import cn.ucai.fulicenter.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    public static final String TAG = CartFragment.class.getSimpleName();
    public static final int ACTION_DOWNLOAD = 0;
    public static final int ACTION_PULL_DOWN = 2;

    ArrayList<CartBean> mCartList;

    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.rvBoutique)
    RecyclerView rvBoutique;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    LinearLayoutManager mLayoutManager;
    CartAdapter mAdapter;
    IModelUser mModel;
    @BindView(R.id.loadMore)
    TextView loadMore;
    User user;
    @BindView(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    UpdateCartReceiver mReceiver;

    int sumPrice = 0;
    int payPrice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        initView();
        mModel = new ModelUser();
        initData();
        initListener();
        setReceiverListener();
        return view;
    }

    private void setReceiverListener() {
        mReceiver = new UpdateCartReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        getContext().registerReceiver(mReceiver, filter);
    }

    private void initListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvHint.setVisibility(View.VISIBLE);
                downloadCarList(ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        downloadCarList(ACTION_DOWNLOAD);
    }

    private void downloadCarList(final int action) {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            mModel.getCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    Log.e(TAG, Arrays.toString(result));
                    srl.setRefreshing(false);
                    tvHint.setVisibility(View.GONE);
                    srl.setVisibility(View.VISIBLE);
                    loadMore.setVisibility(View.GONE);
                    if (result.length > 0 && result != null) {
                        ArrayList<CartBean> cartBean = ConvertUtils.array2List(result);
                        Log.e(TAG, "cartBean=" + cartBean.size());
                        if (action == ACTION_DOWNLOAD || action == ACTION_PULL_DOWN) {
                            mAdapter.initData(cartBean);
                        } else {
                            mAdapter.addData(cartBean);
                        }
                    } else {
                        srl.setVisibility(View.GONE);
                        loadMore.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    srl.setRefreshing(false);
                    srl.setVisibility(View.GONE);
                    loadMore.setVisibility(View.VISIBLE);
                }
            });
        }
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
        mCartList = new ArrayList<>();
        mAdapter = new CartAdapter(getActivity(), mCartList);
        rvBoutique.setAdapter(mAdapter);

        srl.setVisibility(View.GONE);
        loadMore.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.loadMore)
    public void onClick() {
        downloadCarList(ACTION_DOWNLOAD);
    }

    private void setPrice() {
        sumPrice = 0;
        payPrice = 0;
        int savePrice = 0;
        if (mCartList != null && mCartList.size() > 0) {
            for (CartBean cart : mCartList) {
                GoodsDetailsBean goods = cart.getGoods();
                if (cart.isChecked() && goods != null) {
                    sumPrice += cart.getCount() * getPrice(goods.getCurrencyPrice());
                    savePrice += cart.getCount() * (getPrice(goods.getCurrencyPrice())
                            - getPrice(goods.getRankPrice()));
                }
            }
        }
        tvCartSumPrice.setText("合计:￥" + sumPrice);
        tvCartSavePrice.setText("节省:￥" + savePrice);
        mAdapter.notifyDataSetChanged();
        payPrice = sumPrice - savePrice;
    }

    //currencyPrice":"￥140
    int getPrice(String price) {
        int p = 0;
        p = Integer.parseInt(price.substring(price.indexOf("￥") + 1));
        //Log.e("adapter", "p=" + p);
        return p;
    }


    class UpdateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceiver ");
            setPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }

    @OnClick(R.id.tv_cart_buy)
    public void gotoBuy() {
        if (sumPrice > 0) {
            Log.e(TAG,"sumPrice=" + sumPrice);
            MFGT.gotoOrder(getActivity(),payPrice);
        } else {
            CommonUtils.showShortToast(R.string.order_nothing);
        }
    }
}
