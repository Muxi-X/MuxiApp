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
import com.muxistudio.muxiio.data.SharesBean;
import com.muxistudio.muxiio.model.AddShare;
import com.muxistudio.muxiio.model.AfterEditShare;
import com.muxistudio.muxiio.model.EditShare;
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
    private int mShareId;
    private String mShareTag;
    private String mShareTitle;
    private String mShareContent;
    //if the intent is to edit the share, the flage will be set true
    private boolean mEditFlag = false;


    private String mTitleAddedSharing = "";
    private String mShareAddedSharing = "";
    private String mTagsAddedSharing = "product";

    private String mRawShare;
    public SharesBean mRawShareObject;

    //@BindView(R.id.btn_add_sharing_back)
    //ImageButton btnAddSharingBack;
    @BindView(R.id.toolbar_add_sharing)
    Toolbar mToolbarAddSharing;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.txv_add_sharing_theme)
    MaterialEditText mTxvAddSharingTheme;
    @BindView(R.id.edt_add_sharing_link)
    EditText mEdtAddSharingLink;
    @BindView(R.id.btn_add_sharing_confirm)
    Button mBtnAddSharingConfirm;

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
        setContentView(R.layout.activity_add_share);
        ButterKnife.bind(this);
        setExtras();
        initToolbar();
        setupTabs();
        setTabListener();
        getData();
    }

    private void setExtras(){
        Intent intent = getIntent();
        mEditFlag = intent.getBooleanExtra("editFlag",false);
        if(mEditFlag) {
            mRawShareObject = (SharesBean) intent.getExtras().getSerializable("rawShare");

            mShareId = intent.getIntExtra("shareId", -1);
            mShareTag = intent.getStringExtra("shareTag");
            mShareTitle = intent.getStringExtra("mShareTitleTxt");
            mShareContent = intent.getStringExtra("shareContent");

            setDefaultTab(mShareTag);
            mTitleAddedSharing = mShareTitle;
            mShareAddedSharing = mShareContent;

            mTxvAddSharingTheme.setText(mShareTitle);
            mEdtAddSharingLink.setText(mShareContent);
        }
    }
    //set a tab when editFlag set true
    private void setDefaultTab(String tag){
        if(tag.equals("PRODUCT")){
            mTabLayout.getTabAt(0).select();
        }
        if(tag.equals("DESIGN")){
            mTabLayout.getTabAt(1).select();
        }
        if(tag.equals("ANDROID")){
            mTabLayout.getTabAt(2).select();
        }
        if(tag.equals("FRONTEND")){
            mTabLayout.getTabAt(3).select();
        }
        if(tag.equals("BACKEND")){
            mTabLayout.getTabAt(4).select();
        }
    }
    private void initToolbar() {
        mToolbarAddSharing.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        if (mEditFlag){
            mToolbarAddSharing.setTitle("编辑分享");
        }else {
            mToolbarAddSharing.setTitle("新建分享");
        }
        mToolbarAddSharing.setTitleMarginStart(40);
        mToolbarAddSharing.setTitleMarginTop(20);
        setSupportActionBar(mToolbarAddSharing);
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
                mRawShare = data;

                String parsedStrings[] = MyTextUtils.getDataFromOtherApp(mRawShare);

                mTitleAddedSharing = parsedStrings[0];
                mShareAddedSharing = parsedStrings[1];

                mTxvAddSharingTheme.setText(parsedStrings[0]);
                mEdtAddSharingLink.setText(parsedStrings[1]);
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
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.product));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.design));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.android));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frontend));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.backend));
    }
    private void setTabListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mTabLayout.getSelectedTabPosition() == 0){
                    mTagsAddedSharing = "product";
                    ToastUtils.makeToast(AddShareActivity.this, "Product " , Toast.LENGTH_SHORT);
                }else if(mTabLayout.getSelectedTabPosition() == 1){
                    mTagsAddedSharing = "design";
                    ToastUtils.makeToast(AddShareActivity.this, "Design " , Toast.LENGTH_SHORT);
                }else if(mTabLayout.getSelectedTabPosition() == 2){
                    mTagsAddedSharing = "android";
                    ToastUtils.makeToast(AddShareActivity.this, "Android " , Toast.LENGTH_SHORT);
                }else if(mTabLayout.getSelectedTabPosition() == 3){
                    mTagsAddedSharing = "frontend";
                    ToastUtils.makeToast(AddShareActivity.this, "Frontend " , Toast.LENGTH_SHORT);
                }else if(mTabLayout.getSelectedTabPosition() == 4){
                    mTagsAddedSharing = "backend";
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
        mTitleAddedSharing = mTxvAddSharingTheme.getText().toString();
        mShareAddedSharing = mEdtAddSharingLink.getText().toString();
    }
    @OnClick({R.id.btn_add_sharing_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_sharing_confirm:
                //return true all are not ""
                mTitleAddedSharing = mTxvAddSharingTheme.getText().toString();
                mShareAddedSharing = mEdtAddSharingLink.getText().toString();
                if(checkShareMessage(mTitleAddedSharing, mShareAddedSharing, mTagsAddedSharing)) {
                    if(!mEditFlag){
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
        AddShare addShare = new AddShare(mTitleAddedSharing, mShareAddedSharing, mTagsAddedSharing);
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
        String content = mEdtAddSharingLink.getText().toString();
        String title = mTxvAddSharingTheme.getText().toString();

        EditShare editShare = new EditShare(content,title);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .putEditShare(UserInfo.shareToken,editShare,mShareId)
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
