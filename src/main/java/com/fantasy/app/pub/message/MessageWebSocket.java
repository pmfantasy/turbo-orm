package com.fantasy.app.pub.message;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fantasy.app.core.base.CmCommon;
import com.fantasy.app.core.util.JsonUtil;

/**
 * websocket处理
 * 用于向前台界面推送消息的WebSocket
 * @author 公众号：18岁fantasy
 *
 */
@ServerEndpoint(value = "/messageSocket")
public class MessageWebSocket {

	Session session;

	long lastHeartTime;

	MessageManager manager;

	public MessageWebSocket() {
		this.lastHeartTime = System.currentTimeMillis();
		manager = CmCommon.getBean( MessageManager.class );
	}


	public long getLastHeartTime() {
		return lastHeartTime;
	}

	public boolean isClosed() {
		return !session.isOpen();
	}
	
	public void close() {
		if( this.session == null ) this.onClose();
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void postMessage( String name, Object messageContent ) {
		HashMap<String, Object> message = new HashMap<>();
		message.put( "name", name );
		message.put( "content", messageContent );
		message.put( "time", System.currentTimeMillis() );
		session.getAsyncRemote().sendText( JsonUtil.toJson( message ) );
	}

	@OnOpen
	synchronized
	public void onOpen(Session session ){
		this.session = session;
		manager.add( this );
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		System.out.println( "消息WebSocket已经断开！" );
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
//		System.out.println("来自客户端的消息:" + message);
		if( message.equals( "heart" ) ) {
			lastHeartTime = System.currentTimeMillis();
		}
	}

	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		manager.remove( this );
	}
}
