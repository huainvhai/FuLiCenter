package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import cn.ucai.fulicenter.model.net.SharedPreferenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/18.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    IModelUser mModel;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBack, R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btnLogin:
                checkInPut();
                break;
            case R.id.btnRegister:
                MFGT.gotoRegister(this);
                break;
        }
    }

    private void checkInPut() {
        mModel = new ModelUser();
        final String username = etUserName.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.logining));
        mModel.login(this, username, pwd, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            //获取当前用户user
                            User user = (User) result.getRetData();
                            //用户信息保存到数据库中
                            boolean saveUser = UserDao.getInstance().saveUser(user);
                            Log.e(TAG, "saveUser=" + saveUser);
                            //如果数据库保存成功 则保存到内存中及全局中
                            if (saveUser) {
                                SharedPreferenceUtils.getInstance(LoginActivity.this).saveUser(user.getMuserName());
                                FuLiCenterApplication.setUser(user);
                            }
                            setResult(RESULT_OK);
                            MFGT.finish(LoginActivity.this);
                        } else {
                            if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                                CommonUtils.showShortToast(getString(R.string.login_fail_unknow_user));
                            }
                            if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                                CommonUtils.showShortToast(getString(R.string.login_fail_error_password));
                            }
                        }
                    } else {
                        CommonUtils.showShortToast(getString(R.string.login_fail));
                    }
                } else {
                    CommonUtils.showShortToast(getString(R.string.login_fail));
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showShortToast(error);
            }
        });
    }
}
