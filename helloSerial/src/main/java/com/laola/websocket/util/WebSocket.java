package com.laola.websocket.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.laola.websocket.SerialTest;
import gnu.io.SerialPort;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 功能和MyWebSocket相同，属于plan B
 * 若MyWebSocket出问题，使用该类
 */
//websocket的地址。前端连接时，
//地址为ws://'+host+'/webSocket/user1
@ServerEndpoint("/webSocket/{username}")
//使websock在spring boot中生效，交由spring 管理
@Component
public class WebSocket {
    //在线的客户端数量
    private static int onlineCount = 0;
    //客户端集合，连接时放入，key为username
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    //客户端用户名，连接时赋值
    private String username;

    /**
     * 打开websocket连接
     * @param username
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {

        this.username = username;
        this.session = session;
        // 添加一个在线客户端数量
        addOnlineCount();
        //将MyWebSocket对象置入clients
        clients.put(username, this);
        System.out.println("已连接");
    }

    /**
     * 关闭websocket连接
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
    }

    /**
     * 接收消息
     *处理业务
     *              在此处与串口发生通讯
     * @param message
     * @throws IOException MyWebSocket
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        //打印收到的数据
        System.out.println(message);
        //将收到的数据转换为json格式
        JSONObject jsonTo = JSONObject.fromObject(message);
        //将json格式数据转换为字符串
        String mes = (String) jsonTo.get("message");
        //新建连接串口帮助类对象 SerialTest
        SerialTest serialTest = new SerialTest();
        //连接串口并返回串口对象
        SerialPort serialPort = serialTest.startComPort();
        //从串口对象中获得输出流
        OutputStream outputStream = serialPort.getOutputStream();
        //串口命令 字符串
        String st = "E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00";
        //字符串转换成16进制字节码
        byte[] bytes = SerialTest.hexStrToBinaryStr(st);
        //发送字节码串口通讯业务完成
        outputStream.write(bytes, 0,bytes.length);

        //判断是否要发送给所有人
        if (!jsonTo.get("To").equals("All")) {
            sendMessageTo(mes, jsonTo.get("To").toString());
        } else {
            sendMessageAll("给所有人");
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 发送消息
     * @param message
     * @param To
     * @throws IOException
     */
    public void sendMessageTo(String message, String To) throws IOException {
        //关于getBasicRemote 与getAsyncRemote 的区别，在
        /** @throws MyWebSocket **/ //中有
        // session.getBasicRemote().sendText(message);
        //session.getAsyncRemote().sendText(message);
        for (WebSocket item : clients.values()) {
            if (item.username.equals(To))
                //getAsyncRemote 是异步请求
                item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 发送全体消息
     * @param message
     * @throws IOException
     */
    public void sendMessageAll(String message) throws IOException {
        //遍历clients
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 获取所有在线人数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人数自加
     */
    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    /**
     * 在线人数自减
     */
    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    /**
     * 获取所有客户端信息
     * @return
     */
    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }
}