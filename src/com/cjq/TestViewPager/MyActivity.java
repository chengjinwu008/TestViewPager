package com.cjq.TestViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private List<Fragment> fragments;
    private int cursorWidth;
    private View cursor;
    private List<TextView> title;
    private ViewPager pager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(android.R.color.transparent);//通知栏所需颜色
        pager = (ViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new MyFragment().setColor(Color.RED));
        fragments.add(new MyFragment().setColor(Color.GREEN));
        fragments.add(new MyFragment().setColor(Color.BLUE));
        pager.setOffscreenPageLimit(2);

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        pager.setOnPageChangeListener(this);

        //初始化游标
        cursor = findViewById(R.id.cursor);
        ViewGroup.LayoutParams params = cursor.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / fragments.size();
        this.cursorWidth = params.width;
        cursor.setLayoutParams(params);

        TextView text = (TextView) findViewById(R.id.title1);
        TextView text2 = (TextView) findViewById(R.id.title2);
        TextView text3 = (TextView) findViewById(R.id.title3);

        text.setText("title1");
        text2.setText("title2");
        text3.setText("title3");

        text.setTag(0);
        text2.setTag(1);
        text3.setTag(2);

        text.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);

        title = new ArrayList<>();
        title.add(text);
        title.add(text2);
        title.add(text3);
        titleChose(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        //i:页卡序号 v:滚动的距离所占百分比 i1:滚动距离的实际长度
        //所以游标实际应该滚动的距离是(i+v)*cursorWidth
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
        params.leftMargin = (int) ((i + v) * cursorWidth);
        cursor.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int i) {
        titleClear();
        titleChose(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void titleClear() {
        for (TextView view : title) {
            view.setTextColor(Color.WHITE);
        }
    }

    private void titleChose(int i) {
        title.get(i).setTextColor(Color.RED);
    }

    @Override
    public void onClick(View v) {
        Integer tag = (Integer) v.getTag();
        titleClear();
        titleChose(tag);
        pager.setCurrentItem(tag);
    }
}
