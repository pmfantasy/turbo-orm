package com.fantasy.app.core.component.threadpool;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.exception.ServiceException;
import com.fantasy.app.core.util.DateUtil;

/**
 * 线程池
 * @author 公众号：18岁fantasy
 * @2015-3-3 @上午10:43:23
 */
public class ThreadPool {

    static Logger logger = org.apache.log4j.Logger.getLogger(LogType.THREAD);
	
	private static ConcurrentHashMap<String, ThreadPoolTaskExecutor> concurrentHashMap = new ConcurrentHashMap<String, ThreadPoolTaskExecutor>();
	
	/**
	 * 创建线程池
	 * @param poolType 线程池名称，
	 * @param xms 线程池最小值
	 * @param xmx 线程池最大值
	 * @param cache 线程池缓存数，当达到xms时，任务会放入cache中，当cache满后再使用xmx参数。
	 */
	public static  void createPool(String poolType,int xms,int xmx,int cache){
		if(concurrentHashMap.get(poolType)==null){
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setThreadNamePrefix(poolType);
			executor.setWaitForTasksToCompleteOnShutdown(false);
	        executor.setCorePoolSize(xms);
	        executor.setMaxPoolSize(xmx);
	        executor.setQueueCapacity(cache);
	        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        executor.initialize();
	        concurrentHashMap.put(poolType, executor);
		}
	}
	/**
	 * 创建线程池
	 * @param poolType 线程池名称，
	 * @param xms 线程池最小值
	 * @param xmx 线程池最大值
	 */
	public static  void createPool(String poolType,int xms,int xmx){
		createPool(poolType, xms, xmx, Integer.MAX_VALUE);
	}
	/**
	 * 打印单个线程处理详情
	 * @param poolType
	 * @return
	 */
	public static String printPoolStatic(String poolType){
	    StringBuffer print = new StringBuffer();
		ThreadPoolTaskExecutor pool = getPool(poolType);
		ThreadPoolExecutor work = pool.getThreadPoolExecutor();
		print.append("线程池名称："+poolType+
				"/活动线程数："+work.getActiveCount()+
				"/排队线程数："+work.getQueue().size());
		BlockingQueue<Runnable> q = work.getQueue();
        Object[] rs = q.toArray();
        Date now = new Date();
        if(rs!=null&&rs.length!=0) {
          print.append("任务排队详情:\r\n");
          for (int i = 0; i < rs.length; i++) {
            NameRunnable nameRunnable = (NameRunnable)rs[i];
            Date submitTime = nameRunnable.submitTime();
            long waitTime = (now.getTime()-submitTime.getTime());//mill second
            print.append(nameRunnable.getTaskName()).append(",提交时间："+DateUtil.toDefaultDateTimeStr(nameRunnable.submitTime())).append("，已经等待："+waitTime+" 毫秒.").append("\r\n");
          }
        }
        return print.toString();
	}
	/**
	 * 打印所有线程池处理情况
	 * @return
	 */
	public static String printAllPoolStatic(){
		StringBuffer printAll = new StringBuffer();
		for (Iterator<String> iterator = concurrentHashMap.keySet().iterator(); iterator.hasNext();) {
			String poolType = iterator.next();
			printAll.append(printPoolStatic(poolType));
			if(iterator.hasNext()){
			  printAll.append("\r\n");
			}
		}
		return  printAll.toString();
	}
	
	
	/**
	 * 执行任务
	 * @param desc 任务描述
	 * @param poolType 线程池名称
	 * @param task
	 * @throws ServiceException
	 */
	public static  void execute(String desc,String poolType,Runnable task) throws ServiceException{
	    
		ThreadPoolTaskExecutor executor = getPool(poolType);
		if(executor==null){
			throw new ServiceException(poolType+" 线程池未初始化，请使用createPool方法初始化");
		}
		logger.info("添加了任务："+desc);
		executor.execute(new NameRunnable() {
		  private Date submitTime =  new Date();
          @Override
          public void run() {
            task.run();
          }
          @Override
          public Date submitTime() {
            return submitTime;
          }
          @Override
          public String getTaskName() {
            return desc;
          }
        });
	}
	
	private static ThreadPoolTaskExecutor getPool(String poolType){
      return concurrentHashMap.get(poolType);
    }
	public static  void destroy(String poolType) throws ServiceException{
		ThreadPoolTaskExecutor executor = getPool(poolType);
		if(executor==null){
			throw new ServiceException(poolType+" 线程池未初始化，请使用createPool方法初始化");
		}
		executor.destroy();
	}
	
	
	
	
	//------------测试
	private static void testPool() throws Exception{
	    	ThreadPool.createPool("jsm-custom", 10, 10, Integer.MAX_VALUE);
	    	for (int i = 0; i < 20; i++) {
	    		String id = RandomUtils.nextInt(1)+"";
	    		//Thread.sleep(500);
    			ThreadPool.execute("task"+i,"jsm-custom", getTask(id));
			}
	    	System.out.println("初始化线程结束...");
	 }
    public static Runnable getTask(final String num){
    	return new Runnable() {
			@Override
			public void run() {
				 try {
	                    Thread.sleep(2000);
	                    System.out.println("执行完"+num);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
				
			}
		};
    }
    
   public static void main(String[] args) throws Exception {
  	    testPool();
  	    Thread.sleep(1000);
  	    System.out.println(ThreadPool.printPoolStatic("jsm-custom"));
	}
}
