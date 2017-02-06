package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.view.DisplayUtils;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.etOrderName)
    EditText etOrderName;
    @BindView(R.id.etOrderPhone)
    EditText etOrderPhone;
    @BindView(R.id.spinOrderArea)
    Spinner spinOrderArea;
    @BindView(R.id.etOrderStreet)
    EditText etOrderStreet;
    @BindView(R.id.tvOrderPrice)
    TextView tvOrderPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        int payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        DisplayUtils.initBactWithTitle(this, "填写收货地址");
        tvOrderPrice.setText("合计：" + payPrice);
    }
}
