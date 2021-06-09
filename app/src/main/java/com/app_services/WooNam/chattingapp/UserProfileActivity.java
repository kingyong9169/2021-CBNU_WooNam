package com.app_services.WooNam.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app_services.WooNam.chattingapp.UserModel.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {////액티비티를 상속해줄 UesrProfileActivity를 생성

    TextView tv_name, tv_username, tv_about,tv_school,tv_aword,tv_git,tv_favorite;
    CircleImageView profile_pic;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//홈 버튼으로 나갔다 들어왓을 경우 앱을 죽지 않게 처리를 할 수 있도록 도와줌
        super.onCreate(savedInstanceState);//오버라이드된 메소드를처리한다 .
        setContentView(R.layout.activity_user_profile);//user_profile을 보여줌

        Toolbar toolbar = findViewById(R.id.toolbar);//main.xml에서 사용하는 Toolbar를 변경할수 있는 메소드를 지원
        setSupportActionBar(toolbar);//toolbar의 액션바 설정
        getSupportActionBar().setTitle("사용자 정보");//tilte을 보여줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기 버튼
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//뒤로가기 버튼 누를시 toolbar에 표현
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//oncreat에서 사용되는것들 정의
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tv_name = findViewById(R.id.tvName);
        tv_about = findViewById(R.id.tvAbout);
        tv_username = findViewById(R.id.tvUsername);
        tv_school= findViewById(R.id.tvSchool);
        tv_aword=findViewById(R.id.tvAword);
        tv_git=findViewById(R.id.tvGit);
        tv_favorite=findViewById(R.id.tvFavorite);


        profile_pic = findViewById(R.id.user_profile_pic);//유저의 프로필 사진

        String userId = getIntent().getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);//Firebase관리
        reference.addValueEventListener(new ValueEventListener() {//데이터를 읽고 수신 대기하기 위해서 필요
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//경로의 전체 내용을 읽고 변경사항을 수신 대기
                User user = dataSnapshot.getValue(User.class);
                assert user != null;//데이터베이스와 대조후 없는 사용자면 앱이종료됨
                tv_name.setText(user.getName());
                tv_about.setText(user.getUser_about());
                tv_username.setText(user.getUsername());
                tv_school.setText(user.getSchool());
                tv_aword.setText(user.getAword());
                tv_git.setText(user.getGit());
                tv_favorite.setText(user.getFavorite());
                if(user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_default_profile_pic);
                } else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {//DAtabase 에러발생시 필요
                Log.i("Database Error", "onCancelled: Error -> "+ databaseError.toString());
            }
        });
    }


    private void Status(){//유저의 상태를 나타냄 . 온라인일경우 초록색 작은원이 나옴
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "online");
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {//생명주기를 관리함, 유저와 상호작용이 가능하고 onstart일때,화면이 노출되나 상호작용이 불가능
        super.onResume();
        Status();
    }

}