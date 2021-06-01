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
    CircleImageView profile_pic;
    TextView tv_name;
    ProgressBar progressBar;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");


        profile_pic = findViewById(R.id.profile_pic);
        tv_name = findViewById(R.id.name);

        Log.i("Checking Order", "onCreate: ");


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_page);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tv_name.setText(user.getName());
                Log.i("Checking Order", "addValueEventListener: ");
                progressBar.setVisibility(View.GONE);

                user.setStatus(Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString());
                Log.i("User Status in USER", "onDataChange: dataSnapshot :" + user.getStatus());
                if (user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_default_profile_pic);
                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Database Error", "onCancelled: Error = " + databaseError.toString());
            }
        });


        viewPageAdapter.addFragment(new ChatsFragment(), "채팅방");
        viewPageAdapter.addFragment(new UsersFragment(), "유저");
        viewPageAdapter.addFragment(new FavoriteFragment(), "관심분야");
        viewPageAdapter.addFragment(new ProjectlistFragment(), "프로젝트");
        Log.i("Checking Order", "onCreate: After View Page adapter creation");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Log.i("Checking Order", "onCreate: After View Page adapter setup");


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Log.i("LOGOUT", "onOptionsItemSelected: LOGOUT ");
                Toast.makeText(this, "Successfully Logout!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainChatActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            case R.id.board:
                startActivity(new Intent(MainChatActivity.this, BoardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            case R.id.friend:
                Toast.makeText(this, "추가 예정입니다!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    class ViewPageAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> fragTitles;

        ViewPageAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            Log.i("Checking Order", "ViewPageAdapter: ");
            this.fragments = new ArrayList<>();
            this.fragTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("Checking Order", "getItem: ");

            Log.i("PagerAdapter", "getItem: Position = " + String.valueOf(position));
            Log.i("PagerAdapter", "getItem: fragment value = "+ fragments.get(position).toString());
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String fragTitle){
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

    private void Status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        Log.i("Checking Order", "Status: ");

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Checking Order", "onResume: ");
        Status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Checking Order", "onPause: ");
        Status("offline");
    }


}
