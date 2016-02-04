package com.horn.workshop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by Sariga on 1/18/2016.
*/
public class DatePickerFragment extends DialogFragment
       {
public EditText dateView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (ScheduledMaintenanceAppointment)getActivity(), year, month, day);
    }


//
//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        monthOfYear = monthOfYear+1;
//        showDate(year, monthOfYear, dayOfMonth);
//    }
//    private void showDate(int year, int month, int day) {
//       // return(day+"/"+month+"/"+year);
//        String date = day+"/"+month+"/"+year;
//        Log.d("date",date);
//
//    }

}
