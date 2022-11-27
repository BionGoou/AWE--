package com.awe.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 2 * 60 *1000L;// 60 * 60 *1000  一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "BionGo";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
    
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String UUID,String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, UUID);// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("sg")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
//        String jwt = createJWT("12345");
//        Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjM2VjZTI0NjYwMWM0OWUwYTU4ZGZhOThhMmQyZTRmNSIsInN1YiI6IjEyMzQ1IiwiaXNzIjo" +
//                "ic2ciLCJpYXQiOjE2NjE1ODQ0ODIsImV4cCI6MTY2MTU4ODA4Mn0.yO1rZz9sRhbDyrnRsn_FsBv3Zfv6qPVhyjZ32dFuHpg");
//        System.out.println(claims.getSubject());
        /**
         * $2a$10$7tbe.fg1Fh7BGNHFrkWnPOYnbJi2wjL8Z4bewEdIdLTftMrEorhs6
         * $2a$10$40ytx7neK05Nv2Ha3sNJK.Mzx/j9D44N9h9dC9SPo3byydeMUzjZS
         * $2a$10$06MINRXB/MDsxFVfZ7lMkuBPojsLBwfeX3UTIdkSgHpt9m.JOmpUW
         * $2a$10$VqZpqoHBdW81vmRQmwijvOINsjpnqZBO793au/zbiIGiFGOuJYcFS*/
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    }

    public static String getJWT(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}
