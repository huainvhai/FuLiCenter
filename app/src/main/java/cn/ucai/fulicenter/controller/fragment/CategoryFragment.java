package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.adapter.CategoryAdapter;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.IModelCategory;
import cn.ucai.fulicenter.model.net.ModelCategory;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    int groupCount;

    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;

    IModelCategory mModel;
    CategoryAdapter mAdapter;

    ArrayList<CategoryGroupBean> mGroupList = new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> mChildList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new CategoryAdapter(getActivity(), mGroupList, mChildList);
        elvCategory.setAdapter(mAdapter);
        initView(false);
        initData();
        return view;
    }

    private void initData() {
        mModel = new ModelCategory();
        mModel.downloadData(getActivity(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                Log.e("CategoryFragment", result.length + "");
                if (result != null) {
                    initView(true);
                    ArrayList<CategoryGroupBean> groupList = ConvertUtils.array2List(result);
                    mGroupList.addAll(groupList);
                    //通过大类的id下载小类
                    for (int i = 0; i < groupList.size(); i++) {
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        downloadChildData(groupList.get(i).getId(),i);
                    }

                } else {
                    Log.e("CategoryFragment", "onError");
                    initView(false);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("CategoryFragment", error);
                initView(false);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadChildData(int id, final int index) {
        mModel.downloadData(getActivity(), id, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                if (result != null) {
                    ArrayList<CategoryChildBean> childList = ConvertUtils.array2List(result);
                    mChildList.set(index,childList);
                }
                //如果下载成功值=大类的数量 则便表示下载完成 使用adapter填充数据并更新
                if (groupCount == mGroupList.size()) {
                    mAdapter.initData(mGroupList, mChildList);
                }
            }

            @Override
            public void onError(String error) {
                groupCount++;
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //判断是否有数据显示
    private void initView(boolean hasData) {
        elvCategory.setVisibility(hasData ? View.VISIBLE : View.GONE);
        tvNomore.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }

}
