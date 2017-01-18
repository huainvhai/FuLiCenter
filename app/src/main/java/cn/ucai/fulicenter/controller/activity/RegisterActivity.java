package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/18.
 */
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etNick)
    EditText etNick;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    IModelUser mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBack, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btnRegister:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String username = etUserName.getText().toString().trim();
        String usernick = etNick.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();
        String confirmPwd = etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            etUserName.setError(getResources().getString(R.string.user_name_connot_be_empty));
            etUserName.requestFocus();
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            etUserName.setError(getResources().getString(R.string.illegal_user_name));
            etUserName.requestFocus();
        } else if (TextUtils.isEmpty(usernick)) {
            etNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            etNick.requestFocus();
        } else if (TextUtils.isEmpty(pwd)) {
            etPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            etPassword.requestFocus();
        } else if (TextUtils.isEmpty(confirmPwd)) {
            etConfirmPassword.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            etConfirmPassword.requestFocus();
        } else if (!pwd.equals(confirmPwd)) {
            etConfirmPassword.setError(getResources().getString(R.string.two_input_password));
            etConfirmPassword.requestFocus();
        } else {
            register(username, usernick, pwd);
        }
    }

    private void register(String username, String usernick, String pwd) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.registering));
        mModel = new ModelUser();
        mModel.register(this, username, usernick, pwd, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, Result.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            //注册成功
                            CommonUtils.showShortToast(R.string.register_success);
                            MFGT.finish(RegisterActivity.this);
                        } else {
                            //注册失败 102
                            CommonUtils.showShortToast(R.string.register_fail_exists);
                        }
                    } else {
                        CommonUtils.showShortToast(R.string.register_fail);
                    }
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
