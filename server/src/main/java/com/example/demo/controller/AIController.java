package com.example.demo.controller;

import com.example.demo.model.Dialogs;
import com.example.demo.service.OllamaService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class AIController {


    @Autowired
    private OllamaService ollamaService;

//    @GetMapping("/text")
//    public ResponseEntity chat(@RequestParam(value = "msg", defaultValue = "给我讲个笑话") String msg) {
//        Flux<String> res = ollamaService.chat(msg);
//        return ResponseEntity.ok().body(res);
//    }

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

    @DeleteMapping("/clear")
    public ResponseEntity clear() {
        ollamaService.clear();
        return ResponseEntity.ok().build();
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
