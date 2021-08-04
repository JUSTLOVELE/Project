package com.ex.batchprocessing.view;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JmeterControl {

    @GetMapping("/jmeterTest1")
    public String jmeterTest1(String name) {
        System.out.println(name);
        return "hello world ! " + name;
    }
}
