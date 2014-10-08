package com.example.uzzz.translater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by uzzz on 08.10.14.
 */
public class APIConnector {
    private static final String url = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String key = "key=trnsl.1.1.20141006T173746Z.6c8f408bd4ffe228.fe16f7eace3e3534f3565bf70c30409d6077de45";
    private static final String getLangs = "getLangs";
    private static final String translate = "translate";
    HttpURLConnection urlConnection;

    public APIConnector() {

    }

    private String readStream(InputStream is) {
        String s;
        ArrayList<Byte> byteArray = new ArrayList<Byte>();
        while(true) {
            try {
                byteArray.add((byte) is.read());
                if (byteArray.get(byteArray.size() - 1) == -1)
                    break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            byte arr[] = new byte[byteArray.size() - 1];
            for (int i = 0; i < byteArray.size() - 1; i++) {
                arr[i] = byteArray.get(i);
            }
            s = new String(arr, "UTF-8");
            return s;
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String translate(String str, String langfrom, String langto) {
        try {
            String params = "";
            params += "&text=" + URLEncoder.encode(str, "UTF-8");
            params += "&lang=" + langfrom + "-" + langto;
            URL translateURL = new URL(url + translate + "?" + key + params);

            urlConnection = (HttpURLConnection) translateURL.openConnection();
            InputStream is = urlConnection.getInputStream();
            String response = readStream(is);
            return response;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public JSONObject getLanguages() {
        JSONObject jsonObj;
        try {
            URL langUrl = new URL(url + getLangs + "?" + key + "&" + "ui=ru");
            urlConnection = (HttpURLConnection) langUrl.openConnection();
            InputStream is = urlConnection.getInputStream();
            String jsonStr = readStream(is);
            jsonObj = new JSONObject(jsonStr);
            return jsonObj;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
