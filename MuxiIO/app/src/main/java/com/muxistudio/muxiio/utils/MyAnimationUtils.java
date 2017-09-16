package com.muxistudio.muxiio.utils;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.muxistudio.muxiio.R;

/**
 * Created by kolibreath on 17-7-24.
 */

public class MyAnimationUtils {

    public static void FABAnimation(int resAnim, View view, Context context){
        TranslateAnimation translateAnimation =
                (TranslateAnimation) AnimationUtils.loadAnimation(context,resAnim);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
    }

    public static void footerAnimation(final int popup, final int sinkdown, View view, final Context context){
        TranslateAnimation translateAnimation =
                (TranslateAnimation) AnimationUtils.loadAnimation(context,popup);
        view.startAnimation(translateAnimation);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation translateAnimation =
                        (TranslateAnimation) AnimationUtils.loadAnimation(context,sinkdown);
                view.startAnimation(translateAnimation);
            }
        });
    }

    public static void setAnimator(Context context,AnimatorSet outset, AnimatorSet inset){
        outset = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_in);
        inset = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.anim_out);
    }

    public static void setCamera(Context context,View in,View out){
        int distance = 16000;
        float scale = context.getResources().getDisplayMetrics().density*distance;
        in.setCameraDistance(scale);
        out.setCameraDistance(scale);
    }

}
