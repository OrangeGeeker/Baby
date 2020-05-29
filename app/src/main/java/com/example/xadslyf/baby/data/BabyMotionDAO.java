package com.example.xadslyf.baby.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

/**
 * Created by liuyifei on 2018/9/4.
 */
@Dao
public interface BabyMotionDAO {

  @Query("SELECT * FROM babymotion ORDER BY time DESC")
  List<BabyMotion> getAll();

  @Insert
  void insertBabyMotions(BabyMotion... babyMotions);

  @Delete
  void remove(BabyMotion... babyMotions);

  @Query("SELECT * FROM babymotion WHERE type IN (:type) ORDER BY time DESC")
  List<BabyMotion> getByType(int... type);

  @Update()
  void update(BabyMotion babyMotion);
}
