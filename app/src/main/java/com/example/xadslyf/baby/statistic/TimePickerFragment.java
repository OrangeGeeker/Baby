package com.example.xadslyf.baby.statistic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by liuyifei on 2018/9/11.
 */
public class TimePickerFragment extends DialogFragment {

  Callback callback;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callback = (Callback) getActivity();
  }

  public interface Callback {
    void onTimePick(int hourOfDay, int minute);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);

    // Create a new instance of DatePickerDialog and return it
    return new TimePickerDialog(getActivity(), (timePicker, i, i1) -> callback.onTimePick(i, i1), hourOfDay, min, true);
  }
}
