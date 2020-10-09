package com.gofreshuser.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;


public class Empty_cart_fragment extends Fragment {

    private static String TAG = Empty_cart_fragment.class.getSimpleName();

    RelativeLayout Shop_now;


    public Empty_cart_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty_cart, container, false);
        ((MainActivity) getActivity()).setTitle("Cart");
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()

        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fm = new FragmentHome();
                    Bundle args = new Bundle();
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                            .addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });

        Shop_now = (RelativeLayout) view.findViewById(R.id.btn_shopnow);
        Shop_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new FragmentHome();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }


}

