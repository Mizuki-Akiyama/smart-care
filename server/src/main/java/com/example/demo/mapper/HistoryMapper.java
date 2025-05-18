package com.example.demo.mapper;

import com.example.demo.model.History;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HistoryMapper {

    void update(History history);

    void insert(History history);

    void clear(String chatId);

    void setTitle(String chatId, String userId, String title);

    Optional<History> findByChatId(String chatId);

    Optional<History> findByUserIdAndChatId(String userId, String chatId);

    List<History> findAllChat(String userId);
}
