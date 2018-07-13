package org.kh.mypractice;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings mWebViewSetting;

    //private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[] {Manifest.permission.VIBRATE, Manifest.permission.CALL_PHONE,Manifest.permission.RECORD_AUDIO},1000);
        }

        /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
                // 음성인식을 위한 권한 : RECORD_AUDIO
          }
        }*/


        mWebViewSetting = mWebView.getSettings();

        mWebViewSetting.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new WebAppInterface(this,mWebView),"Android");

        mWebView.loadUrl("http://192.168.10.7/m_index.html");
    }



}