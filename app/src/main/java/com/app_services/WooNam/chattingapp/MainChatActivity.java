package com.app_services.WooNam.chattingapp;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.app_services.WooNam.chattingapp.Fragments.ProjectlistFragment;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app_services.WooNam.chattingapp.Fragments.ChatsFragment;
import com.app_services.WooNam.chattingapp.Fragments.UsersFragment;
import com.app_services.WooNam.chattingapp.UserModel.User;
import com.app_services.WooNam.chattingapp.Fragments.FavoriteFragment;
import com.app_services.WooNam.chattingapp.Fragments.PostListFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainChatActivity extends AppCompatActivity {
    CircleImageView profile_pic; //프로필사진
    TextView tv_name;           //이름
    ProgressBar progressBar;    //로딩의 정도를 나타내는 바

    FirebaseUser firebaseUser; //데이터베이스
    DatabaseReference reference; //데이터베에스에서 값 가져오기 위함
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat); //mainchatactivity는 4개의 fragment를 출력, 그에 해당하는 작업을 해주는 activity이다. 4개의 fragment 중 default값으로 출력해줄 fragment를 출력했다.

        progressBar = findViewById(R.id.progress_bar); //로딩의 정도를 나타내는 바
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar); //간단하게 이름 프로필사진을 출력
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");


        profile_pic = findViewById(R.id.profile_pic); //프로필사진
        tv_name = findViewById(R.id.name);              //이름

        Log.i("Checking Order", "onCreate: ");


        TabLayout tabLayout = findViewById(R.id.tab_layout);    //fragment 고르는 탭
        ViewPager viewPager = findViewById(R.id.view_page);     //fragment를 사용하기 위함
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager()); //fragment들의 정보를 주고받기위한 매개체


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //현재 로그인한 유저정보
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()); //Users에서 현재 로그인한 유저의 uid 가져오라는 뜻
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tv_name.setText(user.getName()); //위에서 지정한 데이터베이스 경로에서 이름가져옴
                Log.i("Checking Order", "addValueEventListener: ");
                progressBar.setVisibility(View.GONE);

                user.setStatus(Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString());
                Log.i("User Status in USER", "onDataChange: dataSnapshot :" + user.getStatus());
                if (user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_default_profile_pic); //프로필사진 설정안하면 출력하는 default 프로필사진
                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_pic); //지정한 프로필사진 출력
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Database Error", "onCancelled: Error = " + databaseError.toString()); //에러메세지 출력
            }
        });


        viewPageAdapter.addFragment(new ChatsFragment(), "채팅방"); //다음과 같이 총 4개의 fragment 존재
        viewPageAdapter.addFragment(new UsersFragment(), "유저");
        viewPageAdapter.addFragment(new FavoriteFragment(), "관심분야");
        viewPageAdapter.addFragment(new ProjectlistFragment(), "프로젝트");
        Log.i("Checking Order", "onCreate: After View Page adapter creation");

        viewPager.setAdapter(viewPageAdapter); //각 fragment에 대한 정보를 주고받게 해주는 매개체 세팅
        tabLayout.setupWithViewPager(viewPager); //탭하면 해당 페이지(fragment)출력

        Log.i("Checking Order", "onCreate: After View Page adapter setup");


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, ProfileActivity.class); //toolbar누르면 profileActivity로 이동 즉 프로필 화면으로 이동하게됨
                startActivity(intent); //이동
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //오른쪽 상단에 버튼 클릭하면 메뉴 출력
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //메뉴 안에 들어가는 list 총 3개있음
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Log.i("LOGOUT", "onOptionsItemSelected: LOGOUT ");
                Toast.makeText(this, "Successfully Logout!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainChatActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //로그아웃 누르면 로그인하는 페이지로 이동
                return true;
            case R.id.board:
                startActivity(new Intent(MainChatActivity.this, BoardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //게시판으로 이동
                return true;
            case R.id.friend:
                Toast.makeText(this, "추가 예정입니다!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    class ViewPageAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments; //fragment들
        private ArrayList<String> fragTitles; // fragment들 이름

        ViewPageAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            Log.i("Checking Order", "ViewPageAdapter: ");
            this.fragments = new ArrayList<>(); //값 할당
            this.fragTitles = new ArrayList<>();//값 할당
        }

        @Override
        public Fragment getItem(int position) { //값 가져오기
            Log.i("Checking Order", "getItem: ");

            Log.i("PagerAdapter", "getItem: Position = " + String.valueOf(position));
            Log.i("PagerAdapter", "getItem: fragment value = "+ fragments.get(position).toString());
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        } //fragment가 몇개인지

        void addFragment(Fragment fragment, String fragTitle){ // fragment가 추가되면 실행됨
            Log.i("Checking Order", "addFragment: ");

            fragments.add(fragment);
            fragTitles.add(fragTitle);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragTitles.get(position);
        }
    }

    private void Status(String status){ //접속중인지 아닌지 상태 확인
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        Log.i("Checking Order", "Status: ");

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() { //상태변경
        super.onResume();
        Log.i("Checking Order", "onResume: ");
        Status("online");
    }

    @Override
    protected void onPause() { //상태변경
        super.onPause();
        Log.i("Checking Order", "onPause: ");
        Status("offline");
    }


}
