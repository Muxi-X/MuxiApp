package com.muxistudio.muxiio.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import static com.muxistudio.muxiio.R.id.txv_profile_blog;
import static com.muxistudio.muxiio.model.UserInfo.username;

public class MyProfileActivity extends SwipeBackActivity{

    private RecyclerView mRecyclerView;
    private List<ShareList.SharesBean> mSharesBeanList = new ArrayList<>();
    private OneShareAdapter mOneShareAdapter;

    @BindView(R.id.uploading_hint)
    RelativeLayout mUpLoadingLayout;
    @BindView(R.id.btn_profile_edit)
    ImageButton mBtnProfileEdit;
    @BindView(R.id.toolbar_profile)
    Toolbar mToolbarProfile;
    @BindView(R.id.imv_profile_photo)
    CircleImageView mImvProfilePhoto;
    @BindView(R.id.txv_profile_name)
    TextView mTxvProfileName;
    @BindView(R.id.txv_profile_email)
    TextView mTxvProfileEmail;
    @BindView(R.id.btn_profile_weibo)
    ImageButton mBtnProfileWeibo;
    @BindView(R.id.btn_profile_zhihu)
    ImageButton mBtnProfileZhihu;
    @BindView(R.id.btn_profile_github)
    ImageButton mBtnProfileGithub;
    @BindView(R.id.txv_profile_group)
    TextView mTxvProfileGroup;
    @BindView(R.id.txv_profile_birthday)
    TextView mTxvProfileBirthday;
    @BindView(R.id.txv_profile_home)
    TextView mTxvProfileHome;
    @BindView(R.id.txv_profile_introduction)
    TextView mTxvProfileIntroduction;
    @BindView(R.id.txv_profile_my_sharing)
    TextView mTxvProfileMySharing;
    @BindView(R.id.txv_profile_blog)
    TextView mTxvProfileBlog;
    @BindView(R.id.txv_profile_start_time)
    TextView mTxvProfileStartTime;
    @BindView(R.id.txv_profile_end_time)
    TextView mTxvProfileEndTime;
    @BindView(R.id.txv_profile_duty)
    TextView mTxvProfileDuty;


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
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initToolbar();
        initTextViews();
        getProfile();
        initRecycleView();
    }

    private void initTextViews(){
        //init email and blog
        mTxvProfileBlog.setTextColor(getResources().getColor(R.color.colorGreen));
        mTxvProfileBlog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTxvProfileEmail.setTextColor(getResources().getColor(R.color.colorGreen));
        mTxvProfileEmail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mUpLoadingLayout.setVisibility(View.VISIBLE);
    }
    public void initToolbar() {
        mToolbarProfile.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        mToolbarProfile.setTitle("");
        setSupportActionBar(mToolbarProfile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getProfile() {
        Log.d("fuck", "getProfile: "+UserInfo.authToken);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .getUserProfile(UserInfo.authToken, UserInfo.userAuthId)
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
                        UserInfo.userBirthday = userProfile.getBirthday();
                        UserInfo.userEmail = userProfile.getEmail();
                        UserInfo.userFlickr = userProfile.getFlickr();
                        UserInfo.userGithub = userProfile.getGithub();
                        if(userProfile.equals("")||userProfile==null){
                            UserInfo.userGroup = "默认";
                        }else{
                            UserInfo.userGroup = userProfile.getGroup();
                        }
                        UserInfo.userHometown = userProfile.getHometown();
                        UserInfo.userAuthId = userProfile.getId();
                        UserInfo.userInfo = userProfile.getInfo();
                        UserInfo.userPersonalBlog = userProfile.getPersonal_blog();
                        UserInfo.userTimeJoin = userProfile.getTimejoin();
                        UserInfo.userTimeLeft = userProfile.getTimeleft();
                        username = userProfile.getUsername();
                        UserInfo.userWeibo = userProfile.getWeibo();
                        UserInfo.userZhihu = userProfile.getZhihu();
                        initView();
                    }
                });
    }
    private void initView(){
        if (!"".equals(UserInfo.userWeibo) && UserInfo.userWeibo != null) {
            mBtnProfileWeibo.setAlpha(1f);
        }
        if (!"".equals(UserInfo.userZhihu) && UserInfo.userZhihu != null) {
            mBtnProfileZhihu.setAlpha(1f);
        }
        if (!"".equals(UserInfo.userGithub) && UserInfo.userGithub != null) {
            mBtnProfileGithub.setAlpha(1f);
        }
        NetworkUtils.initPicture(MyProfileActivity.this, UserInfo.userAvatarUrl, mImvProfilePhoto, mUpLoadingLayout);
        mTxvProfileName.setText(username);
        mTxvProfileBlog.setText(UserInfo.userPersonalBlog);
        mTxvProfileEmail.setText(UserInfo.userEmail);
        mTxvProfileGroup.setText(UserInfo.userGroup);
        mTxvProfileBirthday.setText(UserInfo.userBirthday);
        mTxvProfileHome.setText(UserInfo.userHometown);
        mTxvProfileIntroduction.setText(UserInfo.userInfo);
        mTxvProfileStartTime.setText(UserInfo.userTimeJoin);
        mTxvProfileEndTime.setText(UserInfo.userTimeLeft);
        mTxvProfileDuty.setText(UserInfo.userFlickr);
    }

    @OnClick({R.id.txv_profile_blog, R.id.btn_profile_edit,
            R.id.txv_profile_email, R.id.btn_profile_weibo, R.id.btn_profile_zhihu,
            R.id.btn_profile_github })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_profile_edit:
                Intent intent6 = new Intent();
                intent6.setClass(MyProfileActivity.this, EditProfileActivity.class);
                startActivity(intent6);
                finish();
                break;
            case txv_profile_blog:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);

                if (!MyTextUtils.errorLink(UserInfo.userPersonalBlog)) {
                    intent1.setData(Uri.parse(MyTextUtils.urlFormat(UserInfo.userPersonalBlog)));
                    startActivity(intent1);
                }
                break;
            case R.id.txv_profile_email:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( UserInfo.userEmail)) {
                    intent2.setData(Uri.parse(MyTextUtils.urlFormat(UserInfo.userEmail)));
                    startActivity(intent2);
                }
                break;
            case R.id.btn_profile_weibo:
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(UserInfo.userWeibo)) {
                    intent3.setData(Uri.parse(MyTextUtils.urlFormat(UserInfo.userWeibo)));
                    startActivity(intent3);
                }
                break;
            case R.id.btn_profile_zhihu:
                Intent intent4 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( UserInfo.userZhihu)) {
                    intent4.setData(Uri.parse(MyTextUtils.urlFormat(UserInfo.userZhihu)));
                    startActivity(intent4);
                }
                break;
            case R.id.btn_profile_github:
                Intent intent5 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( UserInfo.userGithub)) {
                    intent5.setData(Uri.parse(MyTextUtils.urlFormat(UserInfo.userGithub)));
                    startActivity(intent5);
                }
                break;
        }
    }

    private void initRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_alone);
        getOneShareList();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mOneShareAdapter = new OneShareAdapter(MyProfileActivity.this, R.layout.item_one_share, mSharesBeanList);
        mRecyclerView.setAdapter(mOneShareAdapter);
    }

    private void getOneShareList() {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .getUserAllShare(UserInfo.userShareId)
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
                        int size = list.size();
                        for (int i = size - 1; i >= 0; i--) {
                            ShareList.SharesBean bean = list.get(i);
                            mSharesBeanList.add(bean);
                        }
                        mOneShareAdapter.notifyDataSetChanged();
                    }
                });
    }

}
