package com.app_services.WooNam.chattingapp.Crawl.site;

import android.util.Log;

import com.app_services.WooNam.chattingapp.Crawl.ResultData;
import com.app_services.WooNam.chattingapp.Crawl.SSLConnect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class Cb {
    public static final String NOTICE = "https://www.chungbuk.ac.kr/site/www/boardList.do?boardSeq=112&key=698"; //크롤링 할 사이트
    public static final String FAVICON = "https://www.chungbuk.ac.kr/favicon.ico"; //크롤링 아이콘
    public static final String PAGE = "&page="; //크롤링 번호 쪽 페이지

    public static ArrayList<ResultData> getData(String address, String keyword, int page) {
        ArrayList<ResultData> ret = new ArrayList<>();
        try {
            SSLConnect ssl = new SSLConnect();
            ssl.postHttps(address+PAGE+page, 1000, 1000);//http 인증서 연결
            Document document = Jsoup.connect(address + PAGE + page).get(); //jsoup연결
            Elements elements = document.select("tbody[class=tb]").select("tr");
            for(Element e : elements) {
                String notice = e.select("em").text();//중복 공지사항 부분 제외
                if(!notice.isEmpty()) {
                    continue;
                }
                Elements temp = e.select("td[class=subject]");
                Log.e("test", temp.html());//log 표시
                String title = temp.select("a").text(); //페이지 내에서 크롤링 할 부분: title
                String date = e.select("tr").select("td").last().text(); //페이지 내에서 크롤링 할 부분: date
                String link = temp.select("a").attr("href"); //페이지 내에서 크롤링 할 부분:link
                link = link.substring(1,link.length());//검색 결과 게시물 link편집
                link = "https://www.chungbuk.ac.kr/site/www/"+link; //검색 결과 게시물 link

                String imageUrl = FAVICON; //아이콘

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

