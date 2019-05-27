package com.fantasy.app.pub.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Component;

/**
 * 向前台界面推送消息
 * @author 公众号：18岁fantasy
 *
 */
@Component
public class MessageManager {

	List<MessageWebSocket> mswList = new ArrayList<>();

	List<MessageSessionListener> sessionListeners = new ArrayList<>();
	
	long timeout = 5000;

	public MessageManager() {
		// 定时线程检查心跳
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
//				checkHeart();
				} catch( Throwable t ) {};
			}
		};
		Timer timer = new Timer();  
		timer.schedule( task, 0, timeout );
	}

	public void addSessionListener( MessageSessionListener l ) {
		this.sessionListeners.add( l );
	}
	
	public void removeSessionListener( MessageSessionListener l ) {
		this.sessionListeners.remove( l );
	}
	
	public void fireOnConnect( MessageWebSocket socket ) {
		for( MessageSessionListener l : sessionListeners ) {
			l.onConnect( socket );
		}
	}
	
	public void fireOndisconnect( MessageWebSocket socket ) {
		for( MessageSessionListener l : sessionListeners ) {
			l.onDisconnect( socket );
		}
	}
	
	public void checkHeart() {
		long time = System.currentTimeMillis() - timeout;
		List<MessageWebSocket> newMswList = new ArrayList<>();
		for( MessageWebSocket msw : mswList) {
			if( msw.getLastHeartTime() < time ) {
				msw.close();
			}
			if( !msw.isClosed() ){
				newMswList.add( msw );
				msw.postMessage( "test", "Hello");
			}
		}
		this.mswList = newMswList;
	}
	
	public void postMessage( String type, Object message ) {
		for( MessageWebSocket msw : mswList.toArray( new MessageWebSocket [ 0 ] ) ) {
			if( msw.isClosed() ) {
				continue;
			}
			msw.postMessage( type, message );
		}
	}

	public void add( MessageWebSocket msw ) {
		mswList.add( msw );
		fireOnConnect( msw );
	}

	public void remove( MessageWebSocket msw ) {
		mswList.remove( msw );
		fireOndisconnect( msw );
	}

}
