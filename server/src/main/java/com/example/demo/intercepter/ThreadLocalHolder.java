package com.example.demo.intercepter;

import com.example.demo.model.AuthUser;
import org.apache.logging.log4j.util.Strings;

import java.util.Optional;

public class ThreadLocalHolder {

    public static ThreadLocal<AuthUser> userThread = new ThreadLocal<>();

    public static void setAuthUser(AuthUser accountUser) {
        userThread.set(accountUser);
    }

    public static AuthUser getAuthUser() {
        return userThread.get();
    }

    public static String getUserId() {
        return Optional.ofNullable(userThread.get()).map(accountUser -> accountUser.getUserId()).orElse(Strings.EMPTY);
    }

}
