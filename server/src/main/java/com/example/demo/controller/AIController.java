package com.example.demo.controller;

import com.example.demo.model.Dialogs;
import com.example.demo.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class AIController {


    @Autowired
    private OllamaService ollamaService;

    @GetMapping("/text")
    public ResponseEntity chat(@RequestParam(value = "msg", defaultValue = "Tell me a joke") String msg) {
        String res = ollamaService.chat(msg);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Dialogs dialogs) {
        ollamaService.save(dialogs);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity load() {
        Dialogs res = ollamaService.load();
        return ResponseEntity.ok().body(res);
    }

//    @GetMapping("/steam")
//    public Flux<String> stream(@RequestParam(value = "message", defaultValue = "1+1=?") String message) {
//
//        return streamingChatClient.stream(message);
//    }

}
