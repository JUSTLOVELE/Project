package com.yzl.sentinedemo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    private Integer a = 0;

    @GetMapping("/test2")
    public String test2(){
        System.out.println("test2");
        return "test2" + a++;
    }

//****************************************************sentinel 异常回调***********************************************************************************
    /**
     * fallback：失败调用，若本接口出现未知异常，则调用fallback指定的接口。
     * blockHandler：sentinel定义的失败调用或限制调用，若本次访问被限流或服务降级，则调用blockHandler指定的接口。
     * @param name
     * @return
     */
    @GetMapping("/test1/{name}")
    @SentinelResource(value = "test1", fallback = "sentinelFallBack")
    public String test1(@PathVariable("name") String name) {
        throw new RuntimeException();
    }

    public String sentinelFallBack(String name, Throwable throwable) {
        System.out.println("sentinelFallBack");
        throwable.printStackTrace();
        return "sentinelFallBack:" + name;
    }
//*******************************************************sentinel固定规则********************************************************************************

    @RequestMapping("/hello")
    public String hello() {

        try(Entry entry = SphU.entry("Hello")) {
            return "hello sentinel"; //被保护的资源
        }catch (Exception e) {
            //被降级的资源
            e.printStackTrace();
            return "系统繁忙,稍后";
        }
    }

    @PostConstruct
    public void initFlowRules() {
        System.out.println("创建规则成功");
        //创建存放限流规则的集合
        List<FlowRule> rules = new ArrayList<>();
        //创建限流规则
        FlowRule flowRule = new FlowRule("Hello");
        //flowRule.setRefResource("Hello"); //定义资源表示对哪个资源生效
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);//限流规则的类型
        flowRule.setCount(1);//QPS每秒通过的请求个数
        //将限流规则存放到集合中
        rules.add(flowRule);

        FlowRule blockRule = new FlowRule("test3");
        blockRule.setGrade(RuleConstant.FLOW_GRADE_QPS);//限流规则的类型
        blockRule.setCount(1);//QPS每秒通过的请求个数
        rules.add(blockRule);

        FlowRule test = new FlowRule("test");
        test.setGrade(RuleConstant.FLOW_GRADE_QPS);//限流规则的类型
        test.setCount(1);//QPS每秒通过的请求个数
        rules.add(test);
        //加载限流规则
        FlowRuleManager.loadRules(rules);
    }
//*******************************************sentinel 限流回调,注意参数要一致，要不然反射会失败********************************************************************************************

    /**
     * blockHandler：sentinel定义的失败调用或限制调用，若本次访问被限流或服务降级，则调用blockHandler指定的接口。
     * @return
     */
    @GetMapping("/test")
    @SentinelResource(value = "test", blockHandler = "sentinelblockHandler")
    public String test(){
        System.out.println("test");
        return "test" + a++;
    }

    @GetMapping(value = "/sentinelblockHandler")
    public String sentinelblockHandler(BlockException ex) {
        System.out.println("sentinelblockHandler");
        ex.printStackTrace();
        return "sentinelblockHandler";
    }
//**********************************************完整案例*****************************************************************************************
    @SentinelResource(value="test3",fallback = "testFallback", blockHandler = "testBlockHandler")
    @GetMapping(value = "/test3")
    public String test3(@RequestParam String name){
        throw new RuntimeException("asdasd");
        //return name;
    }

    @GetMapping(value = "/testFallback")
    public String testFallback(@RequestParam String name){
        return "testFallback"+name;
    }

    @GetMapping(value = "/testBlockHandler")
    public String testBlockHandler(@RequestParam String name,BlockException ex){
        return "testBlockHandler"+name;
    }
}
