package com.example.learnme.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseMRanking;
import com.example.learnme.API.ResponseRanking;
import com.example.learnme.Adapter.RewardAdapter;
import com.example.learnme.Login;
import com.example.learnme.Model.MRanking;
import com.example.learnme.Model.Ranking;
import com.example.learnme.Model.User;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class FragmentReward extends Fragment {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private RecyclerView recyclerView;
    private RewardAdapter rewardAdapter;
    private ArrayList<Ranking> userArrayList = new ArrayList<>();
    private Spinner spinner_month,spinner_year;
    private ProgressDialog progressDialog;
    private String id_user;
    private TextView txt_champ_1,txt_point_1;
    private TextView txt_champ_2,txt_point_2;
    private TextView txt_champ_3,txt_point_3;
    private TextView txt_point_me,txt_index_me,txt_date_title,txt_point_title,txt_my_rank,txt_celebration,txt_info_reward;
    private Button btnSearch;
    private ImageView img_title_reward;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward,container,false);

        id_user=getArguments().getString("user");

        //get preference language
        final SharedPreferences pref       = getContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //View ini
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String thisMonth = convertMonth(month,getPref);
        btnSearch = (Button) view.findViewById(R.id.btn_search_rank);

        spinner_month = (Spinner) view.findViewById(R.id.spin_month);
        ArrayList<String> array_year = new ArrayList<>();
        spinner_year  = (Spinner) view.findViewById(R.id.spin_year);

        //init value
        recyclerView  = (RecyclerView) view.findViewById(R.id.recycle_view_user);
        txt_index_me = (TextView) view.findViewById(R.id.txt_my_rank);
        txt_point_me = (TextView) view.findViewById(R.id.txt_my_point);
        txt_champ_1 = view.findViewById(R.id.txt_champ_1);
        txt_champ_2 = view.findViewById(R.id.txt_champ_2);
        txt_champ_3 = view.findViewById(R.id.txt_champ_3);
        txt_date_title = view.findViewById(R.id.txt_date_title);
        txt_my_rank = view.findViewById(R.id.txt_my_rank_title);
        txt_point_title = view.findViewById(R.id.txt_point_title);
        txt_celebration = view.findViewById(R.id.txt_celebration);
        img_title_reward = view.findViewById(R.id.img_title_reward);
        txt_info_reward  = view.findViewById(R.id.txt_info_reward);

        txt_point_1 = view.findViewById(R.id.txt_point_1);
        txt_point_2 = view.findViewById(R.id.txt_point_2);
        txt_point_3 = view.findViewById(R.id.txt_point_3);

        txt_champ_1.setText("-");
        txt_champ_2.setText("-");
        txt_champ_3.setText("-");

        txt_point_1.setText("-");
        txt_point_2.setText("-");
        txt_point_3.setText("-");

        txt_index_me.setText("-");
        txt_point_me.setText("-");

        //Init View language
        initViewLanguage(getPref);

        //assets
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // recycle view ini
        userArrayList = new ArrayList<>();
        userArrayList.clear();
        getRanking(thisMonth,String.valueOf(year));
        myRank(id_user,thisMonth,String.valueOf(year));

        //spinner ini
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.month,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter);
        spinner_month.setSelection(month);

        for(int i=2019;i<=year;i++){
            array_year.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,array_year);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter1);

        //listener
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempMonth = spinner_month.getSelectedItem().toString();
                String tempYear = spinner_year.getSelectedItem().toString();
                userArrayList.clear();
                getRanking(tempMonth,tempYear);
                myRank(id_user,tempMonth,tempYear);
            }
        });

        return view;
    }

    public void initView(List<Ranking> mList){
        if(!mList.isEmpty()) {
            if (mList.size() > 2) {
                txt_champ_1.setText(mList.get(0).getUsername());
                txt_champ_2.setText(mList.get(1).getUsername());
                txt_champ_3.setText(mList.get(2).getUsername());

                txt_point_1.setText(mList.get(0).getPoint());
                txt_point_2.setText(mList.get(1).getPoint());
                txt_point_3.setText(mList.get(2).getPoint());
            } else if (mList.size() > 1) {
                txt_champ_1.setText(mList.get(0).getUsername());
                txt_champ_2.setText(mList.get(1).getUsername());
                txt_champ_3.setText("-");

                txt_point_1.setText(mList.get(0).getPoint());
                txt_point_2.setText(mList.get(1).getPoint());
                txt_point_3.setText("-");
            } else if (mList.size() > 0) {
                txt_champ_1.setText(mList.get(0).getUsername());
                txt_champ_2.setText("-");
                txt_champ_3.setText("-");

                txt_point_1.setText(mList.get(0).getPoint());
                txt_point_2.setText("-");
                txt_point_3.setText("-");
            } else {
                txt_champ_1.setText("-");
                txt_champ_2.setText("-");
                txt_champ_3.setText("-");

                txt_point_1.setText("-");
                txt_point_2.setText("-");
                txt_point_3.setText("-");

            }
        }

    }

    public void initMyRank(final String point,final Integer index){
        txt_index_me = (TextView) getView().findViewById(R.id.txt_my_rank);
        txt_point_me = (TextView) getView().findViewById(R.id.txt_my_point);
        txt_index_me.setText("-");
        txt_point_me.setText("-");

//        if(point.equals("") || index == null){
//        }else{
//            Integer temp= index;
//            temp+=1;
//            txt_index_me.setText(temp.toString());
//            txt_point_me.setText(point);
//        }
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

    private void getRanking(final String month,final String year){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseRanking> mService = mApiService.getRanking(month,year);
        mService.enqueue(new Callback<ResponseRanking>() {
            @Override
            public void onResponse(Call<ResponseRanking> call, Response<ResponseRanking> response) {
                if(response.isSuccessful()){
                    userArrayList = new ArrayList<>();
                    if(response.body().getValue()==0){
//                        Toast.makeText(getContext(), "No Data Selected", Toast.LENGTH_SHORT).show();
                        userArrayList.clear();
                        txt_info_reward.setVisibility(View.VISIBLE);
                    }else{
                        txt_info_reward.setVisibility(View.GONE);
//                        Toast.makeText(getContext(), "Data Selected", Toast.LENGTH_SHORT).show();
                        for(int i=0;i<response.body().getData().size();i++){
                            Ranking ranking= new Ranking(response.body().getData().get(i).getIndex(),response.body().getData().get(i).getPoint(),response.body().getData().get(i).getUsername());
                            userArrayList.add(ranking);
                        }
                        if(userArrayList.size()!=0){
                            initView(userArrayList);
//                            recyclerView  = (RecyclerView) getView().findViewById(R.id.recycle_view_user);
                            rewardAdapter = new RewardAdapter(userArrayList,getContext());

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(rewardAdapter);
                        }
                    }

                    initView(userArrayList);
                    rewardAdapter = new RewardAdapter(userArrayList,getContext());

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(rewardAdapter);
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    //Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRanking> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void myRank(final String id,final String month,final String year){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseMRanking> mService = mApiService.getMyRanking(month,year,id);
        mService.enqueue(new Callback<ResponseMRanking>() {
            @Override
            public void onResponse(Call<ResponseMRanking> call, Response<ResponseMRanking> response) {
                if(response.isSuccessful()){

                    if( response.body().getValue() == 0){
                        initMyRank("No Data",-1);
                    }else {
                        if(response.body().getData() != null){
                            txt_index_me.setText(String.valueOf(response.body().getData().getIndex()+1));
                            txt_point_me.setText(response.body().getData().getPoint());
                            //initMyRank(response.body().getData().getPoint(),response.body().getData().getIndex());
                        }
                    }
                }else {
                    Toast.makeText(getView().getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseMRanking> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertMonth(Integer input,String language){
        String temp="";
            if(input==0){
                temp="January";
            }else if(input==1){
                temp="February";
            }else if(input==2){
                temp="March";
            }else if(input==3){
                temp="April";
            }else if(input==4){
                temp="May";
            }else if(input==5){
                temp="June";
            }else if(input==6){
                temp="July";
            }else if(input==7){
                temp="August";
            }else if(input==8){
                temp="September";
            }else if(input==9){
                temp="October";
            }else if(input==10){
                temp="November";
            }else{
                temp="December";
            }
        return temp;
    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_point_title.setText("Poin");
            txt_my_rank.setText("Peringkat Saya");
            btnSearch.setText("Cari");
            txt_date_title.setText("Tanngal");
            txt_celebration.setText("Selamat Kepada Pemenang");
            img_title_reward.setImageResource(R.drawable.reward_logo_indo);
            txt_info_reward.setText("Tidak ada data");
        }
    }

}
