package com.hyf.likeweixinviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/19.
 */

public class BaseFragment extends Fragment {
    private static final String BUNDLE = "BUNDLE";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String msg = getArguments().getString(BUNDLE);
        TextView textView = new TextView(getContext());
        textView.setText(msg);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public static BaseFragment newInstance(String msg) {
        Bundle bundle = new Bundle();
        BaseFragment fragment = new BaseFragment();
        bundle.putString(BUNDLE, msg);
        fragment.setArguments(bundle);
        return fragment;
    }
}
