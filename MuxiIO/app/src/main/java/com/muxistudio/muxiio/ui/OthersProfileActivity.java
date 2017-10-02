package com.muxistudio.muxiio.ui;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.adapter.OneShareAdapter;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.model.UserProfile;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.swipebackfragment.SwipeBackActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OthersProfileActivity extends SwipeBackActivity {

    private String mOthersProfileWeibo;
    private String mOthersProfileZhihu;
    private String mOthersProfileGithub;
    private RecyclerView mRecyclerView;
    private List<ShareList.SharesBean> mOneShareList = new ArrayList<>();
    private OneShareAdapter mOneShareAdapter;
    private int mOtherPeopleId = 0;

    @BindView(R.id.uploading_hint)
    RelativeLayout mUploadingLayout;
    @BindView(R.id.toolbar_others_profile)
    Toolbar mToolbarOthersProfile;
    @BindView(R.id.imv_others_profile_photo)
    CircleImageView mOtherProfileImg;
    @BindView(R.id.txv_others_profile_name)
    TextView mOtherProfileNameTxt;
    @BindView(R.id.txv_others_profile_blog)
    TextView mTxvOthersProfileBlog;
    @BindView(R.id.txv_others_profile_email)
    TextView mOtherProfileEmailTxt;
    @BindView(R.id.btn_others_profile_weibo)
    ImageButton mOtherProfileWeiboBtn;
    @BindView(R.id.btn_others_profile_zhihu)
    ImageButton mOtherProfileZhihuBtn;
    @BindView(R.id.btn_others_profile_github)
    ImageButton mOtherProfileGithubBtn;
    @BindView(R.id.txv_others_profile_group)
    TextView mTxvOthersProfileGroup;
    @BindView(R.id.txv_others_profile_start_time)
    TextView mTxvOthersProfileStartTime;
    @BindView(R.id.txv_others_profile_end_time)
    TextView mTxvOthersProfileEndTime;
    @BindView(R.id.txv_others_profile_duty)
    TextView mTxvOthersProfileDuty;
    @BindView(R.id.txv_others_profile_birthday)
    TextView mTxvOthersProfileBirthday;
    @BindView(R.id.txv_others_profile_home)
    TextView mTxvOthersProfileHome;
    @BindView(R.id.txv_others_profile_introduction)
    TextView mTxvOthersProfileIntroduction;
    @BindView(R.id.txv_profile_others_sharing)
    TextView mTxvProfileOthersSharing;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        //get otherUserId
        setExtras();
        ButterKnife.bind(this);
        initToolbar();
        initTextViews();
        initRecycleView();
        mUploadingLayout.setVisibility(View.VISIBLE);
        getProfile();
        getOneShareList();

    }
    private void setExtras(){
        Intent intent = getIntent();
        mOtherPeopleId = intent.getIntExtra("otherUserId",0);
    }
    //设置文字的下划线等等
    private void initTextViews(){
        mTxvOthersProfileBlog.setTextColor(getResources().getColor(R.color.colorGreen));
        mTxvOthersProfileBlog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mOtherProfileEmailTxt.setTextColor(getResources().getColor(R.color.colorGreen));
        mOtherProfileEmailTxt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
    public void initToolbar() {
        mToolbarOthersProfile.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        mToolbarOthersProfile.setTitle("");
        setSupportActionBar(mToolbarOthersProfile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getOthersProfile() {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .getUserProfile(UserInfo.authToken, mOtherPeopleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserProfile>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(UserProfile userProfile) {
                        mUploadingLayout.setVisibility(View.GONE);
                        UserProfile user  = userProfile;
                        initView(user);
                    }
                });
    }
    private void initView(UserProfile userProfile) {
        if (!"".equals(userProfile.getWeibo()) && userProfile.getWeibo() != null) {
            mOtherProfileWeiboBtn.setAlpha(1f);
        }
        if (!"".equals(userProfile.getZhihu()) && userProfile.getZhihu() != null) {
            mOtherProfileZhihuBtn.setAlpha(1f);
        }
        if (!"".equals(userProfile.getGithub()) && userProfile.getGithub() != null) {
            mOtherProfileGithubBtn.setAlpha(1f);
        }
        String url = userProfile.getAvatar_url();
        NetworkUtils.initPicture(OthersProfileActivity.this, userProfile.getAvatar_url()
                , mOtherProfileImg, mUploadingLayout);
        mOtherProfileNameTxt.setText(userProfile.getUsername());
        mTxvOthersProfileBlog.setText(userProfile.getPersonal_blog());
        mOtherProfileEmailTxt.setText(userProfile.getEmail());
        mTxvOthersProfileGroup.setText(userProfile.getGroup());
        mTxvOthersProfileBirthday.setText(userProfile.getBirthday());
        mTxvOthersProfileHome.setText(userProfile.getHometown());
        mTxvOthersProfileIntroduction.setText(userProfile.getInfo());
        mTxvOthersProfileStartTime.setText(userProfile.getTimejoin());
        mTxvOthersProfileEndTime.setText(userProfile.getTimeleft());
        mTxvOthersProfileDuty.setText(userProfile.getFlickr());
        mTxvProfileOthersSharing.setText(userProfile.getUsername());
        mOthersProfileWeibo = userProfile.getWeibo();
        mOthersProfileGithub = userProfile.getGithub();
        mOthersProfileZhihu = userProfile.getZhihu();
    }
    @OnClick({R.id.txv_others_profile_blog,
            R.id.txv_others_profile_email, R.id.btn_others_profile_weibo,
            R.id.btn_others_profile_zhihu, R.id.btn_others_profile_github })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txv_others_profile_blog:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(mTxvOthersProfileBlog.getText().toString())) {
                    intent1.setData(Uri.parse(MyTextUtils.urlFormat(mTxvOthersProfileBlog.getText().toString())));
                    startActivity(intent1);
                }
                break;
            case R.id.txv_others_profile_email:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(mOtherProfileEmailTxt.getText().toString())) {
                    intent2.setData(Uri.parse(MyTextUtils.urlFormat(mOtherProfileEmailTxt.getText().toString())));
                    startActivity(intent2);
                }
                break;
            case R.id.btn_others_profile_weibo:
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(mOthersProfileWeibo)) {
                    intent3.setData(Uri.parse(MyTextUtils.urlFormat(mOthersProfileWeibo)));
                    startActivity(intent3);
                }
                break;
            case R.id.btn_others_profile_zhihu:
                Intent intent4 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(mOthersProfileZhihu)) {
                    intent4.setData(Uri.parse(MyTextUtils.urlFormat(mOthersProfileZhihu)));
                    startActivity(intent4);
                }
                break;
            case R.id.btn_others_profile_github:
                Intent intent5 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(mOthersProfileGithub)) {
                    intent5.setData(Uri.parse(MyTextUtils.urlFormat(mOthersProfileGithub)));
                    startActivity(intent5);
                }
                break;
        }
    }
    private void getOneShareList() {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .getUserAllShare(mOtherPeopleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ShareList>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(ShareList list1) {
                        List<ShareList.SharesBean> list = list1.getShares();
                        int size =list.size();
                        for (int i = size - 1;i >= 0; i--) {
                            mOneShareList.add(list.get(i));
                        }
                        mOneShareAdapter.notifyDataSetChanged();
                    }
                });
    }
    private void initRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_alone);
       // oneShareList = getOneShareList();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mOneShareAdapter = new OneShareAdapter(OthersProfileActivity.this, R.layout.item_one_share, mOneShareList);
        mRecyclerView.setAdapter(mOneShareAdapter);
    }
    private void getProfile(){
        getOthersProfile();
    }


}
