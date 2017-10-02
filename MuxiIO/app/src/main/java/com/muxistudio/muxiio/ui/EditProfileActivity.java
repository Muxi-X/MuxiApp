package com.muxistudio.muxiio.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.data.UpLoadKey;
import com.muxistudio.muxiio.model.AfterChangeAvatar;
import com.muxistudio.muxiio.model.AvatarUrl;
import com.muxistudio.muxiio.model.ProfileEdited;
import com.muxistudio.muxiio.model.ProfileInfo;
import com.muxistudio.muxiio.model.Token;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.CacheUtils;
import com.muxistudio.muxiio.utils.CameraUtils;
import com.muxistudio.muxiio.utils.ToastUtils;
import com.muxistudio.muxiio.utils.ToggleButtonGroupTableLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class EditProfileActivity extends AppCompatActivity {
    private boolean isGranted = false;
    private String mUserAvatarUrl = UserInfo.userAvatarUrl;
    private String mAddress;
    private String mDate1;
    private String mDate2;
    private String mDate3;
    private String mSelectedDate;
    private String mDefaultGroup ;
    private static final int PERMISSION_GRANTED = 200;
    //pick up from photo
    private Uri imageUri;
    private AlertDialog alertDialog;
    private Uri mImageUri;
    private AlertDialog mAlertDialog;

    //users always hope the uploading progress will be ended  less than  10 secs
    //in another word, the isCanceled flag is set true b
    private boolean isCanceled = true;
    /**
     * UI
     */
    @BindView(R.id.group_android)
    RadioButton mRbtnAndroid;
    @BindView(R.id.group_frontend)
    RadioButton mRbtnFrontEnd;
    @BindView(R.id.group_backend)
    RadioButton mRbtnBackEnd;
    @BindView(R.id.group_product)
    RadioButton mRbtnProduct;
    @BindView(R.id.group_design)
    RadioButton mRbtnDesign;
    @BindView(R.id.uploading_hint)
    RelativeLayout mRlEditProfileUploading;
    @BindView(R.id.toggle_button_group)
    ToggleButtonGroupTableLayout mToggleButtonGroup;
    @BindView(R.id.info_uploading)
    ProgressBar mUpLoadingProgress;
    @BindView(R.id.ll_calendar)
    LinearLayout mLlCalendar;
    @BindView(R.id.calendar_year)
    TextView mCalendarYear;
    @BindView(R.id.calendar_month_and_day)
    TextView mCalendarMonthAndDay;
    @BindView(R.id.calendar)
    MaterialCalendarView mCalendar;
    @BindView(R.id.btn_date_cancel)
    Button mBtnDateCancel;
    @BindView(R.id.btn_date_confirm)
    Button mBtnDateConfirm;
    @BindView(R.id.imv_edit_profile_photo)
    CircleImageView mImvEditProfilePhoto;
    @BindView(R.id.scroll_view_edit_profile)
    ScrollView mScrollViewEditProfile;
    @BindView(R.id.btn_edit_profile_confirm)
    ImageButton mBtnEditProfileConfirm;
    @BindView(R.id.toolbar_edit_profile)
    Toolbar mToolbarEditProfile;
    @BindView(R.id.edit_profile_blog)
    MaterialEditText mEditProfileBlog;
    @BindView(R.id.edit_profile_start_time)
    MaterialEditText mEditProfileStartTime;
    @BindView(R.id.edit_profile_end_time)
    MaterialEditText mEditProfileEndTime;
    @BindView(R.id.edit_profile_duty)
    MaterialEditText mEditProfileDuty;
    @BindView(R.id.edit_profile_birthday)
    MaterialEditText mEditProfileBirthday;
    @BindView(R.id.edit_profile_hometown)
    MaterialEditText mEditProfileHometown;
    @BindView(R.id.edit_profile_introduction)
    MaterialEditText mEditProfileIntroduction;
    @BindView(R.id.edit_profile_weibo)
    MaterialEditText mEditProfileWeibo;
    @BindView(R.id.edit_profile_zhihu)
    MaterialEditText mEditProfileZhihu;
    @BindView(R.id.edit_profile_github)
    MaterialEditText mEditProfileGithub;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.setClass(EditProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        requestPermission();
        initToolbar();
        getDataBefore();
        mRbtnAndroid.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        mRbtnBackEnd.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        mRbtnFrontEnd.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        mRbtnDesign.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        mRbtnProduct.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GRANTED: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isGranted = true;
                }
                return;
            }
        }
    }
    public void initToolbar() {
        mToolbarEditProfile.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbarEditProfile.setTitle("编辑个人信息");
        mToolbarEditProfile.setTitleMarginStart(40);
        mToolbarEditProfile.setTitleMarginTop(20);
        setSupportActionBar(mToolbarEditProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getDataBefore() {
        mDefaultGroup = UserInfo.userGroup;
        if ("android".equals(mDefaultGroup) || "安卓组".equals(mDefaultGroup)) {
            mRbtnAndroid.setChecked(true);
        } else if ("frontend".equals(mDefaultGroup) || "前端组".equals(mDefaultGroup)) {
            mRbtnFrontEnd.setChecked(true);
        } else if ("backend".equals(mDefaultGroup) || "后台组".equals(mDefaultGroup)) {
            mRbtnBackEnd.setChecked(true);
        } else if ("product".equals(mDefaultGroup) || "产品组".equals(mDefaultGroup)) {
            mRbtnProduct.setChecked(true);
        } else if ("design".equals(mDefaultGroup) || "设计组".equals(mDefaultGroup)) {
            mRbtnDesign.setChecked(true);
        } else if (mDefaultGroup.equals("design") || mDefaultGroup.equals("设计组")) {
           ToastUtils.showShort("请你选择组别 ");
        }else {
            mRbtnAndroid.setChecked(false);
            mRbtnFrontEnd.setChecked(false);
            mRbtnBackEnd.setChecked(false);
            mRbtnProduct.setChecked(false);
            mRbtnDesign.setChecked(false);
        }

        mEditProfileBlog.setText(UserInfo.userPersonalBlog);
        mEditProfileStartTime.setText(UserInfo.userTimeJoin);
        mEditProfileEndTime.setText(UserInfo.userTimeLeft);
        mEditProfileDuty.setText(UserInfo.userFlickr);
        mEditProfileBirthday.setText(UserInfo.userBirthday);
        mEditProfileHometown.setText(UserInfo.userHometown);
        mEditProfileIntroduction.setText(UserInfo.userInfo);
        mEditProfileWeibo.setText(UserInfo.userWeibo);
        mEditProfileZhihu.setText(UserInfo.userZhihu);
        mEditProfileGithub.setText(UserInfo.userGithub);

        Picasso.with(EditProfileActivity.this).load(UserInfo.userAvatarUrl).into(mImvEditProfilePhoto);
    }

    public void getDataNew() {
        UserInfo.userFlickr = mEditProfileDuty.getText().toString();
        UserInfo.userInfo = mEditProfileIntroduction.getText().toString();
        UserInfo.userPersonalBlog = mEditProfileBlog.getText().toString();
        UserInfo.userGithub = mEditProfileGithub.getText().toString();
        UserInfo.userWeibo = mEditProfileWeibo.getText().toString();
        UserInfo.userZhihu = mEditProfileZhihu.getText().toString();
    }


    @OnClick({R.id.btn_edit_profile_confirm, R.id.imv_edit_profile_photo,
            R.id.edit_profile_start_time, R.id.edit_profile_end_time, R.id.edit_profile_birthday,
            R.id.edit_profile_hometown, R.id.btn_date_confirm, R.id.btn_date_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_cancel:
                mLlCalendar.setVisibility(View.GONE);
                break;
            case R.id.btn_date_confirm:
                mEditProfileStartTime.setText(mDate1);
                UserInfo.userTimeJoin = mEditProfileStartTime.getText().toString();
                mEditProfileEndTime.setText(mDate2);
                UserInfo.userTimeLeft = mEditProfileEndTime.getText().toString();
                mEditProfileBirthday.setText(mDate3);
                UserInfo.userBirthday = mEditProfileBirthday.getText().toString();
                mLlCalendar.setVisibility(View.GONE);
                break;

            case R.id.btn_edit_profile_confirm:
                getDataNew();
                postProfile();
                //compare two avatar url
                //userAvatarUrl is the new url if the avatar is changed and uploaded
                if(!UserInfo.userAvatarUrl.equals(mUserAvatarUrl)){
                    //changeShareAvatar();
                }
                Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
                break;

            //change photo
            case R.id.imv_edit_profile_photo:
                LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
                View alertView = inflater.inflate(R.layout.alert_pick_photo, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setView(alertView);
                mAlertDialog = builder.create();
                mAlertDialog.show();
                TextView pickFromAlbum = (TextView) mAlertDialog.findViewById(R.id.alert_from_album);
                TextView pickFromCamera = (TextView) mAlertDialog.findViewById(R.id.alert_from_camera);
                pickFromAlbum.setOnClickListener(new OnAlertClickListener());
                pickFromCamera.setOnClickListener(new OnAlertClickListener());
                break;

            case R.id.edit_profile_start_time:
                initCalendarFirst();
                mLlCalendar.setVisibility(View.VISIBLE);
                mLlCalendar.bringChildToFront(mLlCalendar);
                break;

            case R.id.edit_profile_end_time:
                initCalendarSecond();
                mLlCalendar.setVisibility(View.VISIBLE);
                mLlCalendar.bringChildToFront(mLlCalendar);
                break;

            case R.id.edit_profile_birthday:
                initCalendarThird();
                mLlCalendar.setVisibility(View.VISIBLE);
                mLlCalendar.bringChildToFront(mLlCalendar);
                break;

            case R.id.edit_profile_hometown:
                pickAddress();
                UserInfo.userHometown = mEditProfileHometown.getText().toString();
                break;
        }
    }
    @Override
    protected void onActivityResult(
            final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CameraUtils.TAKE_PHOTO:
                if (resultCode == RESULT_OK&&isGranted) {
                    try {
                        CameraUtils.display(EditProfileActivity.this
                                , mImageUri, mImvEditProfilePhoto);
                        Bitmap bitmap = ((BitmapDrawable) mImvEditProfilePhoto.getDrawable()).getBitmap();
                        File file = createCompressedBitmapFile(bitmap);
                        upLoadPic(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CameraUtils.OPEN_ALBUM:
                if (resultCode != RESULT_CANCELED) {
                    if (resultCode != RESULT_CANCELED&&isGranted) {
                        try {
                        mUpLoadingProgress.setVisibility(View.VISIBLE);
                        String imagePath = CameraUtils.handlImageOnKitKat(EditProfileActivity.this,
                                data);
                        CameraUtils.display(imagePath, mImvEditProfilePhoto);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        File file = null;
                        file = createCompressedBitmapFile( bitmap);
                        upLoadPic(file);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    }
                }
        }
        }
    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    PERMISSION_GRANTED);
        }else{
            isGranted = true;
        }
    }
    public void pickAddress() {
        CityPicker cityPicker = new CityPicker.Builder(EditProfileActivity.this)
                .textSize(16)
                .title("")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#4754B0")
                .titleTextColor("#ffffff")
                .confirTextColor("#ffffff")
                .cancelTextColor("#ffffff")
                .province("湖北省")
                .city("武汉市")
                .textColor(Color.parseColor("#4754B0"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(5)
                .onlyShowProvinceAndCity(true)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                mAddress = province + " " + city;
                mEditProfileHometown.setText(mAddress);
            }

            @Override
            public void onCancel() {
                ToastUtils.makeToast(EditProfileActivity.this, "已取消", Toast.LENGTH_SHORT);
            }
        });
    }
    public void getCurrentDate() {
        String currentYear = new SimpleDateFormat("yyyy").format(new Date());
        String currentDate = new SimpleDateFormat("MM-dd EEE").format(new Date());
        mCalendarYear.setText(currentYear);
        mCalendarMonthAndDay.setText(currentDate);
    }
    // The calendar I used existed bug and the month of it was wrong.
    public void initCalendarFirst() {
        getCurrentDate();
        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay select, boolean selected) {
                mSelectedDate = select.toString();
                String string[] = mSelectedDate.split("\\{|\\}");
                String s = string[1]; //2017-8-14
                String string1[] = s.split("-");
                int tempMonth = Integer.parseInt(string1[1]);
                String month = Integer.toString(tempMonth + 1);
                mDate1 = string1[0] + "-" + month + "-" + string1[2];
            }
        });
    }
    public void initCalendarSecond() {
        getCurrentDate();
        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay select, boolean selected) {
                mSelectedDate = select.toString();
                String string[] = mSelectedDate.split("\\{|\\}");
                String s = string[1];
                String string1[] = s.split("-");
                int tempMonth = Integer.parseInt(string1[1]);
                String month = Integer.toString(tempMonth + 1);
                mDate2 = string1[0] + "-" + month + "-" + string1[2];
            }
        });
    }
    public void initCalendarThird() {
        getCurrentDate();
        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay select, boolean selected) {
                mSelectedDate = select.toString();
                String string[] = mSelectedDate.split("\\{|\\}");
                String s = string[1]; //2017-8-14
                String string1[] = s.split("-");
                int tempMonth = Integer.parseInt(string1[1]);
                String month = Integer.toString(tempMonth + 1);
                mDate3 = string1[0] + "-" + month + "-" + string1[2];
            }
        });
    }
    private File createCompressedBitmapFile(Bitmap bitmap)
            throws IOException {
        File file = File.createTempFile("muxistudio",".jpg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int quality = 30;
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,bos);
        byte[] b = bos.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(b);
        fos.close();
        return file;
    }
    //当上传头像的操作完成了之后就上传url
    //key是上传到七牛上面的key
    private void setAvatarUrl(String key){
        final String avatar = BaseUrls.BASE_URL_PHOTO+key ;
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .postChangeAvatar(UserInfo.shareToken, new AvatarUrl(avatar))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AfterChangeAvatar>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    //设置isChangedAvatar isChangedAvatar的初始值为false
                    @Override
                    public void onNext(AfterChangeAvatar afterChangeAvatar) {
                        UserInfo.userAvatarUrl  = avatar;
                        Log.d("anotherthing", avatar);
                        CacheUtils.removeBitmapCache(CacheUtils.BITMAP_KEY);
                        ToastUtils.showShort("上传头像成功");
                    }
                });
    }
    private void upLoadPic(final File file) throws IOException {
        if(file==null){
            ToastUtils.showSpecificDuration("头像路径错误",2000);
            return;
        }
        final String key = file.getName();
        Log.d("something",UserInfo.shareToken);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .postRetrieveToken(UserInfo.shareToken,new UpLoadKey(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Token>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Token token) {
                        UploadManager uploadManager = new UploadManager();
                        uploadManager.put(file, key, token.getToken(),
                                new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                        if (info.isOK()) {
                                            setAvatarUrl(key);
                                        }
                                    }
                                }, null);
                    }
                });
    }
    private void openSystemCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CameraUtils.TAKE_PHOTO);
    }
    private void openSystemAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CameraUtils.OPEN_ALBUM);
    }
    //net work
    // https://stackoverflow.com/questions/10325158/android-camera-activity-gets-opened-instead-of-picture-gallery
    public void postProfile() {
        Log.d("anotherthingpost", UserInfo.userAvatarUrl);
        ProfileInfo profileInfo =
                new ProfileInfo(UserInfo.userInfo, UserInfo.userBirthday, UserInfo.userHometown, UserInfo.userGroup,
                UserInfo.userTimeJoin, UserInfo.userTimeLeft, UserInfo.userAvatarUrl, UserInfo.userPersonalBlog, UserInfo.userGithub,
                UserInfo.userFlickr, UserInfo.userWeibo, UserInfo.userZhihu);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .postUserProfile(UserInfo.authToken,profileInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<ProfileEdited>() {
                    @Override
                    public void call(ProfileEdited profileEdited) {
                       // UserInfo.userAvatarUrl = mUserAvatarUrl;
                    }
                });
    }
    private class OnAlertClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                //from album
                case R.id.alert_from_album:
                    mAlertDialog.dismiss();
                    mImageUri = CameraUtils.getImageUri(EditProfileActivity.this);
                    openSystemAlbum();
                    break;

                //from camera
                case R.id.alert_from_camera:
                    mAlertDialog.dismiss();
                    mImageUri = CameraUtils.getImageUri(EditProfileActivity.this);
                    openSystemCamera();
                    break;
            }
        }
    }
    class RadioOnCheckChangedListener implements CompoundButton.OnCheckedChangeListener{
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            switch (id){
                case R.id.group_android:
                    if (mRbtnAndroid.isChecked()) {
                        mRbtnBackEnd.setChecked(false);
                        mRbtnFrontEnd.setChecked(false);
                        mRbtnProduct.setChecked(false);
                        mRbtnDesign.setChecked(false);
                    }
                    UserInfo.userGroup = "安卓组";
                    ToastUtils.showSpecificDuration("选择组别安卓组", 2000);
                    break;
                case R.id.group_frontend:
                    if (mRbtnFrontEnd.isChecked()) {
                        mRbtnBackEnd.setChecked(false);
                        mRbtnAndroid.setChecked(false);
                        mRbtnProduct.setChecked(false);
                        mRbtnDesign.setChecked(false);
                    }
                    UserInfo.userGroup = "前端组";
                    ToastUtils.showSpecificDuration("选择组别前端组", 2000);
                    break;
                case R.id.group_backend:
                    if (mRbtnBackEnd.isChecked()) {
                        mRbtnAndroid.setChecked(false);
                        mRbtnProduct.setChecked(false);
                        mRbtnDesign.setChecked(false);
                        mRbtnFrontEnd.setChecked(false);
                    }
                    UserInfo.userGroup = "后台组";
                    ToastUtils.showSpecificDuration("选择组别后台组", 2000);
                    break;
                case R.id.group_product:
                    if (mRbtnProduct.isChecked()) {
                        mRbtnAndroid.setChecked(false);
                        mRbtnBackEnd.setChecked(false);
                        mRbtnDesign.setChecked(false);
                        mRbtnFrontEnd.setChecked(false);
                    }
                    UserInfo.userGroup = "产品组";
                    ToastUtils.showSpecificDuration("选择组别产品组", 2000);
                    break;
                case R.id.group_design:
                    if (mRbtnDesign.isChecked()) {
                        mRbtnAndroid.setChecked(false);
                        mRbtnBackEnd.setChecked(false);
                        mRbtnProduct.setChecked(false);
                        mRbtnFrontEnd.setChecked(false);
                    }
                    UserInfo.userGroup = "设计组";
                    ToastUtils.showSpecificDuration("选择组别设计组", 2000);
                    break;
            }
        }
    }

}
