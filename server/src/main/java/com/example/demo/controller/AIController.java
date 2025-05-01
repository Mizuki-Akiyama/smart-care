package com.example.demo.controller;


import com.example.demo.model.Dialogs;
import com.example.demo.model.History;
import com.example.demo.service.OllamaService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIController {


    @Autowired
    private OllamaService ollamaService;

//    @GetMapping("/text")
//    public ResponseEntity chat(@RequestParam(value = "msg", defaultValue = "给我讲个笑话") String msg) {
//        Flux<String> res = ollamaService.chat(msg);
//        return ResponseEntity.ok().body(res);
//    }

    @PostMapping("/save/{chatId}")
    public ResponseEntity save(@PathVariable("chatId") String chatId, @RequestBody Dialogs dialogs) {
        System.out.println(dialogs.getData());
        ollamaService.save(dialogs,chatId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load/{chatId}")
    public ResponseEntity load(@PathVariable("chatId") String chatId) {
        Dialogs res = ollamaService.load(chatId);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/clear/{chatId}")
    public ResponseEntity clear(@PathVariable("chatId") String chatId) {
        ollamaService.clear(chatId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/loadAll")
    public ResponseEntity loadAll() {
        return ResponseEntity.ok().body(ollamaService.loadAll());
    }
//    @GetMapping("/steam")
//    public Flux<String> stream(@RequestParam(value = "message", defaultValue = "1+1=?") String message) {
//
//        return streamingChatClient.stream(message);
//    }


//    public Flux<ServerSentEvent<String>> stream(@RequestParam (value = "msg",defaultValue = "给我讲个笑话") String msg){
//        return ollamaService.chat(msg)
//                .map(string -> ServerSentEvent.<String>builder()
//                        .data(string)
//                        .build());
//
//    }
    @GetMapping(value = "/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> stream(@RequestParam (value = "msg",defaultValue = "给我讲个笑话") String msg){
        return ollamaService.chat(msg);
    }

}
