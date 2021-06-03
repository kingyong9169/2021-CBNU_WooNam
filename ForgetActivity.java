package com.app_services.WooNam.chattingapp;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity { // forget했을시 찾도록 도와주는함수
    EditText email;
    Button btn_reset;
    TextView tv_clicking;

    FirebaseAuth firebaseAuth; //파이어베이스
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {//홈 버튼으로 나갔다 들어왓을 경우 앱을 죽지 않게 처리를 할 수 있도록 도와줌

        super.onCreate(savedInstanceState);//오버라이드된 메소드를처리한다 .
        setContentView(R.layout.activity_forget);//화면에 나타난 뷰를 지정한다.

        Toolbar toolbar = findViewById(R.id.toolbar);//툴바셋팅
        setSupportActionBar(toolbar);//메서드를 호출하고 활동의 툴바를 전달
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");//활동에서 내 작업 표시 줄로 도구 모음을 사용
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기버튼 , 디폴트값 =true

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//setNavigationOnClickListener의 툴바
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        email = findViewById(R.id.email);//email 참조를 가져올때 필요
        btn_reset = findViewById(R.id.btn_reset);//reset의 참조를 가져올때 필요
        tv_clicking = findViewById(R.id.clicking);//click의 참조를 가져올때 필요
        firebaseAuth = FirebaseAuth.getInstance();//firebase의 참조를 가져올때 필요

        email.addTextChangedListener(new TextWatcher() {//email의 이벤트를 받아올 때 쓰는 API
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//문자열 변경이벤트

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//새로운 text가 시작할때 불러줌
                if (email.getText().toString().equals("")){
                    btn_reset.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {//ontextchange 호출후 호출되는함수

            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {//reset의 버튼이벤트를 관리함
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(ForgetActivity.this, "Field is required!", Toast.LENGTH_SHORT).show();
                } else {
                    btn_reset.setBackgroundColor(getResources().getColor(R.color.colorButton));
                    tv_clicking.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(str_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgetActivity.this, "Check Your Inbox!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(ForgetActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });
    }
}
