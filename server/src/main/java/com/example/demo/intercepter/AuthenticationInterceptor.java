package com.example.demo.intercepter;

import com.example.demo.model.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;


@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");
        if (Objects.isNull(authHeader) || authHeader.equals("")) {

            authHeader = "jyk";

//            return false;
        }

        ThreadLocalHolder.setAuthUser(new AuthUser().setUserId(authHeader));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalHolder.threadLocal.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
