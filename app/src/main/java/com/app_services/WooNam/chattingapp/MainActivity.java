package com.app_services.WooNam.chattingapp;

import android.content.Intent;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {//MainActivity에 필요한 변수들 불러옴
    ProgressBar progressBar;
    EditText email, password;
    Button login, register;
    TextView forget_password;

    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    @Override
    protected void onStart() {//화면의 구성요소를 나타내고 사용자와 상호작용 시작
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, MainChatActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//활동이 생성되면 생성됨 상태가 되도록함
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//R.layout.activity_main를 인자로 받고 그에 대한 내용을 콘텐츠로 보여줌


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//파이어베이스에서 유효한 사용자를 불러옴

        if(firebaseUser != null){//파이어 베이스가 null이 아니면
            Intent intent = new Intent(MainActivity.this, MainChatActivity.class);
            startActivity(intent);//전환될 액티비티를 직접 적어서 표현
            finish();
        }

        progressBar = findViewById(R.id.progress_bar);//main.xml에서 사용하는 progressBar를 변경할수 있는 메소드를 지원

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        forget_password = findViewById(R.id.forget_password);
        forget_password.setPaintFlags(forget_password.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        auth = FirebaseAuth.getInstance();

        email.addTextChangedListener(new TextWatcher() {//EditText 입력 이벤트
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //s문자열이 start위치로부터 count길이만큼이 after기링로 변경되려고 한다는 내용을 전달해주면서 호출되는 함수
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //s가 start위치로 부터 count길이만큼 변경되었다는 것을 알려주는 함수
            }

            @Override
            public void afterTextChanged(Editable s) {//s내의 어느 문자열이 변경되었음을 알려주는 함수
                if (!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    email.setError("Invalid Email Address");
                    login.setEnabled(false);
                    login.setFocusable(false);
                    login.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                }
                else if (password.length()>7){
                    login.setEnabled(true);
                    login.setFocusable(true);
                    login.setBackgroundColor(getResources().getColor(R.color.colorButton));
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {// EditText의 이벤트를 받아올 때 쓰는 API
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   //s문자열이 start위치로부터 count길이만큼이 after기링로 변경되려고 한다는 내용을 전달해주면서 호출되는 함수

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   //s가 start위치로 부터 count길이만큼 변경되었다는 것을 알려주는 함수

            }

            @Override
            public void afterTextChanged(Editable s) {//s내의 어느 문자열이 변경되었음을 알려주는 함수
                if (!(password.getText().toString().trim().length() > 7)) {
                    password.setError("Password must be 8 character long");
                    login.setEnabled(false);
                    login.setFocusable(false);
                    login.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                }
                else if (email.length()>0) {
                    login.setEnabled(true);
                    login.setFocusable(true);
                    login.setBackgroundColor(getResources().getColor(R.color.colorButton));
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {//로그인의 버튼 클릭이벤트를 처리
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);//프로그래스 바를 없애거나 다시보이게 하는기능

                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())// 비밀번호 기반 계정을 파이어베이스로 보냄
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {//모든 데이터의 발행을 완료함을 알림
                                if(task.isSuccessful()){

                                    progressBar.setVisibility(View.GONE);//프로그래스 바를 없애거나 다시보이게 하는기능
                                    Intent intent = new Intent(MainActivity.this, MainChatActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);//프로그래스 바를 없애거나 다시보이게 하는기능
                                    Toast.makeText(MainActivity.this, "Authentication Failed! Please Enter valid Email & Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {//register의 이벤트 리스너를 등록함
            @Override
            public void onClick(View v) {
                register.setBackgroundColor(getResources().getColor(R.color.colorButton));
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {//forget_password의 이벤트 리스너를 등록함
            @Override
            public void onClick(View v) {
                forget_password.setTextColor(getResources().getColor(R.color.colorBlue));
                startActivity(new Intent(MainActivity.this, ForgetActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {//생명주기를 관리함, 유저와 상호작용이 가능하고 onstart일때,화면이 노출되나 상호작용이 불가능
        super.onResume();
        forget_password.setTextColor(getResources().getColor(R.color.colorGrey));
        register.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }

}
