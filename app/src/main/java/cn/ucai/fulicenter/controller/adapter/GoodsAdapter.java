package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/11.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;

    public GoodsAdapter(Context context, ArrayList<NewGoodsBean> list) {
        this.mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater.from(mContext).inflate(R.layout.item_goods, null);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        if (holder != null) {
            vh = (GoodsViewHolder) holder;
        }
        ImageLoader.downloadImg(mContext, vh.ivGoodsThumb, mList.get(position).getGoodsThumb());
        vh.tvGoodsName.setText(mList.get(position).getGoodsName());
        vh.tvGoodsPrice.setText(mList.get(position).getCurrencyPrice());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
