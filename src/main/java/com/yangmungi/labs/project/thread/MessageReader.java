package com.yangmungi.labs.project.thread;

import java.util.Map;

/**
* Created by Yangmun on 7/6/2014.
*/
public class MessageReader implements Runnable {
    private final Map<String, String> messageMap;

    public MessageReader(Map<String, String> messageMap) {
        this.messageMap = messageMap;
    }

    @Override
    public void run() {

    }
}
