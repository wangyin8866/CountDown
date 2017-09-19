package com.wy.wyman.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 截至时间数据源
     **/
    private List<Date> listData;
    /**
     * 当前时间
     **/
    private long time_Current;
    /**
     * ListView控件
     **/
    private ListView listView;
    /**
     * 适配器
     **/
    private MyCountAdapter myCountAdapter;

    //这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time_Current = time_Current + 1000;
            myCountAdapter.notifyDataSetChanged();
            handler_timeCurrent.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv);
        //模拟活动截至时间数据
        listData = new ArrayList<Date>();
        listData.add(new Date(2016, 3, 16, 8, 20, 31));
        listData.add(new Date(2016, 3, 16, 8, 21, 20));
        listData.add(new Date(2016, 3, 16, 13, 21, 22));
        listData.add(new Date(2016, 3, 16, 8, 21, 20));
        listData.add(new Date(2016, 3, 16, 8, 21, 23));
        listData.add(new Date(2016, 3, 16, 14, 21, 20));
        listData.add(new Date(2016, 3, 16, 8, 21, 23));
        listData.add(new Date(2016, 3, 16, 8, 21, 24));
        listData.add(new Date(2016, 3, 16, 8, 21, 20));
        listData.add(new Date(2016, 3, 16, 8, 22, 25));
        listData.add(new Date(2016, 3, 16, 8, 23, 20));
        listData.add(new Date(2016, 3, 16, 8, 24, 26));
        listData.add(new Date(2016, 3, 16, 8, 25, 20));
        listData.add(new Date(2016, 3, 16, 8, 24, 25));
        listData.add(new Date(2016, 3, 16, 8, 25, 20));
        listData.add(new Date(2016, 3, 16, 8, 24, 26));
        listData.add(new Date(2016, 3, 16, 11, 20, 20));
        listData.add(new Date(2016, 3, 16, 14, 40, 20));
        listData.add(new Date(2016, 3, 16, 8, 44, 20));
        listData.add(new Date(2016, 3, 16, 10, 20, 20));

        //模拟当前服务器时间数据
        Date date = new Date(2016, 3, 16, 8, 20, 20);
        time_Current = date.getTime();

        myCountAdapter = new MyCountAdapter();
        listView.setAdapter(myCountAdapter);

        handler_timeCurrent.sendEmptyMessageDelayed(0, 1000);
    }

    class MyCountAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_lv, null);
                holder = new ViewHolder();
                holder.tv_hour = convertView.findViewById(R.id.tv_hour);
                holder.tv_minute = convertView.findViewById(R.id.tv_minute);
                holder.tv_second = convertView.findViewById(R.id.tv_second);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Date date_finish = listData.get(position);
            updateTextView(date_finish.getTime() - time_Current, holder);
            return convertView;
        }

        /****
         * 刷新倒计时控件
         */
        public void updateTextView(long times_remain, ViewHolder hoder) {

            if (times_remain <= 0) {
                hoder.tv_hour.setText("00");
                hoder.tv_minute.setText("00");
                hoder.tv_second.setText("00");
                return;
            }
            //秒钟
            long time_second = (times_remain / 1000) % 60;
            String str_second;
            if (time_second < 10) {
                str_second = "0" + time_second;
            } else {
                str_second = "" + time_second;
            }

            long time_temp = ((times_remain / 1000) - time_second) / 60;
            //分钟
            long time_minute = time_temp % 60;
            String str_minute;
            if (time_minute < 10) {
                str_minute = "0" + time_minute;
            } else {
                str_minute = "" + time_minute;
            }

            time_temp = (time_temp - time_minute) / 60;
            //小时
            long time_hour = time_temp;
            String str_hour;
            if (time_hour < 10) {
                str_hour = "0" + time_hour;
            } else {
                str_hour = "" + time_hour;
            }

            hoder.tv_hour.setText(str_hour);
            hoder.tv_minute.setText(str_minute);
            hoder.tv_second.setText(str_second);

        }

        private class ViewHolder {
            /**
             * 小时
             **/
            private TextView tv_hour;
            /**
             * 分钟
             **/
            private TextView tv_minute;
            /**
             * 秒
             **/
            private TextView tv_second;
        }
    }

    //防止当前Activity结束以后,   handler依然继续循环浪费资源
    @Override
    protected void onDestroy() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
