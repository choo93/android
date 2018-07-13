package org.kh.mypractice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class WebAppInterface {
    private Context mContext;
    private WebView mWebView;

    private AudioManager audio;

    private TextToSpeech tts;

    private Intent i;
    private SpeechRecognizer mRecognizer;

    private Thread runn;

    Handler handler = new Handler();

    private int sttVal = 0;

    PaPaGo papago;


    public WebAppInterface(Context c, WebView w){
        this.mContext = c;
        this.mWebView = w;

        audio = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);

        tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);

        mRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float v) {}

            @Override
            public void onBufferReceived(byte[] bytes) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int i) {}

            @Override
            public void onResults(Bundle results) {
                String key = "";
                key = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList<String> mResult = results.getStringArrayList(key);
                String[] rs = new String[mResult.size()];
                mResult.toArray(rs);
                // rs[0]에 실제 음성 인식된 데이터가 담겨 있음


                if(sttVal==0){
                    Toast.makeText(mContext,rs[0],Toast.LENGTH_SHORT).show();
                }else if(sttVal==1){
                    tts.speak(rs[0], TextToSpeech.QUEUE_FLUSH,null);
                }else if(sttVal==2){
                    tts.speak(papago.testPaPaGo(rs[0]), TextToSpeech.QUEUE_FLUSH,null);
                }


            }

            @Override
            public void onPartialResults(Bundle bundle) {}

            @Override
            public void onEvent(int i, Bundle bundle) {}
        });

        runn = new Thread(){
            @Override
            public void run(){
                mRecognizer.startListening(i);
            }

        };

        new Thread() {
            public void run() {
                papago = new PaPaGo();
            }
        }.start();

    }

    // TTS를 메소드에서 선언하는거는 왜 됐다 안됐다 할까?
    //public void getTTS(){}

    @JavascriptInterface
    public void func1(){
        Toast.makeText(mContext, "버튼이 클릭되었습니다.", Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void func2(String data){
        Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void vibe(){
        Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }

    @JavascriptInterface
    public void date(){
        long now = System.currentTimeMillis();
        Date d = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 kk시 mm분 ss초");
        String getTime = sdf.format(d);
        Toast.makeText(mContext,getTime,Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void volUp(){
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC)+1),
                AudioManager.FLAG_PLAY_SOUND);
    }

    @JavascriptInterface
    public void volDown(){
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC)-1),
                AudioManager.FLAG_PLAY_SOUND);
    }

    @JavascriptInterface
    public void call(String phoneNum){
        String tel = "tel:" + phoneNum;

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));

        ContextCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE);

        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void textToSpeach(String text){
        //getTTS();

        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @JavascriptInterface
    public  void sttToast(){
        sttVal=0;
        handler.post(runn);
    }

    @JavascriptInterface
    public void sttVoice(){
        sttVal=1;
        handler.post(runn);
    }

    @JavascriptInterface
    public void transl(){
        sttVal=2;
        handler.post(runn);
    }

}
