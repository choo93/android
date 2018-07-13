package org.kh.a02_hellopapago;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;   // 웹 뷰 레퍼런스
    private WebSettings mWebViewSetting;    // 웹 뷰 세팅 레퍼런스

    PaPaGo papago;

    private static Context cc;
    private static String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

              cc = this;


        new Thread() {
            public void run() {
                papago = new PaPaGo();
            }
        }.start();




/*
        mWebView = (WebView)findViewById(R.id.webView); // 레이어에서 웹뷰를 찾아 리턴
        mWebView.setWebViewClient(new WebViewClient());
        // 웹 뷰안에서 링크가 있을 경우 클릭시에 별도의 웹 브라우저를 작동 시키게 됨
        // 이때 별도의 웹 브라우저가 아니라 웹뷰를 가지고 이동하기 위해서는
        // 별도의 웹뷰 클라이언트 객체를 등록해두면 됨 (그 객체가 WebViewClient())
        // mWebView.setWebChromeClient(new WebChromeClient()); 요런것도 있음


        mWebViewSetting = mWebView.getSettings();   // 세부 세팅값을 가지고 있는 객체를 리턴

        mWebViewSetting.setJavaScriptEnabled(true);
        // 웹뷰안에서 자바스크립트가 동작시 실행 되지 않기 때문에
        // setJavaScriptEnabled 메소드를 통해 자바스크리트를 허용해줌

        mWebView.addJavascriptInterface(new WebAppInterface(this,mWebView),"Android");

        mWebView.loadUrl("http://192.168.10.7/m_index.html");
        //loadUrl 메소드는 url을 통해 해당 페이지를 호출함

    }

    // 웹뷰에서 뒤로가기 버튼 클릭시 바로 꺼지지 않도록 설정

    // onKeyDown 이라는 메소드를 통하여 기존 뒤로가기 버튼의 이벤트를 새롭게 정의할 것임
    // 단, 이 애플리케이션에서 정의한 것이기 때문에 우리가 만든 애플리케이션에서만 해당 됨

    private int pressedCount = 1;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            pressedCount = 1;
            mWebView.goBack();
            return true;
        }else{  // 그 외(뒤로가기 이벤트가 발생하였으나 뒤로갈 페이지가 없을 때 포함
            if(pressedCount==0){
                return super.onKeyDown(keyCode,event);  // 기존 이벤트(부모)대로 작동(앱 종료)
            }else{
                pressedCount--;
                Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 종료 됩니다.",Toast.LENGTH_LONG).show();
                return true;
            }


        }

    }





        public void onClick(View view){
        switch (view.getId()){
            case R.id.refreshBtn :
                mWebView.reload();
                Toast.makeText(this,"새로고침 하였습니다.",Toast.LENGTH_LONG).show();
                break;
        }
    }*/

    }

        public void translate(View view){
            switch (view.getId()) {

                case R.id.trBtn:
                    String papagoText = ((EditText) findViewById(R.id.trtext)).getText().toString();
                    //papago.testPaPaGo(papagoText);

                    Toast.makeText(this, papago.testPaPaGo(papagoText), Toast.LENGTH_LONG).show();

                    break;


            }
        }


        /*public void translate(View view){
        //String text = ((EditText)findViewById(R.id.trtext)).getText().toString();
        //System.out.println(text);

        PaPaGo ppg = new PaPaGo();


        new Thread(){
            public void run(){
                String text = ((EditText)findViewById(R.id.trtext)).getText().toString();
                result = new PaPaGo().testPaPaGo(text);

                System.out.println("번역한 문장 : "+ result);
                //Toast.makeText(cc,result,Toast.LENGTH_LONG).show();
            }
        }.start();

        System.out.println("오오오오오옹");
        System.out.println("번역한 문장2 : "+ result);
        Toast.makeText(this,result,Toast.LENGTH_LONG).show();

    }*/


}