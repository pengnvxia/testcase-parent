package edu.jiahui.testcase.utils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JWTUtil {//过期时间
    private static final long EXPIRE_TIME = 15 * 60 * 1000;//默认15分钟
    //私钥
    private static final String TOKEN_SECRET = "privateKey";

    /**
     * 生成签名，15分钟过期
     * @param **username**
     * @param **password**
     * @return
     */
//    public static String createToken(String userId) {
//        try {
//            // 设置过期时间
//            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
//            // 私钥和加密算法
//            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
//            // 设置头部信息
//            Map<String, Object> header = new HashMap<>(2);
//            header.put("Type", "Jwt");
//            header.put("alg", "HS256");
//            // 返回token字符串
//            return JWT.create()
//                    .withHeader(header)
//                    .withClaim("userId", userId)
//                    .withExpiresAt(date)
//                    .sign(algorithm);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 生成token，自定义过期时间 毫秒
     * @param **username**
     * @param **password**
     * @return
     */
    public static String createToken(String userId) {
        try {
            // 设置过期时间
//            Date date = new Date(System.currentTimeMillis() + expireDate);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userId)
//                    .withExpiresAt()
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 检验token是否正确
     * @param **token**
     * @return
     */
    public static String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getClaim("userId").asString();
            return userId;
        } catch (Exception e){
            return null;
        }
    }
//    public static void main(String[] args) {
//        String token = JWTUtil.createToken("zhaohy", 3000);
//        System.out.println("token == " + token);
//        String userId = JWTUtil.verifyToken(token);
//        System.out.println("userId == " + userId);
//
//        //新建定时任务
//        Runnable runnable = new Runnable() {
//            //run方法中是定时执行的操作
//            public void run() {
//                System.out.println(new Date());
//                String userId = JWTUtil.verifyToken(token);
//                System.out.println("userId == " + userId);
//            }
//        };
//        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//        /*
//         * 参数一:command：执行线程
//         * 参数二:initialDelay：初始化延时
//         * 参数三:period：两次开始执行最小间隔时间
//         * 参数四:unit：计时单位
//         */
//        service.scheduleAtFixedRate(runnable, 0, 4, TimeUnit.SECONDS);
//    }
}
