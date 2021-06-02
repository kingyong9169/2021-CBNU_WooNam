package com.app_services.WooNam.chattingapp.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_services.WooNam.chattingapp.R;
import com.app_services.WooNam.chattingapp.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView; //제목
    public TextView authorView; //작성자
    public ImageView starView; //좋아요
    public TextView numStarsView; // 좋아요 개수
    public TextView bodyView; //게시글 본문

    public PostViewHolder(View itemView) {  //필요한 item 연결
        super(itemView);

        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);
        bodyView = itemView.findViewById(R.id.postBody);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) { //viewholder에 필요한 정보들 넣어줌
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
    }
}
