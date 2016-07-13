package com.gmail.zubrapps.showmetheweather;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements WeatherFragment.WeatherFragmentListener, CalendarView.CalendarViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addDynamicFragment();

    }


    private void addDynamicFragment() {
        // TODO Auto-generated method stub
        // creating instance of the HelloWorldFragment.
        Fragment fragment = new WeatherFragment();
        // adding fragment to relative layout by using layout id
        getFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void onDaySelected(String day) {
        Toast.makeText(getApplicationContext(),
                day,
                Toast.LENGTH_LONG).show();

        Fragment weatherFragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("mDay", day);
        weatherFragment.setArguments(args);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, weatherFragment, "weather_view");
        ft.commit();
    }

    @Override
    public void goToCalendarView(boolean go) {
        if(go){
            Toast.makeText(getApplicationContext(),
                    "tru",
                    Toast.LENGTH_LONG).show();
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer, new CalendarView(), "CalendarView");
            ft.commit();
        }
    }

}
