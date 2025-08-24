package com.example.weather;

import java.util.List;

public class WeatherResponse {
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private Rain rain;

    public List<Weather> getWeather() { return weather; }
    public Main getMain() { return main; }
    public Wind getWind() { return wind; }
    public Rain getRain() { return rain; }

    public static class Weather {
        private String description;
        public String getDescription() { return description; }
    }

    public static class Main {
        private double temp;
        public double getTemp() { return temp; }
    }

    public static class Wind {
        private double speed;
        public double getSpeed() { return speed; }
    }

    public static class Rain {
        @com.google.gson.annotations.SerializedName("1h")
        private Double oneHour;
        public Double getOneHour() { return oneHour; }
    }
}
