package com.dcodestar.braintester;

import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int totalQuestions;
    int totalAnswers;
    int correctAnswer;
    android.support.v7.widget.GridLayout gridLayout;
    TextView questionTextView;
    TextView resultTextView;
    TextView scoreTextView;
    Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button coverButton=findViewById(R.id.coverButton);
        coverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout cover=findViewById(R.id.cover);
                cover.setVisibility(View.INVISIBLE);
                playGame();
            }
        });
    }
    void playGame(){
        totalQuestions=0;
        totalAnswers=0;
        gridLayout=findViewById(R.id.gridLayout);
        questionTextView=findViewById(R.id.questionTextView);
        resultTextView=findViewById(R.id.resultTextView);
        scoreTextView=findViewById(R.id.scoreTextView);
        playAgainButton=findViewById(R.id.playAgainButton);
        changeTime(30);
        updateResultTextView("");
        updateScoreTextView();
        for(int i=0;i<gridLayout.getChildCount();i++){
            Button button=(Button)gridLayout.getChildAt(i);
            button.setEnabled(true);
        }
        CountDownTimer ct=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                changeTime((int)millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                for(int i=0;i<gridLayout.getChildCount();i++){
                    Button button=(Button)gridLayout.getChildAt(i);
                    button.setEnabled(false);
                }
                playAgainButton.setVisibility(View.VISIBLE);
            }
        }.start();
        setQuestion();
    }
    void changeTime(int timeleft){
        TextView timerTextView=findViewById(R.id.timerTextView);
        StringBuilder s=new StringBuilder();
        if(timeleft<=9){
            s.append("0");
        }
        timerTextView.setText(s+String.valueOf(timeleft)+"s");
    }

    void setQuestion(){
        Random random=new Random();
        int n1=random.nextInt(100)+1;
        int n2=random.nextInt(100)+1;
        int answers[]=new int[4];
        int index=random.nextInt(4);
        answers[index]=n1+n2;
        correctAnswer=index;
        int c=index==0?1:0;
        for(int i=0;i<3;i++){
            boolean again=true;
            int n=0;
            while(again){
                again=false;
                n=random.nextInt(200)+1;
                for(int j=0;j<c;j++){
                    if(answers[j]==n){
                        again=true;
                        break;
                    }
                }
            }
            if(c!=index){
                answers[c]=n;
                c++;
            }else{
                c++;
                answers[c]=n;
                c++;
            }
        }
        for(int i=0;i<gridLayout.getChildCount();i++){
            Button button=(Button)gridLayout.getChildAt(i);
            button.setText(String.valueOf(answers[i]));
        }
        questionTextView.setText(String.valueOf(n1)+"+"+String.valueOf(n2));
    }

    public void answerClicked(View view){
        Button button=(Button)view;
        int tag=Integer.parseInt(button.getTag().toString());
        totalQuestions++;
        if(tag==correctAnswer){
            totalAnswers++;
            updateResultTextView("Correct");
        }else{
            updateResultTextView("Oops!");
        }
        updateScoreTextView();
        setQuestion();
    }

    void updateResultTextView(String s){
        resultTextView.setText(s);
    }

    void updateScoreTextView(){
        scoreTextView.setText(String.valueOf(totalAnswers)+"/"+String.valueOf(totalQuestions));
    }

    public void playAgainPressed(View view){
        playGame();
        view.setVisibility(View.INVISIBLE);
    }
}
