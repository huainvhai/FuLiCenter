package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    private static final String TAG = PersonalFragment.class.getSimpleName();

    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;

    IModelUser model;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            //加载用户信息
            loadUserInfo(user);
            getCollectCount();
        } else {
            Log.e(TAG, "user null");
            //MFGT.gotoLogin(getActivity());
        }
    }

    //更新昵称后 回到个人中心界面也要重新加载数据
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void loadUserInfo(User user) {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), ivUserAvatar);
        tvUserName.setText(user.getMuserNick());
        //加载商品数量
        loadCollectCount("0");
    }

    private void loadCollectCount(String count) {
        tvCollectCount.setText(count);
    }

    public void getCollectCount() {
        model = new ModelUser();
        model.getCollectCount(getActivity(), FuLiCenterApplication.getUser().getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                Log.e(TAG, "result=" + result);
                if (result != null && result.isSuccess()) {
                    loadCollectCount(result.getMsg());
                } else {
                    loadCollectCount("0");
                }
            }

            @Override
            public void onError(String error) {
                loadCollectCount("0");
                Log.e(TAG, "error = " + error);
            }
        });
    }

    @OnClick({R.id.tv_center_settings, R.id.center_user_info})
    public void setting(View view) {
        MFGT.gotoSettiings(getActivity());
    }

}
