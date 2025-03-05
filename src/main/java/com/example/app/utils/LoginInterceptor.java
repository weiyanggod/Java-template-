package com.example.app.utils;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.app.config.GlobalProperties;
import com.example.app.mapper.AdminMapper;
import com.example.app.model.dto.UserDTO;
import com.example.app.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    // 私钥
    private static final String TOKEN_SECRET = "070970107f5842efa99009886bf601d0";
    /**
     * 目标方法执行前
     * 该方法在控制器处理请求方法前执行，其返回值表示是否中断后续操作
     * 返回 true 表示继续向下执行，返回 false 表示中断后续操作
     *
     * @return
     */
    @Resource
    private AdminMapper adminMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查方法是否有passtoken注解，有则跳过认证，直接通过
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                Object id;
                Object username;
                try {
                    username = JWTUtil.parseToken(token).getPayload("username");
                    id = JWTUtil.parseToken(token).getPayload("id");
                } catch (Exception j) {
                    throw new RuntimeException("token不正确，请不要通过非法手段创建token");
                }
                // 查询数据库，看看是否存在此用户，方法要自己写
                QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
                objectQueryWrapper.eq("id", id);
                UserEntity userInfoParam = adminMapper.selectOne(objectQueryWrapper);
                if (userInfoParam == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }

                // 验证 token
                if (JWTUtil.verify(token, TOKEN_SECRET.getBytes())) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId((String) id);
                    userDTO.setUsername((String) username);
                    GlobalProperties.setUserEntity(userDTO);
                    return true;
                } else {
                    throw new RuntimeException("token过期或不正确，请重新登录");
                }
            }
        }
        throw new RuntimeException("没有权限注解一律不通过");
    }

}
