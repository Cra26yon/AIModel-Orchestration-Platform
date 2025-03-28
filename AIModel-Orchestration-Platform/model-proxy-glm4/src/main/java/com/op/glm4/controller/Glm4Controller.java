package com.op.glm4.controller;

import com.op.glm4.service.Glm4Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/glm4")
public class Glm4Controller {

    @Autowired
    private Glm4Service glm4Service;

    @PostMapping("/chat")
    @CrossOrigin
    public String doChat(@RequestBody String request) {
        String response = glm4Service.doChat(request);
        return response;
    }

}
