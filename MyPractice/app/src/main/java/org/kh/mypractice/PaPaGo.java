package org.kh.mypractice;

import android.os.StrictMode;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PaPaGo {
    public String testPaPaGo(String srcData) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String clientId = "_7BxRyOWF5C9IJxUD4eW";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "R9rHjlc1Iq";//애플리케이션 클라이언트 시크릿값";
        try {
            System.out.println("테스트1");
            String text = URLEncoder.encode(srcData, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            // post request
            String postParams = "source=ko&target=en&text=" + text;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONObject json = new JSONObject(response.toString());

            //System.out.println("테슷흐1"+json.getJSONObject("message").toString());

            //System.out.println("테슷흐2"+json.getJSONObject("message").getJSONObject("result").toString());

            String result = json.getJSONObject("message").getJSONObject("result").getString("translatedText");



           // System.out.println("테슷흐3"+json.getJSONObject("message").getJSONObject("result").getJSONObject("translatedText").toString());

            return result;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}
