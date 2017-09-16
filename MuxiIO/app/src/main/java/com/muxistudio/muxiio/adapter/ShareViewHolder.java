package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.muxiio.listener.MyItemClickListener;
import com.muxistudio.muxiio.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kolibreath on 17-7-30.
 */


//share : RecyclerView holder
public class ShareViewHolder extends RecyclerView.ViewHolder implements
        View.OnLongClickListener{
    //bind components here using butterknife

    private Context context;
    private MyItemClickListener myItemClickListener;
    private View frontView;
    private View backView;

    @BindView(R.id.shareaci_cardv)
    CardView mCardViewFront;
    @BindView(R.id.shareaci_cardv_b)
    CardView mCardViewBack;

    //Front
    @BindView(R.id.cardv_tev)
    TextView mShareUsernameTxt;
    @BindView(R.id.cardv_useravatar)
    CircleImageView mUserAvatarImg;
    @BindView(R.id.cardV_title)
    TextView mSharetTitleTxt;
    @BindView(R.id.cardv_date)
    TextView mShareDateTxt;
    @BindView(R.id.share_f_comment)
    Button mShareCommentBtn;
    @BindView(R.id.share_f_delete)
    Button mShareDeleteBtn;
    @BindView(R.id.share_f_edit)
    Button mShareEditBtn;
    @BindView(R.id.cardv_time)
    TextView mShareTimeTxt;

    //Back
    @BindView(R.id.title_cardv_b)
    TextView mShareTitleBack;
    @BindView(R.id.cardv_content)
    TextView mShareContentBack;
    @OnClick(R.id.cardv_content)
    void openLink(){
        Log.d("link", "openLink: ");
    }
    @BindView(R.id.ic_share_group)
    ImageView mShareGroupImg;



    public ShareViewHolder(View view, Context context, MyItemClickListener listener){
        super(view);
        this.context = context;
        ButterKnife.bind(this,view);
        this.myItemClickListener = listener;
        frontView = ((ViewGroup) view).getChildAt(0);
        backView = ((ViewGroup) view).getChildAt(1);
        frontView.setTag(R.id.frontBack,1);
        backView.setTag(R.id.frontBack,0);
        frontView.setOnLongClickListener(this);
        backView.setOnLongClickListener(this);

    }

    @Override
    public boolean onLongClick(View view) {
        if (myItemClickListener != null) {
            int tag = (int) view.getTag(R.id.frontBack);
            View anotherView = null;
            switch (tag) {
                case 1:
                    anotherView = backView;
                    break;
                case 0:
                    anotherView = frontView;
            }
            myItemClickListener.OnItemLongClick(view, anotherView, getPosition());
        }
        return true;
    }

}