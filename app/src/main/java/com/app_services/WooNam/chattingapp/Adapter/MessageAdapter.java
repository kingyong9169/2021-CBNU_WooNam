package com.app_services.WooNam.chattingapp.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_services.WooNam.chattingapp.R;
import com.app_services.WooNam.chattingapp.UserModel.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final int  MSG_TYPE_SENT = 1;
    private static final int  MSG_TYPE_RECEIVED = 0;
    private Context context; //선택한 activity에 대한 context를 가져올 때 사용
    private List<Chat> chatList; //채팅 리스트


    public MessageAdapter(Context context, List<Chat> chatList){ //MessageAdapter는 채팅에 필요한 정보들을 context를 이용해 주고받음
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {// listview가 adapter에 연결 된 다음에 이쪽에서 최초로 view holder를 연결해줌
        if (i == MSG_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sent_chat_item, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.received_chat_item, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) { //각 item(읽음표시, 보냄표시)에 대한 매칭을 시켜줌

        Chat chat = chatList.get(i);
        viewHolder.tv_message_text.setText(chat.getMessage());
        Log.i("DEBUG via COUNT", "onBindViewHolder: isSeen = " + chat.getIsSeen() );

        if (i <= chatList.size()-1){
            if (chat.getIsSeen()){
                viewHolder.iv_seen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_seen_icon)); //채팅 읽었으면 읽음 표시
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    viewHolder.iv_seen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_seen_double_tick, context.getApplicationContext().getTheme()));
//                } else {
//                    viewHolder.iv_seen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_seen_double_tick));
//                }


            } else {
                viewHolder.iv_seen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delivered_icon)); //채팅 보냈으면 보냈다는 표시
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    viewHolder.iv_seen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_deliver_indicator, context.getApplicationContext().getTheme()));
//                } else {
//                    viewHolder.iv_seen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_deliver_indicator));
//                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    } //채팅리스트의 개수

    class ViewHolder extends RecyclerView.ViewHolder{ //viewholder에서는 각 요소들을 연결해줌
        TextView tv_message_text;
        ImageView iv_seen;
        TextView messageTimeStamp;

        ViewHolder(View itemView){
            super(itemView);
            tv_message_text = itemView.findViewById(R.id.messageText);
            iv_seen = itemView.findViewById(R.id.iv_seen);
            messageTimeStamp = itemView.findViewById(R.id.messageTimeStamp);
        }
    }

    @Override
    public int getItemViewType(int position) { //읽음표시, 보냄표시를 위해 값 연결
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_SENT;
        } else {
            return MSG_TYPE_RECEIVED;
        }
    }
}

