package com.app_services.WooNam.chattingapp.Crawl;

import com.app_services.WooNam.chattingapp.Crawl.site.Allcon;
import com.app_services.WooNam.chattingapp.Crawl.site.Cb;
import com.app_services.WooNam.chattingapp.Crawl.site.Compuz;
import com.app_services.WooNam.chattingapp.Crawl.site.Detizen;
import com.app_services.WooNam.chattingapp.Crawl.site.Sw7up;
import com.app_services.WooNam.chattingapp.Crawl.site.Wevity;

import java.util.ArrayList;

//RecyclerView용 list와 adapter 래퍼 클래스
public class AddressDataListWrapper {
    // data to populate the RecyclerView with
    private ArrayList<AddressData> addressDataList;
    private AddressAdapter addressAdapter;

    // constructor
    public AddressDataListWrapper() {
        addressDataList = new ArrayList<>();
    }
    // getter and setter for list
    public ArrayList<AddressData> getAddressDataList() {
        return addressDataList;
    }
    public void setAddressDataList(ArrayList<AddressData> list) {
        addressDataList = list;
    }
    // getter and init for adapter
    public AddressAdapter getAddressAdapter() {
        return addressAdapter;
    }
    public void initAdapter() {
        addressAdapter = new AddressAdapter(addressDataList);
    }
    public void addUpdateAddressData() {
        addressDataList.add(new AddressData(0, "대티즌 공모전", Detizen.CONTEST));
        addressDataList.add(new AddressData(0, "대티즌 대외활동", Detizen.ACTIVITY));
        addressDataList.add(new AddressData(0, "올콘 공모전", Allcon.CONTEST));
        addressDataList.add(new AddressData(0, "올콘 대외활동", Allcon.ACTIVITY));
        addressDataList.add(new AddressData(0, "위비티 공모전-게임/소프트웨어", Wevity.CONTEST_Game));
        addressDataList.add(new AddressData(0, "위비티 공모전-웹/모바일/IT", Wevity.CONTEST_Web));
        addressDataList.add(new AddressData(0, "충북대SW사업단 공지사항", Sw7up.NOTICE));
        addressDataList.add(new AddressData(0, "충북대 공지사항", Cb.NOTICE));
        addressDataList.add(new AddressData(0, "캠퍼즈 공모전", Compuz.CONTEST));
    }//crawling list
}
