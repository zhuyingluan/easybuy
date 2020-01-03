package cn.easybuy.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyBatis工具类
 */
public class MyBatisUtils {
    private static MyBatisUtils myBatisUtils;
    public static SqlSessionFactory sqlSessionFactory;
    private MyBatisUtils(){
        if (sqlSessionFactory==null){
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public synchronized static MyBatisUtils getInstance(){
        if (sqlSessionFactory==null){
            myBatisUtils = new MyBatisUtils();
        }
        return myBatisUtils;
    }
}
