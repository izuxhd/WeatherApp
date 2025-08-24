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



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

package com.example.weatherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private Button fetchBtn;
    private TextView weatherOutput;
    private Double lastTemp = null;
    private final String apiKey = "a83fa0b1680ab718b0fc54c438052"; //npt real api key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInput = findViewById(R.id.cityInput);
        fetchBtn = findViewById(R.id.fetchBtn);
        weatherOutput = findViewById(R.id.weatherOutput);

        createNotificationChannel();

        fetchBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString();
            fetchWeather(city);
        });
    }

    private void fetchWeather(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI service = retrofit.create(WeatherAPI.class);
        Call<WeatherResponse> call = service.getWeather(city, apiKey, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weather = response.body();
                if (weather != null) {
                    double temp = weather.getMain().getTemp();
                    double rain = (weather.getRain() != null && weather.getRain().getOneHour() != null) ? weather.getRain().getOneHour() : 0.0;
                    double wind = weather.getWind().getSpeed();
                    String desc = weather.getWeather().get(0).getDescription();

                    weatherOutput.setText("Temp: " + temp + "°C\nRain: " + rain + " mm\nWind: " + wind + " m/s\nDesc: " + desc);

                    if (lastTemp != null && Math.abs(temp - lastTemp) >= 25) {
                        sendNotification("Temperature changed drastically: " + temp + "°C");
                    }
                    if (rain > 0) {
                        sendNotification("It's raining in " + city + "!");
                    }
                    lastTemp = temp;
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherOutput.setText("Error: " + t.getMessage());
            }
        });
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "weatherChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Weather Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "weatherChannel",
                    "Weather Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for weather notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}


}
