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
import android.os.CountDownTimer;
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
import com.muxistudio.muxiio.model.ProfileEdited;
import com.muxistudio.muxiio.model.ProfileInfo;
import com.muxistudio.muxiio.model.UserInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.MuxiFactory;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class EditProfileActivity extends AppCompatActivity {
    private boolean isGranted = false;
    private String userAvatarUrl = UserInfo.userAvatarUrl;
    private String address;
    private String date1;
    private String date2;
    private String date3;
    private String selectedDate;
    private String defaultGroup;
    private static final  int PERMISSION_GRANTED = 200;
    //pick up from photo
    private Uri imageUri;

    private AlertDialog alertDialog;

    //count down
    private int countDownTime = 10;
    //users always hope the uploading progress will be ended  less than  10 secs
    //in another word, the isCanceled flag is set true b
    private boolean isCanceled = true;
    private CountDownTimer timer = new CountDownTimer(1000,10000) {
        @Override
        public void onTick(long l) {
            countDownTime--;
        }

        //the callback will be fired alse when cancel() is called
        @Override
        public void onFinish() {
            countDownTime = 10;
            if (!isCanceled) {
                ToastUtils.showSpecificDuration("哎呀呀,头像体积太大了,上传超时了", 2000);
            }
            //set isCanceled flag to true
            isCanceled = true;
        }
    };


    /**
     * UI
     */
    @BindView(R.id.group_android)
    RadioButton rBtnAndroid;
    @BindView(R.id.group_frontend)
    RadioButton rBtnFrontEnd;
    @BindView(R.id.group_backend)
    RadioButton rBtnBackEnd;
    @BindView(R.id.group_product)
    RadioButton rBtnProduct;
    @BindView(R.id.group_design)
    RadioButton rBtnDesign;
    @BindView(R.id.uploading_hint)
    RelativeLayout rlEditProfileUploading;
    @BindView(R.id.toggle_button_group)
    ToggleButtonGroupTableLayout toggleButtonGroup;
    @BindView(R.id.info_uploading)
    ProgressBar upLoadingProgress;
    @BindView(R.id.ll_calendar)
    LinearLayout llCalendar;
    @BindView(R.id.calendar_year)
    TextView calendarYear;
    @BindView(R.id.calendar_month_and_day)
    TextView calendarMonthAndDay;
    @BindView(R.id.calendar)
    MaterialCalendarView calendar;
    @BindView(R.id.btn_date_cancel)
    Button btnDateCancel;
    @BindView(R.id.btn_date_confirm)
    Button btnDateConfirm;
    @BindView(R.id.imv_edit_profile_photo)
    CircleImageView imvEditProfilePhoto;
    @BindView(R.id.scroll_view_edit_profile)
    ScrollView scrollViewEditProfile;
    @BindView(R.id.btn_edit_profile_confirm)
    ImageButton btnEditProfileConfirm;
    @BindView(R.id.toolbar_edit_profile)
    Toolbar toolbarEditProfile;
    //@BindView(R.id.edit_profile_name)
    //MaterialEditText editProfileName;
    @BindView(R.id.edit_profile_blog)
    MaterialEditText editProfileBlog;
    //@BindView(R.id.edit_profile_email)
    //MaterialEditText editProfileEmail;
    @BindView(R.id.edit_profile_start_time)
    MaterialEditText editProfileStartTime;
    @BindView(R.id.edit_profile_end_time)
    MaterialEditText editProfileEndTime;
    @BindView(R.id.edit_profile_duty)
    MaterialEditText editProfileDuty;
    @BindView(R.id.edit_profile_birthday)
    MaterialEditText editProfileBirthday;
    @BindView(R.id.edit_profile_hometown)
    MaterialEditText editProfileHometown;
    @BindView(R.id.edit_profile_introduction)
    MaterialEditText editProfileIntroduction;
    @BindView(R.id.edit_profile_weibo)
    MaterialEditText editProfileWeibo;
    @BindView(R.id.edit_profile_zhihu)
    MaterialEditText editProfileZhihu;
    @BindView(R.id.edit_profile_github)
    MaterialEditText editProfileGithub;

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
        initToolbar();

        getDataBefore();

        rBtnAndroid.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        rBtnBackEnd.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        rBtnFrontEnd.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        rBtnDesign.setOnCheckedChangeListener(new RadioOnCheckChangedListener());
        rBtnProduct.setOnCheckedChangeListener(new RadioOnCheckChangedListener());


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
        toolbarEditProfile.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarEditProfile.setTitle("编辑个人信息");
        toolbarEditProfile.setTitleMarginStart(40);
        toolbarEditProfile.setTitleMarginTop(20);
        setSupportActionBar(toolbarEditProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getDataBefore() {
        defaultGroup = UserInfo.userGroup;
        if ("android".equals(defaultGroup) || "安卓组".equals(defaultGroup)) {
            rBtnAndroid.setChecked(true);
        } else if ("frontend".equals(defaultGroup) || "前端组".equals(defaultGroup)) {
            rBtnFrontEnd.setChecked(true);
        } else if ("backend".equals(defaultGroup) || "后台组".equals(defaultGroup)) {
            rBtnBackEnd.setChecked(true);
        } else if ("product".equals(defaultGroup) || "产品组".equals(defaultGroup)) {
            rBtnProduct.setChecked(true);
        } else if ("design".equals(defaultGroup) || "设计组".equals(defaultGroup)) {
            rBtnDesign.setChecked(true);
        } else if (defaultGroup.equals("design") || defaultGroup.equals("设计组")) {
           ToastUtils.showShort("请你选择组别 ");
        }else {
            rBtnAndroid.setChecked(false);
            rBtnFrontEnd.setChecked(false);
            rBtnBackEnd.setChecked(false);
            rBtnProduct.setChecked(false);
            rBtnDesign.setChecked(false);
        }
        //editProfileName.setText(UserInfo.username);
        editProfileBlog.setText(UserInfo.userPersonalBlog);
        //editProfileEmail.setText(UserInfo.userEmail);
        editProfileStartTime.setText(UserInfo.userTimeJoin);
        editProfileEndTime.setText(UserInfo.userTimeLeft);
        editProfileDuty.setText(UserInfo.userFlickr);
        editProfileBirthday.setText(UserInfo.userBirthday);
        editProfileHometown.setText(UserInfo.userHometown);
        editProfileIntroduction.setText(UserInfo.userInfo);
        editProfileWeibo.setText(UserInfo.userWeibo);
        editProfileZhihu.setText(UserInfo.userZhihu);
        editProfileGithub.setText(UserInfo.userGithub);

        Picasso.with(EditProfileActivity.this).load(UserInfo.userAvatarUrl).into(imvEditProfilePhoto);
    }

    public void getDataNew() {
        UserInfo.userFlickr = editProfileDuty.getText().toString();
        UserInfo.userInfo = editProfileIntroduction.getText().toString();
        UserInfo.userPersonalBlog = editProfileBlog.getText().toString();
        UserInfo.userGithub = editProfileGithub.getText().toString();
        UserInfo.userWeibo = editProfileWeibo.getText().toString();
        UserInfo.userZhihu = editProfileZhihu.getText().toString();
    }


    @OnClick({R.id.btn_edit_profile_confirm, R.id.imv_edit_profile_photo,
            R.id.edit_profile_start_time, R.id.edit_profile_end_time, R.id.edit_profile_birthday,
            R.id.edit_profile_hometown, R.id.btn_date_confirm, R.id.btn_date_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_cancel:
                llCalendar.setVisibility(View.GONE);
                break;
            case R.id.btn_date_confirm:
                editProfileStartTime.setText(date1);
                UserInfo.userTimeJoin = editProfileStartTime.getText().toString();
                editProfileEndTime.setText(date2);
                UserInfo.userTimeLeft = editProfileEndTime.getText().toString();
                editProfileBirthday.setText(date3);
                UserInfo.userBirthday = editProfileBirthday.getText().toString();
                llCalendar.setVisibility(View.GONE);
                break;

            case R.id.btn_edit_profile_confirm:
                getDataNew();
                postProfile();
                //compare two avatar url
                //userAvatarUrl is the new url if the avatar is changed and uploaded
                if(!UserInfo.userAvatarUrl.equals(userAvatarUrl)){
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
                alertDialog = builder.create();
                alertDialog.show();
                TextView pickFromAlbum = (TextView) alertDialog.findViewById(R.id.alert_from_album);
                TextView pickFromCamera = (TextView) alertDialog.findViewById(R.id.alert_from_camera);
                pickFromAlbum.setOnClickListener(new OnAlertClickListener());
                pickFromCamera.setOnClickListener(new OnAlertClickListener());
                break;

            case R.id.edit_profile_start_time:
                initCalendarFirst();
                llCalendar.setVisibility(View.VISIBLE);
                llCalendar.bringChildToFront(llCalendar);
                break;

            case R.id.edit_profile_end_time:
                initCalendarSecond();
                llCalendar.setVisibility(View.VISIBLE);
                llCalendar.bringChildToFront(llCalendar);
                break;

            case R.id.edit_profile_birthday:
                initCalendarThird();
                llCalendar.setVisibility(View.VISIBLE);
                llCalendar.bringChildToFront(llCalendar);
                break;

            case R.id.edit_profile_hometown:
                pickAddress();
                UserInfo.userHometown = editProfileHometown.getText().toString();
                break;
        }
    }

    @Override
    protected void onActivityResult(
            final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CameraUtils.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        CameraUtils.display(EditProfileActivity.this
                                , imageUri, imvEditProfilePhoto);
                        Bitmap bitmap = ((BitmapDrawable) imvEditProfilePhoto.getDrawable()).getBitmap();
                        File file = createCompressedBitmapFile(9000000, bitmap);
                        upLoadPic(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case CameraUtils.OPEN_ALBUM:
                if (resultCode != RESULT_CANCELED) {
                    if (resultCode != RESULT_CANCELED&&isGranted) {
                        upLoadingProgress.setVisibility(View.VISIBLE);
                        String imagePath = CameraUtils.handlImageOnKitKat(EditProfileActivity.this,
                                data);
                        CameraUtils.display(imagePath, imvEditProfilePhoto);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        File file = null;
                        try {
                            file = createCompressedBitmapFile(getFileSize(imagePath), bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            upLoadPic(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        timer.start();
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
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_GRANTED);
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
                address = province + " " + city;
                editProfileHometown.setText(address);
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
        calendarYear.setText(currentYear);
        calendarMonthAndDay.setText(currentDate);
    }
    // The calendar I used existed bug and the month of it was wrong.
    public void initCalendarFirst() {
        getCurrentDate();
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay select, boolean selected) {
                selectedDate = select.toString();
                String string[] = selectedDate.split("\\{|\\}");
                String s = string[1]; //2017-8-14
                String string1[] = s.split("-");
                int tempMonth = Integer.parseInt(string1[1]);
                String month = Integer.toString(tempMonth + 1);
                date1 = string1[0] + "-" + month + "-" + string1[2];
            }
        });
    }
    public void initCalendarSecond() {
        getCurrentDate();
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay select, boolean selected) {
                selectedDate = select.toString();
                String string[] = selectedDate.split("\\{|\\}");
                String s = string[1];
                String string1[] = s.split("-");
                int tempMonth = Integer.parseInt(string1[1]);
                String month = Integer.toString(tempMonth + 1);
                date2 = string1[0] + "-" + month + "-" + string1[2];
            }
        });
    }
    public void initCalendarThird() {
        getCurrentDate();
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay select, boolean selected) {
                selectedDate = select.toString();
                String string[] = selectedDate.split("\\{|\\}");
                String s = string[1]; //2017-8-14
                String string1[] = s.split("-");
                int tempMonth = Integer.parseInt(string1[1]);
                String month = Integer.toString(tempMonth + 1);
                date3 = string1[0] + "-" + month + "-" + string1[2];
            }
        });
    }

    private int getFileSize(String imagePath) throws IOException {
        File file = new File(imagePath);
        FileInputStream stream = new FileInputStream(file);
        return  stream.available();
    }

    private File createCompressedBitmapFile(int fileSize,Bitmap bitmap) throws IOException {
        File file = File.createTempFile("muxistudio",".jpg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int quality = 0;
        //the file is more than 4M
        if(fileSize>8000000){
            quality = 30;
        }
        if(fileSize>=4000000&&fileSize<8000000){
            quality = 40;
        }
        if(fileSize<4000000) {
            quality = 70;
        }
        if(fileSize<2000000){
            quality = 90;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,bos);
        byte[] b = bos.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(b);
        fos.close();

        return file;
    }

    private void upLoadPic(File file) throws IOException {
        if(file==null){
            ToastUtils.showSpecificDuration("头像路径错误",2000);
            return;
        }
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager();
        String key = "a.jpg";
        String token ="0bNiwJGpdwmvvuVAzLDjM6gnxj9MiwmSagVpIW81:qAwynbYW7WG-Y0CaX3Y8X49mw0o=:eyJzY29wZSI6Im11eGlzaXRlLWF2YXRhcjphLmpwZyIsImRlYWRsaW5lIjoxNTA1NTM2MzkyfQ==";
        uploadManager.put(file, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置

                        if(info.isOK()) {
                            Log.d("qiniu", "Upload Success");
                        } else {
                            Log.d("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);


        /*
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), imageBody);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .postUpLoadPic(imageBodyPart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<UpLoadResponse>() {
                    @Override
                    public void call(UpLoadResponse upLoadResponse) {
                        //if connection not time out
                        timer.cancel();
                        isCanceled = true;
                        countDownTime = 10;
                        rlEditProfileUploading.setVisibility(View.GONE);
                        upLoadingProgress.setVisibility(View.GONE);
                        ToastUtils.showSpecificDuration("上传头像成功", 2000);
                        userAvatarUrl = "http://share.muxixyz.com"  +
                               upLoadResponse.getFilename();
                        try {
                            CameraUtils.display(EditProfileActivity.this, imageUri, imvEditProfilePhoto);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                */
    }

    private void openSystemCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        requestPermission();
        startActivityForResult(intent, CameraUtils.TAKE_PHOTO);
    }

    private void openSystemAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        requestPermission();
        startActivityForResult(intent, CameraUtils.OPEN_ALBUM);
    }

    //if finished in ten sec. the timer will be canceled;
    //if not, it will pop out connected time out

    /**
     * net work
     */

    //net work
    // https://stackoverflow.com/questions/10325158/android-camera-activity-gets-opened-instead-of-picture-gallery
    public void postProfile() {
        ProfileInfo profileInfo = new ProfileInfo(UserInfo.userInfo, UserInfo.userBirthday, UserInfo.userHometown, UserInfo.userGroup,
                UserInfo.userTimeJoin, UserInfo.userTimeLeft, UserInfo.userAvatarUrl, UserInfo.userPersonalBlog, UserInfo.userGithub,
                UserInfo.userFlickr, UserInfo.userWeibo, UserInfo.userZhihu);
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_AUTH)
                .postUserProfile(UserInfo.authToken,profileInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<ProfileEdited>() {
                    @Override
                    public void call(ProfileEdited profileEdited) {
                        UserInfo.userAvatarUrl = userAvatarUrl;
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
                    alertDialog.dismiss();
                    imageUri = CameraUtils.getImageUri(EditProfileActivity.this);
                    openSystemAlbum();
                    break;

                //from camera
                case R.id.alert_from_camera:
                    alertDialog.dismiss();
                    imageUri = CameraUtils.getImageUri(EditProfileActivity.this);
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
                    if (rBtnAndroid.isChecked()) {
                        rBtnBackEnd.setChecked(false);
                        rBtnFrontEnd.setChecked(false);
                        rBtnProduct.setChecked(false);
                        rBtnDesign.setChecked(false);
                    }
                    UserInfo.userGroup = "安卓组";
                    ToastUtils.showSpecificDuration("选择组别安卓组", 2000);
                    break;
                case R.id.group_frontend:
                    if (rBtnFrontEnd.isChecked()) {
                        rBtnBackEnd.setChecked(false);
                        rBtnAndroid.setChecked(false);
                        rBtnProduct.setChecked(false);
                        rBtnDesign.setChecked(false);
                    }
                    UserInfo.userGroup = "前端组";
                    ToastUtils.showSpecificDuration("选择组别前端组", 2000);
                    break;
                case R.id.group_backend:
                    if (rBtnBackEnd.isChecked()) {
                        rBtnAndroid.setChecked(false);
                        rBtnProduct.setChecked(false);
                        rBtnDesign.setChecked(false);
                        rBtnFrontEnd.setChecked(false);
                    }
                    UserInfo.userGroup = "后台组";
                    ToastUtils.showSpecificDuration("选择组别后台组", 2000);
                    break;
                case R.id.group_product:
                    if (rBtnProduct.isChecked()) {
                        rBtnAndroid.setChecked(false);
                        rBtnBackEnd.setChecked(false);
                        rBtnDesign.setChecked(false);
                        rBtnFrontEnd.setChecked(false);
                    }
                    UserInfo.userGroup = "产品组";
                    ToastUtils.showSpecificDuration("选择组别产品组", 2000);
                    break;
                case R.id.group_design:
                    if (rBtnDesign.isChecked()) {
                        rBtnAndroid.setChecked(false);
                        rBtnBackEnd.setChecked(false);
                        rBtnProduct.setChecked(false);
                        rBtnFrontEnd.setChecked(false);
                    }
                    UserInfo.userGroup = "设计组";
                    ToastUtils.showSpecificDuration("选择组别设计组", 2000);
                    break;
            }
        }
    }

}
