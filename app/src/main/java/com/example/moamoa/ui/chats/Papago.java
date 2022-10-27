package com.example.moamoa.ui.chats;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Papago {
    public String getTranslation(String word, String source, String target) {
        String clientId = "wszn_DvJZDrFTmS0D4_p";   // 애플리케이션 클라이언트 아이디값
        String clientSecret = "co54oTFdb6";         // 애플리케이션 클라이언트 시크릿값

        try {
            // 번역문을 UTF-8로 인코딩
            String wordSource, wordTarget;
            String text = URLEncoder.encode(word, "UTF-8");     // 번역할 텍스트
            wordSource = URLEncoder.encode(source, "UTF-8");    // 원본 언어(source language)의 언어 코드
            wordTarget = URLEncoder.encode(target, "UTF-8");    // 목적 언어(target language)의 언어 코드

            // 파파고 API와 연결 수행
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt"; // 요청 URL
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");   // HTTP 메서드
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            // 번역할 문장을 파라미터로 전송
            String postParams = "source="+wordSource+"&target="+wordTarget+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            Log.e("TEST6", "wr: "+wr);

            // 번역 결과 받아옴
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 서버가 정상 응답한 경우 (200번 보내주면 정상 응답이라고 함)
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {   // readLine: 데이터를 라인 단위로 읽음
                response.append(inputLine);
                Log.e("TEST6", "inputLine: "+inputLine);
            }
            br.close();
            return response.toString();
            //System.out.println(response.toString());
            /*
            String s = response.toString();
            Log.e("TEST6", "s: "+s);
            s = s.split("\"")[27];
            Log.e("TEST6", "s: "+s);
            return s;

             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";  // 아무것도 입력되지 않았을 시 "" 출력
    }
}