package top.jbyf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jbyf.config.ConfirmConfig;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,message);
        log.info("当前时间：{} , 给给队列 发送信息：{}",new Date().toString(),message);
    }
}
