package com.example.learnme.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseTrendsQuestion;
import com.example.learnme.About;
import com.example.learnme.Adapter.TrendingQuestionAdapter;
import com.example.learnme.AskQuestion;
import com.example.learnme.AvatarShop;
import com.example.learnme.ChangeLanguage;
import com.example.learnme.Login;
import com.example.learnme.Model.Trending;
import com.example.learnme.Profile;
import com.example.learnme.QuestionActivity;
import com.example.learnme.QuizList;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private List<Trending> listTrending = new ArrayList<>();
    private CardView cd_es,cd_jhs,cd_shs,cd_g;
    private Button btn_search;
    private RelativeLayout semi_transparent,item_menu;
    private ImageView img_menu,img_promotion;
    private TextView txt_menu_close,txt_menu_profile,txt_menu_about,txt_menu_logout,txt_language,txt_trendings,txt_category,txt_promotion;
    private ProgressDialog progressDialog;
    private String id_user="";
    private EditText et_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_home,container,false);

        id_user=getArguments().getString("user");

        //get preference language
        final SharedPreferences pref       = getContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        // View ini
        btn_search       = (Button) view.findViewById(R.id.btn_search_hp);
        final FabSpeedDial floating_menu = (FabSpeedDial) view.findViewById(R.id.floating_menu);
        semi_transparent = (RelativeLayout) view.findViewById(R.id.semi_white_bg);
        item_menu        = (RelativeLayout) view.findViewById(R.id.item_menu);
        img_menu         = (ImageView) view.findViewById(R.id.btn_menu_homepage);
        txt_menu_close   = (TextView) view.findViewById(R.id.txt_menu_close);
        txt_menu_profile = (TextView) view.findViewById(R.id.txt_menu_profile);
        txt_menu_about   = (TextView) view.findViewById(R.id.txt_menu_about);
        txt_menu_logout  = (TextView) view.findViewById(R.id.txt_menu_logout);
        img_promotion    = (ImageView) view.findViewById(R.id.img_promotion);
        et_search        = (EditText) view.findViewById(R.id.et_search);
        txt_language     = (TextView) view.findViewById(R.id.txt_menu_language);
        txt_trendings    = (TextView) view.findViewById(R.id.txt_trendings);
        txt_category     = (TextView) view.findViewById(R.id.txt_category);
        txt_promotion    = (TextView) view.findViewById(R.id.txt_promotion);

        et_search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //asset
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Set View by Languange
        changeViewLanguange(getPref);

        // initialize recycle view item trending
        trendingQuestion(id_user);

        // cardview ini
        cd_es  = (CardView) view.findViewById(R.id.cd_es);
        cd_jhs = (CardView) view.findViewById(R.id.cd_jhs);
        cd_shs = (CardView) view.findViewById(R.id.cd_shs);
        cd_g   = (CardView) view.findViewById(R.id.cd_g);

        // listener

        img_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AvatarShop.class);
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        });

        txt_menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Profile.class);
                intent.putExtra("id",id_user);
                startActivity(intent);
                item_menu.setVisibility(View.INVISIBLE);
            }
        });

        txt_menu_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), About.class);
                intent.putExtra("id",id_user);
                startActivity(intent);
                item_menu.setVisibility(View.INVISIBLE);
            }
        });

        txt_menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs2",MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(getContext(),Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        txt_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeLanguage.class);
                startActivity(intent);
            }
        });


        item_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_menu.setVisibility(View.INVISIBLE);
            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_menu.setVisibility(View.VISIBLE);
            }
        });

        txt_menu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_menu.setVisibility(View.INVISIBLE);
            }
        });


        // card
        cd_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value","2");
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        });

        cd_jhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value","3");
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        });

        cd_shs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value","4");
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        });

        cd_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value","1");
                intent.putExtra("id",id_user);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value",et_search.getText().toString());
                intent.putExtra("id",id_user);
                et_search.setText("");
                startActivity(intent);
            }
        });

        floating_menu.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                semi_transparent.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.quiz:
                        Intent intent2 = new Intent(getView().getContext(), QuizList.class);
                        intent2.putExtra("id",id_user);
                        intent2.putExtra("request","1");
                        startActivity(intent2);
                        break;
                    case R.id.question:
                        Intent intent = new Intent(getView().getContext(), AskQuestion.class);
                        intent.putExtra("user",id_user);
                        startActivity(intent);
                        break;
                }
                semi_transparent.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onMenuClosed() {
                semi_transparent.setVisibility(View.GONE);
            }
        });


        return view;
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

    private void trendingQuestion(final String id_user){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.getTendingQuestion();
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++) {
                        listTrending.add(new Trending(Integer.valueOf(response.body().getData().get(i).getId()),response.body().getData().get(i).getTitle(),
                                response.body().getData().get(i).getDescription(),
                                Integer.valueOf(response.body().getData().get(i).getLikes()),
                                Integer.valueOf(response.body().getData().get(i).getDislikes()),
                                Integer.valueOf(response.body().getData().get(i).getComment())));

                        // recycle view trending listener
                        if(isAdded()){
                            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_view_trending_question);
                            TrendingQuestionAdapter myAdapter   = new TrendingQuestionAdapter(getContext(),listTrending,id_user);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(myAdapter);
                        }

                    }
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeViewLanguange(String language){
        if(language.equals("Indonesia")){
            txt_trendings.setText("Populer");
            et_search.setHint("Cari Pertanyaan");
            btn_search.setText("Cari");
            txt_category.setText("Kategori");
            txt_promotion.setText("Promosi");
            txt_menu_about.setText("Tentang");
            txt_menu_logout.setText("Keluar");
            txt_menu_profile.setText("Profil");
            txt_language.setText("Bahasa");
            txt_menu_close.setText("Tutup");
        }
    }
}
