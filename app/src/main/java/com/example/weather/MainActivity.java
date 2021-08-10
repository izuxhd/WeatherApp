package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText value;
    TextView text;
    public void GetWeatherData(View view)
    {
        Download a1=new Download();

        a1.execute("https://api.openweathermap.org/data/2.5/weather?q=" +
                value.getText().toString()+
                        "&appid=441185494bc15e3bee68edb5639a4fc0");
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(value.getWindowToken(),0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value =findViewById(R.id.editText);
        text=findViewById(R.id.resultText);

    }

    public class Download extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                String result="";
                InputStreamReader reader = new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    result+=(char)data;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }

        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
// Log.i("Info",s);
            try {
                JSONObject a1=new JSONObject(s);
                String weatherInfo=a1.getString("weather");
                String main=a1.getString("main");
                JSONObject temp=new JSONObject(main);
// Log.i("weatherInfo :",weatherInfo);
                String main1="";
                String desc="";
                String min_temp=temp.getString("temp_min");
                String max_temp=temp.getString("temp_max");
                String pressure=temp.getString("pressure");
                JSONArray object=new JSONArray(weatherInfo);
                for(int i=0;i<object.length();i++)
                {
                    JSONObject newobject=object.getJSONObject(i);
                    main1=newobject.getString("main");
                    Log.i("Main :",main1);
                    desc=newobject.getString("description");
                    Log.i("Description :",desc);

                }
                text.setText("Main :"+main1+
                        "\n Description :"+desc+
                        "\nMax temp :"+
                        max_temp+
                        "\nMin temp" +
                        min_temp+
                        "\nPressure :"+pressure);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}