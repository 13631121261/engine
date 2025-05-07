package com.kunlun.firmwaresystem;


import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class MyWebSocketTag extends WebSocketServer {
    private static MyWebSocketTag webSocket;
    private Map<String, WebSocket> connectlist;
    private List<String> keys;

    public static MyWebSocketTag getWebSocket() {
        if (webSocket == null) {
            webSocket = new MyWebSocketTag(7089);
        }
        return webSocket;
    }

    private MyWebSocketTag(int port) {
        super(new InetSocketAddress(port));
        connectlist = new HashMap<>();
        keys= new ArrayList<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        println("onOpen" + webSocket.toString());

    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        println("连接" + key.toString());
        return super.onConnect(key);

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        println("断开连接" + webSocket.getResourceDescriptor());
        connectlist.remove(s);
        keys.remove(s);

    }
    @Override
    public void onMessage(WebSocket webSocket, String s) {
        println("接收消息" + s);
        for(String key:connectlist.keySet()){
            WebSocket webSocket1=connectlist.get(key);
            if(webSocket1==webSocket){
                connectlist.remove(key);
                break;
            }
        }
        println("返回数据");
        keys.add(s);
        connectlist.put(s, webSocket);
    }
    @Override
    public void onError(WebSocket webSocket, Exception e) {
        println("异常启动");
    }
    @Override
    public void onStart() {
        //println("正常启动");
    }
    public void sendData(String map_key, String msg) {
     //  println("触发发送"+map_key);
        for(String key:keys){
            if(key.contains(map_key)){
                WebSocket webSocket = connectlist.get(key);
                if (webSocket != null && webSocket.isOpen()) {
                    webSocket.send(msg);
                 //   println("位置推送到网页");
                }
            }
        }

    }
}