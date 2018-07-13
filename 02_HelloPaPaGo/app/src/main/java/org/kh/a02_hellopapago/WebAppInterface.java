package org.kh.a02_hellopapago;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebAppInterface {
    private Context mContext;
    private WebView mWebView;

    public WebAppInterface(Context c, WebView w){
        this.mContext = c;
        this.mWebView = w;
    }

    @JavascriptInterface
    public void func1(){
        Toast.makeText(mContext, "버튼이 클릭되었습니다.", Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void func2(String data){
        Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
    }

}
