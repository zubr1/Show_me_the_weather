package com.gmail.zubrapps.showmetheweather;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał on 2016-06-22.
 */
public class FetchData {
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    public static List<String> getCityList(Context context, AssetManager mngr){
        List<String> data = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mngr.open("city_list.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] dividedLine = mLine.split("\t");
                for(String t:dividedLine){
                    //only city names have first character upper case and second lower case
                    if(t.length()>2 && Character.isUpperCase(t.charAt(0)) && !Character.isUpperCase(t.charAt(1))) {
                        data.add(t);
                    }
                }
            }
            return data;
        } catch (IOException e) {
            Toast.makeText(context, "Cannot find city_list.txt. Please reinstall Your app.",Toast.LENGTH_LONG).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Toast.makeText(context, "Unknown error.",Toast.LENGTH_LONG).show();
                }
            }
        }
        return null;

    }


    public static JSONObject getWeatherData(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder();
            String tmp;
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }
        catch(Exception e){
            return null;
        }
    }

}
