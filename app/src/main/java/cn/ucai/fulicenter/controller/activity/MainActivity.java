package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.controller.fragment.CartFragment;
import cn.ucai.fulicenter.controller.fragment.CategoryFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.controller.fragment.PersonalFragment;
import cn.ucai.fulicenter.view.MFGT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    int index, currentIndex;

    RadioButton[] rbs = new RadioButton[5];
    @BindView(R.id.layout_new_good)
    RadioButton layoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton layoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton layoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton layoutCart;
    @BindView(R.id.layout_personal_center)
    RadioButton layoutPersonalCenter;

    NewGoodsFragment mGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    CartFragment mCartFragment;
    PersonalFragment mPersonalFragment;
    Fragment[] mFragemnts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rbs[0] = layoutNewGood;
        rbs[1] = layoutBoutique;
        rbs[2] = layoutCategory;
        rbs[3] = layoutCart;
        rbs[4] = layoutPersonalCenter;

        mFragemnts = new Fragment[5];
        mGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mCartFragment = new CartFragment();
        mPersonalFragment = new PersonalFragment();
        mFragemnts[0] = mGoodsFragment;
        mFragemnts[1] = mBoutiqueFragment;
        mFragemnts[2] = mCategoryFragment;
        mFragemnts[3] = mCartFragment;
        mFragemnts[4] = mPersonalFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mGoodsFragment)
                .add(R.id.container, mBoutiqueFragment)
                .add(R.id.container, mCategoryFragment)
                .add(R.id.container, mPersonalFragment)
                .show(mGoodsFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
                .hide(mPersonalFragment)
                .commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLogin(this, I.REQUEST_CODE_LOGIN_FROM_CART);
                } else {
                    index = 3;
                }
                break;
            case R.id.layout_personal_center:
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLogin(this);
                } else {
                    index = 4;
                }
                break;
        }

        setFragment();
        if (index != currentIndex) {
            setRadioStatus();
        }
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mFragemnts[currentIndex]);
        //判断fragment是否被添加
        if (!mFragemnts[index].isAdded()) {
            ft.add(R.id.container, mFragemnts[index]);
        }
        ft.show(mFragemnts[index]).commit();
    }

    private void setRadioStatus() {
        for (int i = 0; i < rbs.length; i++) {
            if (index != i) {
                rbs[i].setChecked(false);
            } else {
                rbs[i].setChecked(true);
            }
        }
        currentIndex = index;
    }

    //判断加载个人中心界面还是登录界面
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "currentIndex=" + currentIndex + ",index=" + index
                + ",user=" + FuLiCenterApplication.getUser());
        if (index == 4 && FuLiCenterApplication.getUser() == null) {
            index = 0;
        }
        setFragment();
        //重新设置选中的index
        setRadioStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode=" + requestCode + ",resultCode=" + resultCode);
        //要求登录成功而且请求码I.REQUEST_CODE_LOGIN，才能回到个人中心界面
        if (resultCode == RESULT_OK) {
            if (requestCode == I.REQUEST_CODE_LOGIN) {
                index = 4;
            }
            if (requestCode == I.REQUEST_CODE_LOGIN_FROM_CART) {
                index = 3;
            }
            setFragment();
            setRadioStatus();
        }
    }
}
