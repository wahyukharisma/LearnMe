package com.example.learnme.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.learnme.AvatarShop;
import com.example.learnme.Controller.HotItemAdapter;
import com.example.learnme.R;

public class FragmentShop extends Fragment {

    ImageView imgAvatar,imgQuiz,imgMoreAvatar,imgMoreQuiz;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shop,container,false);

        //view pager ini
        ViewPager viewPager           = view.findViewById(R.id.vp_item);
        HotItemAdapter hotItemAdapter = new HotItemAdapter(getContext());
        viewPager.setAdapter(hotItemAdapter);

        //shop ini
        imgAvatar     = (ImageView)view.findViewById(R.id.img_banner_avatar);
        imgQuiz       = (ImageView)view.findViewById(R.id.img_banner_quiz);
        imgMoreAvatar = (ImageView)view.findViewById(R.id.img_more_avatar);
        imgMoreQuiz   = (ImageView)view.findViewById(R.id.img_more_quiz);

        //listener
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent(1);
            }
        });

        imgQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent(2);
            }
        });

        imgMoreQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent(1);
            }
        });

        imgMoreAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent(2);
            }
        });


        return view;

    }

    private void Intent(int value){
        Intent intent = new Intent(getView().getContext(), AvatarShop.class);
        intent.putExtra("index",value);
        startActivity(intent);
        //getActivity().finish();
    }
}
