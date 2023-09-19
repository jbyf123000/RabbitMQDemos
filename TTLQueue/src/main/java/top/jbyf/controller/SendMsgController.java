package top.jbyf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/ttl")
public class SendMsgController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{} , 发送信息给两个TTL队列:{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列:" + message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列:" + message);

    }

}
