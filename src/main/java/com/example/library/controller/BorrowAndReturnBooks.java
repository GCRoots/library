package com.example.library.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.library.pojo.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class BorrowAndReturnBooks {

    @RequestMapping(value = "/borrowAndReturnBooks/borrowBooks", method = RequestMethod.POST)
    public String borrowBooks(@RequestBody JSONObject json) throws IOException {
        Datas d = JSON.parseObject(json.toString(), Datas.class);

        String borrowebBooks=d.getBorrowedBooks();
        String uuid=d.getUuid();
        String userName=d.getUserName();

        String borrowedBook[]=borrowebBooks.split(",");
        SqlSession session= SQL.CreatSqlSession();

        User user =session.selectOne("selectByUuid",uuid);
        String availableQuantity=user.getAvailableQuantity();
        if (Integer.parseInt(availableQuantity)>borrowedBook.length) {
            String borrowed_books = user.getBorrowedBooks() + borrowebBooks;
            String allBooks=user.getAllBooks();
            availableQuantity=String.valueOf(Integer.parseInt(availableQuantity)-borrowedBook.length);
            user.setUuid(uuid);
            user.setBorrowedBooks(borrowed_books);
            String books[]=allBooks.split(",");
            for (int i=0;i<borrowedBook.length;i++){
                for (int j=0;j<books.length;j++){
                    if (!borrowedBook[i].equals(books[j])){
                        allBooks+=","+borrowedBook[i];
                    }
                }

                Book book=session.selectOne("selectByTitleInBooks",borrowedBook[i]);
                int a=Integer.parseInt(book.getSurplus());
                if (a>0){
                    String surplus=String.valueOf(a-1);
                    book.setSurplus(surplus);
                    session.selectOne("updataByTitleInBooks",book);
                }else {
                    session.close();
                    return "您所借图书剩余数量不足！！";
                }
                ReturnBooks returnBook=new ReturnBooks();
                returnBook.setUserName(userName);
                returnBook.setUuid(uuid);
                returnBook.setTitle(borrowedBook[i]);
                SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                Long now=date.getTime();
                String borrowingTime=sf.format(now);
                Long twentDays=new Long(86400000*20);
                for (int i1=0;i1<3;i1++){
                    now+=twentDays;
                }
                String returnTime=sf.format(now);
                returnBook.setBorrowingTime(borrowingTime);
                returnBook.setReturnTime(returnTime);
                session.selectOne("insertToReturnBooks",returnBook);
            }
            user.setAllBooks(allBooks);
            user.setAvailableQuantity(availableQuantity);
            session.selectOne("updataByUuid",user);
        }else{
            session.close();
            return "您可借阅图书数量不足！！！";
        }
        session.close();
        return "借阅成功";
    }

    @RequestMapping(value = "/borrowAndReturnBooks/returnBooks", method = RequestMethod.POST)
    public String returnBooks(@RequestBody JSONObject json) throws IOException {
        Datas d = JSON.parseObject(json.toString(), Datas.class);

        String returnBooks=d.getReturnBooks();
        String uuid=d.getUuid();
        String userName=d.getUserName();

        String returnBook[]=returnBooks.split(",");
        SqlSession session= SQL.CreatSqlSession();

        User user =session.selectOne("selectByUuid",uuid);
        String availableQuantity=user.getAvailableQuantity();
        String borrowedBook[]=user.getBorrowedBooks().split(",");
        String borrowedBooks="";
        String outTime=user.getOutTime();

        for (int i=0;i<borrowedBook.length;i++){
            boolean equal=false;
            for (int j=0;j<returnBook.length;j++){
                if (borrowedBook[i].equals(returnBook[j])){
                    equal=true;
                }
            }
            if (equal==false){
                borrowedBooks+=borrowedBook+",";
            }

        }
        for (int i=0;i<returnBook.length;i++){

            ReturnBooks returnBooks1=new ReturnBooks();
            returnBooks1.setUuid(uuid);
            returnBooks1.setTitle(returnBook[i]);
            returnBooks1=session.selectOne("selectByUuidAndTitle",returnBooks1);
            returnBooks1.setIsReturned("yes");
            session.selectOne("updataByUuidAndTitle",returnBooks1);

            String returnTime=returnBooks1.getReturnTime();
            Date date=new Date();
            Long time=date.getTime()-Integer.parseInt(returnTime);
            if (time>0){
                outTime=String.valueOf(Integer.parseInt(outTime)+(time/86400000)+1);
            }

            Book book=session.selectOne("selectByTitleInBooks",returnBook[i]);
            String surplus=String.valueOf(Integer.parseInt(book.getSurplus())+1);
            book.setSurplus(surplus);
            session.selectOne("updataByTitleInBooks",returnBook[i]);
        }

        borrowedBooks=borrowedBooks.substring(0,borrowedBooks.length()-2);
        user.setBorrowedBooks(borrowedBooks);
        availableQuantity=String.valueOf(Integer.parseInt(availableQuantity)+(borrowedBook.length-borrowedBooks.split(",").length));
        user.setAvailableQuantity(availableQuantity);
        user.setOutTime(outTime);
        session.selectOne("updataByUuid",user);

        session.close();
        return "归还图书成功";
    }


}
