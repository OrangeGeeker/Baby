package com.example.xadslyf.baby.home;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.example.xadslyf.baby.BaseActivity;
import com.example.xadslyf.baby.R;
import com.example.xadslyf.baby.data.BabyMotion;
import com.example.xadslyf.baby.data.BabyMotionRepository;
import com.example.xadslyf.baby.statistic.DatePickerFragment;
import com.example.xadslyf.baby.statistic.MotionFragment;
import com.example.xadslyf.baby.statistic.StatisticFragment;
import com.example.xadslyf.baby.statistic.TimePickerFragment;
import com.example.xadslyf.baby.statistic.OnPickEvent;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.Fragmentation;
import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends BaseActivity implements MotionFragment.Callback,
    DatePickerFragment.Callback, TimePickerFragment.Callback {

  private static final String TAG_DATE_PICKER = "TAG_DATE_PICKER";
  private static final String TAG_TIME_PICKER = "TAG_TIME_PICKER";
  private BabyMotionRepository babyMotionRepository;
  private CompositeDisposable compositeDisposable;
  private GestureDetector gestureDetector;
  private float rawX;
  private float rawY;
  private int pickYear;
  private int pickMonth;
  private int pickDay;
  private int pickMotionType;
  private BabyMotion pickMotion;
  private int pickPos;

  @Override
  protected void onDestroy() {
    compositeDisposable.clear();
    super.onDestroy();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    compositeDisposable = new CompositeDisposable();
    //getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_ALL); // EDGE_LEFT(默认),EDGE_ALL
    babyMotionRepository = new BabyMotionRepository(getApplication());
    gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

      @Override
      public boolean onDown(MotionEvent e) {
        return true;
      }

      @Override
      public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        rawX = e1.getX();
        rawY = e1.getY();
        return super.onScroll(e1, e2, distanceX, distanceY);
      }

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float absX = Math.abs(e2.getX() - e1.getX());
        float absY = Math.abs(e2.getY() - e1.getY());
        if (absX > absY) {
          Log.i("1", "fly open");
          openDataFrag();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
      }
    });
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP) {
      float x = event.getX();
      float y = event.getY();

      float absY = Math.abs(y - rawY);
      float absX = Math.abs(x - rawX);

      if (absX > absY) {
        Log.i("1", "cancel open");
        //openDataFrag(-1);
      }
    }
    return gestureDetector.onTouchEvent(event);
  }

  public void clickNappy(View view) {
    insert(BabyMotion.NAPPY, R.string.nappy);
  }

  public void clickPoo(View view) {
    insert(BabyMotion.POO, R.string.poo);
  }

  public void clickFeed(View view) {
    insert(BabyMotion.FEED, R.string.feed);
  }

  public void clickSleep(View view) {
    insert(BabyMotion.SLEEP, R.string.sleep);
  }

  public void clickWake(View view) {
    insert(BabyMotion.WAKE, R.string.wake);
  }

  private void insert(int type, @StringRes int idStr) {
    Disposable subscribe = Completable.create(emitter -> {
      BabyMotion babyMotion = new BabyMotion();
      babyMotion.setType(type);
      babyMotion.setName(getString(idStr));
      babyMotion.setTime(System.currentTimeMillis());
      babyMotionRepository.insert(babyMotion);
      emitter.onComplete();
    }).subscribeOn(Schedulers.io()).subscribe(() -> {
      int nonFinalType = type;
      if (type == BabyMotion.WAKE) {
        nonFinalType = BabyMotion.SLEEP;
      }
      openDataFrag(nonFinalType);
    });
    compositeDisposable.add(subscribe);
  }

  public void clickData(View view) {
    openDataFrag();
  }

  public static final String FRAG_STATISTIC = "FRAG_STATISTIC";

  private void openDataFrag() {
    openDataFrag(Integer.MIN_VALUE);
  }

  private void openDataFrag(int type) {
    runOnUiThread(() -> {

      FragmentManager fragmentManager = getSupportFragmentManager();
      Fragment fragment = fragmentManager.findFragmentByTag(FRAG_STATISTIC);
      if (fragment == null) {
        fragment = StatisticFragment.newInstance(type);
        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, FRAG_STATISTIC)
            .show(fragment)
            .addToBackStack(null)
            .commit();
      }

    });
  }

  @Override
  public BabyMotionRepository getRepo() {
    return babyMotionRepository;
  }

  @Override
  public void onPick(int motionType, BabyMotion babyMotion, int pos) {
    this.pickMotionType = motionType;
    this.pickMotion = babyMotion;
    this.pickPos = pos;
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getSupportFragmentManager(), TAG_DATE_PICKER);
  }

  @Override
  public void onDatePick(int year, int month, int day) {
    this.pickYear = year;
    this.pickMonth = month;
    this.pickDay = day;
    DialogFragment newFragment = new TimePickerFragment();
    newFragment.show(getSupportFragmentManager(), TAG_TIME_PICKER);
  }

  @Override
  public void onTimePick(int hourOfDay, int minute) {
    EventBus.getDefault().post(new OnPickEvent(pickYear, pickMonth, pickDay, hourOfDay, minute, pickMotion, pickPos));
  }

  //@Override
  //public boolean swipeBackPriority() {
  //  return getSupportFragmentManager().getBackStackEntryCount() < 1;
  //}
}
