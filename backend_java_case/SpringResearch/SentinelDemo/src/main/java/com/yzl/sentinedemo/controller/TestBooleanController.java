package com.yzl.sentinedemo.controller;

import com.alibaba.csp.sentinel.SphO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestBooleanController {

    @GetMapping("/boolean")
    public boolean hello() {

        if(SphO.entry("Sentinel_Boolean")) {

            try{
                //被保护的资源
                System.out.println("hello sentinel");
            }finally {
                SphO.exit();//限流的出口
            }

            return true;
        }else {
            System.out.println("系统繁忙");
            return false;
        }
    }
}
