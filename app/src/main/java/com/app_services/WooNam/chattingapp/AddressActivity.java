package com.app_services.WooNam.chattingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_services.WooNam.chattingapp.Crawl.AddressAdapter;
import com.app_services.WooNam.chattingapp.Crawl.AddressData;
import com.app_services.WooNam.chattingapp.Crawl.AddressDataListWrapper;
import com.app_services.WooNam.chattingapp.Crawl.Crawler;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {
    private String keywordText;
    private String descText;
    public AddressDataListWrapper addresses;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView_address);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        addresses = new AddressDataListWrapper();
        addresses.addUpdateAddressData();
        String keywordText = getIntent().getStringExtra("keywordText");
        // 테스트
        ArrayList<AddressData> a = addresses.getAddressDataList();
        for(AddressData d : a) {
            Log.e("test",d.getTitle());
        }
        Log.e("test", keywordText);

        addresses.initAdapter();
        final AddressAdapter adapter = addresses.getAddressAdapter();
        recyclerView.setAdapter(adapter);
        intent = new Intent(this, ResultActivity.class);
        Context context = this;
        adapter.setOnItemListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                AddressData data = adapter.getItem(pos);
                // 크롤링 하고 result fragment로 이동
                Crawler crawler = new Crawler(AddressActivity.this);
                crawler.execute(data.getAddress(), keywordText, "1", "3");
            }
        });

        addresses.getAddressAdapter().notifyDataSetChanged();
    }

}