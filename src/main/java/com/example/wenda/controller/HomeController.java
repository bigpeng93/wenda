package com.example.wenda.controller;

import com.example.wenda.model.*;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.FollowService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getQuestion(userId,0,10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user",user);
        vo.set("commentCount",commentService.getUserCommentCount(userId));
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("followeeCount",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));


        List<Integer> followeeIds = followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser() !=null){
            vo.set("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId));
            model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(),followeeIds));
        }else {
            vo.set("followed",false);
            model.addAttribute("followees",getUserInfo(0,followeeIds));
        }
        model.addAttribute("profileUser",vo);

        return "profile";
    }

    @RequestMapping(path = {"/index","/"},method = {RequestMethod.GET})
    public String index(Model model){
        model.addAttribute("vos",getQuestion(0,0,10));
        return "index";
    }

    private List<ViewObject> getQuestion(int userId, int offset,int limit){
        List<Question> questionList = questionService.getLatestQuestion(userId,offset,limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
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
            vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
            vo.set("followeeCount",followService.getFolloweeCount(uid,EntityType.ENTITY_USER));
            vo.set("commentCount",commentService.getUserCommentCount(uid));
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
