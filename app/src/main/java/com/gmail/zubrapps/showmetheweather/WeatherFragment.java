package com.gmail.zubrapps.showmetheweather;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WeatherFragment extends Fragment {
    private static final String TAG = "SearchViewFilterMode";

    private ListView mCityList;
    private SearchView mSearchCity;
    private RelativeLayout mWeatherDataLayout;
    private TextView cityField;
    private TextView updatedField;
    private TextView infoField;
    private TextView currentTemperatureField;
    private TextView weatherIcon;
    private Handler handler;


    public WeatherFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        findLayoutItems(rootView);
        //get city list from fetch data and put it into list view
        prepareCityList();

        prepareSearchView();
        //prepare weather icons
        Typeface weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
        weatherIcon.setTypeface(weatherFont);

        updateWeatherData(new SavedCity(getActivity()).getCity());

        return rootView;
    }

    private void prepareSearchView() {
        mSearchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //after submit hide tips and search specified city
                hideList();
                changeCity(s);
                return false;
            }

            //show "cities hint" only if search view is not empty
            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    mCityList.clearTextFilter();
                    hideList();
                } else {
                    mCityList.setFilterText(s);
                    showList();
                }

                return false;
            }
        });
    }

    private void prepareCityList() {
        List<String> mCities = FetchData.getCityList(getActivity(), getActivity().getAssets());
        if(mCities==null) {
            return;
        }
        mCityList.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.my_list_item,
                mCities));
        mCityList.setTextFilterEnabled(true);
        mCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //on every submit, search for picked city
                mSearchCity.setQuery(((TextView)view).getText().toString(), true);
                changeCity(((TextView)view).getText().toString());
            }
        });
    }

    private void findLayoutItems(View rootView){
        mSearchCity = (SearchView)rootView.findViewById(R.id.searchCity);
        mCityList = (ListView)rootView.findViewById(R.id.citiesList);
        mWeatherDataLayout  = (RelativeLayout)rootView.findViewById(R.id.weatherDataLayout);
        weatherIcon = (TextView)rootView.findViewById(R.id.weatherIcon);
        cityField = (TextView)rootView.findViewById(R.id.cityText);
        updatedField = (TextView)rootView.findViewById(R.id.lastUpdateText);
        infoField = (TextView)rootView.findViewById(R.id.weatherInfoText);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.temperatureText);
    }

    private void showList(){
        mCityList.setVisibility(View.VISIBLE);
        mWeatherDataLayout.setVisibility(View.GONE);
    }

    private void hideList(){
        mCityList.setVisibility(View.GONE);
        mWeatherDataLayout.setVisibility(View.VISIBLE);
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = FetchData.getWeatherData(getActivity(), city);
                if(json == null) {//error: cannot find proper city, no internet connection, error while fetching data, etc...
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.city_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            SavedCity mSavedCity = new SavedCity(getActivity());
                            mSavedCity.setCity(city);
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    //get data from json object, and put it into layout items (textview items)
    private void renderWeather(JSONObject json){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            infoField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            Toast.makeText(getActivity().getApplicationContext(), "Cannot download full data",Toast.LENGTH_SHORT).show();
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }




}
