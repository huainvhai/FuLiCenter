package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModeGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11.
 */

public class CollectAdapter extends RecyclerView.Adapter {
    final static int TYPE_GOODS = 0;
    final static int TYPE_FOOTER = 1;
    private static final String TAG = CollectAdapter.class.getSimpleName();

    Context mContext;
    ArrayList<CollectBean> mList;
    String footer;
    boolean isMore;
    boolean isDragging;
    IModelGoods model;
    User user;

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
        model = new ModeGoods();
        user = FuLiCenterApplication.getUser();
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

    //通过goodsId移除 在商品详情发送广播
    public void removeItem(int goodsId) {
        if (goodsId != 0) {
            mList.remove(new CollectBean(goodsId));
            notifyDataSetChanged();
        }
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
        CollectsViewHolder holder = (CollectsViewHolder) parentHolder;
        holder.bind(position);
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

    class CollectsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.iv_collect_del)
        ImageView ivCollectDel;
        @BindView(R.id.layout_goods)
        RelativeLayout layoutGoods;

        int itemPosition;

        CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final int position) {
            ImageLoader.downloadImg(mContext, ivGoodsThumb, mList.get(position).getGoodsThumb());
            tvGoodsName.setText(mList.get(position).getGoodsName());
            itemPosition = position;
        }

        @OnClick(R.id.layout_goods)
        public void collectDetail() {
            MFGT.gotoGoodsDetail(mContext, mList.get(itemPosition).getGoodsId());
        }

        @OnClick(R.id.iv_collect_del)
        public void deleteCollect() {
            model.setCollect(mContext, mList.get(itemPosition).getGoodsId(), user.getMuserName(),
                    I.ACTION_DELETE_COLLECT, new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                mList.remove(itemPosition);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "error=" + error);
                        }
                    });
        }
    }
}
