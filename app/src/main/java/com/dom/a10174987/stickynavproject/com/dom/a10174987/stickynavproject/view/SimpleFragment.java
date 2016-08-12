package com.dom.a10174987.stickynavproject.com.dom.a10174987.stickynavproject.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dom.a10174987.stickynavproject.R;

/**
 * Created by 10174987 on 2016/8/12.
 */

public class SimpleFragment extends Fragment {

    private String title;

    public static SimpleFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString("TITLE", str);
        SimpleFragment fragment = new SimpleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("TITLE", "无题");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_scrollview, container,false);
        return view;
    }
}
