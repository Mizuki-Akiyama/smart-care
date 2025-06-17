package com.example.demo.controller;


import com.example.demo.model.Dialogs;
import com.example.demo.service.OllamaService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/ai")
public class AIController {


    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/save/{chatId}")
    public ResponseEntity save(@PathVariable("chatId") String chatId, @RequestBody Dialogs dialogs) {
//        System.out.println(dialogs.getData());
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


    @GetMapping(value = "/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> stream(@RequestParam (value = "msg",defaultValue = "给我讲个笑话") String msg,@RequestParam("chatId") String chatId){
        return ollamaService.chat(chatId,msg);
    }

    @PostMapping("/setTitle")
    public ResponseEntity setTitle(@RequestParam("chatId") String chatId,@RequestParam("title") String title){
        ollamaService.setTitle(chatId,title);
        return ResponseEntity.ok().build();
    }

}
