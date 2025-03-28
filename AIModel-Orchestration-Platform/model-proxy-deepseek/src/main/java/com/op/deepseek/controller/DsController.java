package com.op.deepseek.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.op.deepseek.service.DeepseekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/deepseek")
public class DsController {

    @Autowired
    private DeepseekService deepseekService;

    @PostMapping("/chat")
    public String doChat(@RequestBody String question) throws UnirestException, IOException {
        String response = deepseekService.doChat(question);
        return response;
    }
}
