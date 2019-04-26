package com.example.learnme;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemExpand extends AppCompatActivity {

    ImageView img_item,btn_close;
    TextView txt_title, txt_price, txt_valid_date, txt_sold, txt_description;
    Button btn_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_expand);

        // get data intent
        Intent intent = getIntent();
        String name   = intent.getExtras().getString("Name");
        Integer price = intent.getExtras().getInt("Price");
        Integer id    = intent.getExtras().getInt("Id");
        String desc   = intent.getExtras().getString("Description");

        // view ini
        img_item        = (ImageView) findViewById(R.id.img_item);
        btn_close       = (ImageView) findViewById(R.id.close_screen);
        txt_title       = (TextView) findViewById(R.id.txt_title_item);
        txt_price       = (TextView) findViewById(R.id.txt_price_item);
        txt_valid_date  = (TextView) findViewById(R.id.txt_valid_date);
        txt_sold        = (TextView) findViewById(R.id.total_sold);
        txt_description = (TextView) findViewById(R.id.txt_desc_item);
        btn_buy         = (Button) findViewById(R.id.btn_buy);

        img_item.setImageResource(id);
        txt_title.setText(name);
        txt_description.setText(desc);
        txt_price.setText(Integer.toString(price));

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
                Toast.makeText(getApplicationContext(),"Button buy clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
