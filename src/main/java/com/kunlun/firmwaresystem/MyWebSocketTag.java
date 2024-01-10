package com.kunlun.firmwaresystem;


import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class MyWebSocketTag extends WebSocketServer {
    private static MyWebSocketTag webSocket;
    private Map<String, WebSocket> connectlist;

    public static MyWebSocketTag getWebSocket() {
        if (webSocket == null) {
            webSocket = new MyWebSocketTag(8089);
        }
        return webSocket;
    }

    private MyWebSocketTag(int port) {
        super(new InetSocketAddress(port));
        connectlist = new HashMap<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("onOpen" + webSocket.toString());

    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        System.out.println("连接" + key.toString());
        return super.onConnect(key);

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("断开连接" + webSocket.getResourceDescriptor());
        connectlist.remove(s);

    }
    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("接收消息" + s);
        for(String key:connectlist.keySet()){
            WebSocket webSocket1=connectlist.get(key);
            if(webSocket1==webSocket){
                connectlist.remove(key);
                break;
            }
        }
      //  webSocket.send("收到了");
        connectlist.put(s, webSocket);
    }
    @Override
    public void onError(WebSocket webSocket, Exception e) {
    }
    @Override
    public void onStart() {
    }
    public void sendData(String key, String msg) {
    //   System.out.println("触发发送");
        WebSocket webSocket = connectlist.get(key);
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(msg);
           // System.out.println("发送和");
        }
    }
}