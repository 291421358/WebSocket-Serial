package com.laola.websocket.controller;

import com.laola.websocket.util.WebSocket;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 第一次接触WebSocket
 * 误以为要用controller 连接服务上的webSocket ，可以delete
 */
@RestController
public class WebSocketController {

    @RequestMapping(value = "websocket")
    public void WebOpen() throws IOException {
        WebSocket ws = new WebSocket();
        JSONObject jo = new JSONObject();

        ws.onMessage("111");
    }
}
