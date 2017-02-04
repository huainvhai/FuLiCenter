package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11.
 */

public class CollectAdapter extends RecyclerView.Adapter {
    final static int TYPE_GOODS = 0;
    final static int TYPE_FOOTER = 1;

    Context mContext;
    ArrayList<CollectBean> mList;
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

    public CollectAdapter(Context context, ArrayList<CollectBean> list) {
        this.mContext = context;
        mList = list;
    }

    public void initGoodsList(ArrayList<CollectBean> list) {
        if (list != null) {
            this.mList.clear();
        }
        addGoodsList(list);
    }

    public void addGoodsList(ArrayList<CollectBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
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
                view = layout.inflate(R.layout.item_collects, parent, false);
                return new CollectsViewHolder(view);
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
        final CollectBean goodsBean = mList.get(position);
        CollectsViewHolder holder = (CollectsViewHolder) parentHolder;
        ImageLoader.downloadImg(mContext, holder.ivGoodsThumb, goodsBean.getGoodsThumb());
        holder.tvGoodsName.setText(goodsBean.getGoodsName());
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

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class CollectsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.iv_collect_del)
        ImageView ivCollectDel;
        @BindView(R.id.layout_goods)
        RelativeLayout layoutGoods;

        CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
