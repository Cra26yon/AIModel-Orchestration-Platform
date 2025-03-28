package com.op.deepseek.service;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public interface DeepseekService {
    String doChat(String question) throws IOException, UnirestException;
}
