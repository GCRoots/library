package com.example.library.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.library.pojo.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Comments {

//    评论
    @RequestMapping(value = "/comments/comment", method = RequestMethod.POST)
    public String comment(@RequestBody JSONObject json) throws IOException {
        Datas datas= JSON.parseObject(json.toString(), Datas.class);

        String content=datas.getContent();
        String uuid=datas.getUuid();
        String userName=datas.getUserName();
        String title=datas.getTitle();

        SqlSession session= SQL.CreatSqlSession();

        if (content.length()>0){
            Comment comment=new Comment();
            comment.setContent(content);
            comment.setTitle(title);
            comment.setUuid(uuid);
            comment.setUserName(userName);
            session.selectOne("insertIntoComment",comment);
            comment=session.selectOne("selectByConnent",content);
            String cid=comment.getCid();

            User user=session.selectOne("selectByUuid",uuid);
            if (user.getCid().length()>0){
                user.setCid(user.getCid()+","+cid);
            }else{
                user.setCid(user.getCid()+cid);
            }
            session.selectOne("updataByUuid",user);

            Book book=session.selectOne("selectByTitleInBooks");
            if (book.getCid().length()>0){
                book.setCid(book.getCid()+","+cid);
            }else{
                book.setCid(book.getCid()+cid);
            }
            session.selectOne("updataByTitleInBooks",book);

        }else{
            session.close();
            return "评论失败，请输入评论内容！！";
        }
        session.close();
        return "评论成功";
    }


//    评分
    @RequestMapping(value = "/comments/score", method = RequestMethod.POST)
    public String score(@RequestBody JSONObject json) throws IOException {
        Datas datas= JSON.parseObject(json.toString(), Datas.class);

        String uuid=datas.getUuid();
        String title=datas.getTitle();
        String score=datas.getScore();

        SqlSession session= SQL.CreatSqlSession();


        Score score1=new Score();
        score1.setUuid(uuid);
        score1.setTitle(title);
        score1.setScore(score);

        if (session.selectOne("selectByUuidAndTitleInScore",score1)!=null){
            return "评分失败，您已为此书评分";
        }else {
            if (Integer.parseInt(score) > 0 || Integer.parseInt(score) <= 10) {

                session.selectOne("insertToScore", score1);

                Book book = session.selectOne("selectByTitleInBooks", title);
                String persons = book.getPersons();
                String scores = book.getScores();
                scores = String.valueOf(Integer.parseInt(scores) + Integer.parseInt(score));
                persons = String.valueOf(Integer.parseInt(persons) + 1);
                score = String.valueOf(Double.parseDouble(scores) / Double.parseDouble(persons));
                book.setScores(scores);
                book.setPersons(persons);
                book.setScore(score);
                book.setTitle(title);
                session.selectOne("updataByTitleInBooks", book);

            } else {
                session.close();
                return "评分失败，请输入分数！！";
            }
        }
        session.close();

        return "评分成功";
    }

//    查看评论
    @RequestMapping(value = "/comments/lookCommentByTitle", method = RequestMethod.POST)
    public String lookCommentByTitle(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String tltle=datas.getTitle();
        SqlSession session= SQL.CreatSqlSession();

        Book book=session.selectOne("selectByTitleInBooks",tltle);
        String cids=book.getCid();
        String cid[]=cids.split(",");
        List<Comment> list=new ArrayList<>();
        for (int i=0;i<cid.length;i++){
            Comment comment=session.selectOne("selectByCid",cid[i]);
            list.add(comment);
        }

        session.close();
        String data= JSON.toJSONString(list);
        return data;
    }

    @RequestMapping(value = "/comments/lookCommentByUser", method = RequestMethod.POST)
    public String lookCommentByUser(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String uuid=datas.getUuid();
        SqlSession session= SQL.CreatSqlSession();

        User user=session.selectOne("selectByUuid",uuid);
        String cids=user.getCid();
        String cid[]=cids.split(",");
        List<Comment> list=new ArrayList<>();

        for (int i=0;i<cid.length;i++){
            Comment comment=session.selectOne("selectByCid",cid[i]);
            list.add(comment);
        }

        session.close();
        String data= JSON.toJSONString(list);
        return data;
    }

//    查看分数
    @RequestMapping(value = "/comments/lookScoreByTitle", method = RequestMethod.POST)
    public String lookScoreByTitle(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String title=datas.getTitle();
        SqlSession session= SQL.CreatSqlSession();
        Book book=session.selectOne("selectByTitleInBooks",title);
        String score=book.getScore();

        session.close();
        return score;
    }

    @RequestMapping(value = "/comments/lookScoreByUser", method = RequestMethod.POST)
    public String lookScoreByUser(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String uuid=datas.getUuid();
        String title=datas.getTitle();
        SqlSession session= SQL.CreatSqlSession();
        Score score=new Score();
        score.setUuid(uuid);
        score.setTitle(title);
        score=session.selectOne("selectByUuidAndTitleInScore",score);
        String score1=score.getScore();

        session.close();
        return score1;
    }


//    修改用户对书籍评论和评分
    @RequestMapping(value = "/comments/alterComment", method = RequestMethod.POST)
    public String alterComment(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String uuid=datas.getUuid();
        String title=datas.getTitle();
        String content=datas.getContent();
        SqlSession session= SQL.CreatSqlSession();

        Comment comment=new Comment();
        comment.setTitle(title);
        comment.setUuid(uuid);
        comment.setContent(content);
        session.selectOne("updataByUuidAndTitleInComment",comment);

        session.close();
        return "修改成功";
    }

    @RequestMapping(value = "/comments/alterScore", method = RequestMethod.POST)
    public String alterScore(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String title=datas.getTitle();
        String uuid=datas.getUuid();
        String score=datas.getScore();
        SqlSession session= SQL.CreatSqlSession();

        Score score1=new Score();
        score1.setTitle(title);
        score1.setUuid(uuid);
        score1.setScore(score);
        session.selectOne("updataByUuidAndTitle",score1);

        session.close();
        return "修改成功";
    }


//    删除评论
    @RequestMapping(value = "/comments/deleteComment", method = RequestMethod.POST)
    public String deleteComment(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String cid=datas.getCid();
        SqlSession session= SQL.CreatSqlSession();

        session.selectOne("deleteByCid",cid);

        session.close();
        return "删除成功";
    }


}
