package com.app_services.WooNam.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_services.WooNam.chattingapp.MessageActivity;
import com.app_services.WooNam.chattingapp.R;
import com.app_services.WooNam.chattingapp.UserModel.Chat;
import com.app_services.WooNam.chattingapp.UserModel.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context; //선택한 activity에 대한 context를 가져올 때 사용
    private List<User> userList;
    private boolean isInChat;
    private String lastMessage;

    public UserAdapter(Context context, List<User> userList, Boolean isInChat){ //user리스트를 출력하는 fragment에대한 값주고받고 설정해주는게 Useradapter
        this.context = context;
        this.userList = userList;
        this.isInChat = isInChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { // listview가 adapter에 연결 된 다음에 이쪽에서 최초로 view holder를 연결해줌
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {//각 item(이름,프로필,접속여부)에 대한 매칭을 시켜줌
        final User user = userList.get(i);
        viewHolder.tv_username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            viewHolder.profile_pic.setImageResource(R.mipmap.ic_default_profile_pic); //프로필사진 설정하지 않았으면 default 프로필사진을 viewholder에 넣음
        } else {
            Glide.with(context).load(user.getImageURL()).into(viewHolder.profile_pic);//설정한 프로필사진 viewholder에 넣기
        }

        Log.i("USER LIST : ", "onBindViewHolder: USER DETAILS = " + user.toString());

        if (isInChat){
            checkLastMessage(user.getId(), viewHolder.tv_user_about_or_last_message); // Inchat이 true면 viewholder에서 마지막 메세지 가져옴
        } else {
            viewHolder.tv_user_about_or_last_message.setText(user.getUser_about()); //마지막 메세지를 viewholder에 넣어줌
        }
        if (isInChat){
            Log.i("USER STATUS", "onBindViewHolder: status = " + user.getStatus());

            switch (user.getStatus()) {
                case "online":
                    viewHolder.status.setImageResource(R.drawable.shape_bubble_online); //online이면 초록불
                    break;
                case "offline":
                    viewHolder.status.setImageResource(R.drawable.shape_bubble_offline); //offline이면 회색불
                    break;

            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);//사용자리스트를 클릭하면 해당 사용자와 1대1채팅하는 화면으로 이동하기 때문에 해당정보를 MessageActivity로 이동시킴
                intent.putExtra("userId", user.getId());
                context.startActivity(intent); //이동
            }
        });
        viewHolder.tv_user_about_or_last_message.setText(user.getUser_about());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_user_about_or_last_message;
        ImageView profile_pic;
        CircleImageView status;

        ViewHolder(View itemView){ //다음과 같은 view를 묶음
            super(itemView);
            tv_username = itemView.findViewById(R.id.username);
            tv_user_about_or_last_message = itemView.findViewById(R.id.tv_user_about_or_last_message);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            status = itemView.findViewById(R.id.status_icon);
        }
    }

    private void checkLastMessage(final String userId, final TextView tv_user_about_or_last_message){ //채팅목록에 마지막으로 작성된 메세지를 띄움
        lastMessage = "";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats"); //chat정보가 있는곳으로 데이터베이스 경로설정
        assert firebaseUser != null;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //메세지 주고받으면 마지막 메세지가 바뀌게 되고 그걸 다시 설정해주는 것
                try{

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        assert chat != null;
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                                chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())) {
                            lastMessage = chat.getMessage();
                        }
                    }


                    switch (lastMessage){
                        case "":
                            tv_user_about_or_last_message.setText(""); //마지막 메세지가 비어있으면 빈칸 출력
                            break;

                        default:
                            tv_user_about_or_last_message.setText(lastMessage); //마지막 메세지가 있으면 그거 출력
                            break;
                    }
                    lastMessage = "";
                } catch (NullPointerException e){
                    Log.i("NullPointerException", "onDataChange: " + e.getMessage()); //에러메세지 처리
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
