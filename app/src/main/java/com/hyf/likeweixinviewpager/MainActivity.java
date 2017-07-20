package com.hyf.likeweixinviewpager;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.LinearLayout;

import com.hyf.likeweixinviewpager.widget.ChangeColorIconWithText;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPager;

    private LinearLayout bottomView;

    private List<Fragment> mFragments = new ArrayList<>();
    private List<ChangeColorIconWithText> bottomItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMenuOverflowAlways();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mViewPager = findViewById(R.id.viewpager);
        bottomView = findViewById(R.id.bottomView);

        for (int i = 0; i < bottomView.getChildCount(); i++) {
            bottomItems.add((ChangeColorIconWithText) bottomView.getChildAt(i));
            final int finalI = i;
            bottomItems.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < bottomItems.size(); j++) {
                        bottomItems.get(j).setIconAlpha(0);
                    }
                    bottomItems.get(finalI).setIconAlpha(1);
                    mViewPager.setCurrentItem(finalI,false);
                }
            });
        }

        for (int i = 0; i < 4; i++) {
            mFragments.add(BaseFragment.newInstance("第" + (i + 1) + "页"));
        }

        FragmentAdapter adapter = new FragmentAdapter(mFragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(mChangeListener);
        mChangeListener.onPageSelected(mViewPager.getCurrentItem());
    }

    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset>0){
                bottomItems.get(position).setIconAlpha(1-positionOffset);
                bottomItems.get(position+1).setIconAlpha(positionOffset);
            }
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < bottomItems.size(); i++) {
                bottomItems.get(i).setIconAlpha(0);
            }
            bottomItems.get(position).setIconAlpha(1);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    //设置menu菜单的第一个图标显示在标题右上角---使用反射机制来完成
    public void setMenuOverflowAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field field = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            field.setAccessible(true);
            field.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置每个Menu显示为左图标右标题
    //解决ICON不显示
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}
