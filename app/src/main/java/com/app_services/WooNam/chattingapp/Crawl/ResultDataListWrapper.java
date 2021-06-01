package com.app_services.WooNam.chattingapp.Crawl;

import java.util.ArrayList;

//RecyclerView용 list와 adapter 래퍼 클래스
public class ResultDataListWrapper {
    private ArrayList<ResultData> resultDataList;
    private ResultAdapter resultAdapter;
    public ResultDataListWrapper() { resultDataList = new ArrayList<>(); }
    // getter
    public ArrayList<ResultData> getResultDataList() {
        return resultDataList;
    }
    public void setResultDataList(ArrayList<ResultData> resultDataList) {
        this.resultDataList = resultDataList;
    }
    // getter
    public ResultAdapter getResultAdapter() {
        return resultAdapter;
    }
    public void initResultAdapter() {
        this.resultAdapter = new ResultAdapter(resultDataList);
    }
}

