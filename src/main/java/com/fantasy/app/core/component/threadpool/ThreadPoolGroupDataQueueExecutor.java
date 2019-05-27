package com.fantasy.app.core.component.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.exception.ServiceException;
import com.fantasy.app.core.util.HashSelect;
import com.fantasy.app.core.util.StrUtil;

/**
 * 数据分组，每个组一个线程按顺序执行数据解析
 * @author joe
 * @time  2016-4-7 下午12:28:15
 */
public class ThreadPoolGroupDataQueueExecutor {

	private static Logger logger = Log.getLogger(LogType.THREAD);
	final Map<Integer, BlockingQueue<Object>> dataGroups = new HashMap<Integer, BlockingQueue<Object>>();
	
	private String threadNamePrefix;
	private int groupSize;
	/**
	 * 初始化线程组
	 * @param groupSize
	 * @param threadNamePrefix
	 */
	public  ThreadPoolGroupDataQueueExecutor(int groupSize,String threadNamePrefix){
		if(groupSize>0){
			this.threadNamePrefix = threadNamePrefix;
			this.groupSize = groupSize;
			for (int i = 0; i < groupSize; i++) {
				ThreadPool.createPool(this.threadNamePrefix+"_"+i, 1, 1,0);
			}
		}
	}
	
	/**
	 * 执行
	 * @param waitGap
	 * @param dataParseCallBack
	 * @throws ServiceException
	 */
	public void startExecutor(final long waitGap,DataParseCallBack dataParseCallBack){
		for (int i = 0; i < groupSize; i++) {
			startExecutor(i, waitGap,dataParseCallBack);
		}
	}
	private void startExecutor(final int key,final long waitGap,final DataParseCallBack dataParseCallBack){
		final long gapsize = waitGap>0?waitGap:10000l;//10s
		try {
		    String tName = this.threadNamePrefix+"_"+key;
			ThreadPool.execute(tName,tName, new Runnable() {
				@Override
				public void run() {
					while(true){
						if(dataGroups.containsKey(key)){
							try {
								Object objToParse = dataGroups.get(key).poll(2,TimeUnit.SECONDS);
								if(objToParse!=null){
									dataParseCallBack.parse(objToParse);
								}
							} catch (Exception e) {
								logger.error("执行线程异常....",e);
							}
						}
						try {
							Thread.sleep(gapsize);
						} catch (InterruptedException e) {
						}
					}
				}

			});
		} catch (ServiceException e) {
			logger.error("执行线程异常....",e);
		}
	}
	public void destroy() throws ServiceException{
		for (int i = 0; i < groupSize; i++) {
			ThreadPool.destroy(this.threadNamePrefix+"_"+i);
		}
	}
	public void putObjectToDataQueue(String id,Object value){
		if(StrUtil.isNotBank(id)){
			Integer target = HashSelect.target(new String[]{id}, this.groupSize);
			if(!dataGroups.containsKey(target)){
				dataGroups.put(target, new LinkedBlockingQueue<Object>(Integer.MAX_VALUE));
			}
			dataGroups.get(target).add(value);
		}
	}
}
