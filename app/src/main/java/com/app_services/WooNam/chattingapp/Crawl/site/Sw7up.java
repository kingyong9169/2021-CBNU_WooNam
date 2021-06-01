package com.app_services.WooNam.chattingapp.Crawl.site;

import android.util.Log;

import com.app_services.WooNam.chattingapp.Crawl.ResultData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Sw7up {
    public static final String NOTICE = "https://sw7up.cbnu.ac.kr/community/notice";//크롤링 할 사이트 url
    public static final String FAVICON = "https://sw7up.cbnu.ac.kr/../../../../assets/images/logo-h.png";//크롤링 아이콘
    public static final String PAGE = "?page=";//크롤링 쪽 페이지

    public static ArrayList<ResultData> getData(String address, String keyword, int page) {
        ArrayList<ResultData> ret = new ArrayList<>();
        try {

            Document document = Jsoup.connect(address + PAGE + page).get(); //jsoup연결
            Elements elements = document.select("div[class=content pt-lg-4]").select("div[class=card card-frame py-3 px-3 px-sm-4]");
            for(Element e : elements) {
                String title = e.select("span[class=mb-0]").text();//한 페이지에서 크롤링 할 부분: title
                String date = e.select("small").last().text();//한 페이지에서 크롤링 할 부분: date
                String link = e.select("a[class=act]").attr("href");//한 페이지에서 크롤링 할 부분: link
                link = "https://sw7up.cbnu.ac.kr/community/notice"+ PAGE + page;//검색 결과 게시물 link
                Log.e("test", e.html());//log표시

                String imageUrl = FAVICON;//아이콘

                if(!title.contains(keyword)) {
                    continue;
                }
                Log.e("getData", "title: " + title + "\nLink: " + link+"\nDESC: "+ date);//log 표시
                ret.add(new ResultData(imageUrl, title, link, date));//검색 결과
            }
        }
        catch(Exception e) {
            Log.e("test", e.toString());//log 표시
            e.printStackTrace();//출력
        }
        return ret;
    }
}