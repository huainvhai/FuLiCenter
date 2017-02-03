package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.SharedPreferenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.MFGT;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.iv_user_profile_avatar)
    ImageView ivUserProfileAvatar;
    @BindView(R.id.tv_user_profile_name)
    TextView tvUserProfileName;
    @BindView(R.id.tv_user_profile_nick)
    TextView tvUserProfileNick;

    OnSetAvatarListener mOnSetAvatarListener;
    IModelUser modelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        DisplayUtils.initBactWithTitle(this, "设置");
        initata();
    }

    private void initata() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            MFGT.gotoLogin(this);
        }
    }

    private void loadUserInfo(User user) {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, ivUserProfileAvatar);
        tvUserProfileName.setText(user.getMuserName());
        tvUserProfileNick.setText(user.getMuserNick());
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        //退出登录进入登录界面 但是要清空FuLiCenterApplication的变量和sharedPreference中的数据
        FuLiCenterApplication.setUser(null);
        SharedPreferenceUtils.getInstance(this).removeUser();
        MFGT.gotoLogin(this);
        MFGT.finish(this);
    }

    @OnClick(R.id.layout_user_profile_nickname)
    public void updateNick() {
        MFGT.gotoUpdateNick(this);
    }

    @OnClick(R.id.layout_user_profile_avatar)
    public void onClickAvatar() {
        Log.e(TAG, "onClickAvatar");
        mOnSetAvatarListener = new OnSetAvatarListener(this,
                R.id.layout_user_profile_avatar,
                FuLiCenterApplication.getUser().getMuserName(),
                I.AVATAR_TYPE_USER_PATH);
    }

    @OnClick(R.id.layout_user_profile_username)
    public void onClickUsername() {
        CommonUtils.showShortToast(R.string.username_connot_be_modify);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode = " + requestCode + ",resultCode= " + requestCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == I.REQUEST_CODE_NICK) {
            //重新从全局获取就要在更新成功后重新保存user数据
            tvUserProfileNick.setText(FuLiCenterApplication.getUser().getMuserNick());
        } else {
            mOnSetAvatarListener.setAvatar(requestCode, data, ivUserProfileAvatar);
        }
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            uploadAvatar();
        }
    }

    private void uploadAvatar() {
        modelUser = new ModelUser();
        User user = FuLiCenterApplication.getUser();
        File file = new File(String.valueOf(OnSetAvatarListener.getAvatarFile(this,
                I.AVATAR_TYPE_USER_PATH + "/" + user.getMuserName() + I.AVATAR_SUFFIX_JPG)));
        Log.e(TAG, file.getAbsolutePath());
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_avatar));
        dialog.show();
        modelUser.updateAvatar(this, user.getMuserName(), file, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                int msg = R.string.update_user_avatar_fail;
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            msg = R.string.update_user_avatar_success;
                        }
                    }
                }
                CommonUtils.showLongToast(msg);
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showShortToast(error);
                Log.e(TAG, error);
            }
        });
    }
}
