package com.beli.code.kamusinggrisku;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TextToSpeech.OnInitListener {
Button btnUbah;
    TextView tvUbah,tvAtas,tvBawah;
    EditText edtAtas,edtBawah;
    FloatingActionButton fab;
    ProgressDialog pDialog;
    TextToSpeech ttsAtas;
    TextToSpeech ttsBawah;
    Button btnBicara,btnBicara2;
    DatabaseOpenHelper dbHelper;
    private final static String TAG = "MainActivity";
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    boolean doubleBackToExitPressedOnce = false;
    private static long back_pressed;

    private InterstitialAd interstitial;
    private AdView mAdView;
    TextView tvCek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCek=(TextView)findViewById(R.id.tvCek);
        mAdView = (AdView) findViewById(R.id.adView);
        interstitial = new InterstitialAd(MainActivity.this);
        mAdView.loadAd(new AdRequest.Builder().build());


        interstitial.setAdUnitId(getString(R.string.ad_unit_intertisial));
        AdRequest adRequest = new AdRequest.Builder().build();

        interstitial.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {


            }
        });

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        openHelper=new DatabaseOpenHelper(this);
        database=openHelper.getWritableDatabase();
        String bhsinggris = "";
        btnBicara=(Button)findViewById(R.id.btnBicara);
        btnBicara2=(Button)findViewById(R.id.btnBicara2);

        ttsAtas = new TextToSpeech(this, this);
        ttsBawah = new TextToSpeech(this, this);
        btnUbah=(Button)findViewById(R.id.btnUbah);
        tvUbah=(TextView) findViewById(R.id.tvUbah);
        tvAtas=(TextView) findViewById(R.id.tvAtas);
        tvBawah=(TextView) findViewById(R.id.tvBawah);
        edtAtas=(EditText)findViewById(R.id.editAtas);
        edtAtas.setText(bhsinggris);
        edtBawah=(EditText)findViewById(R.id.editBawah);
        edtBawah.setKeyListener(null);
        btnBicara2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cek;
                cek=tvBawah.getText().toString();
                if (cek.equals("Indonesia")){
                    speakOutIndoBawah();
                }else if (cek.equals("Inggris")){
                    speakOutUSBawah();
                }
            }
        });
        btnBicara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cek;
                cek=tvAtas.getText().toString();
                if (cek.equals("Indonesia")){
                    speakOutIndo();
                }else if (cek.equals("Inggris")){
                    speakOutUS();
                }

            }
        });
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cek;
                cek=tvUbah.getText().toString();
                if (cek.equals("Indonesia-Inggris")){
                    tvUbah.setText("Inggris-Indonesia");
                    tvAtas.setText("Inggris");
                    tvBawah.setText("Indonesia");
                }else if (cek.equals("Inggris-Indonesia")){
                    tvUbah.setText("Indonesia-Inggris");
                    tvAtas.setText("Indonesia");
                    tvBawah.setText("Inggris");
                }
            }
        });
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String cek2;
                Integer cek,sekarang,angka=1;
                sekarang= Integer.valueOf(tvCek.getText().toString());
                cek=sekarang+angka;
                tvCek.setText(String.valueOf(cek));
                cek2=tvAtas.getText().toString();
                if (cek2.equals("Indonesia")){
                  edtBawah.setText(getQuotes().toString());
                    String st;
                    String cekAd;
                    cekAd=tvCek.getText().toString();
                    st=edtBawah.getText().toString();
                    if (st.equals("[]")){
                        ambilDataIndoIngg();
                    }
                    if (cekAd.equals("3")){
                        displayInterstitial();
                    }else if (cekAd.equals("5")){

                    }else if (cekAd.equals("10")){
                        tvCek.setText("0");
                    }
                }else if (cek2.equals("Inggris")){
                    edtBawah.setText(getQuotes2().toString());
                    String st;
                    String cekAd;
                    cekAd=tvCek.getText().toString();

                    st=edtBawah.getText().toString();
                    if (st.equals("[]")){
                        ambilDataInggIndo();
                    }
                    if (cekAd.equals("3")){
                        displayInterstitial();
                    }else if (cekAd.equals("5")){

                    }else if (cekAd.equals("10")){
                        tvCek.setText("0");
                    }
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void promptSpeechInput() {
        String cek;
        cek = tvAtas.getText().toString();
        if (cek.equals("Indonesia")) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("id", "ID"));
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.speech_prompt));
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (cek.equals("Inggris")) {


            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.speech_prompt));
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    public List<String> getQuotes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT translation FROM ind_dictionary where word='"+edtAtas.getText().toString()+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public List<String> getQuotes2() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT translation FROM eng_dictionary where word='"+edtAtas.getText().toString()+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Ketuk lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 20000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bantuan) {
            Intent intent=new Intent(MainActivity.this,BantuanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tentang) {
            new AboutNoticeDialog()
                    .show(getSupportFragmentManager(), "dialog_about_notice");

        } else if (id == R.id.nav_applain) {
            Intent rateReviewIntent = new Intent(Intent.ACTION_VIEW);
            rateReviewIntent.setData(Uri.parse(
                    getString(R
                            .string.publiser_google_play_url)));
            startActivity(rateReviewIntent);
        } else if (id == R.id.nav_rateapp) {
            Intent rateReviewIntent = new Intent(Intent.ACTION_VIEW);
            rateReviewIntent.setData(Uri.parse(
                    getString(R.string.google_play_url)));
            startActivity(rateReviewIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void ambilDataIndoIngg() {
        pDialog = new ProgressDialog(MainActivity.this);

        pDialog.setTitle("KAMUS INGGRIS");
        pDialog.setMessage("Menerjemahkan..");
        pDialog.show();
          String url="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170407T123038Z.920bd14d4829dae2.b047f6cb81ca37118f96a7a3608e83d873c1010f&text="+edtAtas.getText().toString().replace(" ","+")+"&lang=id-en";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Tidak dapat menerjemahkan, Periksa sambungan", Toast.LENGTH_LONG).show();
pDialog.hide();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void showJSON(String response) {
        String nama = "";
        try {
            ArrayList<String> temp = new ArrayList<String>();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray movies = jsonObject.getJSONArray("text");
            for(int i=0;i<movies.length();i++){
                String movie = movies.getString(i);
                  edtBawah.setText(movie);
                pDialog.hide();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void ambilDataInggIndo() {
        pDialog = new ProgressDialog(MainActivity.this);

        pDialog.setTitle("KAMUS INGGRIS");
        pDialog.setMessage("Menerjemahkan..");
        pDialog.show();

        String url="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170407T123038Z.920bd14d4829dae2.b047f6cb81ca37118f96a7a3608e83d873c1010f&text="+edtAtas.getText().toString().replace(" ","+")+"&lang=en-id";
        StringRequest stringRequest2 = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                showJSON2(response2);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,"Tidak dapat menerjemahkan, Periksa sambungan", Toast.LENGTH_LONG).show();
                            pDialog.hide();
                    }
                });

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }


    private void showJSON2(String response) {
        String nama = "";
        try {
            //  JSONArray movies = jsonResponse.getJSONArray("abridged_cast");
            ArrayList<String> temp = new ArrayList<String>();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray movies = jsonObject.getJSONArray("text");
            for(int i=0;i<movies.length();i++){
                String movie = movies.getString(i);
           edtBawah.setText(movie);
                pDialog.hide();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onInit(int i) {
        String ceks,ceks2;
        ceks2=tvBawah.getText().toString();
        ceks=tvAtas.getText().toString();
        if (i == TextToSpeech.SUCCESS) {

            int result = ttsAtas.setLanguage( new Locale("id","ID"));
            int result2 = ttsBawah.setLanguage(Locale.ENGLISH);
            if (result2==TextToSpeech.LANG_MISSING_DATA||result2==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "Language is not supported");

            }else if (ceks2.equals("Indonesia")){
                speakOutIndo();
            }else if (ceks2.equals("Inggris")){
                speakOutUS();
            }else {
                Log.e("TTS", "Initilization Failed");

            }
   if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else if (ceks.equals("Indonesia")){

               // speakOutIndo();
            }else if (ceks.equals("Inggris")){
                //speakOutUS();
            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }

    }
    private void speakOutIndo() {
String text;
        text=edtAtas.getText().toString();

        ttsAtas.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void speakOutIndoBawah() {
        String text;
        text=edtBawah.getText().toString();

        ttsAtas.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    private void speakOutUS() {
        String text;
        text=edtAtas.getText().toString();

        ttsBawah.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    private void speakOutUSBawah() {
        String text;
        text=edtBawah.getText().toString();

        ttsBawah.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtAtas.setText(result.get(0));
                    if (edtAtas.equals("")){
                   //     imgku.setImageResource(R.drawable.ha);
                    }
                }
                break;
            }

        }
    }


    @Override
    public void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {


        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {


        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    public void displayInterstitial() {

        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
