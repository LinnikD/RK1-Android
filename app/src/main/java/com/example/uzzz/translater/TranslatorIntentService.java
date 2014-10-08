package com.example.uzzz.translater;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by uzzz on 08.10.14.
 */
public class TranslatorIntentService extends IntentService {

    public static final String ACTION_GET_ALL = "com.example.uzzz.translator.action.getAllLanguages";
    public static final String ACTION_TRANSLATE = "com.example.uzzz.translator.action.translate";

    public static final String ALL_LANGUAGES_INTENT = "com.example.uzzz.translator.action.allLangIntent";
    public static final String TRANSLATE_INTENT = "com.example.uzzz.translator.action.translateIntent";

    public static final String RETURN_LANGUAGES = "com.example.uzzz.translator.action.retLangs";
    public static final String RETURN_TRANSLATE = "com.example.uzzz.translator.action.retTranslate";

    public TranslatorIntentService() {
        super("TranslatorIntentService");
    }

    private APIConnector ac = new APIConnector();

    private String getAllLanguages() {
        return ac.getLanguages().toString();
    }

    private String translate(String s) {
        String [] str = s.split(" ; ");
        String response = ac.translate(str[0], str[1], str[2]);
        return parseAnswer(response);
    }

    private String parseAnswer(String s) {
        try {
            JSONObject jObj = new JSONObject(s);
            JSONArray arr = jObj.getJSONArray("text");
            String text = "";
            for(int i = 0; i < arr.length(); i++) {
                text += arr.getString(i);
            }
            return  text;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onHandleIntent(Intent i) {
        if (i != null) {
            if(i.getStringExtra(ACTION_GET_ALL) != null) {
                String s = getAllLanguages();
                Intent in = new Intent(ALL_LANGUAGES_INTENT);
                in.putExtra(RETURN_LANGUAGES, s);
                LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            }
            else if (i.getStringExtra(ACTION_TRANSLATE) != null) {
                String s = translate( i.getStringExtra(ACTION_TRANSLATE) );
                Intent in = new Intent(TRANSLATE_INTENT);
                in.putExtra(RETURN_TRANSLATE, s);
                LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            }
            else {
                System.out.println("No Action Found!");
            }
        }
    }
}
