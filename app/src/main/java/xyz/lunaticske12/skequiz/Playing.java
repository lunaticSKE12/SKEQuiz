package xyz.lunaticske12.skequiz;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import xyz.lunaticske12.skequiz.Common.Common;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue = 0;

    CountDownTimer countDownTimer;

    int index=0,score=0,thisQuestion=0,totalQuestion,correctAnswer;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD;
    TextView txtScore,txtQuestionNum,question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        //views
        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        question_text = findViewById(R.id.question_text);
        question_image = findViewById(R.id.question_image);

        progressBar = findViewById(R.id.progressBar_id);

        btnA = findViewById(R.id.answerABtn);
        btnB = findViewById(R.id.answerBBtn);
        btnC = findViewById(R.id.answerCBtn);
        btnD = findViewById(R.id.answerDBtn);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        countDownTimer.cancel();
        //have question in list
        if(index < totalQuestion){
            Button clickedButton = (Button) view;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())){
                //choose correct answer
                score+=10;
                correctAnswer++;
                showQuestion(++index);
            }
            else {
                Intent intent = new Intent(this, Done.class);
                Bundle datasend = new Bundle();
                datasend.putInt("SCORE", score);
                datasend.putInt("TOTAL", totalQuestion);
                datasend.putInt("CORRECT", correctAnswer);
                intent.putExtras(datasend);
                startActivity(intent);
                finish();
            }

            txtScore.setText(String.format("%d",score));
        }

    }

    private void showQuestion(int index) {
        if(index < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d/%d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true")){
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else {
                question_text.setText(Common.questionList.get(index).getQuestion());

                //if question is text, set image invisible
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            countDownTimer.start();
        }
        else {
            //if it is final question
            Intent intent = new Intent(this, Done.class);
            Bundle datasend = new Bundle();
            datasend.putInt("SCORE", score);
            datasend.putInt("TOTAL", totalQuestion);
            datasend.putInt("CORRECT", correctAnswer);
            intent.putExtras(datasend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        totalQuestion = Common.questionList.size();

        countDownTimer = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {

                countDownTimer.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
