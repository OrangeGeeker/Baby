package com.example.xadslyf.baby.helper;

/**
 * Created by liuyifei on 2018/9/6.
 */
public class TimeHelper {
  public static String ms2HMS(int _ms) {
    String HMStime;
    _ms /= 1000;
    int hour = _ms / 3600;
    int mint = (_ms % 3600) / 60;
    int sed = _ms % 60;
    String hourStr = String.valueOf(hour);
    if (hour < 10) {
      hourStr = "0" + hourStr;
    }
    String mintStr = String.valueOf(mint);
    if (mint < 10) {
      mintStr = "0" + mintStr;
    }
    String sedStr = String.valueOf(sed);
    if (sed < 10) {
      sedStr = "0" + sedStr;
    }
    HMStime = hourStr + ":" + mintStr + ":" + sedStr;
    return HMStime;
  }
}
