/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Warren Zhu
 */
@EnableBinding(Source.class)
@RestController
public class SourceExample {

    @Autowired
    private Source source;

    @PostMapping("/newMessage")
    public UserMessage sendMessage(@RequestParam("messageBody") String messageBody,
            @RequestParam("username") String username) {
        UserMessage userMessage = new UserMessage(messageBody, username, LocalDateTime.now());
        this.source.output().send(new GenericMessage<>(userMessage));
        return userMessage;
    }
}
