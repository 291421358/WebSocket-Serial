package com.laola.websocket.util;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//websocket的地址。前端连接时，
//地址为ws://'+host+'/webSocket/user1
@ServerEndpoint("/websocket/{username}")
//使websock在spring boot中生效，交由spring 管理
@Component
public class MyWebSocket {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //客户端用户名，连接时赋值
    private String username;
    //客户端集合，连接时放入，key为username
    private static Map<String, MyWebSocket> clients = new ConcurrentHashMap<String, MyWebSocket>();

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {

        this.session = session;
        this.username = username;
        //将MyWebSocket对象置入clients
        clients.put(username, this);

    }

    /**
     * 连接关闭调用的方法
     * 关闭websocket
     */
    @OnClose
    public void onClose() {

    }

    /**
     * 收到消息
     * 可以再次处理业务
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, Session session) {


        System.out.println("来自浏览器的消息:" + message);
        //群发消息 在开启连接时 ，曾将每一个MyWebSocket（客户端连接信息）置入 clients，此时，遍历clients，发送消息给所有人
        for (MyWebSocket item : clients.values()) {
            try {
                //调用发送消息方法
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用，java后端代码
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息给前端
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        /**getAsyncRemote是非阻塞式的，getBasicRemote是阻塞式的
                     getAsyncRemote()   和getBasicRemote()确实是异步与同步的区别，大部分情况下，
         推荐使用     getAsyncRemote()。 **/
        this.session.getAsyncRemote().sendText(message);//异步
        /** 由于getBasicRemote()的同步特性，并且它支持部分消息的发送即sendText(xxx,boolean isLast). isLast
        的值表示是否一次发送消息中的部分消息，对于如下情况:
         session.getBasicRemote().sendText(message, false);
         session.getBasicRemote().sendBinary(data);
         session.getBasicRemote().sendText(message, true);
        由于同步特性，第二行的消息必须等待第一行的发送完成才能进行，而第一行的剩余部分消息要等第二行发送完才能继续发送，
         所以在第二行会抛出IllegalStateException异常。
        如果要使用      getBasicRemote()  同步发送消息，则避免尽量一次发送全部消息，使用部分消息来发送。**/
        //this.session.getBasicRemote().sendText(message);//同步
    }


}
