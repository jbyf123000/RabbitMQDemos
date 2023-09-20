package top.jbyf.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jbyf.config.DelayQueueConfig;

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

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg (@PathVariable String message,
                                   @PathVariable String ttlTime){
        log.info("当前时间：{} , 发送时长为{}毫秒的TTL消息给队列QC：{}",new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC",message,(message1 -> {
            // 设置发送消息时候的延迟时长
            message1.getMessageProperties().setExpiration(ttlTime);
            return message1;
        }));
    }

    @ApiOperation("基于插件的消息和时间")
    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendDelayMsg(@PathVariable String message,
                             @PathVariable Integer delayTime){
        log.info("当前时间：{} , 发送时长为{}毫秒的信息给延迟队列delayed.queue：{}",new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_EXCHANGE_NAME,DelayQueueConfig.DELAY_ROUTING_KEY,message,(message1 -> {
            // 设置 发送消息时候的 延迟时长 单位：ms
            message1.getMessageProperties().setDelay(delayTime);
            return message1;
        }));
    }

}
