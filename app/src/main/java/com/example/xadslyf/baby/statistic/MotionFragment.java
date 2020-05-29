package com.example.xadslyf.baby.statistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.xadslyf.baby.BaseFragment;
import com.example.xadslyf.baby.R;
import com.example.xadslyf.baby.data.BabyMotion;
import com.example.xadslyf.baby.data.BabyMotionRepository;
import com.example.xadslyf.baby.statistic.stickyheader.StickyHeaderEntity;
import com.example.xadslyf.baby.stickyitemdecoration.OnStickyChangeListener;
import com.example.xadslyf.baby.stickyitemdecoration.StickyHeadContainer;
import com.example.xadslyf.baby.stickyitemdecoration.StickyItemDecoration;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by liuyifei on 2018/9/5.
 */
public class MotionFragment extends BaseFragment {

  public static final String K_TYPE = "KEY_TYPE";
  private static final String TAG_DATE_PICKER = "TAG_DATE_PICKER";
  private RecyclerView mRecyclerView;
  private Callback callback;

  private List<StickyHeaderEntity> dataList;
  private MotionAdapter mAdapter;
  private CompositeDisposable compositeDisposable;
  private int motionType;
  private StickyHeadContainer shc;
  private TextView pinnedTextView;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callback = (Callback) getActivity();
  }

  public interface Callback {
    BabyMotionRepository getRepo();

    void onPick(int motionType, BabyMotion babyMotion, int pos);
  }

  @Override
  public void onDestroyView() {
    compositeDisposable.clear();
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    motionType = getArguments().getInt(K_TYPE);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View inflate = inflater.inflate(R.layout.fragment_motion, container, false);
    inflate.setOnTouchListener((v, event) -> true);

    mRecyclerView = inflate.findViewById(R.id.rv);
    shc = inflate.findViewById(R.id.shc);
    pinnedTextView = inflate.findViewById(R.id.tv_motion_header);
    compositeDisposable = new CompositeDisposable();
    setView();
    EventBus.getDefault().register(this);
    getDataSource();
    return inflate;
  }

  private void setView() {
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    StickyItemDecoration stickyItemDecoration =
        new StickyItemDecoration(shc, StickyHeaderEntity.Type.HEADER);

    stickyItemDecoration.setOnStickyChangeListener(new OnStickyChangeListener() {
      @Override
      public void onScrollable(int offset) {
        if (shc != null) {
          shc.scrollChild(offset);
          shc.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onInVisible() {
        if (shc != null) {
          shc.reset();
          shc.setVisibility(View.INVISIBLE);
        }
      }
    });
    mRecyclerView.addItemDecoration(stickyItemDecoration);

    mAdapter = new MotionAdapter(dataList);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
      clickDel(position);
    });

    mAdapter.setOnItemClickListener((adapter, view, position) -> {

      clickItem(position);
    });

    shc.setDataCallback(pos -> {
      String headerText = mAdapter.getData().get(pos).getHeaderText();
      if (pinnedTextView != null) {
        pinnedTextView.setText(headerText);
      }
    });
  }

  private SimpleDateFormat simpleDateFormat =
      new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());

  private void clickItem(int position) {
    StickyHeaderEntity stickyHeaderEntity = dataList.get(position);
    if (stickyHeaderEntity.isData()) {
      MaterialDialog materialDialog = new MaterialDialog(getActivity());
      materialDialog.title(R.string.confirm_modify, null)
          .message(null, simpleDateFormat.format(stickyHeaderEntity.getBabyMotion().getTime()))
          .positiveButton(R.string.modify, null, materialDialog1 -> {
            showPicker(stickyHeaderEntity.getBabyMotion(), position);
            return null;
          })
          .negativeButton(R.string.cancel, null, materialDialog12 -> null)
          .show();
    }
  }

  private void showPicker(BabyMotion babyMotion, int pos) {
    callback.onPick(motionType, babyMotion, pos);
  }

  private void clickDel(int position) {
    MaterialDialog materialDialog = new MaterialDialog(getActivity());
    StickyHeaderEntity stickyHeaderEntity = dataList.get(position);
    materialDialog.title(R.string.confirm_del, null)
        .message(null, stickyHeaderEntity.getBabyMotion().dateStr)
        .positiveButton(R.string.del, null, materialDialog1 -> {
          BabyMotion remove = dataList.remove(position).getBabyMotion();
          mAdapter.notifyItemRemoved(position);
          Completable.create(emitter -> {
            callback.getRepo().remove(remove);
          }).subscribeOn(Schedulers.io()).subscribe();
          return null;
        })
        .negativeButton(R.string.cancel, null, materialDialog12 -> null)
        .show();
  }

  private void getDataSource() {
    compositeDisposable.add(Completable.create(emitter -> {
      List<BabyMotion> motionList = getBabyMotions();
      //创建 StickyHeaderEntity;
      List<StickyHeaderEntity> entityList = new ArrayList<>();

      List<StickyHeaderEntity> tmpList = new ArrayList<>();

      String curDate = null;
      for (BabyMotion babyMotion : motionList) {
        babyMotion.dateStr = dateFormat.format(new Date(babyMotion.getTime()));
        if (curDate == null) {
          curDate = babyMotion.dateStr;
        }

        if (curDate.endsWith(babyMotion.dateStr)) {
          //相同日期的数据，放入list
          tmpList.add(new StickyHeaderEntity(babyMotion));
        } else {
          //不同日期
          collectOneDayData(entityList, tmpList, curDate);
          tmpList.clear();
          curDate = babyMotion.dateStr;
          tmpList.add(new StickyHeaderEntity(babyMotion));
        }
      }

      if (!tmpList.isEmpty()) {
        collectOneDayData(entityList, tmpList, curDate);
      }
      dataList = entityList;
      Log.i("1", "获取到数据");
      emitter.onComplete();
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
      mAdapter.setNewData(dataList);
    }));
  }

  private void collectOneDayData(List<StickyHeaderEntity> entityList,
      List<StickyHeaderEntity> tmpList, String curDate) {
    int count = tmpList.size();
    if (motionType == BabyMotion.SLEEP) {
      count /= 2;
    }
    entityList.add(new StickyHeaderEntity(curDate, count));
    entityList.addAll(tmpList);
  }

  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

  private List<BabyMotion> getBabyMotions() {
    List<BabyMotion> motionList;
    if (motionType == BabyMotion.SLEEP) {
      motionList = callback.getRepo().getByType(motionType, BabyMotion.WAKE);
    } else {
      motionList = callback.getRepo().getByType(motionType);
    }
    int size = motionList.size();
    for (int i = 0; i < size; i++) {
      BabyMotion babyMotion = motionList.get(i);
      if (i < size - 1) {
        BabyMotion preMotion = motionList.get(i + 1);
        if (babyMotion.getType() == BabyMotion.WAKE) {
          if (preMotion.getType() == BabyMotion.SLEEP) {
            babyMotion.setSleepTime(babyMotion.getTime() - preMotion.getTime());
          }
        } else if (babyMotion.getType() != BabyMotion.SLEEP) {
          babyMotion.setSleepTime(babyMotion.getTime() - preMotion.getTime());
        }
      }
    }
    return motionList;
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onDateTimePick(OnPickEvent event) {
    int type = event.babyMotion.getType();
    if (type == BabyMotion.WAKE) {
      type = BabyMotion.SLEEP;
    }
    if (type == motionType) {
      compositeDisposable.add(Completable.create(emitter -> {
        Calendar calendar = Calendar.getInstance();
        calendar.set(event.pickYear, event.pickMonth, event.pickDay, event.hourOfDay, event.minute);
        Date time = calendar.getTime();
        event.babyMotion.setTime(time.getTime());
        event.babyMotion.dateStr = dateFormat.format(time);
        callback.getRepo().update(event.babyMotion);
        emitter.onComplete();
      }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
        //mAdapter.notifyItemChanged(event.pos);
        getDataSource();
      }));
    }
  }
}
