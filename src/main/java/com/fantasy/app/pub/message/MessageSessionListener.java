package com.fantasy.app.pub.message;
/**
 * 消息处理
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:21:29
 */
public interface MessageSessionListener {
	void onConnect( MessageWebSocket webSocket );
	void onDisconnect( MessageWebSocket webSocket );
}
