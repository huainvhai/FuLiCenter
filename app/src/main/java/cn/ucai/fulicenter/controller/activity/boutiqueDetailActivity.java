package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.MFGT;

public class BoutiqueDetailActivity extends AppCompatActivity {


    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.ivBack2)
    ImageView ivBack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_detail);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, new NewGoodsFragment()).commit();
        tvCommonTitle.setText(getIntent().getStringExtra(I.Boutique.NAME));
    }


    @OnClick(R.id.ivBack2)
    public void onClick() {
        MFGT.finish(this);
    }
}
