package cn.ucai.fulicenter.controller.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class CategoryActivity extends AppCompatActivity {
    NewGoodsFragment mNewGoodsFragment;
    boolean priceAsc = false;
    boolean addTimeAsc = false;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        mNewGoodsFragment = new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.fragment_container2, mNewGoodsFragment).commit();
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void onClick(View view) {
        int sortBy = I.SORT_BY_ADDTIME_ASC;
        Drawable arrow;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                //将箭头设置在按钮的右边
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrow, null);
                priceAsc = !priceAsc;
                break;
            case R.id.btn_sort_addtime:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                //将箭头设置在按钮的右边
                btnSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrow, null);
                priceAsc = !priceAsc;
                addTimeAsc = !addTimeAsc;
                break;
        }
        mNewGoodsFragment.sortGoods(sortBy);
    }
}
