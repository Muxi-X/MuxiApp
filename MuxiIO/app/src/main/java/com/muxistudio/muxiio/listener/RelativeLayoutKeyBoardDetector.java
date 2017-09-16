package com.muxistudio.muxiio.listener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by kolibreath on 17-8-28.
 */

public class RelativeLayoutKeyBoardDetector extends RelativeLayout{

    private Listener listener;

    public RelativeLayoutKeyBoardDetector(Context context) {
        super(context);
    }

    public RelativeLayoutKeyBoardDetector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RelativeLayoutKeyBoardDetector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface Listener{
        public void onSoftKeyBoardShown(boolean isShowing);
    }
    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width  = MeasureSpec.getSize(widthMeasureSpec);
        Activity activity = (Activity) getContext();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int statusBarHeight = rect.top;
        int diff = screenHeight - statusBarHeight - height;
        if(listener!=null){
            listener.onSoftKeyBoardShown(diff>128);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
