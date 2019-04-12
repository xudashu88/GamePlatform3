package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.ContactAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Contact;
import com.purity.yu.gameplatform.http.HttpRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 客服
 */
@ContentView(R.layout.fragment_contact_serve)
public class ContactServeFragment extends BaseFragment {

    @BindView(R.id.rv_contact)
    RecyclerView rv_contact;
    private Context mContext;
    private ContactAdapter adapter;
    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        postBetRecord();
    }

    private void postBetRecord() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_QRCODE + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.username_existed);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String _data = json.optString("data");
                            contactList.clear();//清除,每一页都是新的
                            contactList = new Gson().fromJson(_data, new TypeToken<List<Contact>>() {
                            }.getType());
                            initAdapter();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        adapter = new ContactAdapter(mContext, contactList);
        rv_contact.setLayoutManager(mLayoutManager);
        rv_contact.setAdapter(adapter);
    }
}
