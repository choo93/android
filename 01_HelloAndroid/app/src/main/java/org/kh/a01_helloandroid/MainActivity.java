package org.kh.a01_helloandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity {
    private AudioManager audio;
    private TextToSpeech tts;

    private Intent i;
    private SpeechRecognizer mRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private Context mainContext;

    // 스레드 선언 부분
    Thread runn;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
                // 음성인식을 위한 권한 : RECORD_AUDIO
            }
        }

        mainContext = this;

        // stt를 사용할 때에는 화면 전환은 일어나지 않지만
        // 음성 인식을 위한 별도의 장치가 동작해야 하기 때문에 정보를 전달하는
        // intent가 꼭 필요함 -> 전역 변수로 선언

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);


        // 마쉬멜로 버전 이후부터 Manifest 권한 설정 이후에 사용자에게
        // 권한 사용을 묻는 설정이 추가로 있어야 함

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //requestPermissions(new String[] {Manifest.permission.CALL_PHONE},1000);
            // 전화를 사용하기 위한 권한
            // requestCode 값은 임의로 사용하는 값

            //requestPermissions(new String[] {Manifest.permission.SEND_SMS},1000);
            // 문자를 전송하기 위한 권한 사용을 사용자에게 물어봄
            // requestCode 값은 임의로 사용하는 값

            requestPermissions(new String[] {Manifest.permission.CALL_PHONE,Manifest.permission.SEND_SMS},1000);
            // 여러개를 하고 싶다면 이런 식으로
        }

        audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        // 오디오를 사용하기 위한 객체를 가져옴 (객체는 이미 생성되어 사용되고 있음)

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) { }

        @Override
        public void onBeginningOfSpeech() { }

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
            Toast.makeText(mainContext,rs[0],Toast.LENGTH_SHORT).show();
            tts.speak(rs[0], TextToSpeech.QUEUE_FLUSH,null);

        }

        @Override
        public void onPartialResults(Bundle bundle) {}

        @Override
        public void onEvent(int i, Bundle bundle) {}

    };

    // 스레드 부분
    /*runn = new Thread(){
        @Override
        public void run(){
            mRecognizer.startListening(i);
        }

    }*/


    public void onClick(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        switch (view.getId()){
            /*case R.id.btn1 :
                // Toast를 사용해서 메시지 박스에 출력하도록 하겠음
                //Toast.makeText(this,"첫번째 버튼이 클릭되었습니다.",Toast.LENGTH_LONG).show();
                String text = ((EditText)findViewById(R.id.editText)).getText().toString();
                Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2 :
                vibrator.vibrate(5000);
                break;
            case R.id.btn3 :
                // Toast를 사용해서 메시지 박스에 출력하도록 하겠음
                vibrator.vibrate(1000);
                break;
            case R.id.btn4 :
                int second = Integer.parseInt(((EditText)findViewById(R.id.editText)).getText().toString());
                vibrator.vibrate(second*1000);
                for(int i=0;i<second;i++){
                    System.out.println(second);
                }
                break;
            case R.id.btn5 :
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 kk시 mm분 ss초");
                String getTime = sdf.format(date);
                Toast.makeText(this,getTime,Toast.LENGTH_SHORT).show();
                break;
            case R.id.dday:
                int year = Integer.parseInt(((EditText)findViewById(R.id.year)).getText().toString());
                int month = Integer.parseInt(((EditText)findViewById(R.id.month)).getText().toString());
                int date = Integer.parseInt(((EditText)findViewById(R.id.date)).getText().toString());

                Calendar cal = Calendar.getInstance();
                cal.set(year,month-1,date);

                Calendar now = Calendar.getInstance();

                int result = ((int)now.getTimeInMillis()-(int)cal.getTimeInMillis())/1000/60/60/24;
                Toast.makeText(this,Integer.toString(result), Toast.LENGTH_LONG).show();

                break;

            case R.id.V_UpBtn:
                audio.setStreamVolume(AudioManager.STREAM_RING,
                        (int)(audio.getStreamVolume(AudioManager.STREAM_RING)+1),
                        AudioManager.FLAG_PLAY_SOUND);      // FLAG_PLAY_SOUND 는 볼륨의 종류 중 전화 볼륨
                break;

            case R.id.V_DownBtn:
                if((int)(audio.getStreamVolume(AudioManager.STREAM_RING))!=0){
                    audio.setStreamVolume(AudioManager.STREAM_RING,
                            (int)(audio.getStreamVolume(AudioManager.STREAM_RING)-1),
                            AudioManager.FLAG_PLAY_SOUND);
                }
                break;
            case R.id.MV_UpBtn:
                audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                        (int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC)+1),
                        AudioManager.FLAG_PLAY_SOUND);      // FLAG_PLAY_SOUND 는 볼륨의 종류 중 전화 볼륨
                break;

            case R.id.MV_DownBtn:
                if((int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC))!=0){
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                            (int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC)-1),
                            AudioManager.FLAG_PLAY_SOUND);
                }
                break;
            case R.id.V_MaxBtn:
                audio.setStreamVolume(AudioManager.STREAM_RING,
                        (audio.getStreamMaxVolume(AudioManager.STREAM_RING)),
                        AudioManager.FLAG_PLAY_SOUND);      // FLAG_PLAY_SOUND 는 볼륨의 종류 중 전화 볼륨
                break;
            case R.id.V_MinBtn:
                audio.setStreamVolume(AudioManager.STREAM_RING,
                        0,
                        AudioManager.FLAG_PLAY_SOUND);      // FLAG_PLAY_SOUND 는 볼륨의 종류 중 전화 볼륨
                break;

            case R.id.call:
                // 사용자가 입력한 폰번호(발신 대상)
                String phoneNumber = ((EditText)findViewById(R.id.phoneNumber)).getText().toString();

                String tel = "tel:" + phoneNumber;

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
                // Intent란 어플리케이션 구성요소 간에 작업 수행을 위한 정보를 가지고 전달하는 역할
                // 화면 전환이 일어날 때 정보를 넘겨주어야 할때 사용

                int result = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);
                // 권한이 있는지 체크하는 코드 (해당코드가 없으면 아래의
                // startActivity(intent)코드가 정상 동작할 수 없음

                startActivity(intent);
                break;

            case R.id.send:
                // 문자
                String msg = ((EditText)findViewById(R.id.msg)).getText().toString();
                // 받을 사람 전화번호
                String phoneNumber = ((EditText)findViewById(R.id.phoneNumber)).getText().toString();

                if(!msg.equals("")){
                    SmsManager sms = SmsManager.getDefault();   // 문자를 사용하려먼 smsManager 객체가 필요함

                    sms.sendTextMessage(phoneNumber,null, "사랑해요",null,null);

                    // sendTextMessage의 인자값
                    // 1. 수신자 전화번호
                    // 2. 서비스 센터 전화번호 (일반적으로 null값)
                    // 3. 메시지
                    // 4. SMS 전송 성공, 실패 등의 이벤트를 알리기 위한 값(메시지를 보내기만 할것이라면 null)
                    // 5. 답장이 올 경우 SMS 수신 이벤트를 알리기 위한 값(메시지를 보내가만 할 것이라면 null)

                    ((EditText)(findViewById(R.id.msg))).setText("");
                    Toast.makeText(this,"전송되었습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"문자 내용을 입력해 주세요",Toast.LENGTH_SHORT).show();
                }

//47458063
                break;

            case R.id.read:
                tts.speak(((EditText)findViewById(R.id.msg)).getText().toString(), TextToSpeech.QUEUE_FLUSH,null);
                break;

            case R.id.pitchUp:
                tts.setPitch(2.0f);         // 음성 톤을 2.0배 올려준다.
                tts.setSpeechRate(1.0f);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak(((EditText)findViewById(R.id.msg)).getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
                break;

            case R.id.pitchDown:
                tts.setPitch(0.5f);         // 음성 톤을 2.0배 올려준다.
                tts.setSpeechRate(1.0f);    // 읽는 속도는 기본 설정
                // editText에 있는 문장을 읽는다.
                tts.speak(((EditText)findViewById(R.id.msg)).getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.today:
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 kk시 mm분 ss초");
                String getTime = sdf.format(date);

                tts.setPitch(9.0f);         // 음성 톤을 2.0배 올려준다.
                tts.setSpeechRate(3.5f);    // 읽는 속도는 기본 설정
                tts.speak(getTime,TextToSpeech.QUEUE_FLUSH, null);
                break;
                */
            case R.id.stt:
                //Toast.makeText(this,"시작",Toast.LENGTH_SHORT);
                mRecognizer.startListening(i);  // 스레드로 한다면 이거 대신 handler.post(runn);
                break;
        }
    }
}

// R은 안드로이드 프로젝트 생성시 자동으로 생성되는 객체
// res 디렉토리에 있는 파일을 접근할 수 있도록 레퍼런스를 제공하고
// 모든 컴포넌트에 대한 정보를 가지고 있는 중요한 객체
// R객체는 자동으로 만들어 지는 것이므로 절대로 건들면 안됨
// 간혹 에러 발생해서 이것저것 하다가 실수로 R 객체 코드를 건드는 경우가
// 있는데, 이때 잘못 건드리면 해당 프로젝트가 망가짐