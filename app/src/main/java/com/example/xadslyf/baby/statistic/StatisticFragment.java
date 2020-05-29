package com.example.xadslyf.baby.statistic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.xadslyf.baby.BaseFragment;
import com.example.xadslyf.baby.R;
import com.example.xadslyf.baby.data.BabyMotion;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import io.reactivex.disposables.CompositeDisposable;
import me.yokeyword.swipebackfragment.SwipeBackLayout;

/**
 * Created by liuyifei on 2018/9/5.
 */
public class StatisticFragment extends BaseFragment {

  private static final String K_INDEX = "K_INDEX";
  private CompositeDisposable compositeDisposable;
  ViewPager mViewPager;
  SmartTabLayout smartTabLayout;
  private static int curIndex;

  public static StatisticFragment newInstance(int type) {

    int index = getIndex(type);

    Bundle args = new Bundle();
    args.putInt(K_INDEX, index);
    StatisticFragment fragment = new StatisticFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle arguments = getArguments();
    if (arguments != null) {

      int anInt = arguments.getInt(K_INDEX);
      if (anInt != Integer.MIN_VALUE) {
        curIndex = anInt;
      }
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View inflate = inflater.inflate(R.layout.fragment_statistic, container, false);
    inflate.setOnTouchListener((v, event) -> true);
    mViewPager = inflate.findViewById(R.id.vp);
    smartTabLayout = inflate.findViewById(R.id.pager_title_strip);
    compositeDisposable = new CompositeDisposable();
    setView();
    getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_ALL); // EDGE_LEFT(默认),EDGE_ALL
    return attachToSwipeBack(inflate);
  }

  private void setView() {
    FragmentManager childFragmentManager = getChildFragmentManager();
    FragmentPagerItemAdapter fragmentPagerItemAdapter =
        new FragmentPagerItemAdapter(childFragmentManager, FragmentPagerItems.with(getActivity())
            .add("睡眠", MotionFragment.class,
                new Bundler().putInt(MotionFragment.K_TYPE, BabyMotion.SLEEP).get())
            .add("喂奶", MotionFragment.class,
                new Bundler().putInt(MotionFragment.K_TYPE, BabyMotion.FEED).get())
            .add("便便", MotionFragment.class,
                new Bundler().putInt(MotionFragment.K_TYPE, BabyMotion.POO).get())
            .add("尿布", MotionFragment.class,
                new Bundler().putInt(MotionFragment.K_TYPE, BabyMotion.NAPPY).get())
            .create());

    mViewPager.setOffscreenPageLimit(1);
    mViewPager.setAdapter(fragmentPagerItemAdapter);
    smartTabLayout.setViewPager(mViewPager);
    mViewPager.setCurrentItem(curIndex, true);
    smartTabLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        curIndex = position;
      }
    });
  }

  private static int getIndex(int type) {
    int curIndex = type;
    if (type == BabyMotion.FEED) {
      curIndex = 1;
    } else if (type == BabyMotion.POO) {
      curIndex = 2;
    } else if (type == BabyMotion.NAPPY) {
      curIndex = 3;
    } else if (type == BabyMotion.WAKE || type == BabyMotion.SLEEP) {
      curIndex = 0;
    }
    return curIndex;
  }
}
