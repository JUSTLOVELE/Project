package com.yzl.sentinedemo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async
    public void hello() {

        System.out.println("异步开始");
        try{
            Thread.sleep(5000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("异步结束");
    }
}
