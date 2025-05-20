package com.example.demo.intercepter;

import com.alibaba.fastjson2.JSON;
import com.example.demo.model.AuthUser;
import com.example.demo.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");
        String token = request.getParameter("token");

        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (Objects.isNull(token) || token.isEmpty()) {
            return AuthResponse(response);
        }

        if (!JWTUtil.verifyToken(token)) {
            return AuthResponse(response);
        }

        AuthUser authUser = JWTUtil.getUser(token);
        if (Objects.isNull(authUser)) {
            return AuthResponse(response);
        }

        ThreadLocalHolder.setAuthUser(authUser);
        return true;
    }

    private boolean AuthResponse(HttpServletResponse response) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("code", HttpStatus.UNAUTHORIZED.value());
        body.put("message", "用户失效，请重新登录");
        body.put("data", null);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSON.toJSONString(body));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalHolder.userThread.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
