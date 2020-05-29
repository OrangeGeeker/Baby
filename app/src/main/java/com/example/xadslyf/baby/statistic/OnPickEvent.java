package com.example.xadslyf.baby.statistic;

import com.example.xadslyf.baby.data.BabyMotion;

/**
 * Created by liuyifei on 2018/9/11.
 */
public class OnPickEvent {

  public int pos;
  public BabyMotion babyMotion;
  public int pickYear;
  public int pickMonth;
  public int pickDay;
  public int hourOfDay;
  public int minute;

  public OnPickEvent(int pickYear, int pickMonth, int pickDay, int hourOfDay, int minute,
      BabyMotion pickMotion, int pickPos) {
        this.pickYear = pickYear;
        this.pickMonth = pickMonth;
        this.pickDay = pickDay;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.babyMotion = pickMotion;
        this.pos = pickPos;
  }
}
