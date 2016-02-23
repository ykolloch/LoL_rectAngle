package com.example.yannic.lol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class LoL extends AppCompatActivity {

    private Button btnSearch;
    private String summonerName = null;
    private String result = "";
    private ArrayList<String> summonerGeneral = new ArrayList<>();

    public LoL() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lo_l);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.findAll();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summonerName = "Waam";
                searchSummoner(summonerName);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void searchSummoner(String summonerName) {
        new GETDATA().execute("https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/"+summonerName+"?api_key=5dc1a212-69d0-4741-bbc0-b0948beace24");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lo_l, menu);
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

    public void findAll() {
        btnSearch = (Button) findViewById(R.id.btnSearch);
    }

    public class GETDATA extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                return stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Test", s);
            fillData(summonerName, s);
        }
    }

    /**
     * Bekomm MAP statt json zureuck PROBLEM!
     * @param summonerName
     * @param s
     */
    public void fillData(String summonerName, String s) {
        if(s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.d("jsonObject", jsonObject.toString());
                JSONObject jsonObject2 = jsonObject.getJSONObject(summonerName.toLowerCase());
                Log.d("jsonObject2", jsonObject2.toString());

                if (jsonObject2 != null) {
                    for (int i=0;i<jsonObject2.length();i++){
                        summonerGeneral.add(jsonObject2.get(i).toString());
                    }
                }
                Log.d("arra", summonerGeneral.get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
