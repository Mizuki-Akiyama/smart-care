package com.example.demo.service;

import com.example.demo.intercepter.ThreadLocalHolder;
import com.example.demo.mapper.HistoryMapper;
import com.example.demo.model.Dialogs;
import com.example.demo.model.History;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OllamaService {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Resource
    private HistoryMapper historyMapper;

    public Flux<ChatResponse> chat(String msg) {
        Prompt prompt = new Prompt(msg);
        return ollamaChatClient.stream(prompt);
    }

    public void save(Dialogs dialogs, String chatId) {
        String userId = ThreadLocalHolder.getAuthUser().getUserId();
        History h = new History().setUserId(userId).setChatId(chatId).setContent(dialogs.getData());
        Optional<History> opHistory = historyMapper.findByUserIdAndChatId(userId, chatId);
        Optional<History> optionalHistory = historyMapper.findByChatId(chatId);
        if (opHistory.isPresent()) {
            if (optionalHistory.isPresent()) {
                historyMapper.update(h);
            } else {
                historyMapper.insert(h);
            }
        } else {
            historyMapper.insert(h);
        }

    }

    public Dialogs load(String chatId) {
        String userId = ThreadLocalHolder.getAuthUser().getUserId();
        Optional<History> opHistory = historyMapper.findByUserIdAndChatId(userId, chatId);
        if (opHistory.isPresent()) {
            Dialogs dialogs = new Dialogs();
            dialogs.setData(opHistory.get().getContent());
            return dialogs;
        }
        return null;
    }

    public void clear(String chatId) {
        String userId = ThreadLocalHolder.getAuthUser().getUserId();
        Optional<History> opHistory = historyMapper.findByUserIdAndChatId(userId, chatId);
        if (opHistory.isPresent()) {
            historyMapper.clear(chatId);
        }
    }

    public List<Map<String, String>> loadAll() {
        String userId = ThreadLocalHolder.getAuthUser().getUserId();
        List<History> histories = historyMapper.findAllChat(userId);

        Map<String, List<History>> grouped = histories.stream()
                .collect(Collectors.groupingBy(History::getChatId));

        return grouped.entrySet().stream()
                .map(entry -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", entry.getKey());

                    // 使用该对话的第一条记录的内容作为标题
                    String content = entry.getValue().get(0).getContent();
                    map.put("content", content);

                    return map;
                })
                .collect(Collectors.toList());
    }

}






