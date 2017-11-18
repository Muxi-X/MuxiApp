package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.data.SharesBean;
import com.muxistudio.muxiio.listener.MyItemClickListener;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.ui.AddShareActivity;
import com.muxistudio.muxiio.ui.CommentActivity;
import com.muxistudio.muxiio.ui.MyProfileActivity;
import com.muxistudio.muxiio.ui.OthersProfileActivity;
import com.muxistudio.muxiio.ui.ShareActivity;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolibreath on 17-7-30.
 */

public class ShareAdapter extends RecyclerView.Adapter {
    private List<SharesBean> sharesBeanList = new ArrayList<>();
    private Context context;
    private int resid =  R.layout.item_share_recycler;
    private MyItemClickListener myItemClickListener;
    public static String DELETE_ACTION = "deleteSpecificShare";

    private ShareViewHolder shareViewHolder;
    public ShareAdapter(List<SharesBean> list) {
        this.sharesBeanList = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(resid,parent,false);
        ShareViewHolder viewHolder = new ShareViewHolder(view,context, myItemClickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (sharesBeanList.size() != 0) {
            final SharesBean object = sharesBeanList.get(position);
            shareViewHolder = (ShareViewHolder) holder;
            shareViewHolder.mShareUsernameTxt.setText(object.getUsername());
            shareViewHolder.mShareContentBack.setText(object.getShare());
            shareViewHolder.mShareTimeTxt.setText(MyTextUtils.parseTime(object.getDate()));
            shareViewHolder.mShareDateTxt.setText(MyTextUtils.formatDataInShare(MyTextUtils.parseDate(object.getDate())));
            shareViewHolder.mSharetTitleTxt.setText(object.getTitle() + "");
            shareViewHolder.mShareGroupImg.setImageResource(getResId(object.getTag()));
            shareViewHolder.mShareTitleBack.setText(object.getTitle() + "");
            String avatarUrl = (String) object.getAvatar();
            shareViewHolder.mShareCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context, CommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shareInfo",(Serializable) object);
                    intent1.putExtras(bundle);
                    context.startActivity(intent1);
                }
            });
            shareViewHolder.mUserAvatarImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userId = object.getUser_id();
                    Intent intent;
                    if(userId== UserInfo.userShareId){
                        intent = new Intent(context, MyProfileActivity.class);
                    }else{
                        intent = new Intent(context,OthersProfileActivity.class);
                    }
                    intent.putExtra("otherUserId",userId);
                    context.startActivity(intent);
                }
            });
            shareViewHolder.mSharetTitleTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rawData = object.getShare();
                    showLinkMenu(rawData);
                }
            });
            if (UserInfo.username != null && object.getUsername() != null) {
                if (!UserInfo.username.equals(object.getUsername())) {
                    shareViewHolder.mShareDeleteBtn.setVisibility(View.INVISIBLE);
                    shareViewHolder.mShareEditBtn.setVisibility(View.INVISIBLE);
                } else {
                    shareViewHolder.mShareDeleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UserInfo.username.equals(object.getUsername())) {
                                shareViewHolder.mShareDeleteBtn.setVisibility(View.VISIBLE);
                                Intent intent3 = new Intent(DELETE_ACTION);
                                intent3.putExtra("position", position);
                                intent3.putExtra("shareId", object.getId());
                                context.sendBroadcast(intent3);
                            }
                        }
                    });
                    shareViewHolder.mShareEditBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (object.getUsername().equals(UserInfo.username)) {
                                Intent intent4 = new Intent(context, AddShareActivity.class);
                                intent4.putExtra("editFlag", true);
                                intent4.putExtra("shareInfo",object);
                                context.startActivity(intent4);
                                ShareActivity.INSTANCE.finish();
                            } else {
                                ToastUtils.showShort("这篇分享的作者不是你");
                            }
                        }
                    });

                }
                loadAvatar(avatarUrl);
            } else {
                ToastUtils.makeToast(context, "list为空", Toast.LENGTH_SHORT);
            }
        }
    }
    @Override
    public int getItemCount() {
        return sharesBeanList.size();
    }
    //如果只有一条链接的时候自动跳转 不然就直接打开一个menu显示链接
    private void showLinkMenu(String rawData){
        //如果是markdown格式的文字需要在解析一下
        UserInfo.isOpenBroswer = true;
        if(MyTextUtils.isMarkDown(rawData)) {
            final List<String > links = MyTextUtils.markDownParser(rawData)[1];
            List<String > titles = MyTextUtils.markDownParser(rawData)[0];
            if(links.size()==1) {
                Uri uri = Uri.parse(MyTextUtils.getLink(links.get(0)));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }else{
                View view1 = LayoutInflater.from(context).inflate(R.layout.alert_links,null);
                LinearLayout layout = (LinearLayout) view1.findViewById(R.id.alert_links);

                TextView title = new TextView(context);
                title.setText("有多条链接，请选择：\n");
                layout.addView(title);
                for(int i=0;i<links.size();i++){
                    TextView titleView = new TextView(context);
                    titleView.setText(titles.get(i)+"\n");
                    layout.addView(titleView);

                    TextView linkView = new TextView(context);
                    layout.addView(linkView);
                    linkView.setText(links.get(i));
                    linkView.setTextColor(context.getResources().getColor(R.color.colorTextPurple));
                    linkView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    final int finalI = i;
                    linkView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(links.get(finalI));
                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                            context.startActivity(intent);
                        }
                    });
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(view1);
                alert.show();
            }
        }else{
            Uri uri = Uri.parse(rawData);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            context.startActivity(intent);
        }
    }
    public void removeItem(int position){
        sharesBeanList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());
    }
    private int getResId(String tag){
        if(tag.equals("android")){
            return R.mipmap.gandroid;
        }
        else if(tag.equals("frontend")){
            return R.mipmap.gfrontend;
        }
        else if(tag.equals("backend")){
            return R.mipmap.gbackend;
        }
        else if(tag.equals("design")){
            return R.mipmap.gdesign;
        }
        return R.mipmap.gproduct;
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.myItemClickListener = listener;
    }
    private void loadAvatar(String avatarUrl){
        if(avatarUrl!=null) {
            //load picture
            Picasso.with(context).
                    load(avatarUrl)
                    .placeholder(R.mipmap.android_dev)
                    .error(R.mipmap.android_dev)
                    .into(shareViewHolder.mUserAvatarImg);
        }else{
            Picasso.with(context).
                    load(R.mipmap.android_dev)
                    .into(shareViewHolder.mUserAvatarImg);
        }
    }
    public List<SharesBean> getList(){
        return sharesBeanList;
    }
    public void setList(List<SharesBean> list){
        sharesBeanList = list;
    }
}