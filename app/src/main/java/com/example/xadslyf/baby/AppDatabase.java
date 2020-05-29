package com.example.xadslyf.baby;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.example.xadslyf.baby.data.BabyMotion;
import com.example.xadslyf.baby.data.BabyMotionDAO;

/**
 * Created by liuyifei on 2018/9/4.
 */

@Database(entities = { BabyMotion.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

  public abstract BabyMotionDAO babyMotionDAO();

  private volatile static AppDatabase instance = null;


  public static AppDatabase getInstance(final Context context) {
      if (instance == null) {
          synchronized (AppDatabase.class) {
              if (instance == null) {
                  instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "baby_db").build();
              }
          }
      }
      return instance;
  }
}
