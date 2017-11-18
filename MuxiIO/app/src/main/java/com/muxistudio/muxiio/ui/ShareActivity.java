package com.muxistudio.muxiio.ui;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.adapter.ShareAdapter;
import com.muxistudio.muxiio.data.SharesBean;
import com.muxistudio.muxiio.listener.MyItemClickListener;
import com.muxistudio.muxiio.model.DeleteInfo;
import com.muxistudio.muxiio.model.LoginInfo;
import com.muxistudio.muxiio.model.RegisterInfo;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.Token;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.IRetrofit;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.AlertDialogUtils;
import com.muxistudio.muxiio.utils.CacheUtils;
import com.muxistudio.muxiio.utils.ListUtils;
import com.muxistudio.muxiio.utils.MyAnimationUtils;
import com.muxistudio.muxiio.utils.NetworkUtils;
import com.muxistudio.muxiio.utils.PreferenceUtils;
import com.muxistudio.muxiio.utils.SnackbarUtils;
import com.muxistudio.muxiio.utils.ToastUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.muxistudio.muxiio.R.menu.share;
import static com.muxistudio.muxiio.model.UserInfo.username;
import static com.muxistudio.muxiio.utils.ListUtils.add;

public class ShareActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean isStatusClicked = false;
    private boolean isShareClicked = false;
    public static Activity INSTANCE;
    private List<SharesBean> shareBeanList = new ArrayList<>();
    private List<SharesBean> templist = new ArrayList<>();
    private LinearLayoutManager manager;
    private ShareAdapter adapter = new ShareAdapter(shareBeanList);
    private final int VISIBLE = 1;
    private final int INVISIBLE = -1;
    private int fabStatus = VISIBLE;
    private static final int SHARE_TOTAL = 20;
    private AnimatorSet inSet;
    private AnimatorSet outSet;
    private DeleteBroadCastReceiver receiver = new DeleteBroadCastReceiver();
    private int deleteShareId = -1;
    private int deletePostion = -1;
    private DialogInterface.OnClickListener deletePositiveListener = (dialogInterface, i) -> {
        dialogInterface.dismiss();
        shareDelete(deleteShareId, deletePostion);
    };
    private DialogInterface.OnClickListener deleteNegetiveListener = (dialogInterface, i) -> dialogInterface.dismiss();
    private String sort = "";
    @BindView(R.id.cl_share)
    CoordinatorLayout clShare;
    @BindView(R.id.uploading_hint)
    RelativeLayout loadingView;
    @BindView(R.id.shareaci_recyc)
    RecyclerView recyclerView;
    @BindView(R.id.share_btn)
    FloatingActionButton fabBtn;
    //TextView mUserNameTxt;

    @OnClick(R.id.share_btn)
    public void clickShare() {
        Intent intent = new Intent(ShareActivity.this, AddShareActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.shareaci_swipelayout)
    SwipeRefreshLayout mRefreshLayout;
    //如果用户有新的消息的话则将会有小红点表示提醒
    @Nullable
    @BindView(R.id.share_message_hint)
    ImageView mUserMessageBadge;
    TextView mUserNameTxt;
    NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView mUserAvatarImg;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(share,menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share);
        INSTANCE = this;
        registerReceiver(receiver, new IntentFilter(ShareAdapter.DELETE_ACTION));
        ButterKnife.bind(this);
        initRecyclerView();
        shareAuth();
        initDrawer();
        initSwipeLayout();
        //初始化所有的showcaseView
        initShowCaseView();
        loadingView.setVisibility(View.VISIBLE);
        //向shareBeanList中加载数据
        loadShareData();
        hideItem();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.android:
                getShareList("android");
                sort = "android";
                //Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.backend:
                getShareList("backend");
                sort = "backend";
                //Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.frontend:
                getShareList("frontend");
                sort = "frontend";
                //Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.product:
                getShareList("product");
                sort = "product";
                //Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.design:
                getShareList("design");
                sort = "design";
                //Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.all:
                getShareList("all");
                sort = "all";
                //Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                isShareClicked = true;
                //no use2333
                item.setCheckable(true);
                drawer.closeDrawers();
                item.setCheckable(false);
                break;
            case R.id.action_message:
                item.setCheckable(true);
                drawer.closeDrawers();
                item.setCheckable(false);
                Intent intent = new Intent(ShareActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.action_status:
                isStatusClicked = true;
                item.setCheckable(true);
                showItem();
                SnackbarUtils.showShort(clShare, "该功能尚未开启");
                drawer.closeDrawers();
                item.setCheckable(false);
                break;
            case R.id.action_logout:
                item.setCheckable(true);
                drawer.closeDrawers();
                item.setCheckable(false);
                Intent intent1 = new Intent(ShareActivity.this, LoginActivity.class);
                intent1.putExtra("fromShare", true);
                startActivity(intent1);
                finish();
                break;
            case R.id.action_hidden:
                if(isStatusClicked&&isShareClicked){
                    isStatusClicked = false;
                    isShareClicked = false;
                    hideItem();
                    Intent intent2  = new Intent(ShareActivity.this,HiddenChartActivity.class);
                    startActivity(intent2);
                }
        }
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    @Override
    protected void onStop() {
        super.onStop();
        shareBeanList.clear();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                int current = manager.findFirstCompletelyVisibleItemPosition();
                if (!(current == 0)) {
                    recyclerView.smoothScrollToPosition(0);
                } else {
                    finish();
                }
                break;
        }
        return false;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            if(UserInfo.isOpenBroswer = true){
                recyclerView.smoothScrollToPosition(UserInfo.sharePosition);
                UserInfo.isOpenBroswer = false;
            }
            shareBeanList = CacheUtils.readListCache(CacheUtils.SHARE_LIST_KEY,SHARE_TOTAL);
            //这个储存的方法暂时是失效的...
             mUserAvatarImg.setImageBitmap(CacheUtils.readBitmapCache(CacheUtils.BITMAP_KEY));
            NetworkUtils.initPicture(ShareActivity.this,UserInfo.userAvatarUrl,mUserAvatarImg,
                    loadingView);
            adapter.setList(shareBeanList);
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        INSTANCE = this;
        ButterKnife.bind(this);
      //`  loadingView.setVisibility(View.VISIBLE);
        mUserNameTxt.setText(PreferenceUtils.readString(R.string.userName));
        mUserAvatarImg.setImageBitmap(CacheUtils.readBitmapCache(CacheUtils.BITMAP_KEY));
    }
    //这个方法是一个包装方法包装了两个loadData() 一个loadData()在shareBeanList为空的时候发起网络请求
    //另一个是从文件中读取缓存的数据
    private void loadShareData() {
        try {
            templist  = CacheUtils.readListCache(CacheUtils.SHARE_LIST_KEY,20);
            shareBeanList = CacheUtils.readListCache(CacheUtils.SHARE_TEMPLIST_KEY,20);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(shareBeanList.isEmpty()){
            try {
                loadData();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            adapter.setList(shareBeanList);
            int netSig = NetworkUtils.getNetworkStatus();
            NetworkUtils.makeToast(netSig);
            if(netSig==NetworkUtils.UNCONNECTED){
                loadingView.setVisibility(View.GONE);
                return;
            }
            adapter.notifyDataSetChanged();
        }
    }
    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    //判断list和 templist比较 然后弹出 是否有更新
    private void loadData(List<SharesBean> list) throws FileNotFoundException {
        if(shareBeanList.isEmpty()){
            add(list,shareBeanList);
            add(list,templist);
            Collections.sort(shareBeanList);
            Collections.sort(templist);
            adapter.setList(shareBeanList);
            adapter.notifyDataSetChanged();
            CacheUtils.saveListCache(CacheUtils.SHARE_LIST_KEY,shareBeanList);
            CacheUtils.saveListCache(CacheUtils.SHARE_TEMPLIST_KEY,templist);
           // loadingView.setVisibility(View.GONE);
            return;
        }
        templist = CacheUtils.readListCache(CacheUtils.SHARE_TEMPLIST_KEY,20);
        Log.d("lists", "loadData: "+shareBeanList.get(0).getDate());
        Log.d("lists","loadData: "+templist.get(0).getDate());
        if(ListUtils.equals(shareBeanList,list)&&!templist.isEmpty()){
            ToastUtils.showShort("没有最新的分享了");
        }else {
            if (sort.equals("") || sort.equals("all")) {
                CacheUtils.clearItems(CacheUtils.SHARE_LIST_KEY, shareBeanList);
                ListUtils.add(list, shareBeanList);
                ListUtils.add(list, templist);
              //  Collections.sort(shareBeanList);
                adapter.setList(shareBeanList);
                CacheUtils.saveListCache(CacheUtils.SHARE_LIST_KEY, shareBeanList);
                //loadingView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }else{
                shareBeanList.clear();
                ListUtils.add(list,shareBeanList);
                ListUtils.add(list,templist);
                adapter.setList(shareBeanList);
                adapter.notifyDataSetChanged();
            }
        }
    }
    //在oncreate从文件中向shareBeanList加载数据
    private void loadData() throws FileNotFoundException {
        templist = CacheUtils.readListCache(CacheUtils.SHARE_LIST_KEY,20);
        shareBeanList = CacheUtils.readListCache(CacheUtils.SHARE_LIST_KEY,20);
        if(shareBeanList.isEmpty()){
            getShareList(sort);
            return;
        }
    }
    public  static void start(Context context){
        Intent starter = new Intent(context,ShareActivity.class);
        context.startActivity(starter);
    }
    private void flipCard(View view, View anotherView, Boolean viewTag) {
        //  Boolean viewTag = (boolean) view.getTag();
        if (viewTag == null || viewTag) {
            outSet.setTarget(view);
            inSet.setTarget(anotherView);
            view.setTag(false);
            //((CardView) view).setCardElevation(0);
            outSet.start();
            //((CardView)anotherView).setCardElevation(3);
            inSet.start();

        } else {
            outSet.setTarget(anotherView);
            inSet.setTarget(view);
            view.setTag(true);
            // ((CardView) view).setCardElevation(0);
            outSet.start();
            //((CardView)anotherView).setCardElevation(3);
            inSet.start();
        }
    }
    private void initSwipeLayout() {
        //init swipelayout and set listener
        mRefreshLayout.setColorSchemeColors(Color.GRAY, Color.BLACK, Color.BLUE);
        mRefreshLayout.setDistanceToTriggerSync(100);
        mRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mRefreshLayout.setOnRefreshListener(() -> {
            NetworkUtils.makeToast(NetworkUtils.getNetworkStatus());
            getShareList(sort);
        });
    }

    //设置一些showcaseview中的东西
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initShowCaseView(){
        int ifFirstLogin = PreferenceUtils.readInteger(R.string.first_login);
        if(ifFirstLogin>0){
            View view = findViewById(R.id.v_target_1);

            TextPaint contentPaint = new TextPaint(),titlePaint = new TextPaint();
            contentPaint.setColor(getResources().getColor(R.color.white));
            titlePaint.setColor(getResources().getColor(R.color.white));

            Button button3 = new Button(this);
            //给第一个RecyclerView的item这个showcaseview
            ShowcaseView showCaseView1 = new ShowcaseView.Builder(this)
                    .setContentTitlePaint(titlePaint)
                    .setContentTextPaint(contentPaint)
                    .setTarget(new ViewTarget(view))
                    .setContentText("长按可以查看分享的详情")
                    .setContentTitle("分享条目")
                    .replaceEndButton(button3)
                    .setStyle(R.style.Custom_semi_transparent_demo)
                    .build();


            showCaseView1.hide();

            //给floatingActionButton设置view
            Button button = new Button(this);
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_circle_fab));
            ShowcaseView showCaseView2 =new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(fabBtn))
                    .setContentText("点击这个可以新增一个分享")
                    .setContentTextPaint(contentPaint)
                    .setContentTitlePaint(titlePaint)
                    .setContentTitle("Add 新增分享")
                    .hideOnTouchOutside()
                    .setStyle(R.style.Custom_semi_transparent_demo)
                    .replaceEndButton(button)
                    .build();


            Button button1 = new Button(this);
            ShowcaseView showcaseView3 = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(findViewById(R.id.v_target_2)))
                    .setContentText("点击可以选择不同的组别查看分享")
                    .setContentTextPaint(contentPaint)
                    .setContentTitlePaint(titlePaint)
                    .setContentTitle("组别选择")
                    .hideOnTouchOutside()
                    .replaceEndButton(button1)
                    .setStyle(R.style.Custom_semi_transparent_demo)
                    .build();
            showcaseView3.hide();

            button.setOnClickListener(v->{button.setVisibility(View.VISIBLE);showCaseView1.show();showCaseView2.hide();});
            button1.setOnClickListener(v->{showCaseView1.hide();showcaseView3.setVisibility(View.GONE);});
            button3.setOnClickListener(v -> {showCaseView1.hide();showcaseView3.show();});
            PreferenceUtils.storeInteger(R.string.first_login,0);

        }
    }
    private void initRecyclerView() {
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        initAdapterListener(adapter);
        //listen to the state
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                floatingActionButtonAnimation(dy);
            }
        });

        //recyclerview animation
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(300);
        animator.setMoveDuration(300);
        recyclerView.setItemAnimator(animator);
    }
    private void initAdapterListener(ShareAdapter adapter) {
        adapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, View anotherView, int position) {
            }

            @Override
            public void OnItemLongClick(View view, View anotherView, final int position) {
                outSet = (AnimatorSet) AnimatorInflater.loadAnimator(ShareActivity.this, R.animator.anim_in);
                inSet = (AnimatorSet) AnimatorInflater.loadAnimator(ShareActivity.this, R.animator.anim_out);

                if (anotherView == null) {
                    int id = view.getId();
                    switch (id) {
                        case R.id.cardv_content:
                            break;
                    }
                } else {
                    Boolean viewTag;
                    //this tag is from flip(); distinguish two sides
                    if ((viewTag = (Boolean) view.getTag()) == null) {
                        viewTag = false;
                    } else {
                        viewTag = (Boolean) view.getTag();
                    }
                    flipCard(view, anotherView, viewTag);
                }

            }
        });
    }
    private void initNavigationHeader() {
        View view = navigationView.getHeaderView(0);
        mUserNameTxt = (TextView) view.findViewById(R.id.share_username);
        mUserAvatarImg = (CircleImageView) view.findViewById(R.id.share_user_avatar);
        mUserAvatarImg.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setClass(ShareActivity.this, MyProfileActivity.class);
            startActivity(intent);
        });
        mUserNameTxt.setText(PreferenceUtils.readString(R.string.userName));
    }

    private void hideItem(){
        MenuItem item = navigationView.getMenu().findItem(R.id.action_hidden);
        item.setVisible(false);
    }
    private void showItem(){
        MenuItem item = navigationView.getMenu().findItem(R.id.action_hidden);
        item.setVisible(true);
    }
    private void floatingActionButtonAnimation(int dy) {
        switch (fabStatus) {
            case VISIBLE:
                if (dy > 0) {
                    //do nothing
                } else {
                    MyAnimationUtils.FABAnimation(R.anim.fab_sink_anim
                            , fabBtn, ShareActivity.this);
                    fabStatus = INVISIBLE;
                }
                break;
            case INVISIBLE:
                if (dy < 0) {
                } else {
                    MyAnimationUtils.FABAnimation(R.anim.fab_popup_anim
                            , fabBtn, ShareActivity.this);
                    fabStatus = VISIBLE;
                }
                break;
        }
    }
    //先尝试注册，如果成功注册或者已经注册完成，就登录
    private void shareAuth() {
        RegisterInfo info = new RegisterInfo(UserInfo.userEmail, username,UserInfo.userpwd);
        final IRetrofit iRetrofit = MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE);
         iRetrofit.postShareRegister(info)
                 .subscribeOn(Schedulers.io())
                 .observeOn(Schedulers.io())
                 .flatMap(createIdResponse -> {
                     int code = createIdResponse.code();
                     if (code == 200 || code == 401 || code == 403 || code == 402) {
                         return iRetrofit.postShareLogin(new LoginInfo(username,UserInfo.userpwd));
                     }else{
                         ToastUtils.showShort("Share 注册失败");
                         return null;
                     }
                 })
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(
                         new Subscriber<Token>() {
                     @Override
                     public void onCompleted() {
                     }
                     @Override
                     public void onError(Throwable e) {
                         e.printStackTrace();
                     }
                     @Override
                     public void onNext(Token token) {
                         UserInfo.shareToken = token.getToken();
                         UserInfo.userAvatarUrl = token.getAvatar();
                         UserInfo.userShareId = token.getUser_id();
                         initNavigationHeader();
                         NetworkUtils.initPicture(ShareActivity.this,
                                 UserInfo.userAvatarUrl, mUserAvatarImg, loadingView);
                     }
                 });
    }
    private void shareDelete(int shareId, final int position) {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .deleteShareDelete(shareId,UserInfo.shareToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeleteInfo>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(DeleteInfo deleteInfo) {
                        adapter.removeItem(position);
                        ToastUtils.showShort("删除成功");
                    }
                });
    }
    private void getShareList(String sort) {
        //achieve
        Observable<ShareList> observable;
        if(sort.equals("")||sort.equals("all")){
            observable = MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE).getShareByNumber(SHARE_TOTAL);
        }else{
            observable = MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE).getShareBySort(1,sort);
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(shareList -> {try {
                    loadData(shareList.getShares());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                    loadingView.setVisibility(View.GONE);
                    mRefreshLayout.setRefreshing(false);},Throwable::printStackTrace,()->{});
    }
    class DeleteBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            deleteShareId = intent.getIntExtra("shareId", -1);
            deletePostion = intent.getIntExtra("position", -1);
            AlertDialogUtils.showAlert(ShareActivity.this, "确定要删除这一条分享吗?", "下定决心", "算了呗"
                    , deletePositiveListener, deleteNegetiveListener);
        }
    }


}