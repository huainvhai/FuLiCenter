package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class UpdateNickActivity extends AppCompatActivity {
    private static final String TAG = UpdateNickActivity.class.getSimpleName();

    @BindView(R.id.et_update_user_name)
    EditText etUpdateUserName;

    IModelUser mModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        DisplayUtils.initBactWithTitle(this, "修改昵称");
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            etUpdateUserName.setText(user.getMuserNick());
        } else {
            finish();
        }
    }

    @OnClick(R.id.btn_save)
    public void checkInput() {
        String nick = etUpdateUserName.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
        } else if (nick.equals(user.getMuserNick())) {
            CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
        } else {
            updateNick(nick);
        }
    }

    private void updateNick(String nick) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_nick));
        dialog.show();
        mModel = new ModelUser();
        mModel.updateNick(this, user.getMuserName(), nick, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                int msg = R.string.update_fail;
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            msg = R.string.update_user_nick_success;
                            User user = (User) result.getRetData();
                            Log.e(TAG, "update success user=" + user);
                            saveUserAfterUpdateNick(user);
                            //给settings返回值 更新昵称有没有成功
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            if (result.getRetCode() == I.MSG_USER_SAME_NICK ||
                                    result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                msg = R.string.update_nick_fail_unmodify;
                            }
                        }
                    }
                }
                CommonUtils.showShortToast(msg);
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(R.string.update_fail);
                dialog.dismiss();
                Log.e(TAG, "error=" + error);
            }
        });
    }

    private void saveUserAfterUpdateNick(User user) {
        FuLiCenterApplication.setUser(user);
        UserDao.getInstance().saveUser(user);
    }
}
