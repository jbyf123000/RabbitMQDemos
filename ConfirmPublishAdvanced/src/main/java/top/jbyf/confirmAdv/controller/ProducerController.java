package top.jbyf.confirmAdv.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jbyf.confirmAdv.config.ConfirmConfig;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private ConcurrentHashMap<String,String> sentMessage ;
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData = new CorrelationData();
        String uuid = UUID.randomUUID().toString();
        correlationData.setId(uuid);
        sentMessage.put(uuid,message);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + 123,
                ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData);
        log.info("当前时间：{} , 给给队列 发送信息：{}",new Date().toString(),message);
    }
}
