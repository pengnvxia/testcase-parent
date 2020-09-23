package edu.jiahui.testcase.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JDBCUtil {

    //获取数据库连接对象
    public static Connection getConnection(String databaseName){
        Connection conn=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/"+databaseName+"?characterEncoding=utf-8&useSSL=false";
            String username="root";
            String password="123456";
            conn=DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e+"数据库连接失败");
        }
        return conn;
    }
    //关闭数据库的方法
//    public static void close(ResultSet rs,Statement sta,Connection conn){
//        if(rs!=null){
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        if(sta!=null){
//            try {
//                sta.close();
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        if(conn!=null){
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
    public static void close(Statement sta,Connection conn){
        if(sta!=null){
            try {
                sta.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
