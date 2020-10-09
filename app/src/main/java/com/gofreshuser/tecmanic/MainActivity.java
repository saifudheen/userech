package com.gofreshuser.tecmanic;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.Fragment.About_us_fragment;
import com.gofreshuser.Fragment.Cart_fragment;
import com.gofreshuser.Fragment.FragmentHome;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public  static Toolbar toolbar;
    int padding = 0;
    NavigationView navigationView;
    private Menu nav_menu;
    public  static String userid;
    RelativeLayout login;
    LinearLayout my_cart;
    LinearLayout my_orders,my_wallet;
    TextView username;
    public  static View count;
    public static TextView totalBudgetCount,logout;
    Session_management session_management;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        session_management = new Session_management(MainActivity.this);

        userid=session_management.getUserDetails().get(Baseurl.KEY_ID);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));

        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                }
            }

            //the method we have create in activity
        }

        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        login=(RelativeLayout) header.findViewById(R.id.login);
        username=header.findViewById(R.id.name);


        username.setText(session_management.getUserDetails().get(Baseurl.KEY_NAME));
        my_orders=(LinearLayout) header.findViewById(R.id.my_orders);
        my_wallet=header.findViewById(R.id.my_wallet);
        my_cart=header.findViewById(R.id.my_cart);




        sideMenu();


        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session_management.isLoggedIn()){
                    Intent intent=new Intent(MainActivity.this,My_Order_activity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(MainActivity.this,ShowLogin.class);
                    startActivity(intent);
                }

            }
        });

        my_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session_management.isLoggedIn()){
                    Intent intent=new Intent(MainActivity.this,WalletActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(MainActivity.this,ShowLogin.class);
                    startActivity(intent);
                }

            }
        });
        my_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session_management.isLoggedIn()){
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                    toggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toggle.syncState();

                    Fragment fm = new Cart_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                            .addToBackStack(null).commit();                }
                else {
                    Intent intent=new Intent(MainActivity.this,ShowLogin.class);
                    startActivity(intent);
                }

            }
        });

        if (savedInstanceState == null) {
            Fragment fm = new FragmentHome();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

        getFragmentManager().

                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            Fragment fr = getFragmentManager().findFragmentById(R.id.main_container);

                            final String fm_name = fr.getClass().getSimpleName();
                            Log.e("backstack: ", ": " + fm_name);
                            if (fm_name.contentEquals("FragmentHome")) {

                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                toggle.setDrawerIndicatorEnabled(true);


                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                toggle.syncState();

                            } else if (fm_name.contentEquals("My_order_fragment") ||
                                    fm_name.contentEquals("Thanks_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Fragment fm = new FragmentHome();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });
                            } else {


                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        onBackPressed();
                                    }
                                });
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.nav_about_us) {
            fm = new About_us_fragment();
            args.putString("url", Baseurl.GET_ABOUT_URL);
            args.putString("title", getResources().getString(R.string.nav_about));
            fm.setArguments(args);

        }  else if (id == R.id.nav_chat) {

            String smsNumber = "+919642422288";
            Uri uri = Uri.parse("smsto:" + smsNumber);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.putExtra("Test", "grocery");
            i.setPackage("com.whatsapp");
            startActivity(i);

        } else if (id == R.id.nav_terms) {
            fm = new Terms_and_Condition_fragment();
            args.putString("url", Baseurl.GET_TERMS_URL);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);

        }
        else if (id == R.id.nav_faq) {
            fm = new FAQFragment();
            args.putString("url", Baseurl.GET_FAQ_URL);
            args.putString("title", "FAQ");
            fm.setArguments(args);

        }

        else if (id == R.id.nav_review) {
            reviewOnApp();
        } else if (id == R.id.nav_contact) {
            fm = new Contact_Us_fragment();
            args.putString("url", Baseurl.GET_SUPPORT_URL);
            args.putString("title", getResources().getString(R.string.nav_contact));
            fm.setArguments(args);

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
            session_management.logoutSession();
            finish();

        }
        if (fm != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);


        final MenuItem item = menu.findItem(R.id.action_cart);

        item.setVisible(true);

         count = item.getActionView();

        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);

         count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                menu.performIdentifierAction(item.getItemId(), 0);

            }
        });









        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) {

            if (session_management.isLoggedIn()){
                Fragment fm = new Cart_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();
            }
            else {
                Intent intent=new Intent(MainActivity.this,ShowLogin.class);
                startActivity(intent);
            }

            }
            return true;
        }
    public static void countshow() {



        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Cart_count, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    if (response != null && response.length() > 0) {


                               String total_count=response.getString("total_item");


                               if (total_count.contains("0")){
                                   totalBudgetCount.setText("0");
                               }
                               else {
                                   totalBudgetCount.setText(total_count);
                               }


                    }




                    else {


//                        Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }


    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
    public void sideMenu() {

        if (session_management.isLoggedIn()) {
            //  tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            login.setVisibility(View.GONE);

            username.setText("Welcome " +
                    ""+session_management.getUserDetails().get(Baseurl.KEY_NAME));


//            nav_menu.findItem(R.id.nav_user).setVisible(true);
        } else {

            //tv_number.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, ShowLogin.class);
                    startActivity(i);
                }
            });

            nav_menu.findItem(R.id.nav_logout).setVisible(false);

            //            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }
}