package com.app_services.WooNam.chattingapp.Crawl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.app_services.WooNam.chattingapp.Crawl.site.Allcon;
import com.app_services.WooNam.chattingapp.Crawl.site.Cb;
import com.app_services.WooNam.chattingapp.Crawl.site.Compuz;
import com.app_services.WooNam.chattingapp.Crawl.site.Detizen;
import com.app_services.WooNam.chattingapp.Crawl.site.Sw7up;
import com.app_services.WooNam.chattingapp.Crawl.site.Wevity;
import com.app_services.WooNam.chattingapp.ResultActivity;

import java.util.ArrayList;

public class Crawler extends AsyncTask<String, Void, ArrayList<ResultData>> {

    private Context context;
    private ProgressDialog progressDialog;
    private ArrayList<ResultData> results = new ArrayList<>();
    private String address;
    private String keyword;
    private String startPage;
    private String lastPage;
    private String nav;
    private Intent intent;
    public Crawler(Context context) {
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // temprary dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시 기다려 주세요.");
        progressDialog.show();
    }

    @Override
    protected ArrayList<ResultData> doInBackground(String... strings) {
        intent = new Intent(context, ResultActivity.class);
        address = strings[0];
        keyword = strings[1];
        startPage = strings[2];
        lastPage = strings[3];
        int start = Integer.parseInt(startPage);
        int last = Integer.parseInt(lastPage);
        intent.putExtra("startPage", start);
        intent.putExtra("lastPage", last);
        switch(address) {
            case Allcon.CONTEST:
            case Allcon.ACTIVITY:
                for(int i=start; i<=last; i++) {
                    results.addAll(Allcon.getData(address, keyword, i));
                }
                break;
            case Compuz.CONTEST:
                for(int i=start; i<=last; i++) {
                    results.addAll(Compuz.getData(address, keyword, i));
                }
                break;
            case Detizen.CONTEST:
            case Detizen.ACTIVITY:
                for(int i=start; i<=last; i++) {
                    results.addAll(Detizen.getData(address, keyword, i));
                }
                break;
            case Sw7up.NOTICE:
                for(int i=start; i<=last; i++) {
                    results.addAll(Sw7up.getData(address, keyword, i));
                }
                break;
            case Cb.NOTICE:
                for(int i=start; i<=last; i++) {
                    results.addAll(Cb.getData(address, keyword, i));
                }
                break;
            case Wevity.CONTEST_Web:
            case Wevity.CONTEST_Game:
                for(int i=start; i<=last; i++) {
                    results.addAll(Wevity.getData(address, keyword, i));
                }
                break;
        }
        return results;
    }
//사이트별 리스트
    @Override
    protected void onPostExecute(ArrayList<ResultData> resultData) {
        super.onPostExecute(resultData);
        progressDialog.dismiss();
        intent.putExtra("results", results);
        intent.putExtra("keyword", keyword);
        intent.putExtra("address", address);
        intent.putExtra("startPage", startPage);
        intent.putExtra("lastPage", lastPage);
        context.startActivity(intent);


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(ArrayList<ResultData> resultData) {
        super.onCancelled(resultData);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
