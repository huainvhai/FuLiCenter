package cn.ucai.fulicenter.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/18.
 */

public class CatFilterButton extends Button {
    boolean isExpand;
    PopupWindow mPopupWindow;
    CatFilterAdapter mAdapter;
    GridView gridView;
    String groupName;

    public CatFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initCatFilterButton(String groupName, ArrayList<CategoryChildBean> list) {
        this.groupName = groupName;
        this.setText(groupName);
        //设置自定义button的点击事件
        setCatFilterButtonListener();
        mAdapter = new CatFilterAdapter(getContext(), list);
        //使用容器将adapter放进popupwindow
        initGridView();
    }

    private void initGridView() {
        gridView = new GridView(getContext());
        gridView.setVerticalSpacing(10);
        gridView.setHorizontalSpacing(10);
        gridView.setNumColumns(2);
        gridView.setAdapter(mAdapter);
    }

    private void setCatFilterButtonListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpand) {
                    //没有展开
                    initPopupWindow();
                } else {
                    //展开
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                //设置箭头指示方向
                setArrow();
            }
        });
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        mPopupWindow.setContentView(gridView);
        mPopupWindow.showAsDropDown(this);
    }

    private void setArrow() {
        Drawable right;
        if (isExpand) {
            right = getResources().getDrawable(R.mipmap.arrow2_up);
        } else {
            right = getResources().getDrawable(R.mipmap.arrow2_down);
        }
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
        isExpand = !isExpand;
    }

    class CatFilterAdapter extends BaseAdapter {
        Context context;
        ArrayList<CategoryChildBean> childList;

        public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.childList = list;
        }

        @Override
        public int getCount() {
            return childList.size();
        }

        @Override
        public CategoryChildBean getItem(int position) {
            return childList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CatFilterViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_cat_filter, null);
                holder = new CatFilterViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (CatFilterViewHolder) convertView.getTag();
            }
            ImageLoader.downloadImg(context, holder.ivCategoryChildThumb, childList.get(position).getImageUrl());
            holder.tvCategoryChildNaem.setText(childList.get(position).getName());
            //从popupwindow中点击进入商品详情界面
            holder.layoutCatFilter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoCategory(context, childList.get(position).getId(), groupName, childList);
                }
            });
            return convertView;
        }

        class CatFilterViewHolder {
            @BindView(R.id.ivCategoryChildThumb)
            ImageView ivCategoryChildThumb;
            @BindView(R.id.tvCategoryChildNaem)
            TextView tvCategoryChildNaem;
            @BindView(R.id.layout_cat_filter)
            RelativeLayout layoutCatFilter;

            CatFilterViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }
}
