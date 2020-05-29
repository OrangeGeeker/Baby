package com.example.xadslyf.baby.data;

import android.app.Application;
import com.example.xadslyf.baby.AppDatabase;
import java.util.List;

/**
 * Created by liuyifei on 2018/9/5.
 */
public class BabyMotionRepository {

  private BabyMotionDAO babyMotionDAO;

  public BabyMotionRepository(Application application) {
    AppDatabase db = AppDatabase.getInstance(application);
    babyMotionDAO = db.babyMotionDAO();
  }

  public List<BabyMotion> getAll() {
    return babyMotionDAO.getAll();
  }

  public List<BabyMotion> getByType(int... type) {
    return babyMotionDAO.getByType(type);
  }

  public void insert(BabyMotion babyMotion) {
    babyMotionDAO.insertBabyMotions(babyMotion);
  }

  public void remove(BabyMotion babyMotion) {
    babyMotionDAO.remove(babyMotion);
  }

  public void update(BabyMotion babyMotion){
    babyMotionDAO.update(babyMotion);
  }
}
