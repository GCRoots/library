package com.example.library.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.library.pojo.Datas;
import com.example.library.pojo.SQL;
import com.example.library.pojo.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class RegistrationAndLanding {

    @RequestMapping(value = "/registrationAndLanding/registration", method = RequestMethod.POST)
    public String registration(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String userName= datas.getUserName();
        String password=datas.getPassword();
        SqlSession session= SQL.CreatSqlSession();


        if (session.selectOne("selectByUserName",userName)==null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(password.getBytes());
                password = Hex.encodeHexString(digest);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            session.selectOne("insertToUsers", user);
        }else {
            session.close();
            return "用户名已存在！！！";
        }

        session.close();
        return "账号建立成功";
    }

    @RequestMapping(value = "/registrationAndLanding/landing", method = RequestMethod.POST)
    public String landing(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String password=datas.getPassword();
        String uuid= datas.getUuid();
        SqlSession session= SQL.CreatSqlSession();

        User user=session.selectOne("selectByUuid",uuid);
        String pwInBase=user.getPassword();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            password = Hex.encodeHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (password.equals(pwInBase)){
            session.close();
            return "yes";
        }else {
            session.close();
            return "no";
        }

    }

    @RequestMapping(value = "/registrationAndLanding/alterPassword", method = RequestMethod.POST)
    public String alterPassword(@RequestBody JSONObject json) throws IOException {
        Datas datas = JSON.parseObject(json.toString(), Datas.class);

        String uuid= datas.getUuid();
        String password=datas.getPassword();
        String newpassword=datas.getNewPassword();
        String passwordAgain=datas.getPasswordAgain();

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
                return "新密码两次输入不一致，请重新输入！！！";
            }
        }else {
            session.close();
            return "原密码错误，请重新输入！！！";
        }

        session.close();
        return "密码修改成功";


    }


}
