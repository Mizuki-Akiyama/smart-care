package com.example.demo.mapper;

import com.example.demo.model.History;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HistoryMapper {

    int update(History history);

    int insert(History history);

    void clear(String chatId);

    Optional<History> findByUserId(String userId);

    Optional<History> findByChatId(String chatId);

    Optional<History> findByUserIdAndChatId(String userId, String chatId);

    List<History> findAllChat(String userId);
}
