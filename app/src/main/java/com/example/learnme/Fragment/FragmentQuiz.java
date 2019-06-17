package com.example.learnme.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseQuizAnswer;
import com.example.learnme.API.ResponseQuizQuestion;
import com.example.learnme.Model.QuizAnswer;
import com.example.learnme.Model.QuizQuestion;
import com.example.learnme.QuizActivity;
import com.example.learnme.R;
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

import static android.content.Context.MODE_PRIVATE;

public class FragmentQuiz extends Fragment {

    public interface eventListener{
        public void someEvent(Integer index, String answer);
    }

    eventListener onSomeEvent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSomeEvent = (eventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement eventListener");
        }
    }

    private RadioGroup rg_answer;
    private RadioButton rb_A,rb_B,rb_C,rb_D;

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;
    private Serializable mListQuestion = new ArrayList<>();
    private List<QuizAnswer> mListAnswer = new ArrayList<>();

    private TextView txt_title,txt_desc,txt_question;
    private Integer index;

    private String answer="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_quiz,container,false);

        //View ini
        txt_title = (TextView) view.findViewById(R.id.txt_quiz_title);
        txt_desc  = (TextView) view.findViewById(R.id.txt_quiz_description);
        txt_question = (TextView) view.findViewById(R.id.txt_question_index);
        rg_answer    = (RadioGroup) view.findViewById(R.id.radio_group_answer);
        rb_A      = (RadioButton) view.findViewById(R.id.radio_button_a);
        rb_B      = (RadioButton) view.findViewById(R.id.radio_button_b);
        rb_C      = (RadioButton) view.findViewById(R.id.radio_button_c);
        rb_D      = (RadioButton) view.findViewById(R.id.radio_button_d);

        ////initialize
        String title = getArguments().getString("title");
        String desc  = getArguments().getString("desc");
        index =getArguments().getInt("index");
        answer= getArguments().getString("answer","");

        txt_title.setText(title);
        txt_desc.setText(desc);

        //Asset
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getQuestion("1",index);
        rg_answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_button_a:
                        onSomeEvent.someEvent(index,rb_A.getText().toString());
                        break;
                    case R.id.radio_button_b:
                        onSomeEvent.someEvent(index,rb_B.getText().toString());
                        break;
                    case R.id.radio_button_c:
                        onSomeEvent.someEvent(index,rb_C.getText().toString());
                        break;
                    case R.id.radio_button_d:
                        onSomeEvent.someEvent(index,rb_D.getText().toString());
                        break;
                }
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

    private void getQuestion(final String id,final Integer index){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseQuizQuestion> mService = mApiService.getQuizQuestion(id);
        mService.enqueue(new Callback<ResponseQuizQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuizQuestion> call, Response<ResponseQuizQuestion> response) {
                if(response.isSuccessful()){
                    txt_question.setText(response.body().getData().get(index-1).getQuestion());
                    getAnswer(response.body().getData().get(index-1).getId());
                }else {
                    Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuizQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAnswer(final String id){
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseQuizAnswer> mService = mApiService.getQuizAnswer(id);
        mService.enqueue(new Callback<ResponseQuizAnswer>() {
            @Override
            public void onResponse(Call<ResponseQuizAnswer> call, Response<ResponseQuizAnswer> response) {
                if(response.isSuccessful()){
                    initializeRadioButton(response.body().getData());
                }else{
                    Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuizAnswer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeRadioButton(List<QuizAnswer> mAnswer){

        rb_A      = (RadioButton) getView().findViewById(R.id.radio_button_a);
        rb_B      = (RadioButton) getView().findViewById(R.id.radio_button_b);
        rb_C      = (RadioButton) getView().findViewById(R.id.radio_button_c);
        rb_D      = (RadioButton) getView().findViewById(R.id.radio_button_d);

        // init
        rb_A.setText(mAnswer.get(0).getAnswer());
        rb_B.setText(mAnswer.get(1).getAnswer());
        rb_C.setText(mAnswer.get(2).getAnswer());
        rb_D.setText(mAnswer.get(3).getAnswer());

        if(!answer.equals("")){
            if(rb_A.getText().toString().equals(answer))
                rb_A.setChecked(true);
            else if(rb_B.getText().toString().equals(answer))
                rb_B.setChecked(true);
            else if(rb_C.getText().toString().equals(answer))
                rb_C.setChecked(true);
            else if(rb_D.getText().toString().equals(answer))
                rb_D.setChecked(true);
        }
        progressDialog.dismiss();
    }
}
