package com.example.wenda.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 当时事件发生的现场
 */
public class EventModel {
    private EventType type;     //事件的类型
    private int actorId;        //事件触发者
    private int entityType;     //entity:事件触发的载体
    private int entityId;       //
    private int entityOwnerId;  //事件的关联者

    public EventModel(EventType type){
        this.type = type;
    }

    public EventModel(){

    }

    private Map<String,String> exts = new HashMap<String,String>();

    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;

    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;

    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;

    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;

    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
