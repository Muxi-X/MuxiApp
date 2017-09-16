package com.muxistudio.muxiio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.muxistudio.muxiio.App;
import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.UserShareInfo;
import com.muxistudio.muxiio.net.BaseUrls;
import com.muxistudio.muxiio.net.IRetrofit;
import com.muxistudio.muxiio.net.MuxiFactory;
import com.muxistudio.muxiio.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import me.yokeyword.swipebackfragment.SwipeBackActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 17-8-27.
 */

public class HiddenChartActivity extends SwipeBackActivity {

    private Retrofit retrofit;
    private HttpLoggingInterceptor interceptor;
    private OkHttpClient client;
    private IRetrofit iRetrofit;


    //memberIndex is the coordinate in X-axis; memberIndex will plus one to itself after add
    // a point value to list
    private int memberIndex = 0;
    private int groupCount;

    private List<SubcolumnValue> values = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    private List<UserShareInfo> infos = new ArrayList<>();
    private int[] tempGroup;
    private static final int FINISHED = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISHED:
                  initChart(infos);
                    break;
            }
        }

    };

    @BindView(R.id.toolbar_hidden_chart)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.hidden_chart)
    ColumnChartView chart;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.android:
                values.clear();
                columns.clear();
                infos.clear();
                memberIndex = 0;
                tempGroup = App.sAndroiGroup;
                initDataForGroup(App.sAndroiGroup);
                break;
            case R.id.backend:
                values.clear();
                columns.clear();
                infos.clear();
                memberIndex = 0;
                tempGroup = App.sBackEndGroup;
                initDataForGroup(App.sBackEndGroup);
                break;
            case R.id.design:
                values.clear();
                columns.clear();
                infos.clear();
                memberIndex = 0;
                tempGroup = App.sDesignGroup;
                initDataForGroup(App.sDesignGroup);
                break;
            case R.id.frontend:
                values.clear();
                columns.clear();
                infos.clear();
                memberIndex = 0;
                tempGroup = App.sFrontEndGroup;
                initDataForGroup(App.sFrontEndGroup);
                break;
            case R.id.product:
                values.clear();
                columns.clear();
                infos.clear();
                memberIndex = 0;
                tempGroup = App.sProductGroup;
                initDataForGroup(App.sProductGroup);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_chart);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        chart.setOnValueTouchListener(new OnValueTouchListener());
        initDataForGroup(App.sAndroiGroup);
        tempGroup  =App.sAndroiGroup;
    }
    private void initChart(List<UserShareInfo> infos){
        if (groupCount == memberIndex) {
            Collections.sort(infos);
            for(int i=0;i<infos.size();i++){
                values.add(new SubcolumnValue(infos.get(i).userShareTotal
                        , ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }
        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setLineColor(getResources().getColor(R.color.colorPrimary));
        axisY.setLineColor(getResources().getColor(R.color.colorPrimary));
        axisX.setTextColor(getResources().getColor(R.color.colorPrimary));
        axisY.setTextColor(getResources().getColor(R.color.colorPrimary));
        axisX.setName("成员名单");
        axisY.setName("分享数量");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        chart.setColumnChartData(data);
    }
    private void initDataForGroup(int memberIds[]) {
        groupCount = memberIds.length;
        for (int i = 0; i < memberIds.length; i++) {
            getOneAllShare(memberIds[i]);
        }
    }

    private void getOneAllShare(final int id) {
        MuxiFactory.getIRetrofit(BaseUrls.BASE_URL_SHARE)
                .getUserAllShare(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<ShareList>() {
                    @Override
                    public void call(ShareList list) {
                        UserShareInfo info = new UserShareInfo(id, list.getShare_num());
                        infos.add(info);
                        memberIndex++;
                        Message message = new Message();
                        message.what = FINISHED;
                        handler.sendMessage(message);
                    }
                });
    }

    class OnValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueDeselected() {
        }

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            if (subcolumnIndex <= tempGroup.length) {
                int userId = tempGroup[subcolumnIndex];
                ToastUtils.showSpecificDuration(App.sUserId2Name.get(userId), 200);
            }
        }
    }
}