package com.example.wenda.service;

import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;


    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFollweeKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFollwerKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, offset, count));
    }


    public List<Integer> getFollowees(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowees(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, count));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }
    public long getFolloweeCount(int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId))!=null;
    }
}