package com.gofreshuser.util;

import android.webkit.JavascriptInterface;

public class PaymentInterface {
    @JavascriptInterface
    public void success(String data){
    }

    @JavascriptInterface
    public void error(String data){
    }
}
