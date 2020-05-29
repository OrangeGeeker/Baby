package com.example.xadslyf.baby.statistic;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xadslyf.baby.R;
import com.example.xadslyf.baby.data.BabyMotion;
import com.example.xadslyf.baby.statistic.stickyheader.StickyHeaderEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.xadslyf.baby.helper.TimeHelper.ms2HMS;

/**
 * Created by liuyifei on 2018/9/5.
 */
public class MotionAdapter extends BaseMultiItemQuickAdapter<StickyHeaderEntity, BaseViewHolder> {

  private SimpleDateFormat simpleDateFormat =
      new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());

  /**
   * Same as QuickAdapter#QuickAdapter(Context,int) but with
   * some initialization data.
   *
   * @param data A new list is created out of this one to avoid mutable list
   */
  public MotionAdapter(List<StickyHeaderEntity> data) {
    super(data);
    addItemType(StickyHeaderEntity.Type.HEADER, R.layout.motion_header);
    addItemType(StickyHeaderEntity.Type.DATA, R.layout.motion_holder);
  }

  //@Override
  //protected void convert(BaseViewHolder helper, BabyMotion item) {
  //
  //}

  @Override
  protected void convert(BaseViewHolder helper, StickyHeaderEntity item) {
    switch (helper.getItemViewType()) {
      case StickyHeaderEntity.Type.HEADER:
        helper.setText(R.id.tv_motion_header, item.getHeaderText());
        break;
      case StickyHeaderEntity.Type.DATA:
        String dataText = item.getBabyMotion().getName() + ": " + simpleDateFormat.format(
            new Date(item.getBabyMotion().getTime()));
        if (item.getBabyMotion().getType() == BabyMotion.WAKE) {
          dataText = dataText + " | 睡了 " + ms2HMS((int) item.getBabyMotion().getSleepTime());
        }else if(item.getBabyMotion().getType() != BabyMotion.SLEEP){
          dataText = dataText + " | 距上次 " + ms2HMS((int) item.getBabyMotion().getSleepTime());
        }
        helper.setText(R.id.tv_motion_holder, dataText).addOnClickListener(R.id.iv_motion_del);
        break;
    }
  }
}
