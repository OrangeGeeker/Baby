package com.example.xadslyf.baby.statistic.stickyheader;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.xadslyf.baby.data.BabyMotion;

/**
 * Created by liuyifei on 2018/9/6.
 */
public class StickyHeaderEntity implements MultiItemEntity {

  private String headerText;
  private String date;
  private int count;
  private int itemType;
  private BabyMotion babyMotion;

  public StickyHeaderEntity(BabyMotion babyMotion) {
    this.itemType = Type.DATA;
    this.babyMotion = babyMotion;
  }

  public StickyHeaderEntity(String date, int count) {
    this.itemType = Type.HEADER;
    this.count = count;
    this.date =date;
    this.headerText = date + " >>> " + count+ " 次";

  }

  public boolean isData(){
    return itemType == Type.DATA;
  }


  @Override
  public int getItemType() {
    return itemType;
  }

  public BabyMotion getBabyMotion() {
    return babyMotion;
  }

  public int getCount() {
    return count;
  }

  public String getDate() {
    return date;
  }

  public String getHeaderText() {
    return headerText;
  }

  public static abstract class Type{
    public static final int HEADER = 1;
    public static final int DATA = 2;
  }
}
