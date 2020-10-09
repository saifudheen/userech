package com.gofreshuser.tecmanic;

import android.content.Context;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.util.CustomVolleyJsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TrackMap extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    //    ImageView rider_call;
    TextView rider_name;
    private String TAG = "so47492459";
    LocationManager locationManager;
    public static  Double userlat,userlng;
    Button getlive,show;
    String lat,lng;
    Marker marker;
    LatLng barcelona;
    public  static Double riderlat,riderlng;
    //    String url="https://thecodecafe.in/gogrocer/index.php/api/set_location";
    String Call, name,
            user_id;

    Handler handler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_map);

//        rider_call= findViewById(R.id.rider_call);
        rider_name= findViewById(R.id.rider_name);
        handler= new Handler();
        user_id=getIntent().getStringExtra("user_id");
        name=  getIntent().getStringExtra("rider_name");
        userlat= Double.valueOf(getIntent().getStringExtra("user_lat"));
        userlng= Double.valueOf(getIntent().getStringExtra("user_lng"));
        Call = getIntent().getStringExtra("rider_phone");
        riderlat= Double.valueOf(getIntent().getStringExtra("rider_lat"));
        riderlng = Double.valueOf(getIntent().getStringExtra("rider_lng"));

        Log.d("sadf",String.valueOf(userlat+userlng));

        rider_name.setText(name);
//        Log.d("adf", String.valueOf(lag+ "  "+loog));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()

                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new TrackMap());


//        LatLng barcelona = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
//        mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker of rider"));
        final Runnable mStatusChecker = new Runnable() {
            @Override
            public void run() {
                try {

                    getdata(user_id);

                    if (String.valueOf(riderlat).contains("N/A")||String.valueOf(riderlng).contains("N/A")){

                    }
                    else {
                        riderlat=Double.valueOf(lat);
                        riderlng=Double.valueOf(lng);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(new TrackMap());

                    }

//
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                handler.postDelayed(this, 19000);


            }
//                            handler.postDelayed(mStatusChecker, 3000);

        };
        mStatusChecker.run();



//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(new MapsActivity());



//        if (riderlat==null&&riderlng==null) {
//            riderlat = 28.54645664;
//            riderlng = 77.52525252;
//        }
//        getdata(user_id);

//        getlive=findViewById(R.id.getloction);
//        show=findViewById(R.id.showonmap);
//        getlive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLocation();
//
//            }
//        });

//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//
//            }
//        });



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;

        mMap.clear();
        Log.d("ds",riderlng+" "+riderlat);

        barcelona = new LatLng(Double.valueOf(riderlat),Double.valueOf(riderlng));

        marker=mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker of rider"));

//     Marker carMarker = googleMap.addMarker(new MarkerOptions().position(barcelona).
//                flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));
//        carMarker.setAnchor(0.5f, 0.5f);

//        googleMap.moveCamera(CameraUpdateFactory
//                .newCameraPosition
//                        (new CameraPosition.Builder()
//                                .target(barcelona)
//                                .zoom(16.5f)
//                                .build()));


        LatLng madrid = new LatLng(userlat,userlng);
        mMap.addMarker(new MarkerOptions().position(madrid).title("Your Current Loction"));

        LatLng zaragoza = new LatLng(Double.valueOf(riderlat),Double.valueOf(riderlng));

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(String.valueOf(R.string.google_maps_key))
                .build();
        String lanog = riderlat+","+riderlng;
        String userlt = userlat+","+userlng;

        DirectionsApiRequest req = DirectionsApi.getDirections(context, lanog, userlt);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {

                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }



            }
        } catch(Exception ex) {
            String err = (ex.getMessage()==null)?"SD Card failed":ex.getMessage();
            Log.e("sdcard-err2:",err);
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(10);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza,8.0f ));
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

//    private  void setapi(){
//
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    JSONObject jsonObject = response.getJSONObject("data");
//
//                    riderlat= jsonObject.getString("lat");
//                    riderlng = jsonObject.getString("lng");
//
//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(new TrackMap());
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//
//
//        queue.add(jsonObjectRequest);
//
//    }


    private void getdata(String  userid){

        String tag_json_obj = "json_socity_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                Baseurl.GET_ORDER_URL, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i =0;i<response.length();i++){

                    try {

                        if (response != null && response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(i);

                              lat= jsonObject.getString("delivery_boy_lat");
                           lng= jsonObject.getString("delivery_boy_lng");


                        }

                        else {
                            Toast.makeText(TrackMap.this, "No data", Toast.LENGTH_SHORT).show();
                        }






                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
