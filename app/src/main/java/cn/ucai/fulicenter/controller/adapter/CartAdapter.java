package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/2/4.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    IModelUser modelUser;
    User user;

    public CartAdapter(Context mContext, ArrayList<CartBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        modelUser = new ModelUser();
        user = FuLiCenterApplication.getUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_cart, null);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentolder, int position) {
        CartViewHolder holder = (CartViewHolder) parentolder;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<CartBean> cartBean) {
        if (cartBean != null) {
            mList.clear();
        }
        addData(cartBean);
    }

    public void addData(ArrayList<CartBean> cartBean) {
        mList.addAll(cartBean);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chkSelect)
        CheckBox chkSelect;
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.ivAddCart)
        ImageView ivAddCart;
        @BindView(R.id.tvCartCount)
        TextView tvCartCount;
        @BindView(R.id.ivReduceCart)
        ImageView ivReduceCart;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        int itemPosition;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            GoodsDetailsBean detailsBean = mList.get(position).getGoods();
            itemPosition = position;
            if (detailsBean != null) {
                ImageLoader.downloadImg(mContext, ivGoodsThumb, detailsBean.getGoodsThumb());
                tvGoodsName.setText(detailsBean.getGoodsName());
                tvGoodsPrice.setText(detailsBean.getCurrencyPrice());
            }
            tvCartCount.setText("(" + mList.get(position).getCount() + ")");
            chkSelect.setChecked(mList.get(itemPosition).isChecked());
        }

        @OnCheckedChanged(R.id.chkSelect)
        public void checkListener(boolean checked) {
            mList.get(itemPosition).setChecked(checked);
            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
        }

        @OnClick(R.id.ivAddCart)
        public void addCart() {
            modelUser.updateCart(mContext, user.getMuserName(), I.ACTION_CART_ADD,
                    mList.get(itemPosition).getGoodsId(), 1, mList.get(itemPosition).getId(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                //改变数量
                                mList.get(itemPosition).setCount(mList.get(itemPosition).getCount() + 1);
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }

        @OnClick(R.id.ivReduceCart)
        public void deleteCart() {
            final int count = mList.get(itemPosition).getCount();
            int action = I.ACTION_CART_UPDATE;
            if (count > 1) {
                action = I.ACTION_CART_UPDATE;
            } else {
                action = I.ACTION_CART_DELETE;
            }
            modelUser.updateCart(mContext, user.getMuserName(), action,
                    mList.get(itemPosition).getGoodsId(), count - 1, mList.get(itemPosition).getId(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                //改变数量
                                if (count <= 1) {
                                    mList.remove(itemPosition);
                                } else {
                                    mList.get(itemPosition).setCount(count - 1);
                                }
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
