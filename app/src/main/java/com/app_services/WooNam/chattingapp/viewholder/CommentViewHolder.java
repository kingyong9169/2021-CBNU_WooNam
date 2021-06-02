package com.app_services.WooNam.chattingapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app_services.WooNam.chattingapp.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public TextView authorView; //작성자
    public TextView bodyView; // 게시글 본문

    public CommentViewHolder(View itemView) { //필요한 item 연결
        super(itemView);
        authorView = itemView.findViewById(R.id.commentAuthor);
        bodyView = itemView.findViewById(R.id.commentBody);
    }
}
