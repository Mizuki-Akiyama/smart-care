package com.example.demo.service;

import com.example.demo.intercepter.ThreadLocalHolder;
import com.example.demo.mapper.HistoryMapper;
import com.example.demo.model.Dialogs;
import com.example.demo.model.History;
import jakarta.annotation.Resource;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OllamaService {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Resource
    private HistoryMapper historyMapper;

    public String chat(String msg) {
        return ollamaChatClient.call(msg);
    }

    public void save(Dialogs dialogs) {
        String userId = ThreadLocalHolder.getAuthUser().getUserId();
        History h = new History().setUserId(userId).setHistory(dialogs.getData());
        Optional<History> opHistory = historyMapper.findByUserId(userId);
        if (opHistory.isPresent()) {
            historyMapper.update(h);
        } else {
            historyMapper.insert(h);
        }
    }

    public Dialogs load() {
        String userId = ThreadLocalHolder.getAuthUser().getUserId();
        Optional<History> opHistory = historyMapper.findByUserId(userId);
        return opHistory.isPresent() ? new Dialogs(opHistory.get().getHistory()) : null;
    }

}
