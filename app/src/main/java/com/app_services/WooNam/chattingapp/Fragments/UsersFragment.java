package com.app_services.WooNam.chattingapp.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.app_services.WooNam.chattingapp.Adapter.UserAdapter;
import com.app_services.WooNam.chattingapp.R;
import com.app_services.WooNam.chattingapp.UserModel.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView; //검색결과를 recyclerview형식으로 출력하기 위함
    private UserAdapter userAdapter;
    private List<User> userList;

    EditText searchText; //검색창
    TextView no_content_tv; //검색결과

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_for_users); //recyclerview에서 사용할거 가져오기
        searchText = view.findViewById(R.id.search_text);
        no_content_tv = view.findViewById(R.id.no_content_tv);

        recyclerView.setHasFixedSize(true); //recyclerview 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();

        getUser(); //사용자 정보 가져오기

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUsers(s.toString().toLowerCase()); // 아래에 검색기능 정의되있음
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String characters) { //검색기능
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("searchable_name") //users 밑에있는 searchable_name 항목에 있는 내용을 검색한다는 뜻
                .startAt(characters) //첫 글자부터 일치하는 검색결과를 찾아주게됨
                .endAt(characters + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class); //유저정보 가져오기
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        userList.add(user);
                    }
                }
                if (userList.isEmpty()){
                    String str = "No User present with this name."; //검색에 해당하는 유저가 없으면 userlist출력하는 부분에 메세지 출력
                    no_content_tv.setText(str);
                    no_content_tv.setVisibility(View.VISIBLE);
                } else {
                    no_content_tv.setVisibility(View.GONE);
                }


                userAdapter = new UserAdapter(getContext(), userList, false);
                recyclerView.setAdapter(userAdapter); //목록을 recyclerview로 출력 / 필요한 정보는 userAdapter를 통해 가져옴

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUser(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users"); //user정보가 있는곳으로 데이터베이스 연결

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (searchText.getText() != null && searchText.getText().toString().equals("")) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        assert firebaseUser != null;
                        if (user.getId()!=null && !user.getId().equals(firebaseUser.getUid())) { //데이터 추가
                            userList.add(user);
                        }
                    }
                    if (userList.isEmpty()){ //비었으면
                        String str = "No User present."; //메세지 출력
                        no_content_tv.setText(str);
                        no_content_tv.setVisibility(View.VISIBLE);
                    } else {
                        no_content_tv.setVisibility(View.GONE);
                    }
                    userAdapter = new UserAdapter(getContext(), userList, false);
                    recyclerView.setAdapter(userAdapter); //recycler view로 출력 / 해당하는 정보는 UserAdapter를 통해 주고받음
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
