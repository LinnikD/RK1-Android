package com.example.uzzz.translater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by uzzz on 08.10.14.
 */
public class Dictionaries {

    private HashMap<String, String> decodeLangMap;
    private HashMap<String, String> codeLangMap;
    private HashMap<String, ArrayList<String>> langMap;
    private ArrayList<String> langsList;

    public Dictionaries(JSONObject jObj) {
        initCodeAndDecode(jObj);
        initLangMap(jObj);
        initLangs(jObj);
    }

    public ArrayList<String> getAllLangs() {
        return langsList;
    }

    public ArrayList<String> getAvailable(String lang) {
        return langMap.get(lang);
    }

    public String codeLanguage(String lang) {
        return codeLangMap.get(lang);
    }

    public String decodeLanguage(String code) {
        return decodeLangMap.get(code);
    }

    private void initLangMap(JSONObject jObj) {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        try {
            JSONArray jsonArr = jObj.getJSONArray("dirs");
            for (int i = 0; i < jsonArr.length(); i++) {
                String key = pairGetFirst(jsonArr.getString(i));
                if( !map.containsKey(key) ) {
                    ArrayList<String> arrayList = new ArrayList<String>();
                    for(int j = 0; j < jsonArr.length(); j++) {
                        String value = jsonArr.getString(j);
                        if( key.equals(pairGetFirst(value)) ) {
                            String val = pairGetSecond(value);
                            arrayList.add(val);
                        }
                    }
                    map.put(key, arrayList);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        langMap = map;
    }

    private void initCodeAndDecode(JSONObject jObj) {
        codeLangMap = new HashMap<String, String>();
        decodeLangMap = new HashMap<String, String>();
        try {
            JSONObject langsJSON = jObj.getJSONObject("langs");
            Iterator<String> keyIterator = langsJSON.keys();
            while(keyIterator.hasNext()) {
                String code = keyIterator.next();
                String name = langsJSON.getString(code);
                codeLangMap.put(name, code);
                decodeLangMap.put(code,name);
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void initLangs(JSONObject jObj) {
        langsList = new ArrayList<String>();
        try {
            JSONObject langs = jObj.getJSONObject("langs");
            Iterator<String> keyIterator = langs.keys();
            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                langsList.add(langs.getString(key));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private String pairGetFirst(String s) {
        return s.substring(0,2);
    }

    private String pairGetSecond(String s) {
        return s.substring(3,5);
    }
}
