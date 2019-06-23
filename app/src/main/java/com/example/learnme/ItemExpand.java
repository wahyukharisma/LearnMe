package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.API.ResponseItem;
import com.example.learnme.API.UpdatePoint;
import com.example.learnme.Fragment.FragmentHome;
import com.example.learnme.Fragment.FragmentShop;
import com.example.learnme.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemExpand extends AppCompatActivity {

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private ImageView img_item,btn_close,img_item_buy;
    private TextView txt_title, txt_price, txt_valid_date, txt_sold, txt_description,txt_purchased,txt_item_buy,txt_valid_date_title,txt_total_sold,txt_category,txt_item_category,txt_description_item,txt_confirmation;
    private TextView txt_mypoint_title,txt_itempoint_title;
    private Button btn_buy;
    private RelativeLayout rl_confirmation,rl_success_buy;

    //Confirmation layout
    private Button btn_purchase,btn_cancel_purchase,btn_success;
    private TextView txt_mypoint,txt_itempoint,txt_status,txt_success_buy;
    private List<User> mList= new ArrayList<>();
    Fragment myFragment;

    private Animation imgAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_expand);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        final String getPref = pref.getString("language","English");

        // get data intent
        Intent intent   = getIntent();
        String name     = intent.getExtras().getString("name");
        final String price    = intent.getExtras().getString("price");
        final String id       = intent.getStringExtra("id");
        String desc     = intent.getExtras().getString("description");
        final String id_user  = intent.getExtras().getString("id_user");
        String date     = intent.getExtras().getString("date");
        String sold     = intent.getExtras().getString("sold");
        String status   = intent.getStringExtra("status");
        String image    = intent.getStringExtra("image");

        // view ini
        img_item        = (ImageView) findViewById(R.id.img_item);
        btn_close       = (ImageView) findViewById(R.id.close_screen);
        txt_title       = (TextView) findViewById(R.id.txt_title_item);
        txt_price       = (TextView) findViewById(R.id.txt_price_item);
        txt_valid_date  = (TextView) findViewById(R.id.txt_valid_date);
        txt_sold        = (TextView) findViewById(R.id.total_sold);
        txt_description = (TextView) findViewById(R.id.txt_desc_item);
        btn_buy         = (Button) findViewById(R.id.btn_buy);
        rl_confirmation = (RelativeLayout) findViewById(R.id.rl_confirmation);
        btn_purchase    = (Button) findViewById(R.id.btn_purchase);
        btn_cancel_purchase = (Button) findViewById(R.id.btn_cancel_purchase);
        txt_itempoint   = (TextView) findViewById(R.id.txt_item_point);
        txt_status      = (TextView) findViewById(R.id.txt_status_buy);
        txt_purchased   = (TextView) findViewById(R.id.txt_purchased);
        img_item_buy    = (ImageView) findViewById(R.id.id_img_buy);
        txt_item_buy    = (TextView) findViewById(R.id.txt_name_buy);
        rl_success_buy  = (RelativeLayout) findViewById(R.id.rl_success_buy);
        btn_success     = (Button) findViewById(R.id.btn_finish);
        imgAnimation       = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        txt_success_buy = (TextView) findViewById(R.id.txt_success_buy);
        txt_confirmation = (TextView) findViewById(R.id.txt_confirmation);
        txt_mypoint_title = (TextView) findViewById(R.id.txt_my_point_title);
        txt_itempoint_title = (TextView) findViewById(R.id.txt_item_point_title);

        txt_valid_date_title = (TextView) findViewById(R.id.txt_valid_title);
        txt_total_sold  = (TextView) findViewById(R.id.txt_total_sold);
        txt_category    = (TextView) findViewById(R.id.txt_category);
        txt_item_category = (TextView) findViewById(R.id.txt_item_category);
        txt_description_item = (TextView) findViewById(R.id.txt_description_item);

        if(status.equals("1")){
            txt_purchased.setVisibility(View.VISIBLE);
            btn_buy.setVisibility(View.GONE);
        }else {
            txt_purchased.setVisibility(View.GONE);
            btn_buy.setVisibility(View.VISIBLE);
        }

        //change view language
        initViewLanguage(getPref);

        //assets
        progressDialog = new ProgressDialog(ItemExpand.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getUser(id_user);
        progressDialog.show();
        Glide.with(getApplicationContext()).load(image).into(img_item);
        Glide.with(getApplicationContext()).load(image).into(img_item_buy);
        //img_item.setImageResource(R.drawable.noimage);
        txt_title.setText(name);
        txt_description.setText(desc);
        txt_price.setText(price);
        txt_valid_date.setText(date);
        txt_sold.setText(sold);
        txt_itempoint.setText(price);
        progressDialog.dismiss();


        // listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_confirmation.setVisibility(View.VISIBLE);
                if(Integer.valueOf(txt_mypoint.getText().toString())>=Integer.valueOf(txt_price.getText().toString())){
                    if(getPref.equals("Indonesia")){
                        txt_status.setText("YA");
                    }else{
                        txt_status.setText("YES");
                    }
                }else{
                    if(getPref.equals("Indonesia")){
                        txt_status.setText("TIDAK");
                    }else {
                        txt_status.setText("NO");
                    }
                    btn_purchase.setVisibility(View.GONE);
                    txt_status.setTextColor(getResources().getColor(R.color.soft_red));
                }
            }
        });

        btn_cancel_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_confirmation.setVisibility(View.GONE);
            }
        });

        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = "Buy item "+txt_title.getText().toString();
                buyItem(id_user,id,description,txt_mypoint.getText().toString(),price);
            }
        });

        rl_confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_confirmation.setVisibility(View.GONE);
            }
        });

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initConfirm(User user){
        txt_mypoint     = (TextView) findViewById(R.id.txt_my_point);
        txt_mypoint.setText(user.getPoint());
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

    private void getUser(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.getUserBy(id);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    mList.add(response.body().getData().get(0));
                    initConfirm(mList.get(0));
                }else{
                    Toast.makeText(ItemExpand.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ItemExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buyItem(final String id, final String id_item, final String description,final String point, final String price){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseItem> mService = mApiService.buyItem(id,id_item,description,point,price);
        mService.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, retrofit2.Response<ResponseItem> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    //Toast.makeText(ItemExpand.this, "Item has been purchase", Toast.LENGTH_SHORT).show();
                    rl_confirmation.setVisibility(View.GONE);
                    rl_success_buy.setVisibility(View.VISIBLE);
                    txt_item_buy.setText(txt_title.getText().toString());
                    img_item_buy.setAnimation(imgAnimation);

                    // adding point
                    UpdatePoint updatePoint = new UpdatePoint();
                    updatePoint.updatePoint(id,"5",price,description);
                }else{
                    Toast.makeText(ItemExpand.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ItemExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_valid_date_title.setText("Berlaku sampai");
            txt_total_sold.setText("Jumlah Terjual");
            txt_category.setText("Kategori");
            txt_item_category.setText("Gambar/Profil Avatar");
            txt_description_item.setText("Deskripsi");
            btn_buy.setText("Beli");
            btn_success.setText("Kembali");
            txt_success_buy.setText("Selamat Anda Memiliki Avatar Baru");
            txt_confirmation.setText("Konfirmasi Pembelian");
            txt_mypoint_title.setText("Poin Saya");
            txt_itempoint_title.setText("Poin Barang");
            btn_purchase.setText("Beli");
            btn_cancel_purchase.setText("Batal");
            txt_purchased.setText("Avatar Sudah Dibeli");



        }
    }

}
