package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.GoodsDetailActivity;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    final static int TYPE_GOODS = 0;
    final static int TYPE_FOOTER = 1;

    Context mContext;
    ArrayList<NewGoodsBean> mList;
    String footer;
    boolean isMore;
    boolean isDragging;

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(Context context, ArrayList<NewGoodsBean> list) {
        this.mContext = context;
        mList = list;
    }

    public void initGoodsList(ArrayList<NewGoodsBean> list) {
        if (list != null) {
            this.mList.clear();
        }
        addGoodsList(list);
    }

    public void addGoodsList(ArrayList<NewGoodsBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void sortGoods(final int sortBy) {
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean leftBean, NewGoodsBean rightBean) {
                int result = 0;
                switch (sortBy) {
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (leftBean.getAddTime() - rightBean.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (rightBean.getAddTime() - leftBean.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(leftBean.getCurrencyPrice()) - getPrice(rightBean.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(rightBean.getCurrencyPrice()) - getPrice(leftBean.getCurrencyPrice());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    //currencyPrice":"￥140
    int getPrice(String price) {
        int p = 0;
        p = Integer.parseInt(price.substring(price.indexOf("￥") + 1));
        Log.e("adapter", "p=" + p);
        return p;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(mContext);
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = layout.inflate(R.layout.item_footer, parent, false);
                return new FooterViewHolder(view);
            case TYPE_GOODS:
                view = layout.inflate(R.layout.item_goods, parent, false);
                return new GoodsViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            FooterViewHolder holder = (FooterViewHolder) parentHolder;
            holder.tvFooter.setText(getFooter());
            return;
        }
        final NewGoodsBean goodsBean = mList.get(position);
        GoodsViewHolder holder = (GoodsViewHolder) parentHolder;
        ImageLoader.downloadImg(mContext, holder.ivGoodsThumb, goodsBean.getGoodsThumb());
        holder.tvGoodsName.setText(goodsBean.getGoodsName());
        holder.tvGoodsPrice.setText(goodsBean.getCurrencyPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoGoodsDetail(mContext, goodsBean.getGoodsId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_GOODS;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
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


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
