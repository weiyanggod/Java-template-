package com.example.app.config;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;
// 公共工具类
public class JwtUtil {
    // 过期时间
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 15;

    // 私钥
    private static final String TOKEN_SECRET = "070970107f5842efa99009886bf601d0";

    // 生成token
    public static String sign(String username, String id) {
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("username", username);
                put("id", id);
            }
        };
        String token = JWTUtil.createToken(map, TOKEN_SECRET.getBytes());
        return token;
    }

}
