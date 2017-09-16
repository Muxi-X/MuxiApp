package com.muxistudio.muxiio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.model.AddShare;
import com.muxistudio.muxiio.model.AfterEditShare;
import com.muxistudio.muxiio.model.EditShare;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.ShareIntentUtils;
import com.muxistudio.muxiio.utils.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.swipebackfragment.SwipeBackActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.KeyEvent.KEYCODE_BACK;

public class AddShareActivity extends SwipeBackActivity {

    //shareId :retrieved from editing this share
    private int shareId;
    private String shareTag;
    private String shareTitle;
    private String shareContent;
    //if the intent is to edit the share, the flage will be set true
    private boolean editFlag = false;


    private String titleAddedSharing = "";
    private String shareAddedSharing = "";
    private String tagsAddedSharing = "product";

    private String rawShare;
    private ShareList.SharesBean rawShareObject;

    //@BindView(R.id.btn_add_sharing_back)
    //ImageButton btnAddSharingBack;
    @BindView(R.id.toolbar_add_sharing)
    Toolbar toolbarAddSharing;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.txv_add_sharing_theme)
    MaterialEditText txvAddSharingTheme;
    @BindView(R.id.edt_add_sharing_link)
    EditText edtAddSharingLink;
    @BindView(R.id.btn_add_sharing_confirm)
    Button btnAddSharingConfirm;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                Intent intent = new Intent();
                intent.setClass(AddShareActivity.this, ShareActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KEYCODE_BACK:
                Intent intent = new Intent();
                intent.setClass(AddShareActivity.this, ShareActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sharing);
        ButterKnife.bind(this);
        setExtras();
        initToolbar();
        setupTabs();
        setTabListener();
        getData();
    }

    private void setExtras(){
        Intent intent = getIntent();
        editFlag = intent.getBooleanExtra("editFlag",false);
        if(editFlag) {
            rawShareObject = (ShareList.SharesBean) intent.getExtras().getSerializable("rawShare");

            shareId = intent.getIntExtra("shareId", -1);
            shareTag = intent.getStringExtra("shareTag");
            shareTitle = intent.getStringExtra("mShareTitleTxt");
            shareContent = intent.getStringExtra("shareContent");

            setDefaultTab(shareTag);
            titleAddedSharing = shareTitle;
            shareAddedSharing = shareContent;

            txvAddSharingTheme.setText(shareTitle);
            edtAddSharingLink.setText(shareContent);
        }
    }
    //set a tab when editFlag set true
    private void setDefaultTab(String tag){
        if(tag.equals("PRODUCT")){
            tabLayout.getTabAt(0).select();
        }
        if(tag.equals("DESIGN")){
            tabLayout.getTabAt(1).select();
        }
        if(tag.equals("ANDROID")){
            tabLayout.getTabAt(2).select();
        }
        if(tag.equals("FRONTEND")){
            tabLayout.getTabAt(3).select();
        }
        if(tag.equals("BACKEND")){
            tabLayout.getTabAt(4).select();
        }
    }
    private void initToolbar() {
        toolbarAddSharing.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        if (editFlag){
            toolbarAddSharing.setTitle("编辑分享");
        }else {
            toolbarAddSharing.setTitle("新建分享");
        }
        toolbarAddSharing.setTitleMarginStart(40);
        toolbarAddSharing.setTitleMarginTop(20);
        setSupportActionBar(toolbarAddSharing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //make sure the textView can set text when this activity comes to back ground
    @Override
    protected void onStart() {
        super.onStart();
        String data = ShareIntentUtils.sharingIntent(this, getIntent());
        String type = getIntent().getType();
        if(!TextUtils.isEmpty(data)) {
            if ("text/plain".equals(type)) {
                //give data to rawShare
                rawShare = data;

                String parsedStrings[] = MyTextUtils.getDataFromOtherApp(rawShare);

                titleAddedSharing = parsedStrings[0];
                shareAddedSharing = parsedStrings[1];

                txvAddSharingTheme.setText(parsedStrings[0]);
                edtAddSharingLink.setText(parsedStrings[1]);
                ToastUtils.makeToast(this, data, Toast.LENGTH_SHORT);
            } else if (type.startsWith("image/")) {
                // here stays question
                ToastUtils.makeToast(this, data, Toast.LENGTH_SHORT);
            }
        }
    }
    private boolean checkShareMessage(String title,String share,String tag){
        boolean flag = true;
        if(title.equals("")){
            ToastUtils.showSpecificDuration("没有填写主题哟", 2000);
            flag = false;
        }
        if(share.equals("")){
            ToastUtils.showSpecificDuration("你的内容是空的哟", 2000);
            flag = false;
        }
        if(tag.equals("")){
            ToastUtils.showSpecificDuration("先选择一下组别吧", 2000);
            flag = false;
        }
        return flag;
    }
    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.design));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.android));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.frontend));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.backend));
    }
    private void setTabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    tagsAddedSharing = "product";
                    ToastUtils.makeToast(AddShareActivity.this, "Product " , Toast.LENGTH_SHORT);
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    tagsAddedSharing = "design";
                    ToastUtils.makeToast(AddShareActivity.this, "Design " , Toast.LENGTH_SHORT);
                }else if(tabLayout.getSelectedTabPosition() == 2){
                    tagsAddedSharing = "android";
                    ToastUtils.makeToast(AddShareActivity.this, "Android " , Toast.LENGTH_SHORT);
                }else if(tabLayout.getSelectedTabPosition() == 3){
                    tagsAddedSharing = "frontend";
                    ToastUtils.makeToast(AddShareActivity.this, "Frontend " , Toast.LENGTH_SHORT);
                }else if(tabLayout.getSelectedTabPosition() == 4){
                    tagsAddedSharing = "backend";
                    ToastUtils.makeToast(AddShareActivity.this, "Backend " , Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void getData() {
        titleAddedSharing = txvAddSharingTheme.getText().toString();
        shareAddedSharing = edtAddSharingLink.getText().toString();
    }
    @OnClick({R.id.btn_add_sharing_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_sharing_confirm:
                //return true all are not ""
                titleAddedSharing = txvAddSharingTheme.getText().toString();
                shareAddedSharing = edtAddSharingLink.getText().toString();
                if(checkShareMessage(titleAddedSharing,shareAddedSharing,tagsAddedSharing)) {
                    if(!editFlag){
                        postAddedSharing();
                    }else{
                        editShare();
                        finish();
                    }
                }
                break;
        }
    }

    public void postAddedSharing() {
        AddShare addShare = new AddShare(titleAddedSharing, shareAddedSharing, tagsAddedSharing);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .postShareAdd(UserInfo.shareToken,addShare)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<AddShare>() {
                    @Override
                    public void call(AddShare addShare) {
                        finish();
                        ToastUtils.showShort("分享发送成功");
                    }
                });
    }

    public void editShare(){
        String content = edtAddSharingLink.getText().toString();
        String title = txvAddSharingTheme.getText().toString();

        EditShare editShare = new EditShare(content,title);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .putEditShare(UserInfo.shareToken,editShare,shareId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<AfterEditShare>() {
                    @Override
                    public void call(AfterEditShare afterEditShare) {
                        ToastUtils.showShort("修改成功");
                        finish();
                    }
                });
    }

}
