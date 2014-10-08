package com.example.uzzz.translater;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    private Button translateButton;
    private Intent translatorServiceIntent;
    private Translator tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter langFilter = new IntentFilter( TranslatorIntentService.ALL_LANGUAGES_INTENT );
        LanguageResponseReceiver languagesReceiver = new LanguageResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(languagesReceiver, langFilter);

        IntentFilter trFilter = new IntentFilter( TranslatorIntentService.TRANSLATE_INTENT );
        TranslateResponseReceiver translateReceiver = new TranslateResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(translateReceiver, trFilter);

        translateButton = (Button) findViewById(R.id.button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatorServiceIntent = new Intent(MainActivity.this, TranslatorIntentService.class);
                translatorServiceIntent.putExtra(TranslatorIntentService.ACTION_GET_ALL, "");
                startService(translatorServiceIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LanguageResponseReceiver extends BroadcastReceiver
    {
        private LanguageResponseReceiver() {

        }
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra(TranslatorIntentService.RETURN_LANGUAGES);
            tr = new Translator(context, s);
            System.out.println( tr.getAllLanguages() );
            System.out.println( tr.getAvailableLanguages("Польский") );
            tr.translate("Я люблю пончики! Сегодня вечером нужно сходить в кино!", "Русский", "Английский");
        }
    }

    private class TranslateResponseReceiver extends BroadcastReceiver
    {
        private TranslateResponseReceiver() {

        }
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra(TranslatorIntentService.RETURN_TRANSLATE);
            System.out.println(s);
        }
    }

}
