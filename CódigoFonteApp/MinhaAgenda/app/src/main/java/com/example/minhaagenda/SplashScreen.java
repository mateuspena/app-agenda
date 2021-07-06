package com.example.minhaagenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (checkAndRequestPermissions()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new JSONParse().execute();
                }
            }, SPLASH_TIME_OUT);
        }
    }


    private boolean checkAndRequestPermissions() {

        int wakeLockPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);
        int accessNetWorkStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wakeLockPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);
        }
        if (accessNetWorkStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public class JSONParse extends AsyncTask<String, String, DataAdapter> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog= new ProgressDialog(SplashScreen.this);
            pDialog.setMessage(getApplicationContext().getString(R.string.txt_obtendo_dados_splash));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected DataAdapter doInBackground(String... args) {
            JSONArray jsonFeriados = requestJson();
            Map<LocalDate,JSONObject> mapFeriados = new HashMap<LocalDate,JSONObject>();

            for (int i = 0; i< jsonFeriados.length(); i++) {
                try {
                    JSONObject feriado = (JSONObject) jsonFeriados.get(i);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate feriadoDate = LocalDate.parse(feriado.getString("date"), formatter);

                    mapFeriados.put(feriadoDate, feriado);
                } catch (JSONException e) {
                }
            }

            Calendario calendario = new Calendario(SplashScreen.this);
            ArrayList<Data> datas = calendario.montaCalendario(mapFeriados);

            return new DataAdapter(datas);
        }

        @Override
        protected void onPostExecute(DataAdapter calendario) {
            try {
                pDialog.dismiss();

                MainActivity.calendario = calendario;
                MainActivity.initPosition = Calendario.retornaPosicaoInicial();

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {

            }
        }
    }


    public static JSONArray requestJson(){
        JSONArray json = null;
        String resp=null;

        try {
            URL url1 = new URL("https://api.calendario.com.br/?json=true&token=bWF0ZXVzXzEyakBob3RtYWlsLmNvbSZoYXNoPTE0MTU0OTAzNg&ano=2021&ibge=2927408");
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            InputStream inputStream = conn.getInputStream();
            resp = IOUtils.toString(inputStream);
            int responceCode = conn.getResponseCode();
            json = new JSONArray(resp);

        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
}