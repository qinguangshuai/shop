package com.bw.qgs.qgs2.myaddress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.qgs.qgs2.R;
import com.bw.qgs.qgs2.homepage.fragment.threefragment.Event1;
import com.bw.qgs.qgs2.homepage.fragment.threefragment.ThreeFragment;
import com.bw.qgs.qgs2.myaddress.adapter.AddressAdapter;
import com.bw.qgs.qgs2.myaddress.bean.AddressUser;
import com.bw.qgs.qgs2.myaddress.bean.MoRenUser;
import com.bw.qgs.qgs2.myaddress.presenter.AddressPresenter;
import com.bw.qgs.qgs2.myaddress.view.AddressView;
import com.bw.qgs.qgs2.url.UrlUtil;
import com.bw.qgs.qgs2.xinzeng.UpdateAddressActivity;
import com.bw.qgs.qgs2.xinzeng.XinZengActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends AppCompatActivity implements AddressView {

    @BindView(R.id.finishaddress)
    TextView finishaddress;
    @BindView(R.id.addressrecycle)
    RecyclerView addressrecycle;
    @BindView(R.id.insertaddress)
    Button insertaddress;
    private AddressPresenter mAddressPresenter;
    private List<AddressUser.ResultBean> mResult1;
    private MoAddressPresenter mMoAddressPresenter;
    public static int mId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        mAddressPresenter = new AddressPresenter(this);
        mAddressPresenter.addre(UrlUtil.ADDRESS);
        mMoAddressPresenter = new MoAddressPresenter(this);
    }

    @OnClick({R.id.finishaddress, R.id.insertaddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finishaddress:
                break;
            case R.id.insertaddress:
                startActivity(new Intent(AddressActivity.this, XinZengActivity.class));
                break;
        }
    }

    @Override
    public void onSuccess(final String result) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        addressrecycle.setLayoutManager(linearLayoutManager);
        Gson gson = new Gson();
        AddressUser addressUser = gson.fromJson(result, AddressUser.class);
        mResult1 = addressUser.getResult();
        final AddressAdapter walletAdapter = new AddressAdapter(getApplicationContext(), mResult1);
        /*walletAdapter.setHttpOnClick(new AddressAdapter.HttpOnClick() {
            @Override
            public void click(View view, int position) {
                walletAdapter.setHttpOnClick(new AddressAdapter.HttpOnClick() {
                    @Override
                    public void click(View view, int position) {
                        int id = mResult1.get(position).getId();
                        mMoPresenter.mo(UrlUtil.MOREN+id,new BaseRequest());
                    }
                });
            }
        });*/
        walletAdapter.setHttpXiuOnClick(new AddressAdapter.HttpXiuOnClick() {
            @Override
            public void click(View view, int position) {
                Intent intent = new Intent(AddressActivity.this, UpdateAddressActivity.class);
                int id = mResult1.get(position).getId();
                String realName = mResult1.get(position).getRealName();
                String phone = mResult1.get(position).getPhone();
                String address = mResult1.get(position).getAddress();
                String zipCode = mResult1.get(position).getZipCode();
                intent.putExtra("id", id);
                intent.putExtra("realName", realName);
                intent.putExtra("phone", phone);
                intent.putExtra("address", address);
                intent.putExtra("zipCode", zipCode);
                startActivity(intent);
            }
        });
        walletAdapter.setHttpCheckOnClick(new AddressAdapter.HttpCheckOnClick() {
            @Override
            public void click(View view, int position) {
                int whetherDefault = mResult1.get(position).getWhetherDefault();
                mId = mResult1.get(position).getId();
                /*Bundle bundle =new Bundle();
                bundle.putInt("id",mId);
                mThreeFragment.setArguments(bundle);*/
                boolean ischeck = mResult1.get(position).isIscheck();
                if (ischeck) {
                    mResult1.get(position).setWhetherDefault(2);
                    walletAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                } else {
                    mMoAddressPresenter.mo(UrlUtil.MORENADDRESS, mId);
                    mResult1.get(position).setWhetherDefault(1);
//                    EventBus.getDefault().post(new Event1(mId));
//                     chuan();
                    walletAdapter.notifyDataSetChanged();
                    // mResult1.get(position).setIscheck(true);
                }
            }

//            private void chuan() {
//                EventBus.getDefault().post(new Event1(mId));
//            }
        });
        
        
        addressrecycle.setAdapter(walletAdapter);
        walletAdapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(), "" + mResult1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoSuccess(String result) {
        Gson gson = new Gson();
        //EventBus.getDefault().post(new Event1(mId));
        MoRenUser moRenUser = gson.fromJson(result, MoRenUser.class);
        String message = moRenUser.getMessage();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailer(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    float x1, x2;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            x2 = event.getX();
            if (x2 - x1 > 200) {
                finish();
            }
        }
        /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            if (x2 - x1 > 100) {
                finish();
            }
        }*/
        return super.onTouchEvent(event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
