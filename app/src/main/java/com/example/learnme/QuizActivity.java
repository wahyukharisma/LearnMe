package com.example.learnme;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseQuiz;
import com.example.learnme.API.ResponseQuizAnswer;
import com.example.learnme.API.ResponseQuizQuestion;
import com.example.learnme.API.UpdatePoint;
import com.example.learnme.Fragment.FragmentHome;
import com.example.learnme.Fragment.FragmentQuiz;
import com.example.learnme.Model.Quiz;
import com.example.learnme.Model.QuizAnswer;
import com.example.learnme.Model.QuizQuestion;
import com.example.learnme.Model.TempAnswer;
import com.example.learnme.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity implements FragmentQuiz.eventListener{

    private Fragment myFragment;
    private ImageView img_back,img_next;
    private TextView txt_index;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;
    private List<QuizQuestion> mList = new ArrayList<>();
    private List<QuizAnswer> mListAnswer = new ArrayList<>();
    private List<TempAnswer> tempAnswer;
    private RelativeLayout rl_result;
    private LinearLayout ll_info_index;
    private Button  btn_result;

    private Button btn_cancel,btn_process;
    private TextView txt_answered;
    private RelativeLayout rl_confirm_quiz;

    private CircleImageView img_circle_1,img_circle_2,img_circle_3,img_circle_4,img_circle_5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        final String id_quiz = intent.getStringExtra("id_quiz");
        final String id_user = intent.getStringExtra("id_user");
        final String point   = intent.getStringExtra("point");
        final String title   = intent.getStringExtra("title");
        final String description   = intent.getStringExtra("description");


        //View ini
        img_back = (ImageView) findViewById(R.id.btn_previous);
        img_next = (ImageView) findViewById(R.id.btn_next);
        txt_index= (TextView) findViewById(R.id.txt_index);
        rl_result= (RelativeLayout) findViewById(R.id.rl_result);
        ll_info_index = (LinearLayout) findViewById(R.id.ll_info_index);
        btn_result = (Button) findViewById(R.id.btn_result);
        btn_process = (Button) findViewById(R.id.btn_process_quiz);
        btn_cancel = (Button) findViewById(R.id.btn_back_quiz);
        txt_answered = (TextView) findViewById(R.id.txt_answered);
        rl_confirm_quiz = (RelativeLayout) findViewById(R.id.rl_confirmation_quiz);


        //Asset
        progressDialog = new ProgressDialog(QuizActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Integer[] index = {1};
        txt_index.setText(index[0]+" / 5");
        tempAnswer = new ArrayList<>();
        //getQuestion("1",index[0]);
        FragmentTransition(title,description,index[0],tempAnswer,id_quiz);

        //Listener
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index[0]>1){
                    index[0]--;
                    txt_index.setText((index[0])+" / 5");
                    FragmentTransition(title,description,index[0],tempAnswer,id_quiz);
                    if(index[0]==1){
                        img_back.setVisibility(View.INVISIBLE);
                    }
                    if(index[0]<5){
                        img_next.setVisibility(View.VISIBLE);
                        rl_result.setVisibility(View.GONE);
                        ll_info_index.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index[0]<5){
                    index[0]++;

                    txt_index.setText((index[0])+" / 5");
                    img_back.setVisibility(View.VISIBLE);
                    FragmentTransition(title,description,index[0],tempAnswer,id_quiz);
                }if(index[0]==5){
                    rl_result.setVisibility(View.VISIBLE);
                    img_next.setVisibility(View.INVISIBLE);
                    ll_info_index.setVisibility(View.GONE);
                    
                    btn_result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer tempAnswered = tempAnswer.size();
                            txt_answered.setText(tempAnswered+"/5");
                            rl_confirm_quiz.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_confirm_quiz.setVisibility(View.GONE);
            }
        });
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate(id_quiz,id_user,tempAnswer,point);
            }
        });
    }

    private void FragmentTransaction(Fragment myFragment){
        FragmentTransaction fragmentTransaction   = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,myFragment);
        fragmentTransaction.commit();
    }

    private void FragmentTransition(final String title,final String description, final Integer index, List<TempAnswer> tempAnswer,final String id_quiz){
        myFragment = new FragmentQuiz();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("desc",description);
        bundle.putInt("index",index);
        bundle.putString("id_quiz",id_quiz);

        if(checkAvailable(index,tempAnswer)!=404){
            bundle.putString("answer",tempAnswer.get(checkAvailable(index,tempAnswer)).getAnswer());
        }

        myFragment.setArguments(bundle);
        FragmentTransaction(myFragment);
    }


    @Override
    public void someEvent(Integer index, String answer ) {

        Integer status=0;

        for(int i=0;i<tempAnswer.size();i++){
            if(tempAnswer.get(i).getIndex().equals(String.valueOf(index))){
                tempAnswer.get(i).setAnswer(answer);
                status=1;
            }
        }
        if(status==0){
            tempAnswer.add(new TempAnswer(String.valueOf(index),answer));
        }

        initAnswerPoint(tempAnswer);


        for(int i=0;i<tempAnswer.size();i++){
            Log.d("message-"+i,tempAnswer.get(i).getAnswer());
        }
        Log.d("index",index.toString());
    }

    public Integer checkAvailable(final Integer index,final List<TempAnswer> tempAnswer){
        if(!tempAnswer.isEmpty()){
            for(int i=0;i<tempAnswer.size();i++){
                if(tempAnswer.get(i).getIndex().equals(String.valueOf(index)))
                    return i;
            }
        }
        return 404;
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

    private void initAnswerPoint(final List<TempAnswer> mList){

        img_circle_1 = (CircleImageView) findViewById(R.id.img_circle_1);
        img_circle_2 = (CircleImageView) findViewById(R.id.img_circle_2);
        img_circle_3 = (CircleImageView) findViewById(R.id.img_circle_3);
        img_circle_4 = (CircleImageView) findViewById(R.id.img_circle_4);
        img_circle_5 = (CircleImageView) findViewById(R.id.img_circle_5);

        if(!mList.isEmpty()){
            for(int i=0;i<mList.size();i++){
                if(Integer.valueOf(mList.get(i).getIndex())==1){
                    img_circle_1.setImageResource(R.color.circle_done);
                }else if(Integer.valueOf(mList.get(i).getIndex())==2){
                    img_circle_2.setImageResource(R.color.circle_done);
                }else if(Integer.valueOf(mList.get(i).getIndex())==3){
                    img_circle_3.setImageResource(R.color.circle_done);
                }else if(Integer.valueOf(mList.get(i).getIndex())==4){
                    img_circle_4.setImageResource(R.color.circle_done);
                }else{
                    img_circle_5.setImageResource(R.color.circle_done);
                }
            }
        }
    }

    private void calculate(final String id,final String id_user,final List<TempAnswer> tempAnswer,final String point){
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseQuizAnswer> mService = mApiService.getQuizAnswerBy(id);
        mService.enqueue(new Callback<ResponseQuizAnswer>() {
            @Override
            public void onResponse(Call<ResponseQuizAnswer> call, Response<ResponseQuizAnswer> response) {
                if(response.isSuccessful()){
                    Integer tempPoint=0;

                    for(int i=0;i<tempAnswer.size();i++){
                        for(int j=0;j<response.body().getData().size();j++){
                            if(tempAnswer.get(i).getAnswer().equals(response.body().getData().get(j).getAnswer())){
                                tempPoint=tempPoint+Integer.valueOf(response.body().getData().get(j).getPoint());
                            }
                        }
                    }

                    // Update History
                    storeHistory(id_user,id,point);

                    Intent intent = new Intent(QuizActivity.this,QuizResult.class);
                    tempPoint=tempPoint*10;
                    Integer totalPoint = 0;

                    if(Integer.valueOf(point)==0){
                        totalPoint =  tempPoint;
                    }else{
                        totalPoint = Integer.valueOf(point)+(Integer.valueOf(point)*(tempPoint/100));
                    }


                   UpdatePoint updatePoint = new UpdatePoint();
                   updatePoint.updatePoint(id,"3",String.valueOf(totalPoint),"Success take paid quiz id:"+id);

                   intent.putExtra("score",String.valueOf(tempPoint));
                   intent.putExtra("totalpoint",String.valueOf(totalPoint));
                   startActivity(intent);
                   finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(QuizActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuizAnswer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuizActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeHistory(final String id,final String id_quiz,final String point){
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseQuiz> mService = mApiService.storeQuizHistory(id,id_quiz);
        mService.enqueue(new Callback<ResponseQuiz>() {
            @Override
            public void onResponse(Call<ResponseQuiz> call, Response<ResponseQuiz> response) {
                if (response.isSuccessful()){
                    UpdatePoint updatePoint = new UpdatePoint();
                    updatePoint.updatePoint(id,"6",point,"Take quiz id: "+id_quiz);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(QuizActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuiz> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuizActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
