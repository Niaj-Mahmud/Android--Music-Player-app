package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class feedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText textname,textfeedback;
    private Button buttonsend,buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        textname = findViewById(R.id.editTextTextPersonName);
        textfeedback= findViewById(R.id.editTextTextfeedback);
        buttonsend= findViewById(R.id.button_send);
        buttonClear= findViewById(R.id.button_clear);
        buttonsend.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        try {
            String name = textname.getText().toString();
            String feedback = textfeedback.getText().toString();
            if (v.getId()==R.id.button_send){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");
                String subject[] = {"niaj.nm@gmail.com"};
                String head ="Feedback from Music app ";
                intent.putExtra(Intent.EXTRA_EMAIL,subject);
                intent.putExtra(Intent.EXTRA_SUBJECT,head);
                intent.putExtra(Intent.EXTRA_TEXT,"Name :"+name +"\n Message :"+feedback);
                startActivity(Intent.createChooser(intent,"Feedback with"));
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
            }else if (v.getId()==R.id.button_clear){
                textname.setText("");
                textfeedback.setText("");
            }
        }catch (Exception e){
        }
    }
}