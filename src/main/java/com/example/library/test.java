package com.example.library;

import com.alibaba.fastjson.JSON;
import com.example.library.pojo.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException {

        String uuid= "1";
        String password="123456";
        String newpassword="123456789";
        String passwordAgain="123456789";

        SqlSession session= SQL.CreatSqlSession();

        User user=session.selectOne("selectByUuid",uuid);
        String pw=user.getPassword();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            password = Hex.encodeHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (password.equals(pw)){
            if (newpassword.equals(passwordAgain)){
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] digest = md.digest(passwordAgain.getBytes());
                    password = Hex.encodeHexString(digest);
                    user.setPassword(password);
                    session.selectOne("updataByUuid",user);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


            }else {
                session.close();
                System.out.println("新密码两次输入不一致，请重新输入！！！");
            }
        }else {
            session.close();
            System.out.println( "原密码错误，请重新输入！！！");
        }

        session.close();
        System.out.println("密码修改成功");




//        String userName="e";
//        String password="123456789abc";
//        SqlSession session= SQL.CreatSqlSession();
//
//
//        if (session.selectOne("selectByUserName",userName)==null) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("MD5");
//                byte[] digest = md.digest(password.getBytes());
//                password = Hex.encodeHexString(digest);
//                //使用的是cc中带的Hex需要转换为十六进制
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//
//            User user = new User();
//            user.setUserName(userName);
//            user.setPassword(password);
//            session.selectOne("insertToUsers", user);
//        }else {
//            session.close();
//            System.out.println("用户名已存在！！！");
//        }
//
//        session.close();
//        System.out.println("账号建立成功");

//        String src="123456";
//        try {
//            MessageDigest md=MessageDigest.getInstance("MD5");
//            byte[] digest = md.digest(src.getBytes());
//            System.out.println(digest);
//            System.out.println("JDK MD5: "+ Hex.encodeHexString(digest));
//            //使用的是cc中带的Hex需要转换为十六进制
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }




//        String tltle="Java学习笔记";
//
//        SqlSession session= SQL.CreatSqlSession();
//
//        Book book=session.selectOne("selectByTitleInBooks",tltle);
//
//        String cids=book.getCid();
//        String cid[]=cids.split(",");
//
//        List<Comment> list=new ArrayList<>();
//        for (int i=0;i<cid.length;i++){
//            Comment comment=session.selectOne("selectByCid",cid[i]);
//            list.add(comment);
//        }
//
//        session.close();
//        String data= JSON.toJSONString(list);
//        System.out.println(data);


//        String uuid="2";
//        String title="Java学习笔记";
//        String score="9";
//
//        SqlSession session= SQL.CreatSqlSession();
//        Score score1=new Score();
//        score1.setUuid(uuid);
//        score1.setTitle(title);
//        score1.setScore(score);
//
//        if (session.selectOne("selectByUuidAndTitleInScore",score1)!=null){
//            System.out.println("评分失败，您已为此书评分");;
//        }else {
//            if (Integer.parseInt(score) > 0 || Integer.parseInt(score) <= 10) {
//
//                session.selectOne("insertToScore", score1);
//
//                Book book = session.selectOne("selectByTitleInBooks", title);
//                String persons = book.getPersons();
//                String scores = book.getScores();
//                scores = String.valueOf(Integer.parseInt(scores) + Integer.parseInt(score));
//                persons = String.valueOf(Integer.parseInt(persons) + 1);
//                score = String.valueOf(Double.parseDouble(scores) / Double.parseDouble(persons));
//                book.setScores(scores);
//                book.setPersons(persons);
//                book.setScore(score);
//                book.setTitle(title);
//                session.selectOne("updataByTitleInBooks", book);
//
//            } else {
//                session.close();
//                System.out.println("评分失败，请输入分数！！");
//            }
//        }
//        session.close();

//        System.out.println("评分成功");

//        String i="5";
//        System.out.println(Integer.parseInt(i)+9);


//        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date=new Date();
//
//        Long l=date.getTime();
//        System.out.println(l);
//        Long ll=new Long(86400000*20);
//        System.out.println(ll);
//
//        l+=ll;
//        l+=ll;
//        l+=ll;
//        System.out.println(l);
//        System.out.println(date.getTime());
//        System.out.println(simpleDateFormat.format(date.getTime()));
//        System.out.println(simpleDateFormat.format(l));




//        String borrowebBooks="g,f";
//        String uuid="1";
//        String borrowebBook[]=borrowebBooks.split(",");
//
//        SqlSession session= SQL.CreatSqlSession();
//
//
//        User user=session.selectOne("selectByUuid",uuid);
//        String availableQuantity=user.getAvailableQuantity();
//
//        if (Integer.parseInt(availableQuantity)>=borrowebBook.length) {
//            String borrowed_books = user.getBorrowedBooks() + borrowebBooks;
//            String allBooks=user.getAllBooks()+","+borrowebBooks;
//            availableQuantity=String.valueOf(Integer.parseInt(availableQuantity)-borrowebBook.length);
//            user.setUuid(uuid);
//            user.setBorrowedBooks(borrowed_books);
//            user.setAllBooks(allBooks);
//            user.setAvailableQuantity(availableQuantity);
//            session.selectOne("updataByUuid",user);
//
//
//        }else{
//            session.close();
//
//        }
//
//        session.close();


    }
}
