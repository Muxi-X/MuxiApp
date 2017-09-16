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

    private String othersProfileWeibo;
    private String othersProfileZhihu;
    private String othersProfileGithub;
    private RecyclerView recyclerView;
    private List<ShareList.SharesBean> oneShareList = new ArrayList<>();
    private OneShareAdapter oneShareAdapter;
    private int otherPeopleId = 0;

    @BindView(R.id.uploading_hint)
    RelativeLayout uploadingLayout;
    @BindView(R.id.toolbar_others_profile)
    Toolbar toolbarOthersProfile;
    @BindView(R.id.imv_others_profile_photo)
    CircleImageView otherProfileImg;
    @BindView(R.id.txv_others_profile_name)
    TextView otherProfileNameTxt;
    @BindView(R.id.txv_others_profile_blog)
    TextView txvOthersProfileBlog;
    @BindView(R.id.txv_others_profile_email)
    TextView otherProfileEmailTxt;
    @BindView(R.id.btn_others_profile_weibo)
    ImageButton otherProfileWeiboBtn;
    @BindView(R.id.btn_others_profile_zhihu)
    ImageButton otherProfileZhihuBtn;
    @BindView(R.id.btn_others_profile_github)
    ImageButton otherProfileGithubBtn;
    @BindView(R.id.txv_others_profile_group)
    TextView txvOthersProfileGroup;
    @BindView(R.id.txv_others_profile_start_time)
    TextView txvOthersProfileStartTime;
    @BindView(R.id.txv_others_profile_end_time)
    TextView txvOthersProfileEndTime;
    @BindView(R.id.txv_others_profile_duty)
    TextView txvOthersProfileDuty;
    @BindView(R.id.txv_others_profile_birthday)
    TextView txvOthersProfileBirthday;
    @BindView(R.id.txv_others_profile_home)
    TextView txvOthersProfileHome;
    @BindView(R.id.txv_others_profile_introduction)
    TextView txvOthersProfileIntroduction;
    @BindView(R.id.txv_profile_others_sharing)
    TextView txvProfileOthersSharing;

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
        uploadingLayout.setVisibility(View.VISIBLE);
        getProfile();
        getOneShareList();

    }
    private void setExtras(){
        Intent intent = getIntent();
        otherPeopleId = intent.getIntExtra("otherUserId",0);
    }
    //设置文字的下划线等等
    private void initTextViews(){
        txvOthersProfileBlog.setTextColor(getResources().getColor(R.color.colorGreen));
        txvOthersProfileBlog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        otherProfileEmailTxt.setTextColor(getResources().getColor(R.color.colorGreen));
        otherProfileEmailTxt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
    public void initToolbar() {
        toolbarOthersProfile.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbarOthersProfile.setTitle("");
        setSupportActionBar(toolbarOthersProfile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getOthersProfile() {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .getUserProfile(UserInfo.authToken,otherPeopleId)
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
                        uploadingLayout.setVisibility(View.GONE);
                        UserProfile user  = userProfile;
                        initView(user);
                    }
                });
    }
    private void initView(UserProfile userProfile) {
        if (!"".equals(userProfile.getWeibo()) && userProfile.getWeibo() != null) {
            otherProfileWeiboBtn.setAlpha(1f);
        }
        if (!"".equals(userProfile.getZhihu()) && userProfile.getZhihu() != null) {
            otherProfileZhihuBtn.setAlpha(1f);
        }
        if (!"".equals(userProfile.getGithub()) && userProfile.getGithub() != null) {
            otherProfileGithubBtn.setAlpha(1f);
        }
        String url = userProfile.getAvatar_url();
        NetworkUtils.initPicture(OthersProfileActivity.this, userProfile.getAvatar_url()
                , otherProfileImg,uploadingLayout);
        otherProfileNameTxt.setText(userProfile.getUsername());
        txvOthersProfileBlog.setText(userProfile.getPersonal_blog());
        otherProfileEmailTxt.setText(userProfile.getEmail());
        txvOthersProfileGroup.setText(userProfile.getGroup());
        txvOthersProfileBirthday.setText(userProfile.getBirthday());
        txvOthersProfileHome.setText(userProfile.getHometown());
        txvOthersProfileIntroduction.setText(userProfile.getInfo());
        txvOthersProfileStartTime.setText(userProfile.getTimejoin());
        txvOthersProfileEndTime.setText(userProfile.getTimeleft());
        txvOthersProfileDuty.setText(userProfile.getFlickr());
        txvProfileOthersSharing.setText(userProfile.getUsername());
        othersProfileWeibo = userProfile.getWeibo();
        othersProfileGithub = userProfile.getGithub();
        othersProfileZhihu = userProfile.getZhihu();
    }
    @OnClick({R.id.txv_others_profile_blog,
            R.id.txv_others_profile_email, R.id.btn_others_profile_weibo,
            R.id.btn_others_profile_zhihu, R.id.btn_others_profile_github })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txv_others_profile_blog:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( txvOthersProfileBlog.getText().toString())) {
                    intent1.setData(Uri.parse(MyTextUtils.urlFormat(txvOthersProfileBlog.getText().toString())));
                    startActivity(intent1);
                }
                break;
            case R.id.txv_others_profile_email:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( otherProfileEmailTxt.getText().toString())) {
                    intent2.setData(Uri.parse(MyTextUtils.urlFormat(otherProfileEmailTxt.getText().toString())));
                    startActivity(intent2);
                }
                break;
            case R.id.btn_others_profile_weibo:
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( othersProfileWeibo)) {
                    intent3.setData(Uri.parse(MyTextUtils.urlFormat(othersProfileWeibo)));
                    startActivity(intent3);
                }
                break;
            case R.id.btn_others_profile_zhihu:
                Intent intent4 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink( othersProfileZhihu)) {
                    intent4.setData(Uri.parse(MyTextUtils.urlFormat(othersProfileZhihu)));
                    startActivity(intent4);
                }
                break;
            case R.id.btn_others_profile_github:
                Intent intent5 = new Intent(Intent.ACTION_VIEW);
                if (!MyTextUtils.errorLink(othersProfileGithub)) {
                    intent5.setData(Uri.parse(MyTextUtils.urlFormat(othersProfileGithub)));
                    startActivity(intent5);
                }
                break;
        }
    }
    private void getOneShareList() {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .getUserAllShare(otherPeopleId)
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
                            oneShareList.add(list.get(i));
                        }
                        oneShareAdapter.notifyDataSetChanged();
                    }
                });
    }
    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_alone);
       // oneShareList = getOneShareList();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        oneShareAdapter = new OneShareAdapter(OthersProfileActivity.this, R.layout.item_one_share, oneShareList);
        recyclerView.setAdapter(oneShareAdapter);
    }
    private void getProfile(){
        getOthersProfile();
    }


}
