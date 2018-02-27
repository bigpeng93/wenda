package com.example.wenda.async;

import java.util.List;

public interface EventHandler {

    //处理事件
    void doHandle(EventModel model);

    //注册处理事件的类型
    List<EventType> getSupportEventTypes();
}
