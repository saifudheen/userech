package com.gofreshuser.GoogleMapWork;

import java.util.ArrayList;

import okhttp3.Route;
import okhttp3.internal.connection.RouteException;

public interface RoutingListener {
    void onRoutingFailure(RouteException var1);

    void onRoutingStart();

    void onRoutingSuccess(ArrayList<Route> var1, int var2);

    void onRoutingCancelled();
}


