package com.app_services.WooNam.chattingapp.Crawl.site;

import android.util.Log;

import com.app_services.WooNam.chattingapp.Crawl.ResultData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Detizen {
    public static final String CONTEST = "http://www.detizen.com/contest/";//크롤링 할 사이트 url
    public static final String ACTIVITY = "http://www.detizen.com/activity/";//크롤링 할 사이트 url
    public static final String FAVICON = "http://www.detizen.com/images/common/img_logo(basic)_2020.png";//크롤링 아이콘
    public static final String PAGE = "?PC=";//크롤링 쪽 페이지

    public static ArrayList<ResultData> getData(String address, String keyword, int page) {
        ArrayList<ResultData> ret = new ArrayList<>();
        try {
            Document document = Jsoup.connect(address + PAGE + page).get(); //jsoup연결
            Elements elements = document.select("ul[class=basic-list page-list]").select("li");
            for(Element e : elements) {
                String title = e.select("a").text();//한 페이지에서 크롤링 할 부분: title
                String date = e.select("p[class=text-category clearfix]").text();//한 페이지에서 크롤링 할 부분: date
                String link = e.select("h4").select("a").attr("href");//한 페이지에서 크롤링 할 부분: link
                link = link.split("\\?")[1];//검색 결과 게시물 link 편집
                link = address.concat("?" + link);//검색 결과 게시물 link

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
