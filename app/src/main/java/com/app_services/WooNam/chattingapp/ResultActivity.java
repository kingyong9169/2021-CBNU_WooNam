package com.app_services.WooNam.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_services.WooNam.chattingapp.Crawl.Crawler;
import com.app_services.WooNam.chattingapp.Crawl.ResultAdapter;
import com.app_services.WooNam.chattingapp.Crawl.ResultData;
import com.app_services.WooNam.chattingapp.Crawl.ResultDataListWrapper;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ResultDataListWrapper results;
    private String address;
    private String keyword;
    private String startPage;
    private String lastPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Inflate the layout for this fragment
        final TextView guideTextView = findViewById(R.id.textView_guide);
        final Button crawlButton = findViewById(R.id.button_crawl);
        final EditText editTextStartPage = findViewById(R.id.editText_startPage);
        final EditText editTextLastPage = findViewById(R.id.editText_lastPage);
        results = new ResultDataListWrapper();

        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        address = intent.getStringExtra("address");
        startPage = intent.getStringExtra("startPage");
        lastPage = intent.getStringExtra("lastPage");

        ArrayList<ResultData> list = intent.getParcelableArrayListExtra("results");
        results.setResultDataList(intent.getParcelableArrayListExtra("results"));

        if(list.size() != 0){
            guideTextView.setText(startPage + "페이지부터 " + lastPage + " 페이지까지의 결과: ");
        } else {
            guideTextView.setText("결과가 없습니다");
        }//search result
        editTextStartPage.setText(startPage);
        editTextLastPage.setText(lastPage);
        editTextLastPage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        crawlButton.performClick();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        crawlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crawler crawler = new Crawler(ResultActivity.this);
                crawler.execute(address, keyword, editTextStartPage.getText().toString(), editTextLastPage.getText().toString());
            }
        });//버튼 클릭 후 결과 도출
        RecyclerView recyclerView = findViewById(R.id.recyclerView_result);
        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        results.initResultAdapter();
        final ResultAdapter resultAdapter = results.getResultAdapter();
        recyclerView.setAdapter(resultAdapter);

        Intent resultIntent = new Intent(this, WebViewActivity.class);
        resultAdapter.setOnItemListener(new ResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ResultData data = resultAdapter.getItem(pos);
                resultIntent.putExtra("URL", data.getAddress());
                startActivity(resultIntent);
                // 웹뷰로이동
            }
        });


    }

}