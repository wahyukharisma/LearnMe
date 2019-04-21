package com.example.learnme.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learnme.Controller.HotItemAdapter;
import com.example.learnme.R;

public class FragmentShop extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop,container,false);

        //view pager ini
        ViewPager viewPager           = view.findViewById(R.id.vp_item);
        HotItemAdapter hotItemAdapter = new HotItemAdapter(getContext());
        viewPager.setAdapter(hotItemAdapter);

        return view;

    }
}
