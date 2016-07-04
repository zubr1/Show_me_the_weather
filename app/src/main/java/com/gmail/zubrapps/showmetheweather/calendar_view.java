package com.gmail.zubrapps.showmetheweather;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class calendar_view extends Fragment {


    public calendar_view() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_calendar_view, container, false);

        //Calendar nextYear = Calendar.getInstance();
        //nextYear.add(Calendar.YEAR, 10);

        Calendar nextYear = Calendar.getInstance();
        String[] mTimeZones = TimeZone.getAvailableIDs();

        for(int i=0;i<mTimeZones.length;i++){
            Log.i("Square", mTimeZones[i]);
        }
        nextYear.add(Calendar.DAY_OF_WEEK, 5);

        CalendarPickerView calendar = (CalendarPickerView) rootView.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);


        return rootView;
    }

}
