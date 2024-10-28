package com.example.demo.intercepter;

import com.example.demo.model.AuthUser;
import org.apache.logging.log4j.util.Strings;

import java.util.Optional;

public class ThreadLocalHolder {

    public static ThreadLocal<AuthUser> threadLocal = new ThreadLocal<>();

    public static void setAuthUser(AuthUser accountUser) {
        threadLocal.set(accountUser);
    }

    public static AuthUser getAuthUser() {
        return threadLocal.get();
    }

    public static String getUserId() {
        return Optional.ofNullable(threadLocal.get()).map(accountUser -> accountUser.getUserId()).orElse(Strings.EMPTY);
    }



}
