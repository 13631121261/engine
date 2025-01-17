package com.kunlun.firmwaresystem;


import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import javax.websocket.*;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class MyWebSocket extends WebSocketServer {
    private static MyWebSocket webSocket;
    private Map<String, WebSocket> connectlist;

    public static MyWebSocket getWebSocket() {
        if (webSocket == null) {
            webSocket = new MyWebSocket(7088);
        }
        return webSocket;
    }

    private MyWebSocket(int port) {
        super(new InetSocketAddress(port));
        connectlist = new HashMap<>();
    }

    @Override
    public void onOpen(org.java_websocket.WebSocket webSocket, ClientHandshake clientHandshake) {
        println("onOpen" + webSocket.toString());

    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        println("连接" + key.toString());
        return super.onConnect(key);

    }

    @Override
    public void onClose(org.java_websocket.WebSocket webSocket, int i, String s, boolean b) {
        println("断开连接" + webSocket.getResourceDescriptor());
        connectlist.remove(s);

    }

    @Override
    public void onMessage(org.java_websocket.WebSocket webSocket, String s) {
        println("接收消息" + s);
      //  webSocket.send("收到了");
        connectlist.put(s, webSocket);
    }

    @Override
    public void onError(org.java_websocket.WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }

    public void sendData(String key, String msg) {
      //  println("触发发送MyWebSocket");
        WebSocket webSocket = connectlist.get(key);
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(msg);
            println("发送和");
        }

    }
}