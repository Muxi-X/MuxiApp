package com.muxistudio.muxiio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.model.CreateId;
import com.muxistudio.muxiio.model.RegisterInfo;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 17-8-2.
 */

public class RegisterActivity extends AppCompatActivity {

    //inputs
    @BindView(R.id.login_useremailedit)
    MaterialEditText mUserEmailEdtTxt;
    @BindView(R.id.login_usernameedit)
    MaterialEditText mUserNameEdtTxt;
    @BindView(R.id.login_userpassworedit)
    MaterialEditText mUserpwdEdtTxt;
    @BindView(R.id.register_reuserpasswordedit)
    MaterialEditText mUserpwdConfirmEdtTxt;

    @BindView(R.id.register_register)
    Button registerButton;
    @BindView(R.id.register_goto_login)
    TextView gotoLoginTextView;

    @OnClick({R.id.register_goto_login,R.id.register_register})
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.register_goto_login:
                LoginActivity.start(RegisterActivity.this);
                finish();
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
                break;
            case R.id.register_register:
                register();
                break;
        }
    }

    public static void start(Context context){
        Intent starter = new Intent(context,RegisterActivity.class);
        context.startActivity(starter);
    }
    private void setEdittexts(MaterialEditText edittext, String hint){
        edittext.setBaseColor(getResources().getColor(R.color.colorGray));
        edittext.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        edittext.setHint(hint);
        edittext.setHintTextColor(getResources().getColor(R.color.colorGray));
    }
    //set min/max characters and hints basecolor etc.
    private void initEditTexts(){
        //set hints
        setEdittexts(mUserEmailEdtTxt,"邮箱");
        setEdittexts(mUserNameEdtTxt,"昵称");
        setEdittexts(mUserpwdEdtTxt,"密码");
        setEdittexts(mUserpwdConfirmEdtTxt,"确认密码");
        //set helper texts /set min max characters
        mUserNameEdtTxt.setHelperText("由中文，英文和数字组成");
        mUserpwdEdtTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mUserpwdEdtTxt.setMinCharacters(8);
        mUserpwdEdtTxt.setMaxCharacters(16);
        mUserpwdConfirmEdtTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mUserpwdConfirmEdtTxt.setMinCharacters(8);
        mUserpwdConfirmEdtTxt.setMaxCharacters(16);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initEditTexts();
    }

    /**
     * NetWorks here
     */

    private void register(){
        final String userEmail = mUserEmailEdtTxt.getText().toString();
        final String username = mUserNameEdtTxt.getText().toString();
        final String userpassword = mUserpwdEdtTxt.getText().toString();
        String userpwdConfirm = mUserpwdConfirmEdtTxt.getText().toString();
        if(!userpassword.equals(userpwdConfirm)){
            ToastUtils.makeToast(RegisterActivity.this,"两次输入的密码不一致", Toast.LENGTH_SHORT);
            mUserpwdEdtTxt.setText("");
            mUserpwdConfirmEdtTxt.setText("");
            return;
        }
        RegisterInfo info = new RegisterInfo(userEmail,username,userpassword);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .postRegister(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateId>() {
                    @Override
                    public void onCompleted() {
                        UserInfo.username = username;
                        UserInfo.userpwd =  userpassword;
                        UserInfo.userEmail = userEmail;
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("这个邮箱已经被注册过了");
                    }

                    @Override
                    public void onNext(CreateId createId) {
                        ToastUtils.makeToast(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT);
                        Intent starter = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(starter);
                        finish();
                        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
                    }
                });
    }
}
