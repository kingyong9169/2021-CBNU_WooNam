package com.app_services.WooNam.chattingapp.Crawl.site;

import android.util.Log;

import com.app_services.WooNam.chattingapp.Crawl.ResultData;
import com.app_services.WooNam.chattingapp.Crawl.SSLConnect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Compuz {
    public static final String CONTEST = "https://www.campuz.net/index.php?mid=contest&category=120501"; //크롤링 할 페이지 url
    public static final String FAVICON = "https://www.campuz.net/files/attach/images/246086/294ebf732bdb14223de9a7b7a9b4fbca.png"; //크롤링 할 페이지 아이콘
    public static final String PAGE = "&page="; //크롤링 번호 페이지

    public static ArrayList<ResultData> getData(String address, String keyword, int page) {
        ArrayList<ResultData> ret = new ArrayList<>();
        try {
            SSLConnect ssl = new SSLConnect();
            ssl.postHttps(address+PAGE+page, 1000, 1000); //http 인증서 연결
            Document document = Jsoup.connect(address + PAGE + page).get(); //jsoup연결
            Elements elements = document.select("ol[class=info_icon2 bd_lst bd_zine zine zine1 img_load2]").select("li[class=clear]");

            for(Element e : elements) {
                String title = e.select("div[class=rt_area is_tmb]").select("h3[class=ngeb]").text();//한 페이지에서 크롤링 할 부분: title
                String date = e.select("p[div=info]").select("span").text();//한 페이지에서 크롤링 할 부분: date
                String link = e.select("a[class=hx]").attr("href");//한 페이지에서 크롤링 할 부분: link
                String imageUrl = FAVICON;//아이콘

                if(!title.contains(keyword)) {
                    continue;
                }
                Log.e("getData", "title: " + title + "\nLink: " + link+"\nDESC: "+ date);//log 표시
                ret.add(new ResultData(imageUrl, title, link, date));//검색 결과
            }
        }
        catch(Exception e) {
            Log.e("test", e.toString());
            e.printStackTrace();//출력
        }
        return ret;
    }
}