package com.fantasy.app.core.util;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.ResultVo;
/**
 * 定时任务util
 * @author 公众号：18岁fantasy
 * @date 2017年9月28日 下午5:01:44
 */
public class TimerUtil {

  /**
   * 尝试一定次数去执行任务
   * @param desc 任务描述
   * @param logger 日志记录器
   * @param times 尝试次数
   * @param runnable
   * @return 返回成功失败结果
   */
  public static ResultVo tryTimes(String desc,Logger logger, int times,Runnable runnable) {
    int currentTimes = 0;
    boolean success = false;
    ResultVo resultVo = new ResultVo();
    while(!success&&(currentTimes<times)) {
      try {
        currentTimes++;
        runnable.run();
        success = true;
        logger.error(desc+" 成功...");
      } catch (Exception e) {
        success = false;
        logger.error(desc+" 失败,"+(currentTimes<times?"即将第"+(currentTimes+1)+"次尝试...":"尝试次数达到上限"+times+",退出尝试..."),e);
      }
    }
    resultVo.setSuccess(success);
    return resultVo;
  }
  static Logger logger = Logger.getLogger(TimerUtil.class);
  public static void main(String[] args) {
    System.out.println(tryTimes("测试任务", logger, 3, new Runnable() {
      
      @Override
      public void run() {
        try {
          System.out.println(1/0);
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          throw new RuntimeException("业务失败...");
        }
      }
    }).isSuccess());
  }
}
