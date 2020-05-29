package com.example.xadslyf.baby.statistic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by liuyifei on 2018/9/11.
 */
public class DatePickerFragment extends DialogFragment {

  Callback callback;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callback = (Callback) getActivity();
  }

  public interface Callback {
    void onDatePick(int year, int month, int day);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    // Create a new instance of DatePickerDialog and return it
    return new DatePickerDialog(Objects.requireNonNull(getActivity()), (datePicker, i, i1, i2) -> {
      callback.onDatePick(i, i1, i2);
    }, year, month, day);
  }
}
