package com.gmail.zubrapps.showmetheweather;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create new fragment and transaction
        Fragment newFragment = new WeatherFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.mWeatherFragment, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();

    }
}
