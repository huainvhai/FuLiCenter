package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/16.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> mGroupList,
                           ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.context = context;
        this.mGroupList = new ArrayList<>();
        mGroupList.addAll(mGroupList);
        this.mChildList = new ArrayList<>();
        mChildList.addAll(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ?
                mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        if (mChildList != null && mChildList.get(groupPosition) != null) {
            return mChildList.get(groupPosition).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            //view = LayoutInflater.from(context).inflate(R.layout.item_category_group, null);
            convertView = View.inflate(context, R.layout.item_category_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(context, holder.ivGroupThumb, mGroupList.get(groupPosition).getImageUrl());
        holder.tvGroupName.setText(mGroupList.get(groupPosition).getName());
        holder.ivIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            //view = LayoutInflater.from(context).inflate(R.layout.item_category_child, null);
            convertView = View.inflate(context, R.layout.item_category_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(context, holder.ivCategoryChildThumb, getChild(groupPosition, childPosition).getImageUrl());
        holder.tvCategoryChildNaem.setText(getChild(groupPosition, childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList,
                         ArrayList<ArrayList<CategoryChildBean>> childList) {
        Log.e("adapter","initData");
        mGroupList.clear();
        mGroupList.addAll(groupList);
        mChildList.clear();
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.ivGroupThumb)
        ImageView ivGroupThumb;
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.ivIndicator)
        ImageView ivIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.ivCategoryChildThumb)
        ImageView ivCategoryChildThumb;
        @BindView(R.id.tvCategoryChildNaem)
        TextView tvCategoryChildNaem;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
