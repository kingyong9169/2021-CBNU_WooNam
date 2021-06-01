package com.app_services.WooNam.chattingapp.Crawl.site;

import android.util.Log;

import com.app_services.WooNam.chattingapp.Crawl.ResultData;
import com.app_services.WooNam.chattingapp.Crawl.SSLConnect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Allcon {
    public static final String CONTEST = "https://www.all-con.co.kr/page/uni_contest.php";//크롤링 할 사이트 url
    public static final String ACTIVITY = "https://www.all-con.co.kr/page/uni_activity.php"; //크롤링 할 사이트 url
    public static final String FAVICON = "https://www.all-con.co.kr/img/logo.new.png"; //크롤링 할 사이트 아이콘
    public static final String PAGE = "?&sc=0&st=0&sstt=&page="; //크롤링 쪽 페이지

    public static ArrayList<ResultData> getData(String address, String keyword, int page) {
        ArrayList<ResultData> ret = new ArrayList<>();
        try {
            SSLConnect ssl = new SSLConnect();
            ssl.postHttps(address+PAGE+page, 1000, 1000); //http 인증서 연결
            Document document = Jsoup.connect(address + PAGE + page).get();//jsoup연결
            Elements elements = document.select("div[class=body_wrap]").select("table").select("tbody").select("tr");
            for(Element e : elements) {
                Elements temp = e.select("td[class=name]");
                if(temp.select("span[class=special]").html().length() > 0 ) {
                    String t = temp.select("span").html();
                    Log.e("test",t);
                    continue;
                }
                String title = temp.select("a").select("p").text();//한 페이지에서 크롤링 할 부분: title
                Log.e("test", title);//log표시
                String date = temp.select("p[class=info]").select("span").text();//한 페이지에서 크롤링 할 부분: date
                String link = temp.select("a").attr("href");//한 페이지에서 크롤링 할 부분: link
                link = link.substring(1, link.length());//검색 결과 게시물 link 편집
                link = "https://www.all-con.co.kr/page/" + link; //검색 결과 게시물 link

                String imageUrl = FAVICON; //크롤링 아이콘

                if (!title.contains(keyword)) {
                    continue;
                }
                ret.add(new ResultData(imageUrl, title, link, date)); //검색 결과

            }
        }
        catch(Exception e) {
            Log.e("test", e.toString());//log 표시
            e.printStackTrace();//출력
        }
        return ret;
    }
}