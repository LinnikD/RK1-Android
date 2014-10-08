package com.example.uzzz.translater;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by uzzz on 08.10.14.
 */
public class Translator {
    private Context cont;
    private Dictionaries dict;
    private Intent translatorServiceIntent;

    public Translator(Context context, String json) {
        cont = context;
        try {
            JSONObject jObj = new JSONObject(json);
            dict = new Dictionaries(jObj);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void translate(String text, String langFrom, String langTo) {
        String from = dict.codeLanguage(langFrom);
        String to = dict.codeLanguage(langTo);
        translatorServiceIntent = new Intent(cont, TranslatorIntentService.class);
        String translateData = text + " ; " + from + " ; " + to;
        translatorServiceIntent.putExtra(TranslatorIntentService.ACTION_TRANSLATE, translateData);
        cont.startService(translatorServiceIntent);
    }

    public ArrayList<String> getAllLanguages() {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> array = dict.getAllLangs();
        for(int i = 0; i < array.size(); i++) {
            result.add( array.get(i) );
        }
        Collections.sort(result);
        return result;
    }

    public ArrayList<String> getAvailableLanguages(String s) {
        String code = dict.codeLanguage(s);
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> array = dict.getAvailable(code);
        for(int i = 0; i < array.size(); i++) {
            result.add( dict.decodeLanguage( array.get(i) ) );
        }
        Collections.sort(result);
        return dict.getAvailable(code);
    }

}
