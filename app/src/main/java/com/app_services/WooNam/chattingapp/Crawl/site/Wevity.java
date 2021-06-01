package com.app_services.WooNam.chattingapp.Crawl.site;

import android.util.Log;

import com.app_services.WooNam.chattingapp.Crawl.ResultData;
import com.app_services.WooNam.chattingapp.Crawl.SSLConnect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class Wevity {
    public static final String CONTEST_Web = "https://www.wevity.com/?c=find&s=1&gub=1&cidx=20";//크롤링 할 사이트 url
    public static final String CONTEST_Game = "https://www.wevity.com/?c=find&s=1&gub=1&cidx=21";//크롤링 할 사이트 url
    public static final String FAVICON = "https://www.wevity.com/images/logo_wevity.png";//크롤링 아이콘
    public static final String PAGE = "&gp=";//크롤링 쪽 페이지
    public static ArrayList<ResultData> getData(String address, String keyword, int page) {
        ArrayList<ResultData> ret = new ArrayList<>();
        try {
            SSLConnect ssl = new SSLConnect();
            ssl.postHttps(address+PAGE+page, 1000, 1000); //http 인증서 연결
            Document document = Jsoup.connect(address + PAGE + page).get(); //jsoup연결
            Elements elements = document.select("div[class=ms-list]").select("li");
            for(Element e : elements) {
                String title = e.select("div[class=tit]").select("a").text();//한 페이지에서 크롤링 할 부분 : title
                String date = e.select("div[class=day]").text();//한 페이지에서 크롤링 할 부분 :date
                String link = e.select("div[class=tit]").select("a").attr("href");//한 페이지에서 크롤링 할 부분 :link
                link = address.concat(link);//검색 결과 게시물 link

                String imageUrl = FAVICON;
                if(!title.contains(keyword)) {
                    continue;
                }
                Log.e("getData", "title: " + title + "\nLink: " + link+"\nDESC: "+ date); //log 표시
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