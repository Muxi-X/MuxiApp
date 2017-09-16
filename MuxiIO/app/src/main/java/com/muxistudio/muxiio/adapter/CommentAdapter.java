package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muxistudio.muxiio.model.Comments;
import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kolibreath on 17-8-9.
 */

public class CommentAdapter extends ArrayAdapter<Comments.CommentBean>{


    private List<Comments.CommentBean> commentList = new ArrayList<>();
    private int resid = R.layout.item_comment;
    private Context context;
    public CommentAdapter(Context context,int resid,List<Comments.CommentBean> commentList){
        super(context,resid,commentList);
        this.context = context;
        this.resid = resid;
        this.commentList= commentList;
    }
    @Override
    public int getCount() {
        return commentList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resid,null);
        CircleImageView commenterAvatar = (CircleImageView) convertView.findViewById(R.id.commenter_avatar);
        TextView commenterDate = (TextView) convertView.findViewById(R.id.commenter_date);
        TextView commenterUsername = (TextView) convertView.findViewById(R.id.commenter_username);
        TextView commentContent = (TextView) convertView.findViewById(R.id.commenter_content);
        Picasso.with(context).load(commentList.get(position).getAvatar()).into(commenterAvatar);
        String parsedTime2 = MyTextUtils.formatDateUsingSlash(commentList.get(position).getDate());
        commenterDate.setText(parsedTime2);
        commenterUsername.setText(commentList.get(position).getUsername());
        commentContent.setText(commentList.get(position).getComment());
        return convertView;
    }
}
