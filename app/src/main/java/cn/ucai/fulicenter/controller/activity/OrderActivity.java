package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @OnClick(R.id.btnBuy)
    public void checkOrder() {
        String receiverName = etOrderName.getText().toString().trim();
        if (TextUtils.isEmpty(receiverName)) {
            etOrderName.setError("收货人姓名不能为空");
            etOrderName.requestFocus();
            return;
        }
        String receiverPhone = etOrderPhone.getText().toString().trim();
        if (TextUtils.isEmpty(receiverPhone)) {
            etOrderPhone.setError("手机号码不能为空");
            etOrderPhone.requestFocus();
            return;
        }
        if (receiverPhone.matches("[\\d]{11}")) {
            etOrderPhone.setError("手机号码格式不正确");
            etOrderPhone.requestFocus();
            return;
        }
        String area = spinOrderArea.getSelectedItem().toString().trim();
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = etOrderStreet.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            etOrderName.setError("街道地址不能为空");
            etOrderName.requestFocus();
            return;
        }
    }
}
