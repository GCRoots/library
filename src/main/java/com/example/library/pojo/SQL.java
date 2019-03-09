package com.example.library.pojo;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SQL {
    public static SqlSession CreatSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory;
        SqlSession session = null;
        String resource = "mybatis/mybatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        return session;
    }

}
