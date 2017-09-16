package com.muxistudio.muxiio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.adapter.CommentAdapter;
import com.muxistudio.muxiio.model.AddComments;
import com.muxistudio.muxiio.model.AddCommentsSuccessful;
import com.muxistudio.muxiio.model.Comments;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.KeyBoardUtils;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.NetworkUtils;
import com.muxistudio.muxiio.utils.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.swipebackfragment.SwipeBackActivity;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by kolibreath on 17-8-8.
 */

public class CommentActivity extends SwipeBackActivity{
    @BindView(R.id.alert_nothing)
    RelativeLayout nothingHint;
    @BindView(R.id.share_title)
    TextView mShareTitleTxt;
    @BindView(R.id.share_content)
    TextView mShareContentTxt;
    @BindView(R.id.uploading_hint)
    RelativeLayout upLoadingProgress;
    @BindView(R.id.share_time)
    TextView mShareTimeTxt;
    @BindView(R.id.share_username)
    TextView mShareUsernameTxt;
    @BindView(R.id.share_avatar)
    CircleImageView mShareAvatarImg;
    @BindView(R.id.comment_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.comment_toolbar)
    Toolbar toolbar;
    @BindView(R.id.comment_list)
    ListView mCommentListView;
    @BindView(R.id.comment_edit)
    MaterialEditText mCommentEdtTxt;
    @BindView(R.id.comment_send)
    ImageButton mCommentSendBtn;
    @OnClick(R.id.comment_send)
    public void onSend() {
        commentsString = mCommentEdtTxt.getText().toString();
        if(commentsString.equals("")) {
            ToastUtils.showShort("至少也要写一点点评论在发送吧...");
        } else {
            AddComments(shareId);
        }
        mCommentEdtTxt.setText("");
        //add the newest share to top of list
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("DD MM YYYY", Locale.CHINA);
        String parseDate = format.format(date);
        addToTopOfList(new Comments.CommentBean(UserInfo.userAvatarUrl
                ,commentsString, parseDate,UserInfo.username));
    }
    private String shareAvatarUrl;
    private String shareTime;
    private String shareUsername;
    private String shareDate;
    private String shareContent;
    private String shareTitle;
    private String commentsString;
    private int shareId;
    private CommentAdapter adapter;
    private List<Comments.CommentBean> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initView();
        upLoadingProgress.setVisibility(View.VISIBLE);
        initSwipeRefresh();
        NetworkUtils.initPicture(CommentActivity.this,shareAvatarUrl, mShareAvatarImg,upLoadingProgress);
    }

    private void getExtra(){
       // Bundle bundle = getIntent().getExtras();
        ShareList.SharesBean bean = (ShareList.SharesBean) getIntent().getSerializableExtra("shareInfo");
        shareId = bean.getId();
        shareAvatarUrl = bean.getAvatar();
        shareUsername = bean.getUsername();
        shareDate = bean.getDate();
        shareTitle = bean.getTitle();
        shareContent = bean.getShare();
    }
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
    public static void start(Context context){
        Intent starter = new Intent(context,CommentActivity.class);
        context.startActivity(starter);
    }
    private void initSwipeRefresh(){
        swipeRefreshLayout.setColorSchemeColors(Color.GRAY,Color.BLACK,Color.BLUE);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setRefreshing(false);
        //init listview and send request
         initListView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initListView();
                nothingHint.setVisibility(View.GONE);
            }
        });
    }
    //初始化toolbar 并且设置一些相关信息：比如share的时间等
    private void initView(){
        getExtra();
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setTitle("评论");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //传递过来的日期格式是：
        shareTime = MyTextUtils.parseTime(shareDate);
       //String parsedTime1 = MyTextUtils.parseDate(shareTime);
       // String parsedTime2 = MyTextUtils.formatDateUsingSlash(parsedTime1);
        mShareTimeTxt.setText(shareTime);
        mShareUsernameTxt.setText(shareUsername);
        mShareTitleTxt.setText(shareTitle);
        mShareContentTxt.setText(shareContent);
    }
    private void initListView(){
        //这是一个异步请求 在完成请求的时候会notifydata 所以大可不必担心list data的数据问题！
        getCommentList();
        adapter = new CommentAdapter(CommentActivity.this,R.layout.item_comment
        ,commentsList);
        mCommentListView.setAdapter(adapter);
    }
    private void addToTopOfList(Comments.CommentBean bean){
        commentsList.add(bean);

    }
    private void AddComments(int shareId){
        final AddComments comments = new AddComments(commentsString);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .postCommentsAdd(UserInfo.shareToken,shareId,comments)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Response<AddCommentsSuccessful>>() {
                    @Override
                    public void call(Response<AddCommentsSuccessful> addCommentsSuccessfulResponse) {
                        int code = addCommentsSuccessfulResponse.code();
                        if(code==200){
                            ToastUtils.showShort("发送成功");
                            KeyBoardUtils.hideKeyBoard(CommentActivity.this);
                        }
                    }
                });
    }
    private void getCommentList(){
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .getCommentList(UserInfo.shareToken,shareId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Comments>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        NetworkUtils.downloadingTimeout(CommentActivity.this,e,upLoadingProgress);
                        ToastUtils.showShort("哎呦,出故障了");
                        swipeRefreshLayout.setRefreshing(false);
                        nothingHint.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onNext(Comments comments) {
                        commentsList = comments.getComment();
                        if (commentsList.size() == 0) {
                            nothingHint.setVisibility(View.VISIBLE);
                        } else {
                            adapter.notifyDataSetChanged();
                            nothingHint.setVisibility(View.GONE);
                            Drawable divider = getResources().getDrawable(R.drawable.comment_divider);
                            mCommentListView.setDivider(divider);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
