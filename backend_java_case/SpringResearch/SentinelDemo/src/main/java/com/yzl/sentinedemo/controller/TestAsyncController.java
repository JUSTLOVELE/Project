package com.yzl.sentinedemo.controller;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.SphU;
import com.yzl.sentinedemo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAsyncController {

    @Autowired
    private AsyncService _asyncService;

    @GetMapping("/async")
    public void hello() {

        AsyncEntry asyncEntry = null;

        try {
            asyncEntry = SphU.asyncEntry("Sentinel_Async");
            _asyncService.hello();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(asyncEntry != null) {
                asyncEntry.exit();//限流的出口
            }
        }
    }
}
