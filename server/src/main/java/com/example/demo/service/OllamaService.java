package com.example.demo.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.intercepter.ThreadLocalHolder;
import com.example.demo.mapper.HistoryMapper;
import com.example.demo.model.Dialogs;
import com.example.demo.model.History;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OllamaService {

    @Resource
    private OllamaChatModel ollamaChatModel;

    @Resource
    private HistoryMapper historyMapper;

    @Resource
    private ChatMemory chatMemory;

    public Flux<ChatResponse> chat(String chatId, String msg) {
        if (load(chatId) != null) {
            String histories = load(chatId).getData();
            JSONArray jsonArray = JSONArray.parseArray(histories);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject oneChat = JSONObject.parseObject(jsonArray.get(i).toString());
                if ((i + 1) % 2 == 0){
                    chatMemory.add(chatId, new UserMessage(oneChat.getString("content")));
                }else if ((i + 1) % 2 == 1){
                    chatMemory.add(chatId, new AssistantMessage(oneChat.getString("content")));
                }
            }
        }

        chatMemory.add(chatId, new UserMessage(msg));
        Prompt prompt = new Prompt(chatMemory.get(chatId, 20));
        return ollamaChatModel.stream(prompt).doOnNext(
                response -> chatMemory.add(chatId, new AssistantMessage(response.getResult().getOutput().getText()))
        );
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






