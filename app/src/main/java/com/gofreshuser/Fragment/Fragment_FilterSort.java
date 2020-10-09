package com.gofreshuser.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gofreshuser.tecmanic.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_FilterSort extends Fragment {
    RadioGroup radioGroup;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RadioButton lowest_item_price,highest_item_price,atoz,ztoa;
    String value;
    public Fragment_FilterSort() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment__filter_sort, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        lowest_item_price=view.findViewById(R.id.lowest_item_price);
        highest_item_price=view.findViewById(R.id.highest_item_price);
        atoz=view.findViewById(R.id.atoz);
        ztoa=view.findViewById(R.id.ztoa);

preferences=getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);

      editor=preferences.edit();
        lowest_item_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fm = new Fragment_Deal();

                editor.putString("value", "0");
                editor.putString("sort","Lowest Item Price");
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();


            }
        });

        highest_item_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Fragment_Deal();
                editor.putString("value", "1");
                editor.putString("sort","Highest Item Price");
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }
        });
        atoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Fragment_Deal();
                editor.putString("value", "2");
                editor.putString("sort","A - Z");
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }
        });
        ztoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Fragment_Deal();
                editor.putString("value", "3");
                editor.putString("sort","Z - A");
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }
        });


        return view;
    }

}
