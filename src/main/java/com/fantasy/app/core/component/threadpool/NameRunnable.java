package com.fantasy.app.core.component.threadpool;

import java.util.Date;

/**
 * 带任务名字的任务
 * @author 公众号：18岁fantasy
 * @date 2017年11月3日 上午11:10:22
 */
public interface  NameRunnable extends Runnable{

  /**
   * 任务名称
   * @return
   */
  public  String getTaskName();
  /**
   * 任务提交时间
   * @return
   */
  public  Date  submitTime();
}
