package com.gmail.zubrapps.showmetheweather;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
public class CalendarView extends Fragment {
    CalendarViewListener mCalendarCallback;

    public interface CalendarViewListener{
        public void onDaySelected(String day);
    }


    public CalendarView() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //make sure that MainActivity has implemented the callback listener
        try {
            mCalendarCallback = (CalendarViewListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CalendarViewListener");
        }
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
            //Log.i("Square", mTimeZones[i]);
        }
        nextYear.add(Calendar.DAY_OF_WEEK, 5);

        final CalendarPickerView calendar = (CalendarPickerView) rootView.findViewById(R.id.calendar_view);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String[] mArray = String.valueOf(date).split(" ");
                mCalendarCallback.onDaySelected(mArray[0]);

            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);

        return rootView;
    }

}
