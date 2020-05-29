package com.example.xadslyf.baby.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by liuyifei on 2018/9/4.
 */

@Entity(indices = { @Index("type") })
public class BabyMotion {

    public static final int SLEEP =1;
    public static final int WAKE =2;
    public static final int FEED =3;
    public static final int POO =4;
    public static final int NAPPY =5;

  @PrimaryKey(autoGenerate = true)
  private int id;

  private int type;
  private String name;
  private long time;

  @Ignore
  private long sleepTime;
  @Ignore
  public String dateStr;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public long getSleepTime() {
    return sleepTime;
  }

  public void setSleepTime(long sleepTime) {
    this.sleepTime = sleepTime;
  }
}
