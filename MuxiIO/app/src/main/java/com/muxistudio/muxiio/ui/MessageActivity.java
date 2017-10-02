package com.muxistudio.muxiio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.adapter.MessageAdapter;
import com.muxistudio.muxiio.model.Comments;
import com.muxistudio.muxiio.model.Message;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.IRetrofit;
import com.muxistudio.muxiio.utils.AlertDialogUtils;
import com.muxistudio.muxiio.utils.CacheUtils;
import com.muxistudio.muxiio.utils.CloseAppUtils;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.NetworkUtils;
import com.muxistudio.muxiio.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.swipebackfragment.SwipeBackActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.muxistudio.muxiio.utils.MyTextUtils.parseDate;

/**
 * Created by kolibreath on 17-8-3.
 */

public class MessageActivity extends SwipeBackActivity {

    /***
     * Values
     */

    //net work
    private List<Message> messageList = new ArrayList<>();
    private Retrofit retrofit;
    private HttpLoggingInterceptor interceptor;
    private OkHttpClient client;
    private IRetrofit iRetrofit;

    //messages
    private int asyncTasks = 0;
    private int finishedAsyncTasks = 0;
    private final int FINISHED = 200;


    private MessageAdapter mesageAdapter;

    //Handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            //Log.d("response" ,"handleMessage"+asyncTasks);
            switch (what){
                case FINISHED:
                    if(finishedAsyncTasks==asyncTasks){
                        Collections.sort(messageList);
                        mesageAdapter = new MessageAdapter(MessageActivity.this, R.layout.item_message_list,messageList);
                        messageListView.setAdapter(mesageAdapter);
                        messageNumber.setText(messageList.size()+"");
                        nothingHint.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    /**
     * UI
     */
    @BindView(R.id.alert_nothing)
    RelativeLayout nothingHint;
    @BindView(R.id.uploading_hint)
    RelativeLayout upLoadingProgress;
    @BindView(R.id.message_number)
    TextView messageNumber;
    @BindView(R.id.message_list)
    ListView messageListView;
    @BindView(R.id.message_toolbar)
    Toolbar toolbar;
    @BindView(R.id.message_progress)
    ContentLoadingProgressBar progressBar;
    //@BindView(R.id.titel)
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

    /**
     * override
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        CloseAppUtils.activityList.add(this);
        ButterKnife.bind(this);
        ImageView test = (ImageView) findViewById(R.id.testimage);
        test.setImageBitmap(CacheUtils.readBitmapCache(CacheUtils.BITMAP_KEY));
        upLoadingProgress.setVisibility(View.VISIBLE);
        initRetrofit();
        initToolbar();
        getMessage();
        initListView();

    }

    /**
     * methods
     */

    private void initToolbar(){
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case android.R.id.home:
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void initRetrofit(){
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //set client
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseUrls.BASE_URL_SHARE)
                .client(client)
                .build();

        iRetrofit = retrofit.create(IRetrofit.class);
    }



    private void initListView(){
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            //view : handle of the view item clicked; i :position in adapter
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                TextView title= (TextView) view.findViewById(R.id.messenger_title);
                String titleText = title.getText().toString();

                TextView content = (TextView) view.findViewById(R.id.messenger_content);
                String contentText = content.getText().toString();

                //get the instance
                Message mMessage = messageList.get(position);
                final int shareId = mMessage.shareId;

                //negative and positive button will cause the alert interface to close
                AlertDialogUtils.showAlert(MessageActivity.this,
                        contentText, "设置为已读",null
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setCommentRead(shareId,position);

                                mesageAdapter.remove(messageList.get(position));
                                messageNumber.setText(messageList.size()+"");
                                mesageAdapter.notifyDataSetChanged();
                            }
                        }
                        , new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                String link =
                                        messageList.get(0).messengerContent.toString();
                                if(MyTextUtils.errorLink(link)) {
                                    intent.setData(Uri.parse(messageList.get(0).messengerContent.toString()));
                                    startActivity(intent);
                                }
                            }
                        });


            }
        });
    }


    //the style of the date of message is like :07/12 July 12

    /**
     * net work
     */

    //get user's all shares
    private void getMessage(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        //due to some user doesn't have a share
        Call<ShareList> call = iRetrofit.getOneAllShare(UserInfo.userShareId);
        call.enqueue(new Callback<ShareList>() {
            @Override
            public void onResponse(Call<ShareList> call, Response<ShareList> response) {
                List<ShareList.SharesBean> list = response.body().getShares();
                int size = list.size();
                upLoadingProgress.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    //if readnum equals 1
                    int readnum = list.get(i).getRead_num();
                    if (readnum != 0) {
                        ShareList.SharesBean bean = list.get(i);
                        String commentUrl = bean.getComment();
                        getComment(commentUrl, bean);
                    } else {
                        messageNumber.setText(0 + "");
                        nothingHint.setVisibility(View.VISIBLE);
                        nothingHint.setGravity(RelativeLayout.CENTER_IN_PARENT);
                    }
                }
                progressBar.setVisibility(View.GONE);
                messageListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ShareList> call, Throwable t) {
                NetworkUtils.downloadingTimeout(MessageActivity.this, t, upLoadingProgress);
            }
        });
    }

    //share -> comment url -> get comments
    private void getComment(String url, final ShareList.SharesBean bean){
        String strings[] = url.split("/");
        final int shareId = Integer.parseInt(strings[3]);
        Call<Comments> call = iRetrofit.getMessageComment(shareId);
        call.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                List<Comments.CommentBean> list = response.body().getComment();

                //the formerSize default is nothing
                String formerSize = (PreferenceUtils.
                        readString( R.integer.last_time_message_number));
                int formerInteger = -1;
                if (formerSize.equals("NOTHING")) {
                    formerInteger = 0;
                } else {
                    formerInteger = Integer.parseInt(formerSize);
                }
                int size = response.body().getComment_num();
                //assign size to asyncTasks
                //store the messages number last time

                PreferenceUtils.storeString(R.integer.last_time_message_number, size + "");
                if (size - formerInteger != 0) {
                    asyncTasks = size  -formerInteger;
                    for (int i = 0; i < size - formerInteger; i++) {
                        String mAvatar = list.get(i).getAvatar();

                        //format date
                        String mDate = MyTextUtils.formatDateUsingSlash(parseDate(list.get(i).getDate()));
                        String mUsername = list.get(i).getUsername();
                        String mTitle = (String) bean.getTitle();
                        String mContent = response.body().getComment().get(i).getComment();

                        Log.d("commentsss", "onResponse: "+mContent);
                        messageList.add(new Message(mAvatar, mDate, mUsername, mTitle, mContent, shareId));
                        finishedAsyncTasks++;
                        android.os.Message message = new android.os.Message();
                        message.what = FINISHED;
                        handler.sendMessage(message);
                    }
                }else{
                    nothingHint.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                NetworkUtils.downloadingTimeout(MessageActivity.this, t, upLoadingProgress);
            }
        });
    }

    /**
     *buggy here and dont know what hell is going on
     * the response code from server is 200, however the callback comes into onFailure
     */
    private void setCommentRead(int shareId, final int position){
        Call<String> call = iRetrofit.postSetMessageRead(UserInfo.shareToken, shareId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.downloadingTimeout(MessageActivity.this, t, upLoadingProgress);
            }
        });
    }

}