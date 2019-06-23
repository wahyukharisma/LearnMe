package com.example.learnme.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.AvatarShop;
import com.example.learnme.Adapter.HotItemAdapter;
import com.example.learnme.Model.User;
import com.example.learnme.PointInfo;
import com.example.learnme.Profile;
import com.example.learnme.QuizList;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class FragmentShop extends Fragment {

    private ImageView imgAvatar,imgQuiz,imgMoreAvatar,imgMoreQuiz,imgProfile;
    private TextView txt_username,txt_point,txt_point_title,txt_desc_avatar,txt_desc_quiz,txt_quiz_title;
    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private String id_user="";
    private ImageView imgInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shop,container,false);

        id_user=getArguments().getString("user");

        //get preference language
        final SharedPreferences pref       = getContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //view pager ini
//        ViewPager viewPager           = view.findViewById(R.id.vp_item);
//        HotItemAdapter hotItemAdapter = new HotItemAdapter(getContext());
//        viewPager.setAdapter(hotItemAdapter);

        //shop ini
        imgAvatar     = (ImageView)view.findViewById(R.id.img_banner_avatar);
        imgQuiz       = (ImageView)view.findViewById(R.id.img_banner_quiz);
        imgMoreAvatar = (ImageView)view.findViewById(R.id.img_more_avatar);
        imgMoreQuiz   = (ImageView)view.findViewById(R.id.img_more_quiz);
        imgInfo       = (ImageView)view.findViewById(R.id.img_info_point);
        imgProfile    = (ImageView)view.findViewById(R.id.img_profile);
        txt_point     = (TextView) view.findViewById(R.id.txt_user_point);
        txt_username  = (TextView) view.findViewById(R.id.txt_user_name);
        txt_point_title = (TextView) view.findViewById(R.id.txt_point_title_shop);
        txt_desc_avatar = (TextView) view.findViewById(R.id.txt_desc_avatar);
        txt_desc_quiz   = (TextView) view.findViewById(R.id.txt_desc_quiz);
        txt_quiz_title  = (TextView) view.findViewById(R.id.info_quiz);

        changeViewLanguage(getPref);

        //assets
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getUserBy(id_user);

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
                Intent(2);
            }
        });

        imgMoreAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent(1);
            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), PointInfo.class);
               startActivity(intent);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Profile.class);
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        });


        return view;

    }

    private void Intent(int value){
        Intent intent;
        if(value==1){
            intent = new Intent(getView().getContext(), AvatarShop.class);
            intent.putExtra("index",value);
        }else{
            intent = new Intent(getView().getContext(), QuizList.class);
            intent.putExtra("request","2");
        }
        intent.putExtra("id",id_user);
        startActivity(intent);
    }

    private APIInterface getInterfaceService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final APIInterface mInterfaceService = retrofit.create(APIInterface.class);
        return mInterfaceService;
    }

    private void getUserBy(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.getUserBy(id);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    initData(response.body().getData().get(0));
                }else {
                    Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(User user){
        txt_username.setText(user.getUsername());
        txt_point.setText(user.getPoint());
        if(isAdded()){
            Glide.with(getContext()).load(user.getImage()).into(imgProfile);
        }
    }

    private void changeViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_quiz_title.setText("Kuis");
            txt_desc_quiz.setText("Ambil kuis, uji kemampuanmu");
            txt_desc_avatar.setText("Beli avatar favoritmu");

        }
    }
}
