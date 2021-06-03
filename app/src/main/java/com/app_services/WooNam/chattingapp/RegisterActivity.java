package com.app_services.WooNam.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {//액티비티를 상속해줄 RegisterActivity를 생성
    ProgressBar progressBar;

    EditText fName, username, email, password, confirmPassword,school,git,aword,favorite;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//홈 버튼으로 나갔다 들어왓을 경우 앱을 죽지 않게 처리를 할 수 있도록 도와줌
        super.onCreate(savedInstanceState);//오버라이드된 메소드를처리한다 .
        setContentView(R.layout.activity_register);//profile을 보여줌

        Toolbar toolbar = findViewById(R.id.toolbar);//main.xml에서 사용하는 Toolbar를 변경할수 있는 메소드를 지원
        setSupportActionBar(toolbar);//toolbar의 액션바 설정
        getSupportActionBar().setTitle("Register");//tilte을 보여줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기 버튼

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//뒤로가기 버튼 누를시 toolbar에 표현
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
//oncreat에서 사용되는것들 정의
        progressBar = findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
        fName = findViewById(R.id.fName);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        btn_register = findViewById(R.id.btn_register);
        school= findViewById(R.id.school);
        git= findViewById(R.id.git);
        aword= findViewById(R.id.aword);
        favorite= findViewById(R.id.favorite);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_fName = fName.getText().toString();
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_conPassword=confirmPassword.getText().toString();
                String txt_school = school.getText().toString();
                String txt_git = git.getText().toString();
                String txt_aword = aword.getText().toString();
                String txt_favorite = favorite.getText().toString();


                if (TextUtils.isEmpty(txt_fName) || TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_conPassword)) {
                //회원가입에 필요한정보들이 모두 채워졌을 경우 출력
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                if(txt_password.length() < 6){//패스워드가 최소 6자리 이상 채워지지않았을 경우

                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 character long!", Toast.LENGTH_SHORT).show();
                }
                if (!txt_password.equals(txt_conPassword)) {//입력한 패스워드와 재확인 패스워드가 일치하지 않을경우

                    Toast.makeText(RegisterActivity.this, "Password not matched!, Please write it again!", Toast.LENGTH_SHORT).show();
                }
                register(txt_fName, txt_username, txt_email, txt_password,txt_school,txt_aword,txt_git,txt_favorite);//입력을 바탕으로 회원가입
            }
        });

    }

    private void register(final String fName, final String username,//register에 관련된 input para
                          final String email, final String password,
                          final String school,final String aword,
                          final String git,final String favorite){

        progressBar.setVisibility(View.VISIBLE);//progressbar 관련내용을 보이게 해줌
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override//email,password를 바탕으로 회원가입이 될수 있게함
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {//회원가입이 성공하지못했을경우
                        progressBar.setVisibility(View.GONE);//아래 toast를 보여준다.
                        Log.i("Task is Unsuccessful", "onComplete: Task: error" + task.getResult().toString());
                        Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser firebaseUser = auth.getCurrentUser();//Firebase에서 사용자관리
                        assert firebaseUser != null;//일치하지않을경우 앱이 종료될수있다.
                        String userId = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        putDataOnHash(hashMap, fName, email, userId, username,school,aword,favorite,git);//데이터베이스에 inputpara를 순서대로 넣어준다.

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {//완료되었다는 이벤트
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this,MainChatActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }
            });

    }
//database에 저장될 함수와 그내용들을 적어줌 (디폴트는 디폴트값으로 )
    private void putDataOnHash(HashMap<String, String> hashMap, String fName, String email, String userId, String username,String school,String aword,String favorite,String git) {
        String default_status = "안녕하세요 ";
        String default_user_status = "offline";
        String default_image = "default";
        hashMap.put("id", userId);
        hashMap.put("name", fName);
        hashMap.put("username", username);
        hashMap.put("school", school);
        hashMap.put("favorite", favorite);
        hashMap.put("aword", aword);
        hashMap.put("git", git);
        hashMap.put("imageURL", default_image);
        hashMap.put("status", default_user_status);
        hashMap.put("searchable_name", username.toLowerCase());
        hashMap.put("user_about", default_status);
    }

}

