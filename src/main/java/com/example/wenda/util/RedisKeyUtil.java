package com.example.wenda.util;

public class RedisKeyUtil {
    private static String SPLIT=":";

    //点赞
    private static String BIZ_LIKE="LIKE";

    //踩
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENTQUEUE="EVENT_QUEUE";

    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    //统计user的点赞数量
    private static String BIZ_USERLIKE = "USERLIKE";

    //redis中点赞的集合的key值
    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE + SPLIT +String.valueOf(entityType) + SPLIT+String.valueOf(entityId);
    }

    //redis中踩的集合的key值
    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE + SPLIT +String.valueOf(entityType) + SPLIT+String.valueOf(entityId);
    }

    //redis中事件队列的key值
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    //redis中关注者的key值
    //可以获取question的关注者集合
    public static String getFollwerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT +String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    //redis中被关注者的key值
    //可以获取用户的关注question的集合
    public static String getFollweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE + SPLIT +String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    }

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

    public static String getUserLikeKye(int entityType,int userId){
        System.out.println(BIZ_USERLIKE+SPLIT +String.valueOf(entityType) + SPLIT + String.valueOf(userId));
        return BIZ_USERLIKE+SPLIT +String.valueOf(entityType) + SPLIT + String.valueOf(userId);
    }

}
