package cn.ucai.fulicenter.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.controller.fragment.CategoryFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
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
        mFragemnts[0] = mGoodsFragment;
        mFragemnts[1] = mBoutiqueFragment;
        mFragemnts[2] = mCategoryFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mGoodsFragment)
                .add(R.id.container, mBoutiqueFragment)
                .add(R.id.container,mCategoryFragment)
                .show(mGoodsFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
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
                index = 3;
                break;
            case R.id.layout_personal_center:
                index = 4;
                break;
        }

        setFragment();
        if (index != currentIndex) {
            setRadioStatus();
        }
    }

    private void setFragment() {
        getSupportFragmentManager().beginTransaction().show(mFragemnts[index])
                .hide(mFragemnts[currentIndex]).commit();
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


}
