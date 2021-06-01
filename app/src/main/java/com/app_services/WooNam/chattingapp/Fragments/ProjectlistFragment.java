package com.app_services.WooNam.chattingapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.app_services.WooNam.chattingapp.AddressActivity;
import com.app_services.WooNam.chattingapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectlistFragment extends Fragment {
    private int imageId;
    private String title;
    private String address;
    private String keywordText;
    private String descText;

    public ProjectlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_projectlist, container, false);
        final EditText keywordEditText = view.findViewById(R.id.editText_keyword);
        final Button addressButton = view.findViewById(R.id.button_address);


        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("keywordText", keywordEditText.getText().toString());
                // address fragment로 이동
                Context context = getContext();
                Intent intent = new Intent(context, AddressActivity.class);

                String keyword = keywordEditText.getText().toString();
                if(!keyword.equals("")) {
                    intent.putExtra("keywordText", keywordEditText.getText().toString());
                    context.startActivity(intent);
                }
                else {
                    // Toast 띄우기
                }

            }
        });


        return view;

    }

}