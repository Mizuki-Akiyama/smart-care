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
    public ResponseEntity chat(@RequestParam(value = "msg", defaultValue = "给我讲个笑话") String msg) {
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

    @DeleteMapping("/clear")
    public ResponseEntity clear() {
        System.out.println(1);
        ollamaService.clear();
        System.out.println(2);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/steam")
//    public Flux<String> stream(@RequestParam(value = "message", defaultValue = "1+1=?") String message) {
//
//        return streamingChatClient.stream(message);
//    }

}
