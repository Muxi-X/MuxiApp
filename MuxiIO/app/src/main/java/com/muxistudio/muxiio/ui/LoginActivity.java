package com.muxistudio.muxiio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.model.LoginInfo;
import com.muxistudio.muxiio.model.Token;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.EncodingUtils;
import com.muxistudio.muxiio.utils.PreferenceUtils;
import com.muxistudio.muxiio.utils.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 17-8-2.
 */

public class LoginActivity extends AppCompatActivity {
    private boolean isFromRegister;
    private boolean isLogout;

    @BindView(R.id.login_usernameedit)
    MaterialEditText mUserNameEdtTxt;
    @BindView(R.id.login_userpassworedit)
    MaterialEditText mUserPwdEdtTxt;
    @BindView(R.id.login_goto_register)
    TextView mRegisterTv;
    @BindView(R.id.login_login)
    Button mLoginBtn;

    @OnClick({R.id.login_goto_register,R.id.login_login})
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_goto_register:
                RegisterActivity.start(LoginActivity.this);
                finish();
                //动画
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                break;
            case R.id.login_login:
                try {
                    login();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //init the editexts
    private void setEdittexts(){
        mUserNameEdtTxt.setBaseColor(getResources().getColor(R.color.colorGray));
        mUserNameEdtTxt.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        mUserNameEdtTxt.setHint("昵称");
        mUserNameEdtTxt.setHintTextColor(getResources().getColor(R.color.colorGray));

        mUserPwdEdtTxt.setBaseColor(getResources().getColor(R.color.colorGray));
        mUserPwdEdtTxt.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        mUserPwdEdtTxt.setHint("密码");
        mUserPwdEdtTxt.setHintTextColor(getResources().getColor(R.color.colorGray));

        //设置密码不可见
        mUserNameEdtTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mUserPwdEdtTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mUserPwdEdtTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setEdittexts();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        isFromRegister = getIntent().getBooleanExtra("fromRegister",false);
        isLogout    = getIntent().getBooleanExtra("fromShare",false);
        //如果是退出登录，需要清除数据
        clearUserInfo(isLogout);
        //避免第二次登录
        secondLogin();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void start(Context context){
        Intent stater = new Intent(context,LoginActivity.class);
        context.startActivity(stater);
    }

    private void secondLogin(){
        String username = PreferenceUtils.readString( R.string.userName);
        String userpassword = PreferenceUtils.readString(R.string.userPassword);
        //if stored
        if(!username.equals("NOTHING")){
            Intent intent = new Intent(LoginActivity.this,ShareActivity.class);
            //just read from above
            UserInfo.username = username;
            UserInfo.userpwd = userpassword;
            UserInfo.authToken = PreferenceUtils.readString( R.string.authToken);
            UserInfo.userAuthId = PreferenceUtils.readInteger(R.integer.authUserId);
            startActivity(intent);
            finish();
        }
    }

    private void clearUserInfo(boolean isLogout){
        if(isLogout){
            PreferenceUtils.storeString(R.string.userName,"NOTHING");
            PreferenceUtils.storeString(R.string.userPassword,"NOTHING");
            PreferenceUtils.storeString(R.string.authToken,"NOTHING");
            return;
        }
        return;
    }

    private void login() throws UnsupportedEncodingException {
            LoginInfo info;
            if(isFromRegister) {
                info = new LoginInfo(UserInfo.username, EncodingUtils.encodePassword(UserInfo.userpwd));
            }else {
                info = new LoginInfo(mUserNameEdtTxt.getText().toString()
                        ,EncodingUtils.encodePassword(mUserPwdEdtTxt.getText().toString()));
            }
            MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                    .postLogin(info)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Token>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort("用户名和密码不匹配！");
                            mUserPwdEdtTxt.setText("");
                        }
                        @Override
                        public void onNext(Token token) {
                            UserInfo.username = mUserNameEdtTxt.getText().toString();
                            UserInfo.userpwd  = mUserPwdEdtTxt.getText().toString();
                            UserInfo.authToken = token.getToken();
                            UserInfo.userAuthId = token.getUser_id();
                            PreferenceUtils.storeString(R.string.userName,UserInfo.username);
                            PreferenceUtils.storeString(R.string.userPassword,UserInfo.userpwd);
                            PreferenceUtils.storeString(R.string.authToken,token.getToken());
                            PreferenceUtils.storeInteger(R.integer.authUserId,UserInfo.userAuthId);
                            ShareActivity.start(LoginActivity.this);
                            finish();
                        }
                    });
        }
    }
