package com.example.wenda.controller;

import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventProducer;
import com.example.wenda.async.EventType;
import com.example.wenda.model.*;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.FollowService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {

    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;


    @RequestMapping(path = {"/followUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }


    @RequestMapping(path = {"/unfollowUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/followQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Question q = questionService.getById(questionId);
        if(q == null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwnerId(q.getUserId()));

        Map<String,Object> info = new HashMap<String,Object>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFolloweeCount(EntityType.ENTITY_QUESTION,questionId));
        return WendaUtil.getJSONString(ret ? 0:1,info);
    }


    @RequestMapping(path = {"/unfollowQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.getById(questionId);
        if(q == null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwnerId(q.getUserId()));

        Map<String,Object> info = new HashMap<String,Object>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFolloweeCount(EntityType.ENTITY_QUESTION,questionId));

        return WendaUtil.getJSONString(ret ? 0:1,info);
    }

    @RequestMapping(path = {"/user/{uid}/followees"},method = {RequestMethod.GET})
    public String followees(Model model,
                            @RequestParam("uid") int userId){

        List<Integer> followeeIds= followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(),followeeIds));

        }else {
            model.addAttribute("followees",getUserInfo(0,followeeIds));
        }
        return "followees";
    }

    @RequestMapping(path = {"/user/{uid}/followers"},method = {RequestMethod.GET})
    public String followers(Model model,
                            @RequestParam("uid") int userId){

        List<Integer> followeeIds= followService.getFollowers(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followers",getUserInfo(hostHolder.getUser().getId(),followeeIds));

        }else {
            model.addAttribute("followers",getUserInfo(0,followeeIds));
        }
        return "followers";
    }

    private List<ViewObject> getUserInfo(int localUserId,List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for(Integer uid:userIds){
            User user = userService.getUser(uid);
            if(user == null){
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user",user);
            vo.set("followerCount",followService.getFolloweeCount(EntityType.ENTITY_USER,uid));
            if(localUserId !=0){
                vo.set("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,uid));
            }else{
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
